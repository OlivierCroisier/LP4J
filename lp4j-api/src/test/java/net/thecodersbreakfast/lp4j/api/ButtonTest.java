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

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ButtonTest {

    @Test
    public void at_topButton() {
        Button button = Button.atTop(0);
        assertEquals(Button.UP, button);
        Assert.assertTrue(button.isTopButton());
    }

    @Test
    public void at_rightButton() {
        Button button = Button.atRight(0);
        assertEquals(Button.VOL, button);
        Assert.assertTrue(button.isRightButton());
    }

    @Test(expected = IllegalArgumentException.class)
    public void at_coordinateTooLow() {
        Button button = Button.atTop(Button.MIN_COORD - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void at_coordinateTooHigh() {
        Button button = Button.atTop(Button.MAX_COORD + 1);
    }

    @Test
    public void isTopOrRight() {
        assertTrue(Button.UP.isTopButton());
        assertFalse(Button.UP.isRightButton());
        assertTrue(Button.VOL.isRightButton());
        assertFalse(Button.VOL.isTopButton());
    }

    @Test
    public void tostring() {
        assertEquals("Button[UP(top,0)]", Button.UP.toString());
    }

}
