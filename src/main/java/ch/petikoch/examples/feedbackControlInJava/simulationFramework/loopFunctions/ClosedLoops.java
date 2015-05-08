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
package ch.petikoch.examples.feedbackControlInJava.simulationFramework.loopFunctions;

import ch.petikoch.examples.feedbackControlInJava.plotting.PlotSimpleSetpointActualItem;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.Component;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.SamplingInterval;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.filtersAndActuators.Identity;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.setpoints.SetpointFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * A java port of the python closed_loop loops from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/feedback.py
 */
public class ClosedLoops {

    public static List<PlotSimpleSetpointActualItem> closed_loop(SamplingInterval samplingInterval,
                                                                 SetpointFunction setpoint,
                                                                 Component<Double, Double> controller,
                                                                 Component<Integer, Double> plant) {
        return closed_loop(samplingInterval, setpoint, controller, plant, 5000, false, new Identity<>(), new Identity<>());
    }

    public static List<PlotSimpleSetpointActualItem> closed_loop(SamplingInterval samplingInterval,
                                                                 SetpointFunction setpoint,
                                                                 Component<Double, Double> controller,
                                                                 Component<Integer, Double> plant,
                                                                 int simDuration) {
        return closed_loop(samplingInterval, setpoint, controller, plant, simDuration, false, new Identity<>(), new Identity<>());
    }

    public static List<PlotSimpleSetpointActualItem> closed_loop(SamplingInterval samplingInterval,
                                                                 SetpointFunction setpoint,
                                                                 Component<Double, Double> controller,
                                                                 Component<Integer, Double> plant,
                                                                 int simDuration,
                                                                 boolean inverted,
                                                                 Component<Double, Double> actuator,
                                                                 Component<Double, Double> returnFilter) {
        List<PlotSimpleSetpointActualItem> plotSimpleSetpointActualItems = new ArrayList<>(simDuration);

        double z = 0.0;
        for (int t = 0; t < simDuration; t++) {
            double r = setpoint.at(t);
            double e = r - z;
            if (inverted) {
                e = -e;
            }
            double u = controller.work(e);
            double v = actuator.work(u);
            double y = plant.work((int) Math.round(v));
            z = returnFilter.work(y);

            System.out.println(t + ", " + (t * samplingInterval.getInterval()) + ", " + r + ", " + e + ", " + u + ", " + v + ", " + y + ", " + z + ", " + plant.monitoring());
            plotSimpleSetpointActualItems.add(new PlotSimpleSetpointActualItem(t, setpoint.at(t), y));
        }

        return plotSimpleSetpointActualItems;
    }
}
