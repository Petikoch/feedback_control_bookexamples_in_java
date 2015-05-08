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
package ch.petikoch.examples.feedbackControlInJava.plotting;

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
import java.util.List;

@ThreadSafe
public class JFreeChartPlotter {

    public static JPanel plot(List<PlotSimpleSetpointActualItem> plotSimpleSetpointActualItems,
                              String chartTitle) {
        XYSeriesCollection dataset = createDataset(plotSimpleSetpointActualItems);
        JFreeChart chart = createChart(dataset, chartTitle);
        return createPanel(chart);
    }

    private static XYSeriesCollection createDataset(List<PlotSimpleSetpointActualItem> plotSimpleSetpointActualItems) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries setpointSeries = new XYSeries("Setpoint");
        XYSeries actualSeries = new XYSeries("Actual");

        dataset.addSeries(setpointSeries);
        dataset.addSeries(actualSeries);

        plotSimpleSetpointActualItems.forEach(plotEvent -> {
            setpointSeries.add(plotEvent.getTime(), plotEvent.getSetpoint());
            actualSeries.add(plotEvent.getTime(), plotEvent.getActual());
        });

        return dataset;
    }

    private static JFreeChart createChart(XYDataset dataset,
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

    private static JPanel createPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
}
