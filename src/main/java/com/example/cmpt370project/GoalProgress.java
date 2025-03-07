package com.example.cmpt370project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/*
 * View class for visualizing the progress of goals.
 * This view displays each goal's progress using separate charts.
 */
public class GoalProgress extends VBox {
 /**
     * The goal model that provides goal data.
     */
    private GoalModel goalModel;

    /**
     * ChoiceBox for selecting the type of chart to display.
     */
    private ChoiceBox<String> chartTypeSelector;

    /**
     * PieChart for visualization
     */
    private PieChart pieChart;

    /**
     * BarChart for visualization
     */
    private BarChart<String, Number> barChart;

    /**
     * LineChart for visualization
     */
    private LineChart<String, Number> lineChart;

   /**
     * Constructs a new GoalProgress view with the specified goal model.
     * @param goalModel the model containing goal data.
     */ 
    public GoalProgress(GoalModel goalModel) {
        this.goalModel = goalModel;
        setupChartSelector();
        setupChart();
        updateChart();  // Initialize with data
    }

    /**
     * Sets up the chart type selector allowing the user to choose the visualization type.
     */
    private void setupChartSelector() {
        chartTypeSelector = new ChoiceBox<>();
        chartTypeSelector.getItems().addAll("Pie Chart", "Bar Chart", "Line Chart"); // Types of visualization
        chartTypeSelector.setValue("Pie Chart"); // Defaulting to pie chart bc they are more epic

        chartTypeSelector.setOnAction(e -> updateChart());
        getChildren().add(chartTypeSelector);
    }


    /**
     * Sets up the charts with the chosen visualization type.
     */
    private void setupChart() {
        pieChart = new PieChart();
        pieChart.setTitle("Time Remaining");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Time Remaining");

        NumberAxis yAxisLine = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxisLine);
        lineChart.setTitle("Time Remaining");
    }


    /**
     * Updates the view by removing existing charts and adding new charts for each goal 
     * based on the selected chart type.
     */
    public void updateChart() { // Updates the chart type that needs to be updated
        getChildren().removeIf(node -> node instanceof Chart); // Remove prior chart s new chart can be reborn
        String selectedChart = chartTypeSelector.getValue();

        if (selectedChart.equals("Pie Chart")) {
            updatePieChart();
           // getChildren().add(pieChart);
        } else if (selectedChart.equals("Bar Chart")) {
            updateBarChart();
            getChildren().add(barChart);
        } else if (selectedChart.equals("Line Chart")) {
            updateLineChart();
            getChildren().add(lineChart);
        }
    }

    /**
     * Creates and adds a PieChart for each goal in the model.
     */
    private void updatePieChart() {
        for (Goal goal : goalModel.getGoals()) {
            PieChart pieChart = createPieChartForGoal(goal);
            getChildren().add(pieChart);
        }
    }


    /**
     * Creates a PieChart for the specified goal.
     * @param goal the goal for which to create the chart.
     * @return a PieChart representing the goal's progress.
     */    
    private PieChart createPieChartForGoal(Goal goal) {
        PieChart pieChart = new PieChart();
        pieChart.setTitle(goal.getTitle());
        // Calculate total days, days left, and days completed.
        long totalDays = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate());
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), goal.getEndDate());
        if (daysLeft < 0) daysLeft = 0;
        long daysCompleted = totalDays - daysLeft;

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
            new PieChart.Data("Completed", daysCompleted),
            new PieChart.Data("Remaining", daysLeft)
        );
        pieChart.setData(data);
        return pieChart;
    }

    /**
     * Creates and adds a BarChart.
     */
    private void updateBarChart() {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Days Left");

        for (Goal goal : goalModel.getGoals()) {
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), goal.getEndDate());
            if (daysLeft < 0) {continue;}
            
            series.getData().add(new XYChart.Data<>(goal.getTitle(), daysLeft));
        }

        barChart.getData().add(series);
    }

    /**
     * Creates and adds a LineChart.
     */
    private void updateLineChart() {
        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Goal Progress");

        for (Goal goal : goalModel.getGoals()) {
            long totalDays = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate());
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), goal.getEndDate());
            if (daysLeft < 0) daysLeft = 0;

            series.getData().add(new XYChart.Data<>(goal.getStartDate().toString(), totalDays));
            series.getData().add(new XYChart.Data<>(goal.getEndDate().toString(), daysLeft));
        }

        lineChart.getData().add(series);
    }
}

