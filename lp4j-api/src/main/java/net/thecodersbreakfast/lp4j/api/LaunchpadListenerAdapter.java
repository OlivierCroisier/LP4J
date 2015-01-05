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
 * Convenient empty implementation of a {@link net.thecodersbreakfast.lp4j.api.LaunchpadListener}.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public abstract class LaunchpadListenerAdapter implements LaunchpadListener {

    @Override
    public void onPadPressed(Pad pad, long timestamp) {
    }

    @Override
    public void onPadReleased(Pad pad, long timestamp) {
    }

    @Override
    public void onButtonPressed(Button button, long timestamp) {
    }

    @Override
    public void onButtonReleased(Button button, long timestamp) {
    }

    @Override
    public void onTextScrolled(long timestamp) {
    }

}
