/*
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
package ch.petikoch.examples.feedbackControlInJava.simulationFramework.filtersAndActuators

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.datatypes.ZeroOrOne
import spock.lang.Specification

import static ch.petikoch.examples.feedbackControlInJava.simulationFramework.datatypes.ZeroOrOne.ONE
import static ch.petikoch.examples.feedbackControlInJava.simulationFramework.datatypes.ZeroOrOne.ZERO

class FixedFilterTest extends Specification {

    def "work using Integer -> Integer"() {
        given:
        def fixedFilter = new FixedFilter<Integer, Integer>(5, Integer.class)

        when:
        def output = fixedFilter.work(1)
        then:
        output == 1

        when:
        output = fixedFilter.work(3)
        then:
        output == 2

        when:
        output = fixedFilter.work(1)
        then:
        output == 2

        when:
        output = fixedFilter.work(3)
        then:
        output == 2

        when:
        output = fixedFilter.work(2)
        then:
        output == 2

        when:
        output = fixedFilter.work(10)
        then:
        output == 4

        when:
        output = fixedFilter.work(10)
        then:
        output == 5

        when:
        output = fixedFilter.work(10)
        then:
        output == 7

        when:
        output = fixedFilter.work(10)
        then:
        output == 8

        when:
        output = fixedFilter.work(10)
        then:
        output == 10
    }

    def "work using Double -> Double"() {
        given:
        def fixedFilter = new FixedFilter<Double, Double>(5, Double.class)

        when:
        def output = fixedFilter.work(1.0d)
        then:
        output == 1.0d

        when:
        output = fixedFilter.work(3.0d)
        then:
        output == 2.0d

        when:
        output = fixedFilter.work(1.0d)
        then:
        output == 1.6666666666666667d

        when:
        output = fixedFilter.work(3.0d)
        then:
        output == 2.0d

        when:
        output = fixedFilter.work(2.0d)
        then:
        output == 2.0d

        when:
        output = fixedFilter.work(10.0d)
        then:
        output == 3.8d

        when:
        output = fixedFilter.work(10.0d)
        then:
        output == 5.2d

        when:
        output = fixedFilter.work(10.0d)
        then:
        output == 7.0d

        when:
        output = fixedFilter.work(10.0d)
        then:
        output == 8.4d

        when:
        output = fixedFilter.work(10.0d)
        then:
        output == 10.0d
    }

    def "work using Long -> Long"() {
        given:
        def fixedFilter = new FixedFilter<Long, Long>(5, Long.class)

        when:
        def output = fixedFilter.work(1l)
        then:
        output == 1l

        when:
        output = fixedFilter.work(3l)
        then:
        output == 2l

        when:
        output = fixedFilter.work(1l)
        then:
        output == 2l

        when:
        output = fixedFilter.work(3l)
        then:
        output == 2l

        when:
        output = fixedFilter.work(2l)
        then:
        output == 2l

        when:
        output = fixedFilter.work(10l)
        then:
        output == 4l

        when:
        output = fixedFilter.work(10l)
        then:
        output == 5l

        when:
        output = fixedFilter.work(10l)
        then:
        output == 7l

        when:
        output = fixedFilter.work(10l)
        then:
        output == 8l

        when:
        output = fixedFilter.work(10l)
        then:
        output == 10l
    }

    def "work using ZeroOrOne"() {
        given:
        def fixedFilter = new FixedFilter<ZeroOrOne, ZeroOrOne>(5, ZeroOrOne.class)

        when:
        def output = fixedFilter.work(ZERO)
        then:
        output == ZERO

        when:
        output = fixedFilter.work(ZERO)
        then:
        output == ZERO

        when:
        output = fixedFilter.work(ONE)
        then:
        output == ZERO

        when:
        output = fixedFilter.work(ONE)
        then:
        output == ONE

        when:
        output = fixedFilter.work(ONE)
        then:
        output == ONE
    }

    def "work using ZeroOrOne -> Double"() {
        given:
        def fixedFilter = new FixedFilter<ZeroOrOne, Double>(3, Double.class)

        when:
        def output = fixedFilter.work(ZERO)
        then:
        output == 0.0d

        when:
        output = fixedFilter.work(ONE)
        then:
        output == 0.5d

        when:
        output = fixedFilter.work(ONE)
        then:
        output == 0.6666666666666666d

        when:
        output = fixedFilter.work(ONE)
        then:
        output == 1.0d
    }
}

