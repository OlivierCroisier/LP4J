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

package net.thecodersbreakfast.lp4j.api;

/**
 * This listener allows to be notified of any event occurring on the Launchpad, such as pads or buttons being pressed or
 * released.
 *
 * <p>Such a listener is usually provided to a Launchpad implementation via the {@link
 * net.thecodersbreakfast.lp4j.api.Launchpad#setListener(LaunchpadListener)} method.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public interface LaunchpadListener {

    /**
     * Called when a pad has been pressed.
     *
     * @param pad The pad that was pressed
     * @param timestamp When the event occurred
     */
    void onPadPressed(Pad pad, long timestamp);

    /**
     * Called when a pad has been released.
     *
     * @param pad The pad that was released
     * @param timestamp When the event occurred
     */
    void onPadReleased(Pad pad, long timestamp);

    /**
     * Called when a button has been pressed.
     *
     * @param button The buttonthat was pressed
     * @param timestamp When the event occurred
     */
    void onButtonPressed(Button button, long timestamp);

    /**
     * Called when a button has been released.
     *
     * @param button The button that was released
     * @param timestamp When the event occurred
     */
    void onButtonReleased(Button button, long timestamp);

    /**
     * Called after text has been successfully displayed by scrolled through the display. This may happen multiple times
     * if the {@link net.thecodersbreakfast.lp4j.api.LaunchpadClient#scrollText(String, Color, ScrollSpeed, boolean,
     * BackBufferOperation)} method has been instructed to repeat the loop endlessly.
     *
     * @param timestamp When the event occurred
     */
    void onTextScrolled(long timestamp);
}
