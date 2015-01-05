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
 * Describes the level of brightness of the pads and buttons lights.
 *
 * <p>{@code Brightness} instances are immutable and cached.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public final class Brightness {

    /** Minimum level of brightness */
    public static final int MIN_VALUE = 0;
    /** Maximum level of brightness */
    public static final int MAX_VALUE = 15;

    /** Cache for all levels of brightness */
    private static final Brightness[] CACHE = new Brightness[16];

    static {
        for (int i = 0; i < 16; i++) {
            CACHE[i] = new Brightness(i);
        }
    }

    /** Minimum brightness */
    public static final Brightness BRIGHTNESS_MIN = of(MIN_VALUE);
    /** Maximum brightness */
    public static final Brightness BRIGHTNESS_MAX = of(MAX_VALUE);

    /**
     * Factory method.
     *
     * @param brightness The desired level of brightess. Must be in range [{@link net.thecodersbreakfast.lp4j.api.Brightness#MIN_VALUE},{@link
     * net.thecodersbreakfast.lp4j.api.Brightness#MAX_VALUE}]
     * @return The Brightness instance.
     * @throws java.lang.IllegalArgumentException If the requested brightness level is out of acceptable range.
     */
    public static Brightness of(int brightness) {
        if (brightness < MIN_VALUE || brightness > MAX_VALUE) {
            throw new IllegalArgumentException("Invalid brightness level : " + brightness + ". Acceptable values are in range [0..15].");
        }
        return CACHE[brightness - MIN_VALUE];
    }

    /** Level of brightness */
    private final int brightness;

    /**
     * Constructor.
     *
     * @param brightness The desired level of brightess.
     */
    private Brightness(int brightness) {
        this.brightness = brightness;
    }

    /**
     * Returns the level of brightness
     *
     * @return the level of brightness
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Returns a brighter Brightness instance, if the maximum level of brightness is not already reached.
     *
     * @return a brighter Brightness instance, or the same instance if it was already the brightest.
     */
    public Brightness more() {
        return brightness < MAX_VALUE ? of(brightness + 1) : this;
    }

    /**
     * Returns a less bright Brightness instance, if the minimum level of brightness is not already reached.
     *
     * @return a less bright Brightness instance, or the same instance if it was already the least bright.
     */
    public Brightness less() {
        return brightness > MIN_VALUE ? of(brightness - 1) : this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Brightness that = (Brightness) o;
        return brightness == that.brightness;
    }

    @Override
    public int hashCode() {
        return brightness;
    }
}
