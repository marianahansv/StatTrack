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
 * two of those or decides to see all of them together. It also helps to create the buttons for selection and the grid
 * panes for the month selections.
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
     * The year selector for the left grid.
     */
    private ComboBox<String> yearSelector_left;

    /**
     * The year selector for the right grid.
     */
    private ComboBox<String> yearSelector_right;

    /**
     * This is a button to generate the visualization based on the preferences set by the users.
     */
    private Button generate_visualizaton;

    /**
     * Constuctor for the UserHIstoryProgressVisuals class that makes use of the UserProgressHIstory model
     * @param historicalChartModel: The model that helps to function with this view
     */
    public UserHIstoryProgressVisuals(UserProgressHIstoryVisModel historicalChartModel){
        this.historicalChartModel = historicalChartModel;
        /* Select, backend prepare and update the month grid panes */
        setmonthGridPane();//need update function to be called

        /* Select, backend prepare and update the charts */
        setChartType(); //need update function to be called

        /* Select, backend prepare and update the descriptive statistics */
        setDescriptiveStatistics(); //need update function to be called

        /* Select, backend prepare and update the color preferences */
        setColorPreferences(); //need update function to be called

        allPreferencesarrangement();
    }

    /**
     * The method that organizes both the left and the right grid pane. Focuses on putting the elements all together
     * in one big piece.
     */
    private void setmonthGridPane(){
        /* Dealing with the month selectors first */
        leftmonth_grid_selector = new ComboBox<>();
        rightmonth_grid_selector = new ComboBox<>();
        leftmonth_grid_selector.getItems().addAll("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        rightmonth_grid_selector.getItems().addAll(leftmonth_grid_selector.getItems());

        leftmonth_grid_selector.setValue("January");
        rightmonth_grid_selector.setValue("January");

        leftmonth_grid_selector.setOnAction(e -> updateGridPane(leftmonth_grid, leftmonth_grid_selector.getValue()));
        rightmonth_grid_selector.setOnAction(e -> updateGridPane(rightmonth_grid, rightmonth_grid_selector.getValue()));

        /* Dealing with the yearly selectors now */
        yearSelector_left = new ComboBox<>();
        for (int year = 1920; year <= 2025; year++){
            yearSelector_left.getItems().add(String.valueOf(year));
        }
        yearSelector_left.setValue("2025");
        yearSelector_right = new ComboBox<>();
        yearSelector_right.getItems().addAll(yearSelector_left.getItems());
        yearSelector_right.setValue("2025");

        /* Dealing with the grid panes - both left and right at the same time */
        leftmonth_grid = grid_with_dates();
        rightmonth_grid = grid_with_dates();

    }

    /**
     * It is a helper function to support the setmonthGridPane and it adds the dates on the calendar (grid panes).
     * @return A grid pane with the dates added to it depending on the month
     */
    private GridPane grid_with_dates(){
        GridPane grid = new GridPane();
        grid.setHgap(5); // for between the elements in the grid
        grid.setVgap(5);
        updateGridPane(grid, "January");
        return grid;
    }

    /**
     * The method supports in allowing the user to select the chart that they are willing to pick. It is the
     * front-end part of the View page.
     */
    private void setChartType(){
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

    }

    /**
     * The method supports in allowing the user to select if they would like some descriptive statistics alongside it.
     * It is the front-end part of the View page.
     */
    private void setDescriptiveStatistics(){
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

    }

    /**
     * The method supports in allowing the user to select if they would like to pick a main specific color alongside it.
     * It is the front-end part of the View page.
     */
    private void setColorPreferences(){
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
    }

    /**
     * The function is creating a huge container element to store all the grid boxes as well as the three user
     * preferences into one big box. This is done to ensure good spacing between all of the elements on the page and
     * to create a visually pleasing experience for the user.
     */
    private void allPreferencesarrangement(){
        VBox bigcontainer = new VBox(30);

        /* Two grid boxes for the calendar month and dates */
        VBox left_side = new VBox(5, new Label("Select Starting Month"),leftmonth_grid_selector,
                new Label("Select Starting Year"), yearSelector_left, leftmonth_grid);
        VBox right_side = new VBox(5, new Label("Select Ending Month"),rightmonth_grid_selector,
                new Label("Select Ending Year"), yearSelector_right, rightmonth_grid);
        HBox month_container = new HBox(50);
        month_container.setAlignment(Pos.CENTER);
        month_container.getChildren().addAll(left_side, right_side);
        getChildren().addAll(month_container);

        /* Container for the chart type */
        HBox horizontalbox_CT = new HBox(30);
        horizontalbox_CT.getChildren().addAll(new Label("Graph Preferences"), checkboxPieChart, checkboxLineChart,
                checkboxScatterGraph);
        horizontalbox_CT.setAlignment(Pos.CENTER);
        getChildren().addAll(horizontalbox_CT);

        /* Container for the color preferences */
        HBox horizontalbox_CP = new HBox(30);
        horizontalbox_CP.getChildren().addAll(new Label("Color Preferences: "), redcolorPreference,
                purplecolorPreference, orangecolorPreference, bluecolorPreference);
        horizontalbox_CP.setAlignment(Pos.CENTER);
        getChildren().addAll(horizontalbox_CP);

        /* Container for descriptive statistics */
        HBox horizontalbox_DS = new HBox(30);
        horizontalbox_DS.getChildren().addAll(new Label("Include Descriptive Statistics?: "), includeDescriptiveStatistics, notincludeDescriptiveStatistics);
        horizontalbox_DS.setAlignment(Pos.CENTER);
        getChildren().addAll(horizontalbox_DS);

        /* Add all the horizontal boxes into one bigcontainer */
        bigcontainer.getChildren().addAll(month_container, horizontalbox_CT, horizontalbox_CP, horizontalbox_DS);
        bigcontainer.setAlignment(Pos.CENTER);
        getChildren().add(bigcontainer);
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

    /**
     * The method is used to generate the accurate number of dates depending on the month that we are interested in.
     * For each of the dates, it helps to add the required buttons inside the grid pane.
     * @param grid: A grid pane where the dates and the months will be stored
     * @param currentmonth: The month selected by the user
     */
    private void updateGridPane(GridPane grid, String currentmonth){
        /* Make the entire grid clear first */
        grid.getChildren().clear();

        /* Figure out how many days there will be in the grid depending on the month */
        int days = switch(currentmonth){
            case "February" -> 28;
            case "April", "November", "September", "June" -> 30;
            default -> 31;
        };

        int row = 0; // current row position
        int col = 0; // current column position

        /* Adding the dates to the month calendar */
        for (int day = 1; day <= days; day++){
            Button dateButton = new Button(String.valueOf(day));
            int finalDay = day;
            dateButton.setOnAction(e -> System.out.println("The selected is: " + currentmonth + " on " + finalDay));
            grid.add(dateButton, col, row);
            col ++;
            if (col == 7){
                col = 0;
                row ++;
            }
        }
    }

}
