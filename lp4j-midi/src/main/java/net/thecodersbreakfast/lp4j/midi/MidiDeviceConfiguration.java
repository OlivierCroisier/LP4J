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

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

/**
 * Configuration for MIDI I/O.
 *
 * <p>Requires both an inbound and outbound MidiDevices, because data is sent from (pad events) and to (lights, etc.)
 * the Launchpad.
 *
 * <p>In most cases, it should suffice to call the {@link MidiDeviceConfiguration#autodetect()} factory method to get
 * a working configuration.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class MidiDeviceConfiguration {

    /** Device signature of a Launchpad S, used for autodetection. */
    public static final String DEVICE_SIGNATURE = "Launchpad S";

    /** Inbound communication channel. */
    private final MidiDevice inputDevice;
    /** Outbound communication channel. */
    private final MidiDevice outputDevice;

    /**
     * Tries to auto-detect a MIDI Launchpad based on its device signature.
     *
     * @return The auto-detected configuration.
     * @throws MidiUnavailableException If an error occurs during device probing.
     */
    public static MidiDeviceConfiguration autodetect() throws MidiUnavailableException {
        MidiDevice inputDevice = autodetectInputDevice();
        MidiDevice outputDevice = autodetectOutputDevice();
        return new MidiDeviceConfiguration(inputDevice, outputDevice);
    }

    /**
     * Constructor.
     *
     * @param inputDevice The inbound Midi channel.
     * @param outputDevice The outbound midi channel.
     */
    public MidiDeviceConfiguration(MidiDevice inputDevice, MidiDevice outputDevice) {
        this.inputDevice = inputDevice;
        this.outputDevice = outputDevice;
    }

    /**
     * Returns the inbound communication channel.
     *
     * @return the inbound communication channel.
     */
    public MidiDevice getInputDevice() {
        return inputDevice;
    }

    /**
     * Returns the outbound communication channel.
     *
     * @return the outbound communication channel.
     */
    public MidiDevice getOutputDevice() {
        return outputDevice;
    }

    /**
     * Tries to detect a valid outbound communication channel, based on a known device signature
     * (see {@link net.thecodersbreakfast.lp4j.midi.MidiDeviceConfiguration#DEVICE_SIGNATURE}).
     *
     * @return A valid outbound communication channel, or {@code null} if non was found.
     * @throws MidiUnavailableException if the requested device is not available due to resource restrictions
     */
    public static MidiDevice autodetectOutputDevice() throws MidiUnavailableException {
        MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : midiDeviceInfo) {
            if (info.getDescription().contains(DEVICE_SIGNATURE) || info.getName().contains(DEVICE_SIGNATURE)) {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                if (device.getMaxReceivers() == -1) {
                    return device;
                }
            }
        }
        return null;
    }

    /**
     * Tries to detect a valid inbound communication channel, based on a known device signature
     * (see {@link net.thecodersbreakfast.lp4j.midi.MidiDeviceConfiguration#DEVICE_SIGNATURE}).
     *
     * @return A valid outbound communication channel, or {@code null} if non was found.
     * @throws MidiUnavailableException if the requested device is not available due to resource restrictions
     */
    public static MidiDevice autodetectInputDevice() throws MidiUnavailableException {
        MidiDevice.Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : midiDeviceInfo) {
            if (info.getDescription().contains(DEVICE_SIGNATURE) || info.getName().contains(DEVICE_SIGNATURE)) {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                if (device.getMaxTransmitters() == -1) {
                    return device;
                }
                device.close();
            }
        }
        return null;
    }

}
