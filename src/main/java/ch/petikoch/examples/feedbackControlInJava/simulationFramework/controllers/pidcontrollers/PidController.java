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
package ch.petikoch.examples.feedbackControlInJava.simulationFramework.controllers.pidcontrollers;

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.Component;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.SamplingInterval;
import com.google.common.base.Preconditions;

/**
 * A java port of the PidController python class from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/feedback.py
 */
public class PidController implements Component<Double, Double> {

    private final double kp, ki, kd;
    private final SamplingInterval samplingInterval;
    private double i, d, prev;

    public PidController(double kp,
                         double ki,
                         SamplingInterval samplingInterval) {
        this(kp, ki, 0.0, samplingInterval);
    }

    public PidController(double kp,
                         double ki,
                         double kd,
                         SamplingInterval samplingInterval) {
        Preconditions.checkArgument(kp >= 0.0d);
        Preconditions.checkArgument(ki >= 0.0d);
        Preconditions.checkArgument(kd >= 0.0d);
        Preconditions.checkNotNull(samplingInterval);

        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.samplingInterval = samplingInterval;

        this.i = 0.0;
        this.d = 0.0;
        this.prev = 0.0;
    }

    @Override
    public Double work(Double e) {
        i += samplingInterval.getInterval() * e;
        d = (e - prev) / samplingInterval.getInterval();
        prev = e;

        return (kp * e) + (ki * i) + (kd * d);
    }
}
