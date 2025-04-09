package com.example.cmpt370project;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class FinalProgressView extends StackPane {
    private GoalModel goalModel;
    private LineChart<String, Number> lineChart;
    private Label averageGoalsLabel;

    public FinalProgressView(GoalModel goalModel) {
        this.goalModel = goalModel;

        // Set up the root layout
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        // Add a title
        Label titleLabel = new Label("Overall Progress");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Set up the line chart
        setupLineChart();
    }

    private void setupLineChart() {
        // Define the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Goals");

        // Create the line chart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Goal Distribution Over Time");
    }
}
