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
 * Represents a square pad on the Launchpad.
 *
 * <p>{@code Pad} instances are immutable and cached.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public class Pad {

    /** Minimal X coordinate for a pad */
    public static final int X_MIN = 0;
    /** Maximal X coordinate for a pad */
    public static final int X_MAX = 7;
    /** Minimal Y coordinate for a pad */
    public static final int Y_MIN = 0;
    /** Maximal Y coordinate for a pad */
    public static final int Y_MAX = 7;

    /** Cache of all pads */
    private static final Pad[][] PADS = new Pad[8][8];

    static {
        for (int i = X_MIN; i <= X_MAX; i++) {
            for (int j = Y_MIN; j <= Y_MAX; j++) {
                PADS[i][j] = new Pad(i, j);
            }
        }
    }

    /**
     * Factory method.
     *
     * @param x The X coordinate of the pad. Must be in range [{@link Pad#X_MIN},{@link Pad#X_MAX}].
     * @param y The Y coordinate of the pad. Must be in range [{@link Pad#Y_MIN},{@link Pad#Y_MAX}].
     * @return The pad.
     * @throws java.lang.IllegalArgumentException If the coordinates are invalid.
     */
    public static Pad at(int x, int y) {
        if (x < X_MIN || x > X_MAX || y < Y_MIN || y > Y_MAX) {
            throw new IllegalArgumentException("Illegal pad coordinates : (" + x + "," + y + "). Acceptable values are in [0..7] on both axis.");
        }
        return PADS[x][y];
    }

    /** The X coordinate */
    private final int x;
    /** The Y coordinate */
    private final int y;

    /**
     * Constructor.
     *
     * @param x The X coordinate of the pad.
     * @param y The Y coordinate of the pad.
     */
    private Pad(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the X coordinate.
     *
     * @return The X coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the Y coordinate.
     *
     * @return The Y coordinate
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pad pad = (Pad) o;
        return x == pad.x && y == pad.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Pad[" + x + ',' + y + ']';
    }
}
