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
     * This is a button to reset the visualization based on the preferences set by the users.
     */
    private Button reset_visualization;

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

        /* Select, backend prepare and update the "Generate" and "Reset" button */
        set_visualization();

        /* Puts all elements neatly in one container */
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
        leftmonth_grid_selector.setStyle("-fx-background-color: #ebebeb;" +
                                         "-fx-alignment: center;" +
                                         "-fx-background-radius: 10px;" +
                                         "-fx-padding: 1px;");
        rightmonth_grid_selector.setStyle("-fx-background-color: #ebebeb;" +
                                          "-fx-alignment: center;" +
                                          "-fx-background-radius: 10px;" +
                                          "-fx-padding: 1px;");

        leftmonth_grid_selector.setOnAction(e -> updateGridPane(leftmonth_grid, leftmonth_grid_selector.getValue()));
        rightmonth_grid_selector.setOnAction(e -> updateGridPane(rightmonth_grid, rightmonth_grid_selector.getValue()));

        /* Dealing with the yearly selectors now */
        yearSelector_left = new ComboBox<>();
        yearSelector_right = new ComboBox<>();

        for (int year = 1920; year <= 2025; year++){
            yearSelector_left.getItems().add(String.valueOf(year));
        }
        yearSelector_left.setValue("2025");
        yearSelector_left.setStyle("-fx-background-color: #ebebeb;" +
                                   "-fx-alignment: center;" +
                                   "-fx-background-radius: 10px;" +
                                   "-fx-padding: 1px;");
        yearSelector_right.getItems().addAll(yearSelector_left.getItems());
        yearSelector_right.setValue("2025");
        yearSelector_right.setStyle("-fx-background-color: #ebebeb;" +
                                    "-fx-alignment: center;" +
                                    "-fx-background-radius: 10px;" +
                                    "-fx-padding: 1px;");

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
     * This method is used to generate and reset the visualization preferences by the user.
     */
    private void set_visualization(){
        generate_visualizaton = new Button("GENERATE");
        generate_visualizaton.setOnAction(e -> updateGenerateView());
        generate_visualizaton.setStyle("-fx-background-color: lightblue;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-background-radius: 10px");
        generate_visualizaton.setOnMouseEntered(e-> generate_visualizaton.setStyle("-fx-background-color: white;" +
                                                            "-fx-font-weight: bold;" + "-fx-background-radius: 10px"));
        generate_visualizaton.setOnMouseExited(e-> generate_visualizaton.setStyle("-fx-background-color: lightblue;" +
                                                            "-fx-font-weight: bold;" + "-fx-background-radius: 10px"));
        reset_visualization = new Button("RESET");
        reset_visualization.setOnAction(e -> updateResetView());
        reset_visualization.setStyle("-fx-background-color: lightblue;" +
                                     "-fx-font-weight: bold;" +
                                     "-fx-background-radius: 10px");
        reset_visualization.setOnMouseEntered(e-> reset_visualization.setStyle("-fx-background-color: white;" +
                                                            "-fx-font-weight: bold;" + "-fx-background-radius: 10px"));
        reset_visualization.setOnMouseExited(e-> reset_visualization.setStyle("-fx-background-color: lightblue;" +
                                                            "-fx-font-weight: bold;" + "-fx-background-radius: 10px"));

    }

    /**
     * The function is creating a huge container element to store all the grid boxes as well as the three user
     * preferences into one big box. This is done to ensure good spacing between all of the elements on the page and
     * to create a visually pleasing experience for the user.
     */
    private void allPreferencesarrangement(){
        VBox bigcontainer = new VBox(30);

        /* Two grid boxes for the calendar month and dates */
        /* Create distinct separation between the buttons to add styling on each respectively */
        HBox left_month_selector = new HBox(10);
        left_month_selector.getChildren().addAll(leftmonth_grid_selector);
        left_month_selector.setAlignment(Pos.CENTER);
        HBox right_month_selector = new HBox(10);
        right_month_selector.getChildren().addAll(rightmonth_grid_selector);
        right_month_selector.setAlignment(Pos.CENTER);
        HBox left_year_selector = new HBox(10);
        left_year_selector.getChildren().addAll(yearSelector_left);
        left_year_selector.setAlignment(Pos.CENTER);
        HBox right_year_selector = new HBox(10);
        right_year_selector.getChildren().addAll(yearSelector_right);
        right_year_selector.setAlignment(Pos.CENTER);

        HBox left_label = new HBox(10);
        left_label.getChildren().addAll(new Label("Select Starting Month and Year: "));
        HBox right_label = new HBox(10);
        right_label.getChildren().addAll(new Label("Select Ending Month and Year: "));
        VBox left_side = new VBox(4, left_month_selector, left_year_selector, leftmonth_grid);
        VBox right_side = new VBox(4, right_month_selector, right_year_selector, rightmonth_grid);
        left_side.setStyle("-fx-background-color: lightblue;" + "-fx-padding: 10px;" + "-fx-background-radius: 10px;");
        right_side.setStyle("-fx-background-color: lightblue;" + "-fx-padding: 10px;" + "-fx-background-radius: 10px;");
        VBox left_container = new VBox(6, left_label, left_side);
        VBox right_container = new VBox(6, right_label, right_side);

        HBox month_container = new HBox(50);
        month_container.setAlignment(Pos.CENTER);
        month_container.getChildren().addAll(left_container, right_container);
        getChildren().addAll(month_container);

        /* Container for the chart type */
        HBox horizontalbox_CT = new HBox(30);
        HBox horizontalbox_CT_label = new HBox(10);
        horizontalbox_CT_label.getChildren().addAll(new Label("Graph Preferences: "));
        horizontalbox_CT_label.setAlignment(Pos.BASELINE_LEFT);
        horizontalbox_CT_label.setStyle("-fx-font-weight:bold;" + "-fx-background-color: lightblue;" +
                                                                    "-fx-background-radius: 5px;" + "-fx-padding: 2px;");
        horizontalbox_CT.getChildren().addAll(horizontalbox_CT_label, checkboxPieChart, checkboxLineChart,
                checkboxScatterGraph);
        horizontalbox_CT.setAlignment(Pos.BASELINE_CENTER);
        getChildren().addAll(horizontalbox_CT);

        /* Container for the color preferences */
        HBox horizontalbox_CP = new HBox(30);
        HBox horizontalbox_CP_label = new HBox(10);
        horizontalbox_CP_label.getChildren().addAll(new Label("Color Preferences: "));
        horizontalbox_CP_label.setAlignment(Pos.BASELINE_LEFT);
        horizontalbox_CP_label.setStyle("-fx-font-weight:bold;" + "-fx-background-color: lightblue;" +
                                                                    "-fx-background-radius: 5px;" + "-fx-padding: 2px;");
        horizontalbox_CP.getChildren().addAll(horizontalbox_CP_label, redcolorPreference,
                purplecolorPreference, orangecolorPreference, bluecolorPreference);
        horizontalbox_CP.setAlignment(Pos.BASELINE_CENTER);
        getChildren().addAll(horizontalbox_CP);

        /* Container for descriptive statistics */
        HBox horizontalbox_DS = new HBox(30);
        HBox horizontalbox_DS_label = new HBox(10);
        horizontalbox_DS_label.getChildren().addAll(new Label("Include Descriptive Statistics: "));
        horizontalbox_DS_label.setAlignment(Pos.BASELINE_LEFT);
        horizontalbox_DS_label.setStyle("-fx-font-weight:bold;" + "-fx-background-color: lightblue;" +
                                                                    "-fx-background-radius: 5px;" + "-fx-padding: 2px;");
        horizontalbox_DS.getChildren().addAll(horizontalbox_DS_label, includeDescriptiveStatistics,
                                              notincludeDescriptiveStatistics);
        horizontalbox_DS.setAlignment(Pos.BASELINE_CENTER);
        getChildren().addAll(horizontalbox_DS);

        /* Container for the generate button */
        HBox horizontalbox_G = new HBox(30);
        horizontalbox_G.getChildren().addAll(generate_visualizaton, reset_visualization);
        horizontalbox_G.setAlignment(Pos.CENTER);
        getChildren().addAll(horizontalbox_G);


        /* Add all the horizontal boxes into one bigcontainer */
        bigcontainer.getChildren().addAll(month_container, horizontalbox_CT, horizontalbox_CP,
                                          horizontalbox_DS, horizontalbox_G);
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

    private void updateGenerateView(){

    }

    private void updateResetView(){
        yearSelector_left.setValue("2025");
        yearSelector_right.setValue("2025");
        leftmonth_grid_selector.setValue("January");
        rightmonth_grid_selector.setValue("January");
        checkboxPieChart.setSelected(false);
        checkboxLineChart.setSelected(false);
        checkboxScatterGraph.setSelected(false);
        redcolorPreference.setSelected(false);
        purplecolorPreference.setSelected(false);
        orangecolorPreference.setSelected(false);
        bluecolorPreference.setSelected(false);
        includeDescriptiveStatistics.setSelected(false);
        notincludeDescriptiveStatistics.setSelected(false);
    }

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
            /* Default of the dateButton */
            dateButton.setStyle("-fx-background-color: #ebebeb;" + "-fx-background-radius: 10px;" +
                    "-fx-text-fill: #4A4A4A;" + "-fx-font-weight: bold;");

            /* Working with the colors of the button and how they react with each other */
            dateButton.setOnMouseEntered(e -> {if(!HasButtonPressed(dateButton)){
                                                            dateButton.setStyle("-fx-background-color: white;" +
                                                            "-fx-background-radius: 10px;" +
                                                            "-fx-text-fill: #4A4A4A;" + "-fx-font-weight: bold;");
                                                }
                                            });
            dateButton.setOnMouseExited(e -> {if(!HasButtonPressed(dateButton)){
                                                            dateButton.setStyle("-fx-background-color: #ebebeb;" +
                                                            "-fx-background-radius: 10px;" +
                                                            "-fx-text-fill: #4A4A4A;" + "-fx-font-weight: bold;");
                                                }
                                            });
            dateButton.setOnMousePressed(e -> {dateButton.setStyle("-fx-background-color: #222243;" +
                                        "-fx-background-radius: 10px;" + "-fx-text-fill: white;" +
                                        "-fx-font-weight: bold;"); setButtonOutlook(dateButton, true);});

            dateButton.setOnMouseReleased(e -> {});

            int finalDay = day;
            /* Everytime the user clicks, it will print out the following on the terminal. */
            dateButton.setOnAction(e -> System.out.println("The selected is: " + currentmonth + " on " + finalDay));
            grid.add(dateButton, col, row);
            col ++;
            if (col == 7){
                col = 0;
                row ++;
            }
        }
    }

    private boolean HasButtonPressed(Button button){
        return button.getStyle().contains("#222243");
    }

    private void setButtonOutlook(Button button, boolean pressed){
        if (pressed){
            button.setStyle("-fx-background-color: #222243;" +
                    "-fx-background-radius: 10px;" + "-fx-text-fill: white;" + "-fx-font-weight: bold;");
        }
        else{
            button.setStyle("-fx-background-color: #ebebeb;" + "-fx-background-radius: 10px;" +
                    "-fx-text-fill: #4A4A4A;" + "-fx-font-weight: bold;");
        }
    }
}
