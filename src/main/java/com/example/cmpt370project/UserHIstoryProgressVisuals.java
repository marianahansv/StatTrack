package com.example.cmpt370project;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;

/**
 * This class focuses on creating the pie charts, line charts and scatter charts everytime the user picks either one or
 * two of those or decides to see all of them together.
 */
public class UserHIstoryProgressVisuals extends Node {
    private UserProgressHIstoryVisModel historicalChartModel;

    private String GraphChoices[] = {"Line Graph", "Scatter Graph", "Pie Chart"};
    private PieChart pieChart;
    private LineChart<String, Integer> lineChart;
    private ScatterChart<String, Integer> scatterChart;

    public UserHIstoryProgressVisuals(UserProgressHIstoryVisModel historicalChartModel){
        this.historicalChartModel = historicalChartModel;
        // we will have to create a way to choose the type of chart that we are interested in
        // we will need to have an update function as well
    }

    private void drawPieCharts(){}

    private void drawLineCharts(){}

    private void drawScatterCharts(){}

    private void generateDescriptiveStatistics(){}
    
    void updateRequiredCharts(){}

    private void updatePieCharts(){}

    private void updateLineCharts(){}

    private void updateScatterCharts(){}

    private void updateDescriptiveStatistics(){}

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
