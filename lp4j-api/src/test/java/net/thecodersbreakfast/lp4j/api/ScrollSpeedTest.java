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
import static org.junit.Assert.assertNotNull;

public class ScrollSpeedTest {

    @Test
    public void valueOf() {
        ScrollSpeed scrollSpeed = ScrollSpeed.of(ScrollSpeed.MIN_VALUE);
        assertNotNull(scrollSpeed);
        assertEquals(ScrollSpeed.MIN_VALUE, scrollSpeed.getScrollSpeed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_tooLow() {
        ScrollSpeed.of(ScrollSpeed.MIN_VALUE - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_tooHigh() {
        ScrollSpeed.of(ScrollSpeed.MAX_VALUE + 1);
    }


}
