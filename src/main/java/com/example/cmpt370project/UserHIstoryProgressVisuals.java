package com.example.cmpt370project;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class focuses on creating the pie charts, line charts and scatter charts everytime the user picks either one or
 * two of those or decides to see all of them together.
 */
public class UserHIstoryProgressVisuals extends VBox {
    /**
     * The UserProgressHIstoryVisModel is the model that consists of the pieces of data that we need for this view.
     */
    private UserProgressHIstoryVisModel historicalChartModel;

    /**
     * CheckBox to select if the Pie Chart should be selected or not.
     */
    private CheckBox checkboxPieChart;

    /**
     * CheckBox to select if the Line Chart should be selected or not.
     */
    private CheckBox checkboxLineChart;

    /**
     * CheckBox to select if the Scatter Chart should be selected or not.
     */
    private CheckBox checkboxScatterGraph;

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

    /**
     * Constuctor for the UserHIstoryProgressVisuals class that makes use of the UserProgressHIstory model
     * @param historicalChartModel: The model that helps to function with this view
     */
    public UserHIstoryProgressVisuals(UserProgressHIstoryVisModel historicalChartModel){
        this.historicalChartModel = historicalChartModel;
        /* Select, backend prepare and update the charts */
        setChartType();
        // we will need to have an update function as well

        /*Select, backend prepare and update the descriptive statistics */

        /*Select, backend prepare and update the color preferences */
    }

    private void setChartType(){
        Label label = new Label("Graph Preferences: ");
        checkboxPieChart= new CheckBox("Pie Chart");
        checkboxLineChart = new CheckBox("Line Chart");
        checkboxScatterGraph = new CheckBox("Scatter Chart");

        /* Setting each of the graphs to be unchecked */
        checkboxPieChart.setSelected(false);
        checkboxLineChart.setSelected(false);
        checkboxScatterGraph.setSelected(false);

        /* Settinng up listeners for each of my buttons */
        checkboxPieChart.setOnAction(e -> updatePieCharts());
        checkboxLineChart.setOnAction(e -> updateLineCharts());
        checkboxScatterGraph.setOnAction(e -> updateScatterCharts());

        HBox horizontalbox = new HBox(10);
        horizontalbox.getChildren().addAll(label, checkboxPieChart, checkboxLineChart, checkboxScatterGraph);
        horizontalbox.setAlignment(Pos.CENTER);
        getChildren().addAll(horizontalbox);
    }

    private void setDescriptiveStatistics(){

    }
    private void drawPieCharts(){}

    private void drawLineCharts(){}

    private void drawScatterCharts(){}

    private void generateDescriptiveStatistics(){}

    void updatePieCharts(){}

    void updateLineCharts(){}

    void updateScatterCharts(){}

    private void updateDescriptiveStatistics(){}

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

}
