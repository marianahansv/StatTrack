package com.example.cmpt370project;

import javafx.scene.Node;

/**
 * This class focuses on creating the pie charts, line charts and scatter charts everytime the user picks either one or
 * two of those or decides to see all of them together.
 */
public class UserHIstoryProgressVisuals extends Node {
    private UserProgressHIstoryVisModel historicalChartModel;

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
