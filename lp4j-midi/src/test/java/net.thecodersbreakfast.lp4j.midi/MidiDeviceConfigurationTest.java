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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sound.midi.MidiDevice;

@RunWith(MockitoJUnitRunner.class)
public class MidiDeviceConfigurationTest {

    @Mock
    private MidiDevice inputDevice;

    @Mock
    private MidiDevice outputDevice;

    private MidiDeviceConfiguration configuration;

    @Before
    public void init() {
        configuration = new MidiDeviceConfiguration(inputDevice, outputDevice);
    }

    @Test
    public void testGetInputDevice() throws Exception {
        MidiDevice device = configuration.getInputDevice();
        Assert.assertEquals(inputDevice, device);
    }

    @Test
    public void testGetOutputDevice() throws Exception {
        MidiDevice device = configuration.getOutputDevice();
        Assert.assertEquals(outputDevice, device);
    }
}
