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

import ch.petikoch.examples.feedbackControlInJava.ch15.QueueLoadingWorkFunction;
import ch.petikoch.examples.feedbackControlInJava.ch15.ServerPool;
import ch.petikoch.examples.feedbackControlInJava.ch15.ServerWorkFunction;
import ch.petikoch.examples.feedbackControlInJava.ch15.SpecialController;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.SamplingInterval;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.filtersAndActuators.Identity;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.filtersAndActuators.Integrator;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.loopFunctions.ClosedLoops;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.plotting.PlotItem;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.setpoints.SetpointFunction;
import ch.petikoch.examples.feedbackControlInJava.ui.PlottingAndSysOutPrintingSubscriber;
import ch.petikoch.examples.feedbackControlInJava.util.RandomNumberUtils;
import rx.Observable;

/**
 * A java port of the 'closedloop3' python function from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/877e96053dff96fcd565230efbe16aced4137e08/ch15-ch16-serverpool-and-queue.py
 */
public class Ch15_serverpool {

    public static void main(String[] args) {
        SamplingInterval samplingInterval = new SamplingInterval(1);
        SetpointFunction setpoint = new Setpoint1();
        ServerPool serverPool = new ServerPool(0, new ConsumeQueueFunction(), new LoadQueueFunction());
        SpecialController controller = new SpecialController(100, 10);

        Observable<PlotItem> plotDataSource = ClosedLoops.closed_loop(samplingInterval, setpoint, controller, serverPool, 5000, false, new Integrator(samplingInterval), new Identity<>());
        plotDataSource.onBackpressureBlock().subscribe(new PlottingAndSysOutPrintingSubscriber("Completion rate", "Number of server instances", 50));
    }

    private static class Setpoint1 implements SetpointFunction {

        @Override
        public Double at(long time) {
            return 1.0d;
        }
    }

    private static class ConsumeQueueFunction implements ServerWorkFunction<Integer> {

        @Override
        public Integer work() {
            double alpha = 20.0;
            double beta = 2.0;
            return (int) Math.round(RandomNumberUtils.betavariate(alpha, beta));
        }
    }

    private static class LoadQueueFunction implements QueueLoadingWorkFunction<Integer> {

        @Override
        public Integer getLoad() {
            return (int) Math.round(RandomNumberUtils.randomGaussian(1000.0, 5.0));
        }
    }
}

/*
def closedloop3():
    # Closed loop, setpoint 1.0, incremental controller (non-PID)

    def setpoint( t ):
        return 1.0

    class SpecialController( fb.Component ):
        def __init__( self, period1, period2 ):
            self.period1 = period1
            self.period2 = period2
            self.t = 0

        def work( self, u ):
            if u > 0:
                self.t = self.period1
                return +1

            self.t -= 1          # At this point: u <= 0 guaranteed!

            if self.t == 0:
                self.t = self.period2
                return -1

            return 0

    p = ServerPool( 0, consume_queue, load_queue )
    c = SpecialController( 100, 10 )
    fb.closed_loop( setpoint, c, p, actuator=fb.Integrator() )
 */