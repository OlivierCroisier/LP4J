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

import net.thecodersbreakfast.lp4j.midi.protocol.DefaultMidiProtocolClient;
import net.thecodersbreakfast.lp4j.midi.protocol.MidiProtocolClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMidiProtocolClientTest {

    public static final int LIGHT_INTENSITY = 125;
    public static final int COLOR_RED = 3;
    public static final int COLOR_BLACK = 0;
    public static final int NOTE_1_1 = 17;
    public static final int LAYOUT_XY = 1;
    public static final int BUTTON_UP = 104;

    @Mock
    private Receiver receiver;
    private MidiProtocolClient midiProtocolClient;
    private ArgumentCaptor<ShortMessage> shortMessage;
    private ArgumentCaptor<SysexMessage> sysexMessage;

    @Before
    public void init() {
        midiProtocolClient = new DefaultMidiProtocolClient(receiver);
        shortMessage = ArgumentCaptor.forClass(ShortMessage.class);
        sysexMessage = ArgumentCaptor.forClass(SysexMessage.class);
    }

    /*
    ================================================================================
    reset
    ================================================================================
    */

    @Test
    public void testReset() throws Exception {
        midiProtocolClient.reset();

        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, 0, 0);
    }

    /*
    ================================================================================
    lightsOn
    ================================================================================
    */

    @Test
    public void testLightsOn() throws Exception {
        midiProtocolClient.lightsOn(LIGHT_INTENSITY);

        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, 0, LIGHT_INTENSITY);
    }

    /*
    ================================================================================
    noteOn
    ================================================================================
    */

    @Test
    public void testNoteOn() throws Exception {
        midiProtocolClient.noteOn(NOTE_1_1, COLOR_RED);

        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.NOTE_ON, NOTE_1_1, COLOR_RED);
    }

    /*
    ================================================================================
    noteOff
    ================================================================================
    */

    @Test
    public void testNoteOff() throws Exception {
        midiProtocolClient.noteOff(NOTE_1_1);

        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.NOTE_OFF, NOTE_1_1, COLOR_BLACK);
    }

    /*
    ================================================================================
    notesOn
    ================================================================================
    */

    @Test
    public void testNotesOn() throws Exception {
        midiProtocolClient.notesOn(42, 42, 42, 42);

        verify(receiver, times(2)).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.NOTE_ON, 42, 42);
        Assert.assertEquals(3, shortMessage.getValue().getChannel());
    }

    /*
    ================================================================================
    layout
    ================================================================================
    */

    @Test
    public void testLayout() throws Exception {
        midiProtocolClient.layout(LAYOUT_XY);

        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, 0, LAYOUT_XY);
    }

    /*
    ================================================================================
    buttonOn
    ================================================================================
    */

    @Test
    public void testButtonOn() throws Exception {
        midiProtocolClient.buttonOn(BUTTON_UP, COLOR_RED);

        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, BUTTON_UP, COLOR_RED);
    }

    /*
    ================================================================================
    brightness
    ================================================================================
    */

    @Test
    public void testBrightness_low() throws Exception {
        midiProtocolClient.brightness(1, 3);

        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, 30, 0);
    }

    @Test
    public void testBrightness_high() throws Exception {
        midiProtocolClient.brightness(9, 3);

        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, 31, 0);
    }

    /*
    ================================================================================
    text
    ================================================================================
    */

    @Test
    public void testText() throws Exception {
        midiProtocolClient.text("Hello", COLOR_BLACK, 1, false);

        verify(receiver).send(sysexMessage.capture(), eq(-1L));
        checkSysexMessage(sysexMessage.getValue(), new byte[]{0, 32, 41, 9, 0, 1, 72, 101, 108, 108, 111, (byte) 247});
    }

    @Test
    public void testText_loop() throws Exception {
        midiProtocolClient.text("Hello", COLOR_BLACK, 1, true);

        verify(receiver).send(sysexMessage.capture(), eq(-1L));
        checkSysexMessage(sysexMessage.getValue(), new byte[]{0, 32, 41, 9, 64, 1, 72, 101, 108, 108, 111, (byte) 247});
    }

    /*
    ================================================================================
    doubleBufferMode
    ================================================================================
    */

    @Test
    public void testDoubleBufferMode_0_1() throws Exception {
        midiProtocolClient.doubleBufferMode(0, 1, false, false);
        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, 0, 36);
    }

    @Test
    public void testDoubleBufferMode_1_0() throws Exception {
        midiProtocolClient.doubleBufferMode(1, 0, false, false);
        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, 0, 33);
    }

    @Test
    public void testDoubleBufferMode_COPY() throws Exception {
        midiProtocolClient.doubleBufferMode(0, 0, true, false);
        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, 0, 32 + 16);
    }

    @Test
    public void testDoubleBufferMode_LOOP() throws Exception {
        midiProtocolClient.doubleBufferMode(0, 0, false, true);
        verify(receiver).send(shortMessage.capture(), eq(-1L));
        checkShortMessage(shortMessage.getValue(), ShortMessage.CONTROL_CHANGE, 0, 32 + 8);
    }

    /*
    ================================================================================
    UTILS
    ================================================================================
    */

    private void checkShortMessage(ShortMessage message, int command, int data1, int data2) {
        Assert.assertEquals(command, message.getCommand());
        Assert.assertEquals(data1, message.getData1());
        Assert.assertEquals(data2, message.getData2());
    }

    private void checkSysexMessage(SysexMessage message, byte[] data) {
        Assert.assertArrayEquals(data, message.getData());
    }
}
