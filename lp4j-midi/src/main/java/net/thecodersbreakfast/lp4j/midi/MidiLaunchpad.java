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

import net.thecodersbreakfast.lp4j.api.Launchpad;
import net.thecodersbreakfast.lp4j.api.LaunchpadClient;
import net.thecodersbreakfast.lp4j.api.LaunchpadException;
import net.thecodersbreakfast.lp4j.api.LaunchpadListener;
import net.thecodersbreakfast.lp4j.midi.protocol.DefaultMidiProtocolClient;
import net.thecodersbreakfast.lp4j.midi.protocol.DefaultMidiProtocolListener;
import net.thecodersbreakfast.lp4j.midi.protocol.DefaultMidiProtocolReceiver;
import net.thecodersbreakfast.lp4j.midi.protocol.MidiProtocolListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import java.io.IOException;

/**
 * Represents a physical MIDI Launchpad device.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class MidiLaunchpad implements Launchpad {

    /** The Launchpad's input channel (Device -> LP4J). */
    private final Receiver receiver;
    /** The Launchpad's output channel (LP4J -> Device). */
    private final Transmitter transmitter;
    /** The MIDI configuration holder. */
    private MidiDeviceConfiguration configuration;

    /** Indicates that the output channel has been successfully opened. */
    private boolean openedOutputDevice = false;
    /** Indicates that the input channel has been successfully opened. */
    private boolean openedInputDevice = false;

    /**
     * Constructor.
     *
     * @param configuration The MIDI configuration to use. Must not be null.
     * @throws MidiUnavailableException If the input or output channels cannot be opened.
     */
    public MidiLaunchpad(MidiDeviceConfiguration configuration) throws MidiUnavailableException {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration must not be null");
        }
        this.configuration = configuration;

        MidiDevice outputDevice = configuration.getOutputDevice();
        if (outputDevice != null) {
            if (!outputDevice.isOpen()) {
                outputDevice.open();
            }
            openedOutputDevice = true;
            this.receiver = outputDevice.getReceiver();
        } else {
            this.receiver = null;
        }

        MidiDevice inputDevice = configuration.getInputDevice();
        if (inputDevice != null) {
            if (!inputDevice.isOpen()) {
                inputDevice.open();
            }
            openedInputDevice = true;
            this.transmitter = inputDevice.getTransmitter();
        } else {
            this.transmitter = null;

        }
    }

    /** {@inheritDoc} */
    @Override
    public LaunchpadClient getClient() {
        if (this.receiver == null) {
            throw new LaunchpadException("Unable to provide a client, because no Receiver or Output Device have been configured.");
        }
        return new MidiLaunchpadClient(new DefaultMidiProtocolClient(this.receiver));
    }

    /** {@inheritDoc} */
    @Override
    public void setListener(LaunchpadListener listener) {
        if (transmitter == null) {
            throw new LaunchpadException("Unable to set the listener, because no Transmitter or Input Device have been configured.");
        }
        MidiProtocolListener midiProtocolListener = new DefaultMidiProtocolListener(listener);
        Receiver midiReceiver = new DefaultMidiProtocolReceiver(midiProtocolListener);
        transmitter.setReceiver(midiReceiver);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        if (configuration == null) {
            return;
        }
        if (openedOutputDevice) {
            MidiDevice outputDevice = configuration.getOutputDevice();
            if (outputDevice != null && outputDevice.isOpen()) {
                outputDevice.close();
            }
        }
        if (openedInputDevice) {
            MidiDevice inputDevice = configuration.getInputDevice();
            if (inputDevice != null && inputDevice.isOpen()) {
                inputDevice.close();
            }
        }
    }

}
