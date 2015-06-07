/**
 * Copyright 2015 Peti Koch, All rights reserved.
 * <p>
 * Project Info: https://github.com/Petikoch/feedback_control_bookexamples_in_java
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ch.petikoch.examples.feedbackControlInJava.examples;

import ch.petikoch.examples.feedbackControlInJava.ch14.AdPublisher;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.Component;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.SamplingInterval;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.controllers.pidcontrollers.PidController;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.filtersAndActuators.Identity;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.filtersAndActuators.RecursiveFilter;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.loopFunctions.ClosedLoops;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.plotting.PlotItem;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.setpoints.SetpointFunction;
import ch.petikoch.examples.feedbackControlInJava.ui.PlottingAndSysOutPrintingSubscriber;
import rx.Observable;

/**
 * A java port of the closedloop python function from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch14-adserving.py
 */
public class Ch14_adserving {

	public static void main(String[] args) {
		closedloop(1.0, 0.125, new RecursiveFilter(0.125));
	}

	private static void closedloop(double kp, double ki, Component<Double, Double> returnFilter) {
		SamplingInterval samplingInterval = new SamplingInterval(1);
		SetpointFunction setpoint = new HigherSetpointAfter1000();
		double k = 1.0 / 20.0;
		AdPublisher adPublisher = new AdPublisher(100, 2);
		PidController pidController = new PidController(k * kp, k * ki, samplingInterval);

		Observable<PlotItem> plotDataSource = ClosedLoops.closed_loop(samplingInterval, setpoint, pidController, adPublisher, 5000, false, new Identity<>(), returnFilter);
		plotDataSource.onBackpressureBlock().subscribe(new PlottingAndSysOutPrintingSubscriber("Impressions per Day", 50));
	}

	private static class HigherSetpointAfter1000 implements SetpointFunction {

		@Override
		public Double at(long time) {
			if (time > 1000) {
				return 125d;
			} else {
				return 100d;
			}
		}
	}
}
