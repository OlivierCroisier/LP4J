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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sound.midi.InvalidMidiDataException;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyVararg;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MidiLaunchpadClientTest {

    private LaunchpadClient launchpadClient;

    @Mock
    private MidiProtocolClient midiProtocolClient;

    @Before
    public void init() {
        midiProtocolClient = mock(MidiProtocolClient.class);
        launchpadClient = new MidiLaunchpadClient(midiProtocolClient);
    }

    /*
    ================================================================================
    setBrightness
    ================================================================================
    */

    @Test
    public void setBrightness() {
        launchpadClient.setBrightness(Brightness.BRIGHTNESS_MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setBrightness_tooLow() {
        launchpadClient.setBrightness(Brightness.of(Brightness.MIN_VALUE - 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setBrightness_tooHigh() {
        launchpadClient.setBrightness(Brightness.of(Brightness.MAX_VALUE + 1));
    }

    @Test(expected = LaunchpadException.class)
    public void setBrightness_exception() throws InvalidMidiDataException {
        doThrow(new InvalidMidiDataException()).when(midiProtocolClient).brightness(anyInt(), anyInt());
        launchpadClient.setBrightness(Brightness.BRIGHTNESS_MIN);
    }

    /*
    ================================================================================
    setBuffers
    ================================================================================
    */

    @Test
    public void setBuffers() throws InvalidMidiDataException {
        launchpadClient.setBuffers(Buffer.BUFFER_0, Buffer.BUFFER_1, false, false);
        verify(midiProtocolClient).doubleBufferMode(0, 1, false, false);
    }

    @Test(expected = LaunchpadException.class)
    public void setBuffers_exception() throws InvalidMidiDataException {
        doThrow(new InvalidMidiDataException()).when(midiProtocolClient).doubleBufferMode(anyInt(), anyInt(), anyBoolean(), anyBoolean());
        launchpadClient.setBuffers(Buffer.BUFFER_0, Buffer.BUFFER_1, false, false);
    }

    /*
    ================================================================================
    setButtonLight
    ================================================================================
    */

    @Test(expected = LaunchpadException.class)
    public void setButtonLight_exception() throws InvalidMidiDataException {
        doThrow(new InvalidMidiDataException()).when(midiProtocolClient).buttonOn(anyInt(), anyInt());
        launchpadClient.setButtonLight(Button.UP, Color.BLACK, BackBufferOperation.COPY);
    }

    @Test
    public void setButtonLight_topButton_NONE() throws InvalidMidiDataException {
        launchpadClient.setButtonLight(Button.UP, Color.BLACK, BackBufferOperation.NONE);
        verify(midiProtocolClient).buttonOn(104, 0);
    }

    @Test
    public void setButtonLight_topButton_CLEAR() throws InvalidMidiDataException {
        launchpadClient.setButtonLight(Button.UP, Color.BLACK, BackBufferOperation.CLEAR);
        verify(midiProtocolClient).buttonOn(104, 8);
    }

    @Test
    public void setButtonLight_topButton_COPY() throws InvalidMidiDataException {
        launchpadClient.setButtonLight(Button.UP, Color.BLACK, BackBufferOperation.COPY);
        verify(midiProtocolClient).buttonOn(104, 12);
    }

    @Test
    public void setButtonLight_rightButton() throws InvalidMidiDataException {
        launchpadClient.setButtonLight(Button.VOL, Color.BLACK, BackBufferOperation.COPY);
        verify(midiProtocolClient).noteOn(8, 12);
    }

    @Test
    public void setButtonLight_rightButton_COPY() throws InvalidMidiDataException {
        launchpadClient.setButtonLight(Button.VOL, Color.BLACK, BackBufferOperation.COPY);
        verify(midiProtocolClient).noteOn(8, 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setButtonLight_XY_illegal_COPY() {
        launchpadClient.setButtonLight(Button.atTop(-1), Color.BLACK, BackBufferOperation.COPY);
    }

    @Test
    public void setButtonLight_topButton_XY_COPY() throws InvalidMidiDataException {
        launchpadClient.setButtonLight(Button.atTop(0), Color.BLACK, BackBufferOperation.COPY); // UP
        verify(midiProtocolClient).buttonOn(104, 12);
    }

    @Test
    public void setButtonLight_rightButton_XY_COPY() throws InvalidMidiDataException {
        launchpadClient.setButtonLight(Button.atRight(0), Color.BLACK, BackBufferOperation.COPY); // VOL
        verify(midiProtocolClient).noteOn(8, 12);
    }

    /*
    ================================================================================
    setPadLight
    ================================================================================
    */

    @Test
    public void setPadLight_COPY() throws InvalidMidiDataException {
        launchpadClient.setPadLight(Pad.at(0, 0), Color.BLACK, BackBufferOperation.COPY);
        verify(midiProtocolClient).noteOn(0, 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPadLight_illegal() {
        launchpadClient.setPadLight(Pad.at(-1, -1), Color.BLACK, BackBufferOperation.COPY);
    }

    @Test(expected = LaunchpadException.class)
    public void setPadLight_exception() throws InvalidMidiDataException {
        doThrow(new InvalidMidiDataException()).when(midiProtocolClient).noteOn(anyInt(), anyInt());
        launchpadClient.setPadLight(Pad.at(1, 1), Color.BLACK, BackBufferOperation.COPY);
    }

    /*
    ================================================================================
    reset
    ================================================================================
    */

    @Test
    public void reset() throws InvalidMidiDataException {
        launchpadClient.reset();
        verify(midiProtocolClient).reset();
    }

    @Test(expected = LaunchpadException.class)
    public void reset_exception() throws InvalidMidiDataException {
        doThrow(new InvalidMidiDataException()).when(midiProtocolClient).reset();
        launchpadClient.reset();
        verify(midiProtocolClient).reset();
    }

    /*
    ================================================================================
    testLights
    ================================================================================
    */

    @Test
    public void testLights() throws InvalidMidiDataException {
        launchpadClient.testLights(LightIntensity.LOW);
        verify(midiProtocolClient).lightsOn(125);
        launchpadClient.testLights(LightIntensity.MEDIUM);
        verify(midiProtocolClient).lightsOn(126);
        launchpadClient.testLights(LightIntensity.HIGH);
        verify(midiProtocolClient).lightsOn(127);
    }

    @Test(expected = LaunchpadException.class)
    public void testLights_exception() throws InvalidMidiDataException {
        doThrow(new InvalidMidiDataException()).when(midiProtocolClient).lightsOn(anyInt());
        launchpadClient.testLights(LightIntensity.LOW);
    }

    /*
    ================================================================================
    setLights
    ================================================================================
    */

    @Test(expected = IllegalArgumentException.class)
    public void setLights_null() {
        launchpadClient.setLights(null, BackBufferOperation.NONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLights_odd() {
        launchpadClient.setLights(new Color[1], BackBufferOperation.NONE);
    }

    @Test(expected = LaunchpadException.class)
    public void setLights_exception() throws InvalidMidiDataException {
        doThrow(new InvalidMidiDataException()).when(midiProtocolClient).notesOn((int[]) anyVararg());
        launchpadClient.setLights(new Color[]{Color.BLACK, Color.BLACK}, BackBufferOperation.COPY);
    }

    @Test
    public void setLights_COPY() throws InvalidMidiDataException {
        launchpadClient.setLights(new Color[]{Color.BLACK, Color.BLACK}, BackBufferOperation.COPY);
        verify(midiProtocolClient).notesOn(12, 12);
    }

    /*
    ================================================================================
    scrollText
    ================================================================================
    */

    @Test(expected = IllegalArgumentException.class)
    public void scrollText_speedTooLow() {
        launchpadClient.scrollText("Hello", Color.BLACK, ScrollSpeed.of(ScrollSpeed.MIN_VALUE - 1), false, BackBufferOperation.COPY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scrollText_speedTooHIgh() {
        launchpadClient.scrollText("Hello", Color.BLACK, ScrollSpeed.of(ScrollSpeed.MAX_VALUE + 1), false, BackBufferOperation.COPY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scrollText_nullColor() {
        launchpadClient.scrollText("Hello", null, ScrollSpeed.SPEED_MIN, false, BackBufferOperation.COPY);
    }

    @Test
    public void scrollText_COPY() throws InvalidMidiDataException {
        launchpadClient.scrollText("Hello", Color.BLACK, ScrollSpeed.SPEED_MIN, false, BackBufferOperation.COPY);
        verify(midiProtocolClient).text("Hello", 12, 1, false);
    }

    @Test(expected = LaunchpadException.class)
    public void scrollText_exception() throws InvalidMidiDataException {
        doThrow(new InvalidMidiDataException()).when(midiProtocolClient).text(anyString(), anyInt(), anyInt(), anyBoolean());
        launchpadClient.scrollText("Hello", Color.BLACK, ScrollSpeed.SPEED_MIN, false, BackBufferOperation.COPY);
    }


}
