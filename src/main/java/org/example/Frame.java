package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Frame extends JFrame {
    private final JLabel cityLabel;

    public Frame() {
        cityLabel = new JLabel("Now viewing: Denver", SwingConstants.CENTER);
        cityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        cityLabel.setPreferredSize(new Dimension(800, 50));
        createSearchPanel();

        // Initial screen
        updateScreen("Denver");
    }

    public void createSearchPanel() {
        JMenuBar menuBar = new JMenuBar();
        JMenu cityMenu = new JMenu("Cities");

        JMenuItem london = new JMenuItem("London");
        JMenuItem losAngeles = new JMenuItem("Los Angeles");
        JMenuItem sacramento = new JMenuItem("Sacramento");
        JMenuItem denver = new JMenuItem("Denver");

        london.addActionListener(e -> updateScreen("London"));
        losAngeles.addActionListener(e -> updateScreen("LA"));
        sacramento.addActionListener(e -> updateScreen("Sacramento"));
        denver.addActionListener(e -> updateScreen("Denver"));

        cityMenu.add(london);
        cityMenu.add(losAngeles);
        cityMenu.add(sacramento);
        cityMenu.add(denver);

        menuBar.add(cityMenu);

        setJMenuBar(menuBar);
    }

    public void updateScreen(String city) {
        cityLabel.setText("Now viewing: " + city);

        APIPuller apiPuller = APIPuller.getInstance();
        ArrayList<Integer> temperatureForecast = apiPuller.getForecastTemperature(city, 7, false);
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

        // Create main panel and add components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(cityLabel, BorderLayout.NORTH);
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        Frame app = new Frame();
        app.setSize(800, 800);
        app.setTitle("Weather App");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setResizable(false);
        app.setVisible(true);
    }
}