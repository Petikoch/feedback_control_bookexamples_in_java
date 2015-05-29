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
package ch.petikoch.examples.feedbackControlInJava.ui.plotting;

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.plotting.TimeSetpointActualPlotItem;
import ch.petikoch.examples.feedbackControlInJava.ui.util.SwingUtils;
import net.jcip.annotations.ThreadSafe;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

@ThreadSafe
public class JFreeChartPlotter {

    private XYSeriesCollection dataset;

    public JFreeChartPlotter(String chartTitle) {
        try {
            SwingUtils.executeBlockingOnEdt(() -> {
                JPanelDisplayer.clearDisplay();
                dataset = createDataset();
                JFreeChart chart = createChart(dataset, chartTitle);
                JPanel panel = createPanel(chart);
                JPanelDisplayer.displayPanel(panel);
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            SwingUtils.printStackTraceAndDisplayToUser(ex);
        }
    }

    private XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries setpointSeries = new XYSeries("Setpoint");
        XYSeries actualSeries = new XYSeries("Actual");

        dataset.addSeries(setpointSeries);
        dataset.addSeries(actualSeries);

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset,
                                   String chartTitle) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle,
                "Time steps",
                "Hit rate",
                dataset,
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        renderer.setSeriesShapesVisible(0, false);

        return chart;
    }

    private JPanel createPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    public void plot(TimeSetpointActualPlotItem plotItem) {
        if (!Thread.currentThread().isInterrupted()) {
            try {
                SwingUtils.executeBlockingOnEdt(() -> {
                            XYSeries setpointSeries = dataset.getSeries(0);
                            XYSeries actualSeries = dataset.getSeries(1);

                            setpointSeries.add(plotItem.getTime(), plotItem.getSetpoint());
                            actualSeries.add(plotItem.getTime(), plotItem.getActual());
                        }
                );
            } catch (InvocationTargetException ex) {
                SwingUtils.printStackTraceAndDisplayToUser(ex);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
