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
 * Represents any error that could occur while using the Launchpad API.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class LaunchpadException extends RuntimeException {

    /**
     * Constructor.
     */
    public LaunchpadException() {
    }

    /**
     * Constructor.
     *
     * @param message The error message.
     */
    public LaunchpadException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message The error message.
     * @param cause The root cause of the exception.
     */
    public LaunchpadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param cause The root cause of the exception.
     */
    public LaunchpadException(Throwable cause) {
        super(cause);
    }

}
