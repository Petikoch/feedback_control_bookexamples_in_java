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
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.datatypes.ZeroOrOne;
import com.google.common.base.Preconditions;
import net.jcip.annotations.NotThreadSafe;

import java.util.ArrayList;
import java.util.List;

/**
 * A java port of the FixedFilter python class from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/feedback.py
 * <p>
 * A fixed filter "smoothens" an input signal to a "smoother" output signal.
 * The way it's done here is just by mathematical mean averaging the last n number of input signals.
 *
 * @param <I> Type of input to "smoothen"
 * @param <O> Type of output
 */
@NotThreadSafe
public class FixedFilter<I extends Number, O extends Number> implements Component<I, O> {

    private final List<I> inputsToAvg = new ArrayList<>();
    private final int numberOfInputsToAvg;
    private final Class<O> outputType;

    public FixedFilter(int numberOfInputsToAvg, Class<O> outputType) {
        Preconditions.checkArgument(numberOfInputsToAvg > 0);
        Preconditions.checkNotNull(outputType);
        this.numberOfInputsToAvg = numberOfInputsToAvg;
        this.outputType = outputType;
    }

    @Override
    public O work(I input) {
        inputsToAvg.add(input);
        if (inputsToAvg.size() > numberOfInputsToAvg) {
            inputsToAvg.remove(0);
        }
        double sum = inputsToAvg.stream().mapToDouble(Number::doubleValue).sum();
        Double avg = sum / inputsToAvg.size();
        if (outputType.equals(Integer.class)) {
            return outputType.cast((int) Math.round(avg));
        } else if (outputType.equals(Long.class)) {
            return outputType.cast(Math.round(avg));
        } else if (outputType.equals(Double.class)) {
            return outputType.cast(avg);
        } else if (outputType.equals(ZeroOrOne.class)) {
            return outputType.cast(new ZeroOrOne((int) Math.round(avg)));
        } else if (outputType.equals(Float.class)) {
            return outputType.cast(avg.floatValue());
        } else {
            throw new RuntimeException("Not implemented conversion to " + outputType);
        }
    }
}

/*
class FixedFilter( Component ):
    def __init__( self, n ):
        self.n = n
        self.data = []

    def work( self, x ):
        self.data.append(x)

        if len(self.data) > self.n:
            self.data.pop(0)

        return float(sum(self.data))/len(self.data)
 */