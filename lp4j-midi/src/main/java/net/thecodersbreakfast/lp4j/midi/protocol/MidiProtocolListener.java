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

/**
 * This listener allows to be notified of any MIDI event occurring on the Launchpad
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public interface MidiProtocolListener {

    /**
     * Called when a note starts being emitted.
     *
     * @param note The note emitted.
     * @param timestamp When the event occurred.
     */
    public void onNoteOn(int note, long timestamp);

    /**
     * Called when a note is not emitted anymore.
     *
     * @param note The note silenced.
     * @param timestamp When the event occurred.
     */
    public void onNoteOff(int note, long timestamp);

    /**
     * Called when a button starts being pressed.
     *
     * @param button The button pressed.
     * @param timestamp When the event occurred.
     */
    public void onButtonOn(int button, long timestamp);

    /**
     * Called when a button is released.
     *
     * @param button The button released.
     * @param timestamp When the event occurred.
     */
    public void onButtonOff(int button, long timestamp);

    /**
     * Called when text has been fully scrolled through the board.
     *
     * @param timestamp When the event occurred.
     */
    void onTextScrolled(long timestamp);
}
