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
 * A java port of the 'AbstractServerPool' python class from
 * https://github.com/oreillymedia/feedback_control_for_computer_systems/blob/master/ch15-ch16-serverpool-and-queue.py
 */
public abstract class AbstractServerPool implements Component<Double, Double> {

    protected int numberOfServerInstances;
    protected int numberOfItemsInQueue;
    protected ServerWorkFunction<Integer> serverWorkFunction;
    protected QueueLoadingWorkFunction<Integer> queueLoadingWorkFunction;

    public AbstractServerPool(int numberOfServerInstances,
                              ServerWorkFunction<Integer> serverWorkFunction,
                              QueueLoadingWorkFunction queueLoadingWorkFunction) {
        this.numberOfServerInstances = numberOfServerInstances;
        this.numberOfItemsInQueue = 0;
        this.serverWorkFunction = serverWorkFunction;
        this.queueLoadingWorkFunction = queueLoadingWorkFunction;
    }

    @Override
    public Double work(Double input) {
        numberOfServerInstances = Math.max(0, (int) Math.round(input));

        int completed = 0;
        for (int i = 1; i < numberOfServerInstances; i++) {
            completed += serverWorkFunction.work(); // each server does some amount of work

            if (completed >= numberOfItemsInQueue) {
                completed = numberOfItemsInQueue; // "trim" completed to queue length
                break; // stop if queue is empty
            }
        }

        numberOfItemsInQueue -= completed; // reduce queue by work completed

        return Integer.valueOf(completed).doubleValue();
    }

    @Override
    public String monitoring() {
        return Integer.toString(numberOfServerInstances); // TODO queue length for ch16
    }
}

/*
class AbstractServerPool( fb.Component ):
    def __init__( self, n, server, load ):
        self.n = n           # number of server instances
        self.queue = 0       # number of items in queue

        self.server = server # server work function
        self.load = load     # queue-loading work function


    def work( self, u ):
        self.n = max(0, int(round(u))) # server count: non-negative integer

        completed = 0
        for _ in range(self.n):
            completed += self.server() # each server does some amount of work

            if completed >= self.queue:
                completed = self.queue # "trim" completed to queue length
                break                  # stop if queue is empty

        self.queue -= completed        # reduce queue by work completed

        return completed


    def monitoring( self ):
        return "%d %d" % ( self.n, self.queue )
 */
