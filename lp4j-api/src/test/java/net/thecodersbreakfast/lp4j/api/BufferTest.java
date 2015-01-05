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

public class BufferTest {

    @Test
    public void other_0() {
        Buffer other = Buffer.BUFFER_0.other();
        Assert.assertEquals(Buffer.BUFFER_1, other);
    }

    @Test
    public void other_1() {
        Buffer other = Buffer.BUFFER_1.other();
        Assert.assertEquals(Buffer.BUFFER_0, other);
    }

}
