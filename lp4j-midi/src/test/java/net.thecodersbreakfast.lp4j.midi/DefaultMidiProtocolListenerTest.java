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

import net.thecodersbreakfast.lp4j.api.Button;
import net.thecodersbreakfast.lp4j.api.LaunchpadListener;
import net.thecodersbreakfast.lp4j.api.Pad;
import net.thecodersbreakfast.lp4j.midi.protocol.DefaultMidiProtocolListener;
import net.thecodersbreakfast.lp4j.midi.protocol.MidiProtocolListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DefaultMidiProtocolListenerTest {

    private static final long TIMESTAMP = -1;

    private static final int BUTTON_UP = 104;
    private static final int BUTTON_UNKNOWN = 100;
    private static final int NOTE_VOL = 8;
    private static final int NOTE_PAD00 = 0;
    private static final int NOTE_UNKNOWN = -1;

    private MidiProtocolListener midiProtocolListener;
    private LaunchpadListener listener;

    @Before
    public void init() {
        this.listener = Mockito.mock(LaunchpadListener.class);
        this.midiProtocolListener = new DefaultMidiProtocolListener(listener);
    }

    @Test
    public void onButtonOn_up() {
        midiProtocolListener.onButtonOn(BUTTON_UP, TIMESTAMP);
        Mockito.verify(listener).onButtonPressed(Button.UP, TIMESTAMP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void onButtonOn_unknown() {
        midiProtocolListener.onButtonOn(BUTTON_UNKNOWN, TIMESTAMP);
        Mockito.verify(listener).onButtonPressed(Button.UP, TIMESTAMP);
    }

    @Test
    public void onButtonOff_up() {
        midiProtocolListener.onButtonOff(BUTTON_UP, TIMESTAMP);
        Mockito.verify(listener).onButtonReleased(Button.UP, TIMESTAMP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void onButtonOff_unknown() {
        midiProtocolListener.onButtonOff(BUTTON_UNKNOWN, TIMESTAMP);
        Mockito.verify(listener).onButtonReleased(Button.UP, TIMESTAMP);
    }

    @Test
    public void onNoteOn_vol() {
        midiProtocolListener.onNoteOn(NOTE_VOL, TIMESTAMP);
        Mockito.verify(listener).onButtonPressed(Button.VOL, TIMESTAMP);
    }

    @Test
    public void onNoteOff_vol() {
        midiProtocolListener.onNoteOff(NOTE_VOL, TIMESTAMP);
        Mockito.verify(listener).onButtonReleased(Button.VOL, TIMESTAMP);
    }

    @Test
    public void onNoteOn_pad00() {
        midiProtocolListener.onNoteOn(NOTE_PAD00, TIMESTAMP);
        Mockito.verify(listener).onPadPressed(Pad.at(0, 0), TIMESTAMP);
    }

    @Test
    public void onNoteOff_pad00() {
        midiProtocolListener.onNoteOff(NOTE_PAD00, TIMESTAMP);
        Mockito.verify(listener).onPadReleased(Pad.at(0, 0), TIMESTAMP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void onNoteOn_unknown() {
        midiProtocolListener.onNoteOn(NOTE_UNKNOWN, TIMESTAMP);
        Mockito.verify(listener).onPadPressed(Pad.at(0, 0), TIMESTAMP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void onNoteOff_unknown() {
        midiProtocolListener.onNoteOff(NOTE_UNKNOWN, TIMESTAMP);
        Mockito.verify(listener).onPadReleased(Pad.at(0, 0), TIMESTAMP);
    }

    @Test
    public void onTextScrolled() {
        midiProtocolListener.onTextScrolled(TIMESTAMP);
        Mockito.verify(listener).onTextScrolled(TIMESTAMP);
    }


}
