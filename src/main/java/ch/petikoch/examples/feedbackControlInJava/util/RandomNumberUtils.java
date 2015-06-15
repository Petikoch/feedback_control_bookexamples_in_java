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
package ch.petikoch.examples.feedbackControlInJava.util;

import com.google.common.collect.Maps;
import net.jcip.annotations.ThreadSafe;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.SynchronizedRandomGenerator;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@ThreadSafe
public class RandomNumberUtils {

    private static final Map<AlphaBetaPair, Queue<Double>> SPARE_BETADISTRIBUTION_SAMPLES = Maps.newConcurrentMap();
    private static final RandomGenerator THREADSAFE_RANDOMNUMBER_GENERATOR = new SynchronizedRandomGenerator(new MersenneTwister());

    // http://stackoverflow.com/questions/10881998/equivalent-method-in-java-numpy-random-normalmean-var
    public static double randomGaussian(double mean, double variance) {
        return mean + (ThreadLocalRandom.current().nextGaussian() * variance);
    }

    public static double betavariate(double alpha, double beta) {
        if (Runtime.getRuntime().availableProcessors() == 1) {
            return calculateNewBetaDistributionSample(alpha, beta);
        } else {
            return getAsyncCalculatedBetaDistributionSample(alpha, beta);
        }
    }

    // the implementation here is far away from optimal, but enough for the purpose here
    private static double getAsyncCalculatedBetaDistributionSample(double alpha, double beta) {
        AlphaBetaPair mapKey = new AlphaBetaPair(alpha, beta);
        Queue<Double> spares = SPARE_BETADISTRIBUTION_SAMPLES.computeIfAbsent(mapKey, alphaBetaPair -> {
            Queue<Double> newSpares = new LinkedBlockingQueue<>(1000);
            IntStream.range(1, 1001).parallel().forEach(itemNumber -> {
                newSpares.add(calculateNewBetaDistributionSample(alpha, beta));
            });
            return newSpares;
        });
        Double nextSpare = spares.poll();
        if (nextSpare == null) {
            // no spares anymore...
            SPARE_BETADISTRIBUTION_SAMPLES.remove(mapKey); // force create new spares
            return calculateNewBetaDistributionSample(alpha, beta);
        } else {
            return nextSpare;
        }
    }

    private static double calculateNewBetaDistributionSample(double alpha, double beta) {
        return new BetaDistribution(THREADSAFE_RANDOMNUMBER_GENERATOR, alpha, beta).sample();
    }

    private static class AlphaBetaPair {
        private final double alpha;
        private final double beta;

        public AlphaBetaPair(double alpha, double beta) {
            this.alpha = alpha;
            this.beta = beta;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AlphaBetaPair that = (AlphaBetaPair) o;

            if (Double.compare(that.alpha, alpha) != 0) return false;
            return Double.compare(that.beta, beta) == 0;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(alpha);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(beta);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}
