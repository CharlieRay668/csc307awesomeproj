package org.example;

import javax.swing.*;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * (author) Charlie Ray
 * (author) Christopher Grigorian
 * (author) Eric Canihuante
 * (author) Harold Ellis
 */

public class Frame extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        APIPuller apiPuller = APIPuller.getInstance();
        ArrayList<Integer> temperatureForecast = apiPuller.getForecastTemperature("Denver", 7, false);
        // Create dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < temperatureForecast.size(); i++) {
            dataset.addValue(temperatureForecast.get(i), "Temperature", "Day " + (i + 1));
        }

        // Create chart
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Temperature Forecast",
                "Days",
                "Temperature (F)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Add chart to panel
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        frame.setContentPane(chartPanel);

        frame.setVisible(true);
    }
}