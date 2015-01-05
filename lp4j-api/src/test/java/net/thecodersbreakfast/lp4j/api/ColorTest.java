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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColorTest {

    @Test
    public void valueOf() {
        Color color = Color.of(Color.MIN_INTENSITY, Color.MIN_INTENSITY);
        assertEquals(Color.BLACK, color);
        assertEquals(Color.MIN_INTENSITY, color.getRed());
        assertEquals(Color.MIN_INTENSITY, color.getGreen());
        Color colorMax = Color.of(Color.MAX_INTENSITY, Color.MAX_INTENSITY);
        assertEquals(Color.AMBER, colorMax);
        assertEquals(Color.MAX_INTENSITY, colorMax.getRed());
        assertEquals(Color.MAX_INTENSITY, colorMax.getGreen());
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_redTooLow() {
        Color.of(Color.MIN_INTENSITY - 1, Color.MIN_INTENSITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_redTooHigh() {
        Color.of(Color.MAX_INTENSITY + 1, Color.MIN_INTENSITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_greenTooLow() {
        Color.of(Color.MIN_INTENSITY, Color.MIN_INTENSITY - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_greenTooHigh() {
        Color.of(Color.MIN_INTENSITY, Color.MAX_INTENSITY + 1);
    }

}
