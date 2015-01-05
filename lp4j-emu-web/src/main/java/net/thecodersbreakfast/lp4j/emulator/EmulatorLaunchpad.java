/*
 * Copyright 2015 Olivier Croisier (thecodersbreakfast.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.thecodersbreakfast.lp4j.emulator;

import net.thecodersbreakfast.lp4j.api.*;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A web-based (HTML/SVG/Sebsockets) Launchpad emulator.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class EmulatorLaunchpad implements Launchpad {

    /** Directory to serve static files from. */
    public static final String WEB_RESOURCES_PREFIX = "/web";
    /** URL of the Vertx eventbus bridge */
    public static final String EVENTBUS_ADDRESS = "/eventbus";
    /** Eventbus ID of the emulator, on the server side */
    public static final String EVENTBUS_SERVER_HANDLER_ID = "lp4j:server";
    /** Handler for Vertx eventbus messages. */
    private final EventBusHandler eventBusHandler = new EventBusHandler();
    /** Vertx engine instance. */
    private final Vertx vertx;

    /**
     * Constructor.
     *
     * @param httpPort The HTTP port on which the emulator should run.
     */
    public EmulatorLaunchpad(int httpPort) {

        vertx = VertxFactory.newVertx();

        // Static files
        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(new WebResourceHandler());

        // Eventbus bridge
        JsonObject bridgeConfig = new JsonObject().putString("prefix", EVENTBUS_ADDRESS);
        JsonArray credentialsPermitAll = new JsonArray().add(new JsonObject());
        vertx.createSockJSServer(httpServer).bridge(bridgeConfig, credentialsPermitAll, credentialsPermitAll);
        vertx.eventBus().registerLocalHandler(EVENTBUS_SERVER_HANDLER_ID, eventBusHandler);

        System.out.println("Launchpad emulator is ready on http://localhost:" + httpPort + "/");
        httpServer.listen(httpPort);
    }

    /** {@inheritDoc} */
    @Override
    public LaunchpadClient getClient() {
        return new EmulatorLaunchpadClient(vertx);
    }

    /** {@inheritDoc} */
    @Override
    public void setListener(LaunchpadListener listener) {
        this.eventBusHandler.setListener(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        vertx.stop();
    }

    /**
     * Handler for standart HTTP requests, used to serve static files.
     */
    private static class WebResourceHandler implements Handler<HttpServerRequest> {
        @Override
        public void handle(HttpServerRequest req) {
            String resourcePath = req.path();
            HttpServerResponse response = req.response();

            if (shouldRedirectToIndexHtml(resourcePath)) {
                redirectToIndexHtml(response);
                return;
            }

            if (!isLegalResource(resourcePath)) {
                sendResponseForbidden(response);
                return;
            }

            if (!writeResourceToResponse(response, resourcePath)) {
                sendResponseNotFound(response);
            }

        }

        private boolean shouldRedirectToIndexHtml(String resourcePath) {
            return "/".equals(resourcePath);
        }

        private void redirectToIndexHtml(HttpServerResponse response) {
            response.headers().add("Location", WEB_RESOURCES_PREFIX + "/index.html");
            response.setStatusCode(301).end();
        }

        private boolean isLegalResource(String resourcePath) {
            return resourcePath.startsWith(WEB_RESOURCES_PREFIX);
        }

        private void sendResponseForbidden(HttpServerResponse response) {
            response.setStatusCode(403).end();
        }

        private void setResponseContentType(HttpServerResponse response, String resourcePath) {
            String contentType = findContentType(resourcePath);
            response.headers().add("Content-Type", contentType);
        }

        private String findContentType(String resourcePath) {
            if (resourcePath.endsWith(".js")) {
                return "application/javascript";
            } else {
                return "text/html";
            }
        }

        private boolean writeResourceToResponse(HttpServerResponse response, String resourcePath) {
            setResponseContentType(response, resourcePath);
            byte[] bytes = readResource(resourcePath);
            if (bytes == null) {
                return false;
            }
            Buffer buffer = new Buffer(bytes);
            response.end(buffer);
            return true;
        }

        private static byte[] readResource(String path) {
            try {
                InputStream is = EmulatorLaunchpad.class.getResourceAsStream(path);
                if (is == null) {
                    return null;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int reads = is.read();
                while (reads != -1) {
                    baos.write(reads);
                    reads = is.read();
                }
                return baos.toByteArray();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        private void sendResponseNotFound(HttpServerResponse response) {
            response.setStatusCode(404).end();
        }
    }

    /**
     * Handler for Vertx eventbus messages.
     */
    private static class EventBusHandler implements Handler<Message> {

        private static enum InputEventType {
            /** Pad pressed */
            PP,
            /** Pad released */
            PR,
            /** Button pressed */
            BP,
            /** Button released */
            BR,
            /** Text scrolled */
            TS
        }

        private LaunchpadListener listener;

        @Override
        public void handle(Message message) {
            if (listener == null) {
                return;
            }

            long timestamp = System.currentTimeMillis();
            JsonObject body = (JsonObject) message.body();
            InputEventType inputEventType = InputEventType.valueOf(body.getString("evt"));
            switch (inputEventType) {
                case PP: {
                    Integer x = body.getInteger("x");
                    Integer y = body.getInteger("y");
                    listener.onPadPressed(Pad.at(x, y), timestamp);
                    break;
                }
                case PR: {
                    Integer x = body.getInteger("x");
                    Integer y = body.getInteger("y");
                    listener.onPadReleased(Pad.at(x, y), timestamp);
                    break;
                }
                case BP: {
                    int x = body.getInteger("x");
                    int y = body.getInteger("y");
                    int c = x == -1 ? y : x;
                    Button button = (x != -1) ? Button.atTop(c) : Button.atRight(c);
                    listener.onButtonPressed(button, timestamp);
                    break;
                }
                case BR: {
                    int x = body.getInteger("x");
                    int y = body.getInteger("y");
                    int c = x == -1 ? y : x;
                    Button button = (x != -1) ? Button.atTop(c) : Button.atRight(c);
                    listener.onButtonReleased(button, timestamp);
                    break;
                }
                case TS: {
                    listener.onTextScrolled(timestamp);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown input event type " + inputEventType.name());
                }
            }

        }

        public void setListener(LaunchpadListener listener) {
            this.listener = listener;
        }
    }
}
