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
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.SamplingInterval;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.controllers.pidcontrollers.PidController;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.loopFunctions.ClosedLoops;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.plotting.PlotItem;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.setpoints.ConstantSetpointFunction;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.setpoints.SetpointFunction;
import ch.petikoch.examples.feedbackControlInJava.ui.PlottingAndSysOutPrintingSubscriber;
import org.apache.commons.math3.distribution.NormalDistribution;
import rx.Observable;

/**
 * A java port of the closedloop_jumps python function from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch13-cache.py
 */
public class Ch13_cache_closedloop_jumps {

    public static void main(String[] args) {
        SamplingInterval samplingInterval = new SamplingInterval(1);
        DemandFunction<Integer> demandFunction = new RandomJumpDemandFunction();
        SmoothedCache smoothedCache = new SmoothedCache(0, demandFunction, 100);
        PidController pidController = new PidController(270.0, 7.5, samplingInterval); //Ziegler-Nichols - closedloop1
        //PidController pidController = new PidController(100.0, 4.3, samplingInterval); // Cohen-Coon - 2
        //PidController pidController = new PidController(80.0, 2.0, samplingInterval);  // AMIGO - 3
        //PidController pidController = new PidController(150.0, 2.0, samplingInterval); // 4
        SetpointFunction setpoint = new ConstantSetpointFunction(0.7);

        Observable<PlotItem> plotDataSource = ClosedLoops.closed_loop(samplingInterval, setpoint, pidController, smoothedCache, 10000);
        plotDataSource.onBackpressureBlock().subscribe(new PlottingAndSysOutPrintingSubscriber("Cache hitrate", 50));
    }

    private static class RandomJumpDemandFunction implements DemandFunction<Integer> {

        @Override
        public Integer demand(long time) {
            if (time < 3000) {
                return (int) new NormalDistribution(0.0, 15.0).sample(); // similiar to pythons random.gauss( 0, 15 )
            } else if (time < 5000) {
                return (int) new NormalDistribution(0.0, 35.0).sample();
            } else {
                return (int) new NormalDistribution(100.0, 15.0).sample();
            }
        }
    }

}
