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

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.Component;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.datatypes.ZeroOrOne;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.jcip.annotations.NotThreadSafe;

import java.util.Map;

/**
 * A java port of the Cache python class from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch13-cache.py
 */
@NotThreadSafe
public class Cache implements Component<Integer, Double> {

    //TODO test

    private final DemandFunction<Integer> demandFunction;
    private final BiMap<Integer, Long> items2LastAccessTimeMap;

    private long time = 0;
    private int cacheSize;

    public Cache(int initialCacheSize, DemandFunction<Integer> demandFunction) {
        Preconditions.checkArgument(initialCacheSize >= 0);
        Preconditions.checkNotNull(demandFunction);

        this.cacheSize = initialCacheSize;
        this.demandFunction = demandFunction;
        this.items2LastAccessTimeMap = HashBiMap.create(initialCacheSize);
    }

    /**
     * @param newCacheSize input from the controller which tells the cache how large it should be
     * @return the hitrate: a zero if it wasn't a hit, a one if it was a hit
     */
    @Override
    public Double work(Integer newCacheSize) {
        return simulateCacheAccess(newCacheSize).doubleValue();
    }

    protected final ZeroOrOne simulateCacheAccess(Integer newCacheSize) {
        time++;
        cacheSize = newCacheSize > 0 ? newCacheSize : -newCacheSize;
        Integer requestedItemAtCurrentTime = demandFunction.demand(time);
        if (items2LastAccessTimeMap.containsKey(requestedItemAtCurrentTime)) {
            items2LastAccessTimeMap.put(requestedItemAtCurrentTime, time);
            return ZeroOrOne.ONE; // hit :-)
        } else {
            if (items2LastAccessTimeMap.size() >= cacheSize) {
                shrinkCacheByRemovingOldestEntries();
            }
            items2LastAccessTimeMap.put(requestedItemAtCurrentTime, time);
            return ZeroOrOne.ZERO; // no hit :-(
        }
    }

    private void shrinkCacheByRemovingOldestEntries() {
        int numberOfElementsToDelete = 1 + items2LastAccessTimeMap.size() - cacheSize;
        Map<Long, Integer> lastAccessTimeCache2Item = items2LastAccessTimeMap.inverse();
        lastAccessTimeCache2Item.keySet().stream()
                .sorted()
                .limit(numberOfElementsToDelete)
                .forEach(lastAccessTime -> items2LastAccessTimeMap.remove(lastAccessTimeCache2Item.get(lastAccessTime)));
    }
}
