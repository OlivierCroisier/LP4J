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

import net.thecodersbreakfast.lp4j.api.Button;
import net.thecodersbreakfast.lp4j.api.LaunchpadListener;
import net.thecodersbreakfast.lp4j.api.Pad;

/**
 * Parses low-level messages and notifies a high-level {@link net.thecodersbreakfast.lp4j.api.LaunchpadListener}.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class DefaultMidiProtocolListener implements MidiProtocolListener {

    /** The high-level LaunchpadListener to notify. */
    private final LaunchpadListener listener;

    /**
     * Constructor.
     *
     * @param listener The high-level LaunchpadListener to notify.
     */
    public DefaultMidiProtocolListener(LaunchpadListener listener) {
        this.listener = listener;
    }

    /** {@inheritDoc} */
    @Override
    public void onNoteOn(int note, long timestamp) {
        if (listener == null) {
            return;
        }
        int x = note % 16;
        int y = note / 16;
        if (x >= 8) {
            Button button = Button.atRight(y);
            listener.onButtonPressed(button, timestamp);
        } else {
            if (x < 0 || y < 0 || y > 7) {
                throw new IllegalArgumentException("Invalid pad coordinates : (" + x + "," + y + "). Acceptable values on either axis are in range [0..7].");
            }
            listener.onPadPressed(Pad.at(x, y), timestamp);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onNoteOff(int note, long timestamp) {
        if (listener == null) {
            return;
        }
        int x = note % 16;
        int y = note / 16;
        if (x >= 8) {
            Button button = Button.atRight(y);
            listener.onButtonReleased(button, timestamp);
        } else {
            if (x < 0 || y < 0 || y > 7) {
                throw new IllegalArgumentException("Invalid pad coordinates : (" + x + "," + y + "). Acceptable values on either axis are in range [0..7].");
            }
            listener.onPadReleased(Pad.at(x, y), timestamp);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onButtonOn(int note, long timestamp) {
        if (listener == null) {
            return;
        }
        int value = note - 104;
        Button button = Button.atTop(value);
        listener.onButtonPressed(button, timestamp);
    }

    /** {@inheritDoc} */
    @Override
    public void onButtonOff(int note, long timestamp) {
        if (listener == null) {
            return;
        }
        int value = note - 104;
        Button button = Button.atTop(value);
        listener.onButtonReleased(button, timestamp);
    }

    /** {@inheritDoc} */
    @Override
    public void onTextScrolled(long timestamp) {
        listener.onTextScrolled(timestamp);
    }
}
