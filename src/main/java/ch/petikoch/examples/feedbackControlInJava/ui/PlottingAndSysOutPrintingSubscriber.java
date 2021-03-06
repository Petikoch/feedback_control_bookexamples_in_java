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
package ch.petikoch.examples.feedbackControlInJava.ui;

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.plotting.PlotItem;
import ch.petikoch.examples.feedbackControlInJava.ui.plotting.JFreeChartPlotter;
import ch.petikoch.examples.feedbackControlInJava.ui.util.SwingUtils;
import net.jcip.annotations.ThreadSafe;
import rx.Subscriber;

import java.util.concurrent.atomic.AtomicLong;

@ThreadSafe
public class PlottingAndSysOutPrintingSubscriber extends Subscriber<PlotItem> {

    private final int plotEveryNth;
    private final JFreeChartPlotter plotter;
    private final boolean doPrintToSysOut;

    private final AtomicLong itemCounter = new AtomicLong(0);

    public PlottingAndSysOutPrintingSubscriber(String chartTitle,
                                               String monitoringSeriesName) {
        this(chartTitle, monitoringSeriesName, 1, true);
    }

    public PlottingAndSysOutPrintingSubscriber(String chartTitle,
                                               String monitoringSeriesName,
                                               int plotEveryNth) {
        this(chartTitle, monitoringSeriesName, plotEveryNth, true);
    }

    public PlottingAndSysOutPrintingSubscriber(String chartTitle,
                                               String monitoringSeriesName,
                                               int plotEveryNth,
                                               boolean doPrintToSysOut) {
        this.plotEveryNth = plotEveryNth;
        this.doPrintToSysOut = doPrintToSysOut;

        plotter = new JFreeChartPlotter(chartTitle, monitoringSeriesName);
    }

    @Override
    public void onCompleted() {
        if (doPrintToSysOut) {
            System.out.println("Finished");
        }
    }

    @Override
    public void onError(Throwable ex) {
        if (ex instanceof InterruptedException) {
            if (doPrintToSysOut) {
                System.out.println("Aborted");
            }
        } else {
            SwingUtils.printStackTraceAndDisplayToUser(ex);
        }
    }

    @Override
    public void onNext(PlotItem plotItem) {
        if (doPrintToSysOut) {
            System.out.println(plotItem.getLogLine());
        }

        if ((plotEveryNth < 1) || ((itemCounter.incrementAndGet() % plotEveryNth) == 0)) {
            plotter.plot(plotItem.getTimeSetpointActualPlotItem(),
                    plotItem.getTimeMonitoringPlotItem());
        }
    }
}
