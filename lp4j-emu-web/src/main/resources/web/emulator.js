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

// The standart SVG namespace
var SVGNS = "http://www.w3.org/2000/svg";

// The Launchpad emulator
var launchpad;

// The Vertx event bus
var eventbus;

// Eventbus IDs of the client (browser-side) and server (application-side) emulator parts
var CLIENT_EVENTBUS_ID = 'lp4j:client';
var SERVER_EVENTBUS_ID = 'lp4j:server';


// Sends the given message on the event bus, to the server-side part of the emulator
function sendToServer(params) {
    if (eventbus) {
        eventbus.send(SERVER_EVENTBUS_ID, params);
    }
}


// Initializes the launchpad instance and its listener
function configureLaunchpad() {

    var listener = new LaunchpadListener();
    listener.onPadPressed = function (x, y) {
        sendToServer({"evt": "PP", "x": x, "y": y});
    };
    listener.onPadReleased = function (x, y) {
        sendToServer({"evt": "PR", "x": x, "y": y});
    };
    listener.onButtonPressed = function (x, y) {
        sendToServer({"evt": "BP", "x": x, "y": y});
    };
    listener.onButtonReleased = function (x, y) {
        sendToServer({"evt": "BR", "x": x, "y": y});
    };

    launchpad = new Launchpad();
    launchpad.setListener(listener);
}


// Handles commands send to the launchpad
function handleClientCommand(event) {
    var eventType = event.evt;
    // Event types defined in EmulatorClient$OutputEventType enum
    switch (eventType) {
        case "RST" :
            launchpad.reset();
            break;
        case "PADLGT" :
            launchpad.setPadLight(event.x, event.y, event.c, event.o);
            break;
        case "BTNLGT" :
            launchpad.setButtonLight(event.t, event.i, event.c, event.o);
            break;
        case "BRGHT" :
            launchpad.setBrightness(event.b);
            break;
        case "BUF" :
            launchpad.setBuffers(event.v, event.w, event.c, event.a);
            break;
        case "TST" :
            launchpad.testLights(event.i);
            break;
    }
}


$(document).ready(function () {

    configureLaunchpad();
    initDisplay(document.getElementById("launchpad"));

    eventbus = new vertx.EventBus('/eventbus');
    eventbus.onopen = function () {
        eventbus.registerHandler(CLIENT_EVENTBUS_ID, function (event) {
            handleClientCommand(event);
            updateDisplay(launchpad);
        });
    }

});


// Creates the emulator SVG display
function initDisplay(container) {

    // Root SVG node
    var svg = document.createElementNS(SVGNS, "svg");
    svg.setAttribute("xmlns", SVGNS);
    svg.setAttribute("viewBox", "0 0 110 110"); // 10px per pad/button, 2px gutter
    svg.setAttribute("preserveAspectRatio", "xMinYMin meet");
    container.appendChild(svg);

    // Launchpad background
    var bg = document.createElementNS(SVGNS, "rect");
    bg.setAttribute("width", "110");
    bg.setAttribute("height", "110");
    bg.setAttribute("fill", "#444");
    bg.setAttribute("stroke", "#000");
    bg.setAttribute("stroke-width", "2");
    svg.appendChild(bg);

    var listener = launchpad.listener || new LaunchpadListener();

    var btnNames = ["^", "v", "<", ">", "SES", "USR1", "USR2", "MIX", "VOL", "PAN", "SNDA", "SNDB", "STOP","TRCK", "SOLO", "ARM"];

    for (var x = 0; x < 9; x++) {
        for (var y = 0; y < 9; y++) {
            if (x == 8 && y == 0) continue;
            var pad;

            // Round buttons on top and right sides
            if (x == 8 || y == 0) {
                var centerX = (2 + 12 * x + 5).toString();
                var centerY = (2 + 12 * y + 5).toString();
                pad = document.createElementNS(SVGNS, "circle");
                pad.setAttribute("cx", centerX);
                pad.setAttribute("cy", centerY);
                pad.setAttribute("r", "4");
                pad.onmousedown = (function (pX, pY) {
                    return function (e) {
                        e.preventDefault();
                        if (pX == 8) pX = -1;
                        listener.onButtonPressed(pX, pY - 1);
                    }
                })(x, y);
                pad.onmouseup = (function (pX, pY) {
                    return function (e) {
                        e.preventDefault();
                        // Ctrl-clicking skips the "button released" event
                        if (e.ctrlKey) return;
                        if (pX == 8) pX = -1;
                        listener.onButtonReleased(pX, pY - 1);
                    }
                })(x, y);
                pad.setAttribute("id", "key" + x + y);
                pad.style.fill = launchpad.colors[0][0];
                svg.appendChild(pad);

                // Button text
                var btnName;
                if (y==0) {
                    btnName = btnNames[x]
                } else {
                    btnName = btnNames[7+y];
                }
                var txtEl = document.createElementNS(SVGNS, "text");
                txtEl.setAttribute("text-anchor","middle");
                txtEl.setAttribute("x",centerX);
                txtEl.setAttribute("y",centerY);
                txtEl.setAttribute("font-size","12%");
                txtEl.setAttribute("font-family","sans-serif");
                txtEl.setAttribute("font-weight","bold");
                txtEl.setAttribute("pointer-events","none");
                var txt = document.createTextNode(btnName);
                txtEl.appendChild(txt);
                svg.appendChild(txtEl);
            }

            // Square pads
            else {
                pad = document.createElementNS(SVGNS, "rect");
                pad.setAttribute("x", (2 + 12 * x).toString());
                pad.setAttribute("y", (2 + 12 * y).toString());
                pad.setAttribute("width", "10");
                pad.setAttribute("height", "10");
                pad.onmousedown = (function (pX, pY) {
                    return function (e) {
                        e.preventDefault();
                        listener.onPadPressed(pX, pY - 1);
                    }
                })(x, y);
                pad.onmouseup = (function (pX, pY) {
                    return function (e) {
                        e.preventDefault();
                        // Ctrl-clicking skips the "button released" event
                        if (e.ctrlKey) return;
                        listener.onPadReleased(pX, pY - 1);
                    }
                })(x, y);
                pad.setAttribute("id", "key" + x + y);
                pad.style.fill = launchpad.colors[0][0];
                svg.appendChild(pad);
            }
        }
    }
}


// Updates the SVG display with the latest Launchpad state
function updateDisplay(launchpad) {
    for (var x = 0; x < 9; x++) {
        for (var y = 0; y < 9; y++) {
            if (x == 8 && y == 0) continue;
            var color = launchpad.buffers[launchpad.visibleBuffer][x][y] || launchpad.colors[0][0];
            var shape = document.getElementById("key" + x + y);
            // Do not apply brightness correction to non-lit pads and buttons
            shape.style.opacity = (color == launchpad.colors[0][0]) ? 1 : launchpad.brightness;
            shape.style.fill = color;
        }
    }
}
