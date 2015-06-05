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
package ch.petikoch.examples.feedbackControlInJava.ch13

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.datatypes.ZeroOrOne
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class CacheTest extends Specification {

    Cache testee
    DemandFunction<Integer> demandFunctionMock

    def setup() {
        demandFunctionMock = Mock(DemandFunction)
        testee = new Cache(42, demandFunctionMock)
    }

    def 'work: not cached before, afterwards cached and no hit'() {
        given:
        assert testee.items2LastAccessTimeMap.isEmpty()
        demandFunctionMock.demand(_) >> 3

        when:
        def result = testee.work(42)

        then:
        result == ZeroOrOne.ZERO.toDouble() // no-hit
        testee.items2LastAccessTimeMap.containsKey(3)
    }

    def 'work: cached before, afterwards cached and hit'() {
        given:
        assert testee.items2LastAccessTimeMap.isEmpty()
        int demand = 3
        demandFunctionMock.demand(_) >> demand
        testee.items2LastAccessTimeMap[demand] = 1l

        when:
        def result = testee.work(42)

        then:
        result == ZeroOrOne.ONE.toDouble() // hit
        testee.items2LastAccessTimeMap.containsKey(3)
    }

    def 'work: shrinking in case of no hit'() {
        given:
        assert testee.items2LastAccessTimeMap.isEmpty()
        testee.items2LastAccessTimeMap[7] = 1l
        testee.items2LastAccessTimeMap[3] = 2l
        testee.items2LastAccessTimeMap[5] = 3l
        testee.time = 3
        demandFunctionMock.demand(_) >> 9

        when:
        def result = testee.work(2)

        then:
        result == ZeroOrOne.ZERO.toDouble() // no-hit
        testee.items2LastAccessTimeMap.size() == 2
        testee.items2LastAccessTimeMap[9] == 4l
        testee.items2LastAccessTimeMap[5] == 3l
    }

    def 'work: shrinking in case of hit'() {
        given:
        assert testee.items2LastAccessTimeMap.isEmpty()
        testee.items2LastAccessTimeMap[7] = 1l
        testee.items2LastAccessTimeMap[3] = 2l
        testee.items2LastAccessTimeMap[5] = 3l
        testee.time = 3
        demandFunctionMock.demand(_) >> 5

        when:
        def result = testee.work(2)

        then:
        result == ZeroOrOne.ONE.toDouble() // hit
        testee.items2LastAccessTimeMap.size() == 2
        testee.items2LastAccessTimeMap[5] == 4l
        testee.items2LastAccessTimeMap[3] == 2l
    }
}
