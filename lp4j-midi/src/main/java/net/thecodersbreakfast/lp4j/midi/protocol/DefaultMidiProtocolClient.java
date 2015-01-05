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

package net.thecodersbreakfast.lp4j.midi.protocol;

import net.thecodersbreakfast.lp4j.api.LaunchpadException;

import javax.sound.midi.*;
import java.nio.charset.Charset;

/**
 * Default implementation of a  {@link net.thecodersbreakfast.lp4j.midi.protocol.MidiProtocolClient}.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class DefaultMidiProtocolClient implements MidiProtocolClient {

    /** The Launchpad's Receiver, to which commands are sent. */
    private final Receiver receiver;

    /**
     * Constructor.
     *
     * @param receiver The Launchpad's MIDI Receiver. Must not be null.
     */
    public DefaultMidiProtocolClient(Receiver receiver) {
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver must not be null.");
        }
        this.receiver = receiver;
    }

    // ================================================================================
    // Low-level MIDI output API
    // ================================================================================

    /** {@inheritDoc} */
    @Override
    public void reset() throws InvalidMidiDataException {
        sendShortMessage(ShortMessage.CONTROL_CHANGE, 0, 0);
    }

    /** {@inheritDoc} */
    @Override
    public void lightsOn(int intensity) throws InvalidMidiDataException {
        sendShortMessage(ShortMessage.CONTROL_CHANGE, 0, intensity);
    }

    /** {@inheritDoc} */
    @Override
    public void noteOn(int note, int color) throws InvalidMidiDataException {
        sendShortMessage(ShortMessage.NOTE_ON, note, color);
    }

    /** {@inheritDoc} */
    @Override
    public void noteOff(int note) throws InvalidMidiDataException {
        sendShortMessage(ShortMessage.NOTE_OFF, note, 0);
    }

    /**
     * {@inheritDoc}
     *
     * @param colors {@inheritDoc} Must not be null.
     */
    @Override
    public void notesOn(int... colors) throws InvalidMidiDataException {
        if (colors == null) {
            throw new IllegalArgumentException("Colors should not be null.");
        }
        int nbMessages = colors.length / 2;
        for (int i = 0; i < nbMessages; i++) {
            sendShortMessage(ShortMessage.NOTE_ON, 3, colors[i * 2], colors[i * 2 + 1]);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void layout(int mode) throws InvalidMidiDataException {
        sendShortMessage(ShortMessage.CONTROL_CHANGE, 0, mode);
    }

    /** {@inheritDoc} */
    @Override
    public void buttonOn(int button, int color) throws InvalidMidiDataException {
        sendShortMessage(ShortMessage.CONTROL_CHANGE, button, color);
    }

    /** {@inheritDoc} */
    @Override
    public void brightness(int numerator, int denominator) throws InvalidMidiDataException {
        if (numerator < 9) {
            sendShortMessage(ShortMessage.CONTROL_CHANGE, 30, 16 * (numerator - 1) + (denominator - 3));
        } else {
            sendShortMessage(ShortMessage.CONTROL_CHANGE, 31, 16 * (numerator - 9) + (denominator - 3));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void text(String text, int color, int speed, boolean loop) throws InvalidMidiDataException {
        if (loop) {
            color += 64;
        }
        byte[] header = {(byte) 240, 0, 32, 41, 9, (byte) color};
        byte[] chars = text == null ? new byte[]{} : text.getBytes(Charset.forName("ASCII"));
        byte[] message = new byte[chars.length + 8];
        System.arraycopy(header, 0, message, 0, header.length);
        message[header.length] = (byte) speed;
        System.arraycopy(chars, 0, message, header.length + 1, chars.length);
        message[message.length - 1] = (byte) 247;
        sendSysExMessage(message);
    }

    /** {@inheritDoc} */
    @Override
    public void doubleBufferMode(int displayBuffer, int writeBuffer, boolean copyVisibleBufferToWriteBuffer, boolean autoSwap) throws InvalidMidiDataException {
        int mode = 32 + 4 * writeBuffer + displayBuffer;
        if (copyVisibleBufferToWriteBuffer) {
            mode |= 16;
        }
        if (autoSwap) {
            mode |= 8;
        }
        sendShortMessage(ShortMessage.CONTROL_CHANGE, 0, mode);
    }


    // ================================================================================
    // Utils
    // ================================================================================

    private void sendShortMessage(int command, int controller, int data) throws LaunchpadException, InvalidMidiDataException {
        ShortMessage message = new ShortMessage();
        message.setMessage(command, controller, data);
        send(message);
    }

    private void sendShortMessage(int command, int channel, int controller, int data) throws LaunchpadException, InvalidMidiDataException {
        ShortMessage message = new ShortMessage();
        message.setMessage(command, channel, controller, data);
        send(message);
    }

    private void sendSysExMessage(byte[] data) throws InvalidMidiDataException {
        SysexMessage message = new SysexMessage();
        message.setMessage(data, data.length);
        send(message);
    }

    private void send(MidiMessage message) {
        this.receiver.send(message, -1);
    }

}
