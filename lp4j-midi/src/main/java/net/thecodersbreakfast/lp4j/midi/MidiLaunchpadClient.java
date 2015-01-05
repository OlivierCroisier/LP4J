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

package net.thecodersbreakfast.lp4j.midi;

import net.thecodersbreakfast.lp4j.api.*;
import net.thecodersbreakfast.lp4j.midi.protocol.MidiProtocolClient;

import javax.sound.midi.InvalidMidiDataException;

/**
 * A client to communicate with a MIDI Launchpad device.
 *
 * <p>This class serves as an adapter between the high-level LP4J API and the low-level MIDI communication layer
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class MidiLaunchpadClient implements LaunchpadClient {

    /** Low-level MIDI client to communicate with the Launchpad. */
    private final MidiProtocolClient midiProtocolClient;

    /**
     * Constructor.
     *
     * @param midiProtocolClient The low-level client to adapt. Must not be null.
     */
    public MidiLaunchpadClient(MidiProtocolClient midiProtocolClient) {
        if (midiProtocolClient == null) {
            throw new IllegalArgumentException("MidiClient must not be null.");
        }
        this.midiProtocolClient = midiProtocolClient;
    }

    /*
    ================================================================================
    Launchpad API
    ================================================================================
    */

    /** {@inheritDoc} */
    @Override
    public void reset() {
        try {
            midiProtocolClient.reset();
        } catch (InvalidMidiDataException e) {
            throw new LaunchpadException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param intensity {@inheritDoc} Must not be null.
     */
    @Override
    public void testLights(LightIntensity intensity) {
        if (intensity == null) {
            throw new IllegalArgumentException("Light intensity must not be null.");
        }

        int value;
        switch (intensity) {
            case LOW:
                value = 125;
                break;
            case MEDIUM:
                value = 126;
                break;
            case HIGH:
                value = 127;
                break;
            default:
                throw new IllegalArgumentException("Unknown intensity value : " + intensity.name());
        }

        try {
            midiProtocolClient.lightsOn(value);
        } catch (InvalidMidiDataException e) {
            throw new LaunchpadException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param colors {@inheritDoc} Must be of even size.
     * @param operation {@inheritDoc} Must not be null.
     */
    @Override
    public void setLights(Color[] colors, BackBufferOperation operation) {
        if (colors == null) {
            throw new IllegalArgumentException("Colors must not be null");
        }
        int nbColors = colors.length;
        if ((nbColors & 1) != 0) {
            throw new IllegalArgumentException("The number of colors for a batch update must be even.");
        }
        if (operation == null) {
            throw new IllegalArgumentException("BackBuffer operation must not be null.");
        }
        int[] rawColors = new int[nbColors];
        for (int i = 0; i < nbColors; i++) {
            rawColors[i] = toRawColor(colors[i], operation);
        }

        try {
            midiProtocolClient.notesOn(rawColors);
        } catch (InvalidMidiDataException e) {
            throw new LaunchpadException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param pad {@inheritDoc} Must not be null.
     * @param color {@inheritDoc} Must not be null.
     * @param operation {@inheritDoc} Must not be null.
     */
    @Override
    public void setPadLight(Pad pad, Color color, BackBufferOperation operation) {
        if (pad == null) {
            throw new IllegalArgumentException("Pad must not be null.");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color must not be null.");
        }
        if (operation == null) {
            throw new IllegalArgumentException("BackBuffer operation must not be null.");
        }

        int rawCoords = toRawCoords(pad.getX(), pad.getY());
        int rawColor = toRawColor(color, operation);

        try {
            midiProtocolClient.noteOn(rawCoords, rawColor);
        } catch (InvalidMidiDataException e) {
            throw new LaunchpadException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param button {@inheritDoc} Must not be null.
     * @param color {@inheritDoc} Must not be null.
     * @param operation {@inheritDoc} Must not be null.
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

        try {
            int rawColor = toRawColor(color, operation);
            if (button.isTopButton()) {
                int rawCoords = 104 + button.getCoordinate();
                midiProtocolClient.buttonOn(rawCoords, rawColor);
            } else {
                int rawCoords = toRawCoords(8, button.getCoordinate());
                midiProtocolClient.noteOn(rawCoords, rawColor);
            }
        } catch (InvalidMidiDataException e) {
            throw new LaunchpadException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param brightness {@inheritDoc} Must not be null.
     */
    @Override
    public void setBrightness(Brightness brightness) {
        if (brightness == null) {
            throw new IllegalArgumentException("Brightness must not be null");
        }

        int level = brightness.getBrightness();
        try {
            midiProtocolClient.brightness(1, 18 - level);
        } catch (InvalidMidiDataException e) {
            throw new LaunchpadException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param visibleBuffer {@inheritDoc} Must not be null.
     * @param writeBuffer {@inheritDoc} Must not be null.
     */
    @Override
    public void setBuffers(Buffer visibleBuffer, Buffer writeBuffer, boolean copyVisibleBufferToWriteBuffer, boolean autoSwap) {
        if (visibleBuffer == null) {
            throw new IllegalArgumentException("Visible buffer must not be null.");
        }
        if (writeBuffer == null) {
            throw new IllegalArgumentException("Write buffer must not be null.");
        }

        try {
            midiProtocolClient.doubleBufferMode(getBufferValue(visibleBuffer), getBufferValue(writeBuffer), copyVisibleBufferToWriteBuffer, autoSwap);
        } catch (InvalidMidiDataException e) {
            throw new LaunchpadException(e);
        }
    }

    /**
     * Converts an abstract Buffer into its Launchpad-specific low-level representation.
     *
     * @param buffer The buffer to convert. Must not be null.
     * @return The buffer's low-level representation.
     */
    private int getBufferValue(Buffer buffer) {
        return buffer == Buffer.BUFFER_0 ? 0 : 1;
    }

    /**
     * {@inheritDoc}
     *
     * @param color {@inheritDoc} Must not be null.
     * @param speed {@inheritDoc} Must not be null.
     * @param operation {@inheritDoc} Must not be null.
     */
    @Override
    public void scrollText(String text, Color color, ScrollSpeed speed, boolean loop, BackBufferOperation operation) {
        if (color == null) {
            throw new IllegalArgumentException("Color must not be null.");
        }
        if (speed == null) {
            throw new IllegalArgumentException("Speed must not be null.");
        }
        if (operation == null) {
            throw new IllegalArgumentException("Operation must not be null.");
        }

        int rawColor = toRawColor(color, operation);

        try {
            midiProtocolClient.text(text, rawColor, speed.getScrollSpeed(), loop);
        } catch (InvalidMidiDataException e) {
            throw new LaunchpadException(e);
        }
    }

    /*
    ================================================================================
    Utils
    ================================================================================
    */

    /**
     * Converts a Color into its Launchpad-specific low-level representation
     *
     * @param color The Color to convert. Must not be null.
     * @param operation What to do on the backbuffer. Must not be null.
     * @return A binary representation of the color and how it should be applied to the Launchpad's buffers.
     */
    private byte toRawColor(Color color, BackBufferOperation operation) {
        int flags = 0;
        switch (operation) {
            case NONE:
                flags = 0;
                break;
            case CLEAR:
                flags = 8;
                break;
            case COPY:
                flags = 12;
                break;
        }
        return (byte) (flags + color.getRed() + (16 * color.getGreen()));
    }

    /**
     * Converts an X-Y coordinates into its Launchpad-specific low-level representation.
     *
     * @param x The X coordinate.
     * @param y The Y corrdinate.
     * @return The low-level representation of those coordinates.
     */
    private int toRawCoords(int x, int y) {
        return x + 16 * y;
    }

}
