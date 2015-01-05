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
 * Represents the Launchpad's 16 round buttons (8 at the top, 8 on the right side)
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public enum Button {

    /** The UP button (1st position from the left, on the top row) */
    UP(0, true),
    /** The DOWN button (2nd position from the left, on the top row) */
    DOWN(1, true),
    /** The LEFT button (3rd position from the left, on the top row) */
    LEFT(2, true),
    /** The RIGHT button (4th position from the left, on the top row) */
    RIGHT(3, true),
    /** The SESSION button (5th position from the left, on the top row) */
    SESSION(4, true),
    /** The USER_1 button (6th position from the left, on the top row) */
    USER_1(5, true),
    /** The USER_2 button (7th position from the left, on the top row) */
    USER_2(6, true),
    /** The MIXER button (8th position from the left, on the top row) */
    MIXER(7, true),
    /** The VOL button (1st position from the top, on the right side) */
    VOL(0, false),
    /** The PAN button (2nd position from the top, on the right side) */
    PAN(1, false),
    /** The SND_A button (3rd position from the top, on the right side) */
    SND_A(2, false),
    /** The SND_B button (4th position from the top, on the right side) */
    SND_B(3, false),
    /** The STOP button (5th position from the top, on the right side) */
    STOP(4, false),
    /** The TRACK_ON button (6th position from the top, on the right side) */
    TRACK_ON(5, false),
    /** The SOLO button (7th position from the top, on the right side) */
    SOLO(6, false),
    /** The ARM button (8th position from the top, on the right side) */
    ARM(7, false);

    /** Minimal coordinate for a button */
    public static final int MIN_COORD = 0;
    /** Maximal coordinate for a button */
    public static final int MAX_COORD = 7;

    /** Top-row buttons, in left-to-right order */
    public static final Button[] BUTTONS_TOP = {UP, DOWN, LEFT, RIGHT, SESSION, USER_1, USER_2, MIXER};
    /** Right-side buttons, in top-to-bottom order */
    public static final Button[] BUTTONS_RIGHT = {VOL, PAN, SND_A, SND_B, STOP, TRACK_ON, SOLO, ARM};

    /**
     * Factory method for a top-row button.
     *
     * @param isTopButton {@code true} if the button is on the top row, {@code false} if it is on the right side
     * @param c The coordinate of the button. Must be in range [{@link Button#MIN_COORD},{@link Button#MAX_COORD}].
     * @return The button
     * @throws IllegalArgumentException if the coordinates are invalid.
     */
    private static Button at(boolean isTopButton, int c) {
        if (c < MIN_COORD || c > MAX_COORD) {
            throw new IllegalArgumentException(String.format("Invalid button coordinates : %d. Value must be between %d and %d inclusive.", c, MIN_COORD, MAX_COORD));
        }
        return isTopButton ? BUTTONS_TOP[c] : BUTTONS_RIGHT[c];
    }

    /**
     * Factory method for a top-row button.
     *
     * @param c The coordinate of the button. Must be in range [{@link Button#MIN_COORD},{@link Button#MAX_COORD}].
     * @return The button
     * @throws IllegalArgumentException if the coordinates are invalid.
     */
    public static Button atTop(int c) {
        return at(true, c);
    }

    /**
     * Factory method for a top-row button.
     *
     * @param c The coordinate of the button. Must be in range [{@link Button#MIN_COORD},{@link Button#MAX_COORD}].
     * @return The button
     * @throws IllegalArgumentException if the coordinates are invalid.
     */
    public static Button atRight(int c) {
        return at(false, c);
    }

    /** The button coordinates */
    private final int coordinate;
    /** Tells if it is a top-row button ({@code true}), as opposed to a righ-side button ({@code false}) */
    private final boolean topButton;

    /**
     * Constructor.
     *
     * @param coordinate The coordinate of the button.
     * @param topButton {@code true} for a top-row button, {@code false} otherwise.
     */
    private Button(int coordinate, boolean topButton) {
        this.coordinate = coordinate;
        this.topButton = topButton;
    }

    /**
     * Returns the button coordinate.
     *
     * @return The coordinate.
     */
    public int getCoordinate() {
        return coordinate;
    }

    /**
     * Tells if this button is located on the top row.
     *
     * @return {@code true} if this button belongs to the top row, {@code false} otherwise.
     */
    public boolean isTopButton() {
        return topButton;
    }

    /**
     * Tells if this button is located on the right side.
     *
     * @return {@code true} if this button belongs to the right side, {@code false} otherwise.
     */
    public boolean isRightButton() {
        return !topButton;
    }

    @Override
    public String toString() {
        return String.format("Button[%s(%s,%d)]", name(), topButton ? "top" : "right", coordinate);
    }
}
