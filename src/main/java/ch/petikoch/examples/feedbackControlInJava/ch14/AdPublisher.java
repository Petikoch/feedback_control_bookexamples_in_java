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
package ch.petikoch.examples.feedbackControlInJava.ch14;

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.Component;
import ch.petikoch.examples.feedbackControlInJava.util.RandomNumberUtils;
import net.jcip.annotations.NotThreadSafe;

/**
 * A java port of the AdPublisher python class from
 * <p>
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch14-adserving.py
 */
@NotThreadSafe
public class AdPublisher implements Component<Double, Double> {

	private final double scale;
	private final double min;
	private final double width;

	private double lastPrice = 0.0;

	public AdPublisher(final double scale,
	                   final double min_price) {
		this(scale, min_price, 0.1);
	}

	public AdPublisher(final double scale,
	                   final double min_price,
	                   final double relative_width) {
		this.scale = scale;
		this.min = min_price;
		this.width = relative_width;
	}

	@Override
	public Double work(final Double u) {
		lastPrice = u;
		if (u <= min) {
			return 0.0d;
		}

		// "demand" is the number of impressions served per day
		// The demand is modeled (!) as Gaussian distribution with
		// a mean that depends logarithmically on the price u.
		double mean = scale * Math.log(u / min);
		int demand = (int) RandomNumberUtils.randomGaussian(mean, width * mean);

		return Integer.valueOf(Math.max(0, demand)).doubleValue();
	}

	@Override
	public String monitoring() {
		return Double.toString(lastPrice);
	}
}

/*
class AdPublisher( fb.Component ):

    def __init__( self, scale, min_price, relative_width=0.1 ):
        self.scale = scale
        self.min = min_price
        self.width = relative_width

    def work( self, u ):
        if u <= self.min:       # Price below min: no impressions
            return 0

        # "demand" is the number of impressions served per day
        # The demand is modeled (!) as Gaussian distribution with
        # a mean that depends logarithmically on the price u.

        mean = self.scale*math.log( u/self.min )
        demand = int( random.gauss( mean, self.width*mean ) )

        return max( 0, demand ) # Impression demand is greater than zero
 */
