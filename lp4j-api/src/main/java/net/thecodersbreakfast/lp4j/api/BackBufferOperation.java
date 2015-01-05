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
 * Describes how the backbuffer is impacted by the operation currently applied to the write buffer.
 *
 * <p>The backbuffer is defined as "the buffer that is not the current write buffer". It has nothing to do with which
 * buffer is currently visible.
 *
 * @author Olivier Croisier (olivier.croisier@gmail.com)
 */
public enum BackBufferOperation {
    /** Ths operation is applied to the write buffer only, the back buffer is not modified. */
    NONE,
    /** The operation is applied to the write buffer and to backbuffer. */
    COPY,
    /** The operation is applied to the write buffer, and the corresponding location on the backbuffer is cleared */
    CLEAR
}
