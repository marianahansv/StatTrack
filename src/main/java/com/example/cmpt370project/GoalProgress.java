package com.example.cmpt370project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class GoalProgress extends VBox {
    private GoalModel goalModel;
    private PieChart pieChart;

    public GoalProgress(GoalModel goalModel) {
        this.goalModel = goalModel;
        setupChart();
        updateChart();
    }

    private void setupChart() {
        pieChart = new PieChart();
        pieChart.setTitle("Time Remaining");
        getChildren().add(pieChart);
    }

    public void updateChart() {
        pieChart.getData().clear();
        List<Goal> goals = goalModel.getGoals();
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

        for (Goal goal : goals) {
            long totalDays = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate());
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), goal.getEndDate());
            if (daysLeft < 0) daysLeft = 0;  // Avoid negative values if the goal is past due

            chartData.add(new PieChart.Data(goal.getTitle(), daysLeft));
        }

        pieChart.setData(chartData);
    }
}

