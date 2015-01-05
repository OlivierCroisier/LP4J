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

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 * A MIDI Receiver, to which the Launchpad sends low-level commands.
 * Those commands are parsed and transmitted (still in a close-to-the-metal format) to a
 * {@link net.thecodersbreakfast.lp4j.midi.protocol.MidiProtocolListener} for further processing.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class DefaultMidiProtocolReceiver implements Receiver {

    /** The MidiProtocolListener to to notify when commands are received. */
    private MidiProtocolListener midiProtocolListener;

    /**
     * Constructor.
     *
     * @param listener The MidiProtocolListener to to notify when commands are received. Must not be null.
     */
    public DefaultMidiProtocolReceiver(MidiProtocolListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null.");
        }
        this.midiProtocolListener = listener;
    }

    /**
     * {@inheritDoc}
     *
     * THIS METHOD SHOULD ONLY BE CALLED BY THE LAUNCHPAD DEVICE.
     */
    @Override
    public void send(MidiMessage message, long timestamp) {
        if (message instanceof ShortMessage) {
            handleShortMessage((ShortMessage) message, timestamp);
        } else {
            throw new LaunchpadException("Unknown event : " + message);
        }
    }

    /**
     * Parses and routes MIDI short messages to adequate sub-handlers.
     *
     * @param message The inconming message.
     * @param timestamp When the message arrived.
     */
    protected void handleShortMessage(ShortMessage message, long timestamp) {
        int status = message.getStatus();
        int note = message.getData1();
        int velocity = message.getData2();

        if (status == ShortMessage.NOTE_ON) {
            handleNoteOnMessage(note, velocity, timestamp);
        } else if (status == ShortMessage.CONTROL_CHANGE) {
            handleControlChangeMessage(note, velocity, timestamp);
        } else {
            throw new LaunchpadException("Unknown event : " + message);
        }
    }

    /**
     * Parses "note on" messages and notifies the to the higher-level {@code midiProtocolListener}
     *
     * @param note The activated note.
     * @param velocity The note velocity.
     * @param timestamp When the note was activated.
     */
    protected void handleNoteOnMessage(int note, int velocity, long timestamp) {
        if (velocity == 0) {
            midiProtocolListener.onNoteOff(note, timestamp);
        } else {
            midiProtocolListener.onNoteOn(note, timestamp);
        }
    }

    /**
     * Parses "control" messages and notifies the to the higher-level {@code midiProtocolListener}
     *
     * @param note The activated note.
     * @param velocity The note velocity.
     * @param timestamp When the note was activated.
     */
    protected void handleControlChangeMessage(int note, int velocity, long timestamp) {
        if (note == 0 && velocity == 3) {
            midiProtocolListener.onTextScrolled(timestamp);
        } else if (velocity == 0) {
            midiProtocolListener.onButtonOff(note, timestamp);
        } else {
            midiProtocolListener.onButtonOn(note, timestamp);
        }
    }

    @Override
    public void close() {
    }
}
