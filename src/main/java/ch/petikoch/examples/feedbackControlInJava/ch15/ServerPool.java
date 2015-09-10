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

/**
 * A java port of the 'ServerPool' python class from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch15-ch16-serverpool-and-queue.py
 */
public class ServerPool extends AbstractServerPool {

    public ServerPool(int numberOfServerInstances,
                      ServerWorkFunction<Integer> serverWorkFunction,
                      QueueLoadingWorkFunction queueLoadingWorkFunction) {
        super(numberOfServerInstances, serverWorkFunction, queueLoadingWorkFunction);
    }

    @Override
    public Double work(Double input) {
        int load = queueLoadingWorkFunction.getLoad(); // additions to the queue
        numberOfItemsInQueue = load;  // new load replaces old load

        if (load == 0) {
            return 1.0; // no work: 100 percent completion rate
        }

        double completed = super.work(input);
        return completed / load;
    }
}

/*

class ServerPool( AbstractServerPool ):
    def work( self, u ):
        load = self.load()        # additions to the queue
        self.queue = load         # new load replaces old load

        if load == 0: return 1    # no work: 100 percent completion rate

        completed = AbstractServerPool.work( self, u )

        return completed/load     # completion rate
 */