
package com.example.cmpt370project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class GoalChartController {
    private GoalProgress chartView;
    private GoalModel goalModel;

    public GoalChartController(GoalProgress chartView, GoalModel goalModel) {
        this.chartView = chartView;
        this.goalModel = goalModel;

        goalModel.addSubscriber(() -> chartView.updateChart()); // Updates the view
    }
}
