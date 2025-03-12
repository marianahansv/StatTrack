package com.example.cmpt370project;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
     * created alongside the graphs.
     */
    private RadioButton includeDescriptiveStatistics;

    /**
     * Radio Button to select if the user doesn't want descriptive statistics to be
     * created alongside the graphs.
     */
    private RadioButton notincludeDescriptiveStatistics;

    /**
     * Radio Button to select to allow the user to choose the red color as main
     * for the graphs.
     */
    private RadioButton redcolorPreference;

    /**
     * Radio Button to select to allow the user to choose the purple color as main
     * for the graphs.
     */
    private RadioButton purplecolorPreference;

    /**
     * Radio Button to select to allow the user to choose the blue color as main
     * for the graphs.
     */
    private RadioButton bluecolorPreference;

    /**
     * Radio Button to select to allow the user to choose the orange color as main
     * for the graphs.
     */
    private RadioButton orangecolorPreference;

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
     * The month grid on the left-hand side of the page.
     */
    private GridPane leftmonth_grid;

    /**
     * The month grid on the right-hand side of the page.
     */
    private GridPane rightmonth_grid;

    /**
     * The selector for the month on the left grid box.
     */
    private ComboBox<String> leftmonth_grid_selector;

    /**
     * The selector for the month on the right grid box.
     */
    private ComboBox<String> rightmonth_grid_selector;

    /**
     * The year selector for the grids.
     */
    private ComboBox<String> yearSelector;

    /**
     * Constuctor for the UserHIstoryProgressVisuals class that makes use of the UserProgressHIstory model
     * @param historicalChartModel: The model that helps to function with this view
     */
    public UserHIstoryProgressVisuals(UserProgressHIstoryVisModel historicalChartModel){
        this.historicalChartModel = historicalChartModel;
        /* Select, backend prepare and update the charts */
        setChartType(); //need update function to be called
        /* Select, backend prepare and update the month grid panes */
        setmonthGridPane();
        /* Select, backend prepare and update the descriptive statistics */
        setDescriptiveStatistics();
        /* Select, backend prepare and update the color preferences */
        setColorPreferences();
    }

    private void setmonthGridPane(){
        /* Dealing with the month selectors first */
        leftmonth_grid_selector = new ComboBox<>();
        rightmonth_grid_selector = new ComboBox<>();
        leftmonth_grid_selector.getItems().addAll("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        rightmonth_grid_selector.getItems().addAll(leftmonth_grid_selector.getItems());

        leftmonth_grid_selector.setValue("January");
        rightmonth_grid_selector.setValue("January");

        leftmonth_grid_selector.setOnAction(e -> updateGridPane());
        rightmonth_grid_selector.setOnAction(e -> updateGridPane());

        /* Dealing with the yearly selectors now */
        yearSelector = new ComboBox<>();
        yearSelector.getItems().addAll("1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988",
                "1989", "1990", "1991", "1992", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000",
                "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013",
                "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025");
        yearSelector.setValue("2025");

        /* Dealing with the grid panes - both left and right at the same time */
        leftmonth_grid = grid_with_dates();
        rightmonth_grid = grid_with_dates();

        VBox left_side = new VBox(5, new Label("Select Starting Month"),leftmonth_grid_selector, leftmonth_grid);
        VBox right_side = new VBox(5, new Label("Select Ending Month"),rightmonth_grid_selector, rightmonth_grid);
    }

    private GridPane grid_with_dates(){}
    /**
     * The method supports in allowing the user to select the chart that they are willing to pick. It is the
     * front-end part of the View page.
     */
    private void setChartType(){
        Label label = new Label("Graph Preferences: ");
        checkboxPieChart= new CheckBox("Pie Chart");
        checkboxLineChart = new CheckBox("Line Chart");
        checkboxScatterGraph = new CheckBox("Scatter Chart");

        /* Setting each of the graphs to be unchecked */
        checkboxPieChart.setSelected(false);
        checkboxLineChart.setSelected(false);
        checkboxScatterGraph.setSelected(false);

        /* Setting up listeners for each of my buttons */
        checkboxPieChart.setOnAction(e -> updatePieCharts());
        checkboxLineChart.setOnAction(e -> updateLineCharts());
        checkboxScatterGraph.setOnAction(e -> updateScatterCharts());

        HBox horizontalbox = new HBox(10);
        horizontalbox.getChildren().addAll(label, checkboxPieChart, checkboxLineChart, checkboxScatterGraph);
        horizontalbox.setAlignment(Pos.CENTER);
        getChildren().addAll(horizontalbox);
    }

    /**
     * The method supports in allowing the user to select if they would like some descriptive statistics alongside it.
     * It is the front-end part of the View page.
     */
    private void setDescriptiveStatistics(){
        Label label = new Label("Descriptive Statistics Included?: ");
        includeDescriptiveStatistics = new RadioButton("Included");
        notincludeDescriptiveStatistics = new RadioButton("Not Included");

        /* Setting each of the radio buttons to be unchecked */
        /* Will ensure that only one is being selected at one time */
        ToggleGroup chooseDescriptiveStatistics = new ToggleGroup();
        chooseDescriptiveStatistics.getToggles().add(includeDescriptiveStatistics);
        chooseDescriptiveStatistics.getToggles().add(notincludeDescriptiveStatistics);

        /* Setting up listeners for the buttons */
        includeDescriptiveStatistics.setOnAction(e -> updateDescriptiveStatistics());
        notincludeDescriptiveStatistics.setOnAction(e -> updateDescriptiveStatistics());

        HBox horizontalBox_DS = new HBox(10);
        horizontalBox_DS.getChildren().addAll(label, includeDescriptiveStatistics, notincludeDescriptiveStatistics);
        horizontalBox_DS.setAlignment(Pos.CENTER);
        getChildren().addAll(horizontalBox_DS);
    }

    /**
     * The method supports in allowing the user to select if they would like to pick a main specific color alongside it.
     * It is the front-end part of the View page.
     */
    private void setColorPreferences(){
        Label colorchoice = new Label("Color Preference: ");
        redcolorPreference = new RadioButton("Red");
        purplecolorPreference = new RadioButton("Purple");
        bluecolorPreference = new RadioButton("Blue");
        orangecolorPreference = new RadioButton("Orange");

        /* Put them all into a toggle group so only one can be selected at once */
        ToggleGroup chooseColorPreferences = new ToggleGroup();
        chooseColorPreferences.getToggles().add(redcolorPreference);
        chooseColorPreferences.getToggles().add(purplecolorPreference);
        chooseColorPreferences.getToggles().add(bluecolorPreference);
        chooseColorPreferences.getToggles().add(orangecolorPreference);

        redcolorPreference.setOnAction(e -> updateColorPreferences());
        bluecolorPreference.setOnAction(e -> updateColorPreferences());
        orangecolorPreference.setOnAction(e -> updateColorPreferences());
        purplecolorPreference.setOnAction(e -> updateColorPreferences());

        HBox horizontalBox_CP = new HBox(30);
        horizontalBox_CP.getChildren().addAll(colorchoice, redcolorPreference, purplecolorPreference, orangecolorPreference, bluecolorPreference);
        horizontalBox_CP.setAlignment(Pos.CENTER);
        getChildren().addAll(horizontalBox_CP);
    }
    private void drawPieCharts(){}

    private void drawLineCharts(){}

    private void drawScatterCharts(){}

    private void generateDescriptiveStatistics(){}

    void updatePieCharts(){}

    void updateLineCharts(){}

    void updateScatterCharts(){}

    private void updateDescriptiveStatistics(){}

    private void updateColorPreferences(){}

    private void updateGridPane(){}

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

}
