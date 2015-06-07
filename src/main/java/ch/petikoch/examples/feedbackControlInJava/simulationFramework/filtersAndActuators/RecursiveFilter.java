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
package ch.petikoch.examples.feedbackControlInJava.simulationFramework.filtersAndActuators;

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.Component;

/**
 * A java port of the RecursiveFilter python class from https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/feedback.py
 * <p>
 * A recusrive filter "smoothens" an input signal to a "smoother" output signal.
 */
public class RecursiveFilter implements Component<Double, Double> {

	//TODO write tests to show behaviour

	private final double alpha;
	private double y;

	public RecursiveFilter(double alpha) {
		this.alpha = alpha;
		this.y = 0;
	}

	@Override
	public Double work(Double x) {
		y = (alpha * x) + (1 - alpha) * y;
		return y;
	}
}

/*
class RecursiveFilter( Component ):
    def __init__( self, alpha ):
        self.alpha = alpha
        self.y = 0

    def work( self, x ):
        self.y = self.alpha*x + (1-self.alpha)*self.y
        return self.y
 */