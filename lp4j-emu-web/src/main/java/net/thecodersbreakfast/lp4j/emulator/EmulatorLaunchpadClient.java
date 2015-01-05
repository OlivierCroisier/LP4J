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
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;

/**
 * A client to communicate with the Launchpad emulator
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class EmulatorLaunchpadClient implements LaunchpadClient {

    /** Eventbus ID of the emulator, on the browser side */
    private static final String EVENTBUS_CLIENT_HANDLER_ID = "lp4j:client";

    /** Types of events sent to the emulator, on the browser side */
    private static enum OutputEventType {
        /** Reset */
        RST,
        /** Padlight */
        PADLGT,
        /** Button light */
        BTNLGT,
        /** Test */
        TST,
        /** Brightness */
        BRGHT,
        /** SetBuffers */
        BUF
    }

    /** The Vertx engine that powers the emulator on the server side */
    private final Vertx vertx;

    /**
     * Constructor
     *
     * @param vertx The Vertx engine to use
     */
    public EmulatorLaunchpadClient(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void reset() {
        publishEvent(OutputEventType.RST);
    }

    /**
     * {@inheritDoc}
     *
     * @param intensity The desired light intensity. Must not be null.
     */
    @Override
    public void testLights(LightIntensity intensity) {
        if (intensity == null) {
            throw new IllegalArgumentException("Light intensity must not be null.");
        }
        int brightness = 0;
        if (intensity == LightIntensity.LOW) {
            brightness = 5;
        }
        if (intensity == LightIntensity.MEDIUM) {
            brightness = 10;
        }
        if (intensity == LightIntensity.HIGH) {
            brightness = 15;
        }
        JsonObject params = new JsonObject()
                .putNumber("i", brightness);
        publishEvent(OutputEventType.TST, params);
    }

    /**
     * {@inheritDoc}
     *
     * <p>NOT IMPLEMENTED YET ON THE EMULATOR
     */
    @Override
    public void setLights(Color[] colors, BackBufferOperation operation) {
        if (colors == null) {
            throw new IllegalArgumentException("Colors must not be null");
        }
        if (operation == null) {
            throw new IllegalArgumentException("BackBuffer operation must not be null.");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param pad The pad to light up. Must not be null.
     * @param color The color to use. Use {@link net.thecodersbreakfast.lp4j.api.Color#BLACK} to switch the light off. Must not be null.
     * @param operation What to do on the backbuffer. Must not be null.
     */
    @Override
    public void setPadLight(Pad pad, Color color, BackBufferOperation operation) {
        if (pad == null) {
            throw new IllegalArgumentException("Pad must not be null");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color must not be null.");
        }
        if (operation == null) {
            throw new IllegalArgumentException("BackBuffer operation must not be null.");
        }
        JsonObject params = new JsonObject()
                .putNumber("x", pad.getX())
                .putNumber("y", pad.getY())
                .putObject("c", new JsonObject().putNumber("r", color.getRed()).putNumber("g", color.getGreen()))
                .putString("o", operation.name());
        publishEvent(OutputEventType.PADLGT, params);
    }

    /**
     * {@inheritDoc}
     *
     * @param button The button to light up. Must not be null.
     * @param color The color to use. Use {@link net.thecodersbreakfast.lp4j.api.Color#BLACK} to switch the light off. Must not be null.
     * @param operation What to do on the backbuffer. Must not be null.
     */
    @Override
    public void setButtonLight(Button button, Color color, BackBufferOperation operation) {
        if (button == null) {
            throw new IllegalArgumentException("Button must not be null.");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color must not be null.");
        }
        if (operation == null) {
            throw new IllegalArgumentException("BackBuffer operation must not be null.");
        }
        JsonObject params = new JsonObject()
                .putBoolean("t", button.isTopButton())
                .putNumber("i", button.getCoordinate())
                .putObject("c", new JsonObject().putNumber("r", color.getRed()).putNumber("g", color.getGreen()))
                .putString("o", operation.name());
        publishEvent(OutputEventType.BTNLGT, params);
    }

    /**
     * {@inheritDoc}
     *
     * @param brightness The desired brightness. Must not be null.
     */
    @Override
    public void setBrightness(Brightness brightness) {
        if (brightness == null) {
            throw new IllegalArgumentException("Brightness must not be null");
        }
        JsonObject params = new JsonObject()
                .putNumber("b", brightness.getBrightness());
        publishEvent(OutputEventType.BRGHT, params);
    }

    /**
     * {@inheritDoc}
     *
     * @param visibleBuffer The buffer to display. Must not be null.
     * @param writeBuffer The buffer to which the commands are applied. Must not be null.
     */
    @Override
    public void setBuffers(Buffer visibleBuffer, Buffer writeBuffer, boolean copyVisibleBufferToWriteBuffer, boolean autoSwap) {
        if (visibleBuffer == null) {
            throw new IllegalArgumentException("Visible buffer must not be null.");
        }
        if (writeBuffer == null) {
            throw new IllegalArgumentException("Write buffer must not be null.");
        }
        JsonObject params = new JsonObject()
                .putString("v", visibleBuffer.name())
                .putString("w", writeBuffer.name())
                .putBoolean("c", copyVisibleBufferToWriteBuffer)
                .putBoolean("a", autoSwap);
        publishEvent(OutputEventType.BUF, params);
    }

    /**
     * {@inheritDoc}
     *
     * <p>NOT IMPLEMENTED YET ON THE EMULATOR
     */
    @Override
    public void scrollText(String text, Color color, ScrollSpeed speed, boolean loop, BackBufferOperation operation) {
        if (color == null) {
            throw new IllegalArgumentException("Color must not be null.");
        }
    }

    /**
     * Sends the given event to the emulator, with no additional parameters
     *
     * @param outputEventType The event to send
     */
    private void publishEvent(OutputEventType outputEventType) {
        publishEvent(outputEventType, null);
    }

    /**
     * Sends the given event to the emulator, with the given additional parameters
     *
     * @param outputEventType The event to send
     * @param params The additional parameters
     */
    private void publishEvent(OutputEventType outputEventType, JsonObject params) {
        JsonObject payload = new JsonObject();
        payload.putString("evt", outputEventType.name());
        if (params != null) {
            payload.mergeIn(params);
        }
        vertx.eventBus().publish(EVENTBUS_CLIENT_HANDLER_ID, payload);
    }

}
