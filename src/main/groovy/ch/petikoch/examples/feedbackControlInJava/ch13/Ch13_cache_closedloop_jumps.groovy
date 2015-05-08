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

import ch.petikoch.examples.feedbackControlInJava.plotting.JFreeChartPlotter
import ch.petikoch.examples.feedbackControlInJava.plotting.JPanelDisplayer
import ch.petikoch.examples.feedbackControlInJava.plotting.PlotSimpleSetpointActualItem
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.SamplingInterval
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.controllers.pidcontrollers.PidController
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.loopFunctions.ClosedLoops
import org.apache.commons.math3.distribution.NormalDistribution

class JumpsDemand implements DemandFunction<Integer> {

    @Override
    public Integer demand(long time) {
        int result;
        if (time < 3000) {
            result = (int) new NormalDistribution(0.0, 15.0).sample();
            // should be similiar to pythons random.gauss( 0, 15 )
        } else if (time < 5000) {
            result = (int) new NormalDistribution(0.0, 35.0).sample();
        } else {
            result = (int) new NormalDistribution(100.0, 15.0).sample();
        }
        return result;
    }
}

DemandFunction demandFunction = new JumpsDemand();
double setpoint = 0.7;

SamplingInterval samplingInterval = new SamplingInterval(1);
SmoothedCache smoothedCache = new SmoothedCache(0, demandFunction, 100);
PidController pidController = new PidController(270.0, 7.5, samplingInterval);

JPanelDisplayer.clearDisplay()
List<PlotSimpleSetpointActualItem> plotDataItems = ClosedLoops.closed_loop(samplingInterval, setpoint, pidController, smoothedCache, 1000);
JPanelDisplayer.displayPanel(JFreeChartPlotter.plot(plotDataItems, "Cache hitrate"));


