
package com.example.cmpt370project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Controller class for linking the GoalChart view with the Goal model.
 * This controller listens for updates in the model and refreshes the chart view accordingly.
 */
public class GoalChartController {
    /**
     * The view that displays goal charts.
     */
    private GoalProgress chartView;
    
    /**
     * The model that contains goal data.
     */
    private GoalModel goalModel;

    /**
     * Constructs a new GoalChartController with the specified chart view and goal model.
     * @param chartView the view responsible for displaying goal charts.
     * @param goalModel the model providing goal data.
     */
    public GoalChartController(GoalProgress chartView, GoalModel goalModel) {
        this.chartView = chartView;
        this.goalModel = goalModel;
        
        // Subscribe to model updates so that the chart view is refreshed whenever the model changes.
//        goalModel.addSubscriber(() -> chartView.updateChart()); // Updates the view
    }
}