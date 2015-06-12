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

import spock.lang.Specification

/**
 * More infos to recursive filters? See https://en.wikipedia.org/wiki/Recursive_filter
 */
class RecursiveFilterTest extends Specification {

    def 'work: demo with alpha 0.125'() {
        given:
        def alpha = 0.125
        def testee = new RecursiveFilter(alpha)

        when:
        def result = testee.work(1)
        then:
        result == 0.125d

        when:
        result = testee.work(1)
        then:
        result == 0.234375d

        when:
        result = testee.work(1)
        then:
        result == 0.330078125d

        when:
        result = testee.work(1)
        then:
        result == 0.413818359375d

        when:
        result = testee.work(1)
        then:
        result == 0.487091064453125d
    }

    def 'work: demo with alpha 0.5'() {
        given:
        def alpha = 0.5
        def testee = new RecursiveFilter(alpha)

        when:
        def result = testee.work(1)
        then:
        result == 0.5d

        when:
        result = testee.work(1)
        then:
        result == 0.75d

        when:
        result = testee.work(1)
        then:
        result == 0.875d

        when:
        result = testee.work(1)
        then:
        result == 0.9375d

        when:
        result = testee.work(1)
        then:
        result == 0.96875d
    }

    def 'work: demo with alpha 1.0'() {
        given:
        def alpha = 1.0
        def testee = new RecursiveFilter(alpha)

        when:
        def result = testee.work(1)
        then:
        result == 1.0d

        when:
        result = testee.work(1)
        then:
        result == 1.0d

        when:
        result = testee.work(2)
        then:
        result == 2.0d

        when:
        result = testee.work(2)
        then:
        result == 2.0d
    }

    def 'work: demo with alpha 0.125 and negative values'() {
        given:
        def alpha = 0.125
        def testee = new RecursiveFilter(alpha)

        when:
        def result = testee.work(-1)
        then:
        result == -0.125d

        when:
        result = testee.work(-1)
        then:
        result == -0.234375d

        when:
        result = testee.work(-1)
        then:
        result == -0.330078125d
    }
}
