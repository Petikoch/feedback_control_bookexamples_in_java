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
package ch.petikoch.examples.feedbackControlInJava.simulationFramework.datatypes;

import com.google.common.base.Preconditions;

/**
 * A number which can be either 0 or 1
 */
public final class ZeroOrOne extends Number {

    public static final ZeroOrOne ONE = new ZeroOrOne(1);
    public static final ZeroOrOne ZERO = new ZeroOrOne(0);

    private final short shortValue;

    public ZeroOrOne(int initialValue) {
        Preconditions.checkArgument((initialValue == 0) || (initialValue == 1));
        shortValue = (short) initialValue;
    }

    @Override
    public int intValue() {
        return shortValue;
    }

    @Override
    public long longValue() {
        return shortValue;
    }

    @Override
    public float floatValue() {
        return shortValue;
    }

    @Override
    public double doubleValue() {
        return shortValue;
    }

    @Override
    public String toString() {
        return "" + shortValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ZeroOrOne zeroOrOne = (ZeroOrOne) o;

        return shortValue == zeroOrOne.shortValue;
    }

    @Override
    public int hashCode() {
        return (int) shortValue;
    }
}
