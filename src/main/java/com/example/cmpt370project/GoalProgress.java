package com.example.cmpt370project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

/*
 * View class for visualizing the progress of goals.
 * This view displays each goal's progress using separate charts.
 */
public class GoalProgress extends VBox implements Subscriber {
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
        //this.goalModel.notifySubscribers();
        goalModel.addSubscriber(this);
        setupChartSelector();
        setupChart();
        updateChart();  // Initialize with data
    }

    @Override
    public void modelUpdated() {
        // When the model changes, update the chart automatically.
        updateChart();
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
        CategoryAxis xAxisLine = new CategoryAxis();
        lineChart = new LineChart<>(xAxisLine, yAxisLine);
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
        pieChart.setTitle((isDue(goal)));
        // Calculate total days, days left, and days completed.
        long totalDays = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate());
        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), goal.getEndDate());
        if (daysLeft < 0) daysLeft = 0;
        long daysCompleted = totalDays - daysLeft;

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
            new PieChart.Data("Completed "+"("+daysCompleted+" Days)", daysCompleted),
            new PieChart.Data("Remaining "+"("+daysLeft+" Days)", daysLeft)
        ); //intiializes data and has a default that will work

        if (daysCompleted == 1 && daysLeft != 1) {
            data = FXCollections.observableArrayList(
            new PieChart.Data("Completed "+"("+daysCompleted+" Day)", daysCompleted),
            new PieChart.Data("Remaining "+"("+daysLeft+" Days)", daysLeft)
        );
        }else if (daysCompleted != 1 && daysLeft == 1) {
            data = FXCollections.observableArrayList(
            new PieChart.Data("Completed "+"("+daysCompleted+" Days)", daysCompleted),
            new PieChart.Data("Remaining "+"("+daysLeft+" Day)", daysLeft)
        );
        }else if (daysCompleted == 1 && daysLeft == 1) {
            data = FXCollections.observableArrayList(
            new PieChart.Data("Completed "+"("+daysCompleted+" Day)", daysCompleted),
            new PieChart.Data("Remaining "+"("+daysLeft+" Day)", daysLeft)
        );
        }else if (daysCompleted != 1 && daysLeft != 1) {
            data = FXCollections.observableArrayList(
            new PieChart.Data("Completed "+"("+daysCompleted+" Days)", daysCompleted),
            new PieChart.Data("Remaining "+"("+daysLeft+" Days)", daysLeft)
        );
        }
        
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
            
            series.getData().add(new XYChart.Data<>((isDue(goal)), daysLeft));
        }

        barChart.getData().add(series);
    }

  /**
 * Updates the LineChart so that each goal is displayed as its own line.
 * Each goal gets a separate series with its own start and end data points.
 */
private void updateLineChart() {
    // Clear any existing data from the LineChart
    lineChart.getData().clear();
    // Makes the X Axis consistent
    CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
    Axis<Number> yAxis = (Axis<Number>) lineChart.getYAxis();
    // List to hold dates as categories (so they can overlap and not connect)
    ObservableList<String> categories = FXCollections.observableArrayList();

    LocalDate maxDate = LocalDate.now();
    LocalDate minDate = LocalDate.now();

    // Loop through each goal and get the earliest date and latest date for parameters for the graph
    for (Goal goal : goalModel.getGoals()) {
        if (goal.getStartDate().isBefore(minDate)) {
            minDate = goal.getStartDate();
        }
        if (goal.getEndDate().isBefore(minDate)) {
            minDate = goal.getEndDate();
        }
        if (goal.getStartDate().isAfter(maxDate)) {
            maxDate = goal.getStartDate();
        }
        if (goal.getEndDate().isAfter(maxDate)) {
            maxDate = goal.getEndDate();
        }
    }
    
    // List to hold a separate series for each goal
    ArrayList<XYChart.Series<String, Number>> list = new ArrayList<>();
    // A line to indicate today
    XYChart.Series<String, Number> todayLine = new XYChart.Series<>();
    todayLine.getData().add(new XYChart.Data<>(LocalDate.now().toString(), 40));
    todayLine.getData().add(new XYChart.Data<>(LocalDate.now().toString(), 0));
    todayLine.setName("Today");

    LocalDate today = LocalDate.now();
    // Today also should also be checked if it should be the start or end time
    if (today.isBefore(minDate)) {
        minDate = today;
    }
    if (today.isAfter(maxDate)) {
        maxDate = today;
    }

    // Gets every date between the min and max and adds them to the categories list as they will act as their own category making the x axis even
    for (LocalDate i = minDate; !i.isAfter(maxDate); i = i.plusDays(1)) { // Potentially change the one to someothing else or make it different depending on the span of days
        categories.add(i.toString());
    }
    // Set the sorted categories on the X axis
    xAxis.setCategories(categories);
    yAxis.setLabel("Days Left");
    
    
    
    Map<String, Integer> startCount = new HashMap<>();
    Map<String, Integer> endCount = new HashMap<>();
    // Loop through each goal in the model
    for (Goal goal : goalModel.getGoals()) {
        // Create a new series for the current goal
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(isDue(goal));

        // Format dates as strings
        String startDateStr = goal.getStartDate().toString();
        String endDateStr = goal.getEndDate().toString();

        // Count how many times these dates have been seen
        int startOffsetCount = startCount.getOrDefault(startDateStr, 0);
        int endOffsetCount = endCount.getOrDefault(endDateStr, 0);

        // Calculate the total days from the start date to the end date
        long totalDays = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate());

        // Defines a small offset value so the lines dont overlap too much
        double offset = 0.5;

        // Apply a small offset to each point based on the number of overlaps
        double adjustedStartValue = totalDays + (startOffsetCount * offset);
        double adjustedEndValue = 0 + (endOffsetCount * offset);

        series.getData().add(new XYChart.Data<>(startDateStr, adjustedStartValue));
        series.getData().add(new XYChart.Data<>(endDateStr, adjustedEndValue));

        // Update the count maps for the next goal with the same date
        startCount.put(startDateStr, startOffsetCount + 1);
        endCount.put(endDateStr, endOffsetCount + 1);

    list.add(series);
    }
        list.add(todayLine);
        lineChart.getData().addAll(list);
    }
   
    /*
     * Indicates if a goal is past due or due today
     *  @param goal goal to be checked when its duedate is by comparision of today
     */
    private String isDue(Goal goal) {
        String title = null;
        LocalDate today = LocalDate.now();
        
        if (goal.getEndDate().isBefore(today)) {
            title = (goal.getTitle()+ " (Past Due!)");
        } else if (goal.getEndDate().isEqual(today)) {
            title = (goal.getTitle()+ " (Due Today!)");
        } else {
            title = (goal.getTitle());
        }
        return title;
        }

}

