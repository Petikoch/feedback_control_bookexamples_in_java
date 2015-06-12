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
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.SamplingInterval;
import net.jcip.annotations.NotThreadSafe;

/**
 * A java port of the Integrator python class from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/feedback.py
 */
@NotThreadSafe
public class Integrator implements Component<Double, Double> {

    private final SamplingInterval samplingInterval;
    private double data;

    public Integrator(SamplingInterval samplingInterval) {
        this.samplingInterval = samplingInterval;
    }

    @Override
    public Double work(Double input) {
        data += input;
        return samplingInterval.getInterval() * data;
    }
}

/*
class Integrator( Component ):
    def __init__( self ):
        self.data = 0

    def work( self, u ):
        self.data += u
        return DT*self.data
*/
