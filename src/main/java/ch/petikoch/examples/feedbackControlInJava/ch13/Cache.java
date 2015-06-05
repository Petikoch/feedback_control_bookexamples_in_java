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

    private final DemandFunction<Integer> demandFunction;
    private final BiMap<Integer, Long> items2LastAccessTimeMap;

    private long time = 0;

    public Cache(int initialCacheSize, DemandFunction<Integer> demandFunction) {
        Preconditions.checkArgument(initialCacheSize >= 0);
        Preconditions.checkNotNull(demandFunction);

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

        Preconditions.checkArgument(newCacheSize >= 0, "A negative cache size doesn't make sense.");

        Integer requestedItemAtCurrentTime = demandFunction.demand(time);

        ZeroOrOne result;
        if (items2LastAccessTimeMap.containsKey(requestedItemAtCurrentTime)) {
            items2LastAccessTimeMap.put(requestedItemAtCurrentTime, time);
            result = ZeroOrOne.ONE; // hit :-)
        } else {
            items2LastAccessTimeMap.put(requestedItemAtCurrentTime, time);
            result = ZeroOrOne.ZERO; // no hit :-(
        }

        if (items2LastAccessTimeMap.size() > newCacheSize) {
            shrinkCacheByRemovingOldestEntries(newCacheSize);
        }

        return result;
    }

    private void shrinkCacheByRemovingOldestEntries(int newCacheSize) {
        int numberOfElementsToDelete = items2LastAccessTimeMap.size() - newCacheSize;
        Map<Long, Integer> lastAccessTimeCache2Item = items2LastAccessTimeMap.inverse();
        lastAccessTimeCache2Item.keySet().stream()
                .sorted()
                .limit(numberOfElementsToDelete)
                .forEach(lastAccessTime -> items2LastAccessTimeMap.remove(lastAccessTimeCache2Item.get(lastAccessTime)));
    }

    @Override
    public String monitoring() {
        return Integer.toString(items2LastAccessTimeMap.size());
    }
}
