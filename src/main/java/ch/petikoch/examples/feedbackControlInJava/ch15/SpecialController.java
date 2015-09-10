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
package ch.petikoch.examples.feedbackControlInJava.ch15;

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.Component;

/**
 * A java port of the 'SpecialController' python class from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch15-ch16-serverpool-and-queue.py
 */
public class SpecialController implements Component<Double, Double> {

    private int period1;
    private int period2;
    private long t;

    public SpecialController(int period1, int period2) {
        this.period1 = period1;
        this.period2 = period2;
        this.t = 0;
    }

    @Override
    public Double work(Double input) {
        if (input > 0) {
            t = period1;
            return 1.0;
        }

        t -= 1; // At this point: input <= 0 guaranteed!

        if (t == 0) {
            t = period2;
            return -1.0;
        }

        return 0.0;
    }
}

/*
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
 */
