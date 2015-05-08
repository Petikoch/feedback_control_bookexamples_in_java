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
package ch.petikoch.examples.feedbackControlInJava.examples;

import ch.petikoch.examples.feedbackControlInJava.ch13.DemandFunction;
import ch.petikoch.examples.feedbackControlInJava.ch13.SmoothedCache;
import ch.petikoch.examples.feedbackControlInJava.plotting.JFreeChartPlotter;
import ch.petikoch.examples.feedbackControlInJava.plotting.JPanelDisplayer;
import ch.petikoch.examples.feedbackControlInJava.plotting.PlotSimpleSetpointActualItem;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.SamplingInterval;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.controllers.pidcontrollers.PidController;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.loopFunctions.ClosedLoops;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.setpoints.SetpointFunction;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.List;

/**
 * A java port of the closedloop python function from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch13-cache.py
 */
public class Ch13_cache_closedloop {

    public static void main(String[] args) {
        SamplingInterval samplingInterval = new SamplingInterval(1);
        DemandFunction<Integer> demandFunction = new RandomDemandFunction();
        SetpointFunction setpoint = new LowerDemandAfter5000SetpointFunction();
        SmoothedCache smoothedCache = new SmoothedCache(0, demandFunction, 100);
        PidController pidController = new PidController(100, 250, samplingInterval);

        JPanelDisplayer.clearDisplay();
        List<PlotSimpleSetpointActualItem> plotDataItems = ClosedLoops.closed_loop(samplingInterval, setpoint, pidController, smoothedCache, 10000);
        JPanelDisplayer.displayPanel(JFreeChartPlotter.plot(plotDataItems, "Cache hitrate"));
    }

    // TODO how can we write this using java 8 lambdas, so that groovy can understand it?
    // groovy closure syntay != java 8 lambda syntax
    private static class RandomDemandFunction implements DemandFunction<Integer> {

        @Override
        public Integer demand(long time) {
            return (int) new NormalDistribution(0.0, 15.0).sample();  // should be similiar to pythons random.gauss( 0, 15 )
        }
    }

    private static class LowerDemandAfter5000SetpointFunction implements SetpointFunction {

        @Override
        public Double at(long time) {
            if (time > 5000) {
                return 0.5;
            } else {
                return 0.7;
            }
        }
    }

}
