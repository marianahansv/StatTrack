package com.example.cmpt370project;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;

/**
 * This class focuses on creating the pie charts, line charts and scatter charts everytime the user picks either one or
 * two of those or decides to see all of them together.
 */
public class UserHIstoryProgressVisuals extends Node {
    /**
     * The UserProgressHIstoryVisModel is the model that consists of the pieces of data that we need for this view.
     */
    private UserProgressHIstoryVisModel historicalChartModel;

    /**
     * CheckBox to select which graphs do the user want to view.
     * This could be one, two or more than two graphs.
     */
    private CheckBox chartSelector; //no parameters needed

    /**
     * Radio Button to select if the user wants descriptive statistics to be
     * created alongside the graphs or not.
     */
    private RadioButton includeDescriptiveStatistics;

    /**
     * Radio Button to select to allow the user to choose the color of their
     * graphs.
     */
    private RadioButton colorPreference;

    /**
     * Piechart for the visualization purposes.
     */
    private PieChart pieChart;

    /**
     * Linechart for the visualization purposes.
     */
    private LineChart<String, Integer> lineChart;

    /**
     * Scatter chart for the visualization purposes.
     */
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
