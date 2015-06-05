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
package ch.petikoch.examples.feedbackControlInJava.util

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class RandomNumberUtilsTest extends Specification {

    def 'randomGaussian: Specs according http://en.wikipedia.org/wiki/Normal_distribution#Standard_deviation_and_tolerance_intervals'() {
        expect:
        numberOfValuesInsideTolerance(mean, deviation, consideredDeviationFactor, numberOfSamples) >= percentage

        where:
        mean  | deviation | consideredDeviationFactor | numberOfSamples | percentage
        0.0   | 1.0       | 1                         | 100000          | 0.68
        0.0   | 1.0       | 2                         | 100000          | 0.95
        0.0   | 1.0       | 3                         | 100000          | 0.99
        0.0   | 1.0       | 4                         | 100000          | 0.999
        0.0   | 1.0       | 5                         | 100000          | 0.99999

        100.0 | 15.0      | 1                         | 100000          | 0.68
        100.0 | 15.0      | 2                         | 100000          | 0.95
        100.0 | 15.0      | 3                         | 100000          | 0.99
        100.0 | 15.0      | 4                         | 100000          | 0.999
        100.0 | 15.0      | 5                         | 100000          | 0.99999
    }

    private static double numberOfValuesInsideTolerance(double mean,
                                                        double deviation,
                                                        int consideredDeviationFactor,
                                                        int numberOfSamples) {
        int numberOfHits = 0
        numberOfSamples.times {
            def randomValue = RandomNumberUtils.randomGaussian(mean, deviation)
            def isOnOrAboveLowerToleranceValue = randomValue >= (mean - (deviation * consideredDeviationFactor))
            def isOnOrBlowUpperToleranceValue = randomValue <= (mean + (deviation * consideredDeviationFactor))
            if (isOnOrAboveLowerToleranceValue && isOnOrBlowUpperToleranceValue) {
                numberOfHits++
            }
        }
        return numberOfHits / numberOfSamples
    }
}
