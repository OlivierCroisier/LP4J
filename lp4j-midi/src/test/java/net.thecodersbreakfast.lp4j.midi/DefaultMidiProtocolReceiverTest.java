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

import net.thecodersbreakfast.lp4j.api.LaunchpadException;
import net.thecodersbreakfast.lp4j.midi.protocol.DefaultMidiProtocolReceiver;
import net.thecodersbreakfast.lp4j.midi.protocol.MidiProtocolListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sound.midi.*;

public class DefaultMidiProtocolReceiverTest {

    private static final int VELOCITY_PRESSED = 127;
    private static final int VELOCITY_RELEASED = 0;
    private static final int VELOCITY_TEXT_SCROLLED = 3;
    private static final long TIMESTAMP = -1;

    private Receiver receiver;
    private MidiProtocolListener listener;

    @Before
    public void init() {
        listener = Mockito.mock(MidiProtocolListener.class);
        receiver = new DefaultMidiProtocolReceiver(listener);
    }

    @Test(expected = LaunchpadException.class)
    public void send_sysexMessage() {
        MidiMessage message = new SysexMessage();
        receiver.send(message, TIMESTAMP);
    }

    @Test(expected = LaunchpadException.class)
    public void send_metaMessage() {
        MidiMessage message = new MetaMessage();
        receiver.send(message, TIMESTAMP);
    }

    @Test(expected = LaunchpadException.class)
    public void send_shortMessage_unknown() throws Exception {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.STOP, 0, 0);
        receiver.send(message, TIMESTAMP);
    }

    @Test
    public void send_noteOn_pressed() throws Exception {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.NOTE_ON, 42, VELOCITY_PRESSED);
        receiver.send(message, TIMESTAMP);
        Mockito.verify(listener).onNoteOn(42, TIMESTAMP);
    }

    @Test
    public void send_noteOn_released() throws Exception {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.NOTE_ON, 42, VELOCITY_RELEASED);
        receiver.send(message, TIMESTAMP);
        Mockito.verify(listener).onNoteOff(42, TIMESTAMP);
    }

    @Test
    public void send_controlChange_pressed() throws Exception {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.CONTROL_CHANGE, 42, VELOCITY_PRESSED);
        receiver.send(message, TIMESTAMP);
        Mockito.verify(listener).onButtonOn(42, TIMESTAMP);
    }

    @Test
    public void send_controlChange_released() throws Exception {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.CONTROL_CHANGE, 42, VELOCITY_RELEASED);
        receiver.send(message, TIMESTAMP);
        Mockito.verify(listener).onButtonOff(42, TIMESTAMP);
    }

    @Test
    public void send_controlChange_textScrolled() throws Exception {
        ShortMessage message = new ShortMessage();
        message.setMessage(ShortMessage.CONTROL_CHANGE, 0, VELOCITY_TEXT_SCROLLED);
        receiver.send(message, TIMESTAMP);
        Mockito.verify(listener).onTextScrolled(TIMESTAMP);
    }

    @Test
    public void close() throws Exception {
        receiver.close();
    }


}
