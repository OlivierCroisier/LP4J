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

import java.io.Closeable;

/**
 * Describes a generic Launchpad interface.
 *
 * <p>Implementations are responsible for providing a {@link net.thecodersbreakfast.lp4j.api.LaunchpadClient} which allows
 * to send commands to the Launchpad, and to accept a {@link net.thecodersbreakfast.lp4j.api.LaunchpadListener} through
 * which the API user will be notified of events such as pad or button presses.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public interface Launchpad extends Closeable {

    /**
     * Returns an implementation of {@link net.thecodersbreakfast.lp4j.api.LaunchpadClient} suitable to send commands to
     * a Launchpad.
     *
     * @return a client
     */
    public LaunchpadClient getClient();

    /**
     * Accepts a {@link net.thecodersbreakfast.lp4j.api.LaunchpadListener}, which will be notified of any
     * Launchpad-related event such as pad or button presses.
     *
     * @param listener The listener to be notified
     */
    public void setListener(LaunchpadListener listener);

}
