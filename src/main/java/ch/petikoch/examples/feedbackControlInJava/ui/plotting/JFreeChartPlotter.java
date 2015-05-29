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

import ch.petikoch.examples.feedbackControlInJava.simulationFramework.plotting.TimeMonitoringPlotItem;
import ch.petikoch.examples.feedbackControlInJava.simulationFramework.plotting.TimeSetpointActualPlotItem;
import ch.petikoch.examples.feedbackControlInJava.ui.util.SwingUtils;
import com.google.common.collect.Lists;
import net.jcip.annotations.ThreadSafe;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@ThreadSafe
public class JFreeChartPlotter {

    private XYSeriesCollection setpointActualDataset;
    private XYSeriesCollection monitoringDataset;

    public JFreeChartPlotter(String chartTitle) {
        try {
            SwingUtils.executeBlockingOnEdt(() -> {
                JPanelDisplayer.clearDisplay();
                setpointActualDataset = createSetpointActualDataset();
                monitoringDataset = createMonitoringDataset();
                JFreeChart chart = createChart(
                        Lists.newArrayList(setpointActualDataset, monitoringDataset),
                        chartTitle);
                JPanel panel = createPanel(chart);
                JPanelDisplayer.displayPanel(panel);
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            SwingUtils.printStackTraceAndDisplayToUser(ex);
        }
    }

    private XYSeriesCollection createSetpointActualDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries setpointSeries = new XYSeries("Setpoint hit rate");
        XYSeries actualSeries = new XYSeries("Actual hit rate");
        dataset.addSeries(setpointSeries);
        dataset.addSeries(actualSeries);
        return dataset;
    }

    private XYSeriesCollection createMonitoringDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries monitoringSeries = new XYSeries("Actual cache size");
        dataset.addSeries(monitoringSeries);
        return dataset;
    }

    private JFreeChart createChart(List<XYDataset> datasets,
                                   String chartTitle) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle,
                "Time steps",
                "Hit rate",
                datasets.get(0),
                PlotOrientation.VERTICAL,
                true,                     // include legend
                true,                     // tooltips
                false                     // urls
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);

        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(true, false);
        renderer1.setSeriesPaint(0, Color.blue);
        renderer1.setSeriesPaint(1, Color.red);
        plot.setRenderer(0, renderer1);

        NumberAxis monitoringRangeAxis = new NumberAxis("Actual cache size");
        plot.setRangeAxis(1, monitoringRangeAxis);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, datasets.get(1));
        plot.mapDatasetToRangeAxis(1, 1);

        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        renderer2.setSeriesPaint(0, Color.green.darker());
        plot.setRenderer(1, renderer2);

        return chart;
    }

    private JPanel createPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    public void plot(TimeSetpointActualPlotItem timeSetpointActualPlotItem,
                     TimeMonitoringPlotItem timeMonitoringPlotItem) {
        if (!Thread.currentThread().isInterrupted()) {
            try {
                SwingUtils.executeBlockingOnEdt(() -> {
                            XYSeries setpointSeries = setpointActualDataset.getSeries(0);
                            XYSeries actualSeries = setpointActualDataset.getSeries(1);

                            setpointSeries.add(timeSetpointActualPlotItem.getTime(), timeSetpointActualPlotItem.getSetpoint());
                            actualSeries.add(timeSetpointActualPlotItem.getTime(), timeSetpointActualPlotItem.getActual());

                            if (isDouble(timeMonitoringPlotItem.getMonitoring())) {
                                XYSeries monitoringSeries = monitoringDataset.getSeries(0);
                                monitoringSeries.add(timeMonitoringPlotItem.getTime(), Double.valueOf(timeMonitoringPlotItem.getMonitoring()));
                            }
                        }
                );
            } catch (InvocationTargetException ex) {
                SwingUtils.printStackTraceAndDisplayToUser(ex);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean isDouble(String monitoring) {
        try {
            Double.parseDouble(monitoring);
            return true;
        } catch (Exception nfe) {
            return false;
        }
    }
}
