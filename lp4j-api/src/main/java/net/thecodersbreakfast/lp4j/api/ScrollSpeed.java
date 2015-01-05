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
 * Represents the speed at which the text scrolls through the Launchpad grid (see {@link
 * net.thecodersbreakfast.lp4j.api.LaunchpadClient#scrollText}).
 *
 * <p>{@code ScrollSpeed} instances are immutable and cached.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class ScrollSpeed {

    /** Minimum value for a scrolling speed */
    public static final int MIN_VALUE = 1;
    /** Maximum value for a scrolling speed */
    public static final int MAX_VALUE = 7;

    /** Cache of all possible scrolling speeds */
    private static final ScrollSpeed[] CACHE;

    static {
        CACHE = new ScrollSpeed[MAX_VALUE - MIN_VALUE + 1];
        for (int i = MIN_VALUE; i <= MAX_VALUE; i++) {
            CACHE[i - MIN_VALUE] = new ScrollSpeed(i);
        }
    }

    /** Slowest scrolling speed */
    public static final ScrollSpeed SPEED_MIN = of(MIN_VALUE);
    /** Fastest scrolling speed */
    public static final ScrollSpeed SPEED_MAX = of(MAX_VALUE);

    /**
     * Factory method.
     *
     * @param speed The desired scrolling speed. Must be in range [{@link net.thecodersbreakfast.lp4j.api.ScrollSpeed#MIN_VALUE},{@link
     * net.thecodersbreakfast.lp4j.api.ScrollSpeed#MAX_VALUE}].
     * @return The ScrollSpeed instance
     * @throws java.lang.IllegalArgumentException If the requested speed is out of acceptable range.
     */
    public static ScrollSpeed of(int speed) {
        if (speed < MIN_VALUE || speed > MAX_VALUE) {
            throw new IllegalArgumentException("Invalid speed value : " + speed + ". Acceptable values are in range [1..7].");
        }
        return CACHE[speed - MIN_VALUE];
    }

    /** The speed value. */
    private final int scrollSpeed;

    /**
     * Constructor
     *
     * @param scrollSpeed The scrolling speed
     */
    private ScrollSpeed(int scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    /**
     * Returns the speed value.
     *
     * @return the speed value
     */
    public int getScrollSpeed() {
        return scrollSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScrollSpeed that = (ScrollSpeed) o;
        return scrollSpeed == that.scrollSpeed;
    }

    @Override
    public int hashCode() {
        return scrollSpeed;
    }
}
