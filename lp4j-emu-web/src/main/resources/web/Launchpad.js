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

// Abstract Launchpad definition.
function Launchpad() {
    this.colors = [
        ["#CCC", "#060", "#0C2", "#0F4"],
        ["#600", "#660", "#6C2", "#6F4"],
        ["#C00", "#C60", "#CC2", "#CF4"],
        ["#F00", "#F60", "#FC2", "#FF4"]
    ];
    this.buffers = new Array(2);
    this.visibleBuffer = 0;
    this.writeBuffer = 0;
    this.backBuffer = 1;
    this.brightness = 1;
    this.listener = null;
    this.reset();
}

Launchpad.prototype.setListener = function (launchpadListener) {
    this.listener = launchpadListener;
};

// ----------------------------------------
// Client methods
// ----------------------------------------

Launchpad.prototype.reset = function () {
    this.buffers[0] = new Array(9);
    this.buffers[1] = new Array(9);
    for (var i = 0; i < 9; i++) {
        this.buffers[0][i] = new Array(9);
        this.buffers[1][i] = new Array(9);
    }
};

Launchpad.prototype.testLights = function (intensity) {
    this.setBrightness(intensity);
    var self = this;
    this.buffers[this.writeBuffer].map(function (a) {
        a.fill(self.colors[3][3]);
    });
};

Launchpad.prototype.setLights = function (colors, operation) {
    // Not implemented yet
};

Launchpad.prototype.setPadLight = function (x, y, color, operation) {
    this.buffers[this.writeBuffer][x][y + 1] = this.colors[color.r][color.g];
    switch (operation) {
        case 'NONE' :
            break;
        case 'COPY' :
            this.buffers[this.backBuffer][x][y + 1] = this.colors[color.r][color.g];
            break;
        case 'CLEAR' :
            this.buffers[this.backBuffer][x][y + 1] = this.colors[0][0];
            break;
    }
};

Launchpad.prototype.setButtonLight = function (t, i, color, operation) {
    var x = 0;
    var y = 0;
    if (t === true) {
        x = i;
        y = -1;
    } else {
        x = 8;
        y = i;
    }
    this.setPadLight(x, y, color, operation);
};

Launchpad.prototype.setBrightness = function (level) {
    this.brightness = 0.1 + 0.06 * level;
};

Launchpad.prototype.setBuffers = function (visibleBuffer, writeBuffer, copyVisibleBufferToWriteBuffer, autoSwap) {
    this.visibleBuffer = this.bufIdx(visibleBuffer);
    this.writeBuffer = this.bufIdx(writeBuffer);
    this.backBuffer = 1 - this.writeBuffer;
    if (copyVisibleBufferToWriteBuffer === true) {
        this.buffers[this.writeBuffer] = this.buffers[this.visibleBuffer].clone;
    }
};

Launchpad.prototype.bufIdx = function (buffer) {
    return (buffer === 'BUFFER_0' ? 0 : 1);
};

Launchpad.prototype.scrollText = function (text, color, speed, loop, operation) {
    // Not implemented yet
};

// ----------------------------------------
// Listener Methods
// ----------------------------------------

// Definition of a listener allowing to be notified when the user interacts with the Launchpad interface
function LaunchpadListener() {
}

LaunchpadListener.prototype.onPadPressed = function (x, y) {
};

LaunchpadListener.prototype.onPadReleased = function (x, y) {
};

LaunchpadListener.prototype.onButtonPressed = function (x, y) {
};

LaunchpadListener.prototype.onButtonReleased = function (x, y) {
};

LaunchpadListener.prototype.onTextScrolled = function () {
};

