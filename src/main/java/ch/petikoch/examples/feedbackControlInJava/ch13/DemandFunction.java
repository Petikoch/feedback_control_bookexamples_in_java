/**
 * Copyright 2015 Peti Koch, All rights reserved.
 *
 * Project Info:
 * https://github.com/Petikoch/feedback_control_bookexamples_in_java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.petikoch.examples.feedbackControlInJava.ch13;

/**
 * A java port of the demand python function from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch13-cache.py
 * <p>
 * Can be used e.g. to simulate the demand for a cache.
 * Think of at time 0 the item 4 is demanded, at time 1 the item 9 is demanded.
 *
 * @param <O> output type
 */
@FunctionalInterface
public interface DemandFunction<O> {

    /**
     * @param time usually a virtual time like 0, 1, 2, ... and not the wall-clock time
     * @return the item at the given time
     */
    O demand(long time);

}
