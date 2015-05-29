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

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.datatypes.ZeroOrOne;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.filtersAndActuators.FixedFilter;
import com.google.common.base.Preconditions;

/**
 * A java port of the SmoothedCache python class from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch13-cache.py
 */
public class SmoothedCache extends Cache {

    private final FixedFilter<ZeroOrOne, Double> fixedFilter;

    public SmoothedCache(int cacheSize,
                         DemandFunction<Integer> demandFunction,
                         int numberOfInputsToAvg) {
        super(cacheSize, demandFunction);
        Preconditions.checkArgument(numberOfInputsToAvg >= 1);
        this.fixedFilter = new FixedFilter<>(numberOfInputsToAvg, Double.class);
    }

    /**
     * @param newCacheSize input from the controller which tells the cache how large it should be
     * @return the hitrate: a smoothened (averaged) hitrate in range 0..1
     */
    @Override
    public Double work(Integer newCacheSize) {
        ZeroOrOne hitOrNot = simulateCacheAccess(newCacheSize);
        return fixedFilter.work(hitOrNot);
    }
}
