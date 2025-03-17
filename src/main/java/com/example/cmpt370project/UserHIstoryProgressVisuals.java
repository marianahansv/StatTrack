package com.example.cmpt370project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
     * The UserProgressHistoryController is the controller that will store some functions which will be used by the
     * view and the model. It acts as a communication linkage between the model and the view respectively.
     */
    private UserProgressHistoryController historicalChartController;

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
    private LineChart<String, Number> lineChart;

    /**
     * Scatter chart for the visualization purposes.
     */
    private ScatterChart<String, Integer> scatterChart;

    /**
     * A VBox to store the descriptive statistics of the user's goals.
     */
    private VBox descriptiveStatisticsTable;

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
    ComboBox<String> leftmonth_grid_selector;

    /**
     * The selector for the month on the right grid box.
     */
    ComboBox<String> rightmonth_grid_selector;

    /**
     * The year selector for the left grid.
     */
    ComboBox<String> yearSelector_left;

    /**
     * The year selector for the right grid.
     */
    ComboBox<String> yearSelector_right;

    /**
     * This is a button to generate the visualization based on the preferences set by the users.
     */
    private Button generate_visualizaton;


    /**
     * This is a button to reset the visualization based on the preferences set by the users.
     */
    private Button reset_visualization;

    /**
     * Global variable currentButton to store to keep track of each date button the user clicks. This allows
     * only one date in the calendar to be selected at a time.
     */
    AtomicReference<Button> currentButton = new AtomicReference<>(null); // had to make this atomic

    /**
     * Global variable to store the date button for the starting month.
     */
    AtomicReference<Button> leftgrid_dates = new AtomicReference<>();

    /**
     * Global variable to store the date button for the ending month.
     */
    AtomicReference<Button> rightgrid_dates = new AtomicReference<>();

    /**
     * Constuctor for the UserHIstoryProgressVisuals class that makes use of the UserProgressHIstory model
     * @param historicalChartModel: The model that helps to function with this view
     */
    public UserHIstoryProgressVisuals(UserProgressHIstoryVisModel historicalChartModel, UserProgressHistoryController historicalChartController) {
        this.historicalChartModel = historicalChartModel;
        this.historicalChartController = historicalChartController;
        this.historicalChartController.setupViewClass(this); /* Required to set up the view classes correctly */

        leftmonth_grid = new GridPane();
        rightmonth_grid = new GridPane();
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

        leftmonth_grid_selector.setOnAction(e -> updateGridPane(leftmonth_grid, leftmonth_grid_selector.getValue(),true));
        rightmonth_grid_selector.setOnAction(e -> updateGridPane(rightmonth_grid, rightmonth_grid_selector.getValue(),false));

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
        leftmonth_grid = grid_with_dates(true);
        rightmonth_grid = grid_with_dates(false);

    }

    /**
     * It is a helper function to support the setmonthGridPane and it adds the dates on the calendar (grid panes).
     * @return A grid pane with the dates added to it depending on the month
     */
    private GridPane grid_with_dates(boolean is_left_grid){
        GridPane grid;
        if (is_left_grid){
            grid = leftmonth_grid;
        }
        else{
            grid = rightmonth_grid;
        }
        grid.setHgap(5); // for between the elements in the grid
        grid.setVgap(5);
        updateGridPane(grid, "January", is_left_grid);
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
     * preferences into one big box. This is done to ensure good spacing between all the elements on the page and
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

    /**
     * The pie chart created will display the information about the goals in categories of difficulty which
     * ranges from Small, Medium and Large.
     * @return: A VBox containing the Pie Chart inside it.
     */
    private VBox drawPieCharts() {
        VBox pieChart_display = new VBox(10);

        /* Storing all useful information in the respective variables */
        pieChart = new PieChart();
        pieChart.setTitle("Pie Chart by Categories");
        pieChart.setPrefWidth(400);
        pieChart.setPrefHeight(400);

        /* Deriving all calculations used for the charts */
        LocalDate startFormatDate = dateFormatting(historicalChartController.getStartYear(),
                                    historicalChartController.getStartMonth(), historicalChartController.getStartDate());
        LocalDate endFormatDate = dateFormatting(historicalChartController.getEndYear(),
                                        historicalChartController.getEndMonth(), historicalChartController.getEndDate());
        List<Goal> easyGoals = filteredGoalList(historicalChartModel.easyGoals(), startFormatDate, endFormatDate);
        List<Goal> mediumGoals = filteredGoalList(historicalChartModel.mediumGoals(), startFormatDate, endFormatDate);
        List<Goal> hardGoals = filteredGoalList(historicalChartModel.hardGoals(), startFormatDate, endFormatDate);
        int easyCount = (easyGoals != null) ? easyGoals.size() : 0;
        int mediumCount = (mediumGoals != null) ? mediumGoals.size() : 0;
        int hardCount = (hardGoals != null) ? hardGoals.size() : 0;

        System.out.println("Easy Goals: " + easyGoals);
        System.out.println("Medium Goals: " + mediumGoals);
        System.out.println("Hard Goals: " + hardGoals);

        /* Use the Pie Chart data to create the pie chart required */
        ObservableList<PieChart.Data> data_PC  = FXCollections.observableArrayList(
                new PieChart.Data("Easy", easyCount),
                new PieChart.Data("Medium", mediumCount),
                new PieChart.Data("Hard", hardCount));
        pieChart.setData(data_PC);

        /* Adding all the elements into the HBox*/
        pieChart_display.getChildren().addAll(pieChart, curatedLegendPieChart());
        pieChart_display.setAlignment(Pos.CENTER);
        getChildren().add(pieChart_display);

        pieChart.setLegendVisible(false); // Will create our own legend from next function
        colorPieChart(pieChart); // To color our pie chart with the accurate shade color

        return pieChart_display;
    }

    private void colorPieChart(PieChart pieChart) {
        String colorChosen = colorChosen();
        String[] shadesofColor = colorpreferenceShades(colorChosen);
        int current_index = 0;
        /* Working on getting the right colors for the slices on the pie chart */
        for (PieChart.Data data: pieChart.getData()){
            String colorObserved = shadesofColor[current_index % shadesofColor.length];
            data.getNode().setStyle("-fx-pie-color: " + colorObserved + ";");
            current_index++;
        }
    }

    private HBox curatedLegendPieChart(){
        String colorChosen = colorChosen();
        String[] shadesofColor = colorpreferenceShades(colorChosen);
        /* Working on the legend box of pie chart to make sure that the color alignment matches well */
        /* Working with the default one is tricky - we will hide the default one and only show our created */
        /* legend box instead to the user. */
        HBox curatedLegend = new HBox(10);
        int current_index_again = 0;
        for (PieChart.Data data: pieChart.getData()){
            Rectangle colorbox = new Rectangle(20, 20); // Square to store the color
            colorbox.setFill(Color.web(shadesofColor[current_index_again % shadesofColor.length]));
            Text label_info = new Text(data.getName());
            curatedLegend.getChildren().addAll(colorbox, label_info);
            current_index_again++;
        }
        curatedLegend.setAlignment(Pos.CENTER);
        return curatedLegend;
    }

    private VBox drawLineCharts() {
        VBox lineChart_display = new VBox(10);

        /* Adding all the basics elements of the line chart together */
        CategoryAxis x_axis = new CategoryAxis();
        NumberAxis y_axis = new NumberAxis();
        x_axis.setLabel("Date");
        y_axis.setLabel("Goals Completed Per Day");
        lineChart = new LineChart<>(x_axis, y_axis);
        lineChart.setTitle("Goals Completed Per Day");
        XYChart.Series<String, Number> easyGoals_LC = new XYChart.Series<>();
        easyGoals_LC.setName("Easy");
        XYChart.Series<String, Number> mediumGoals_LC = new XYChart.Series<>();
        mediumGoals_LC.setName("Medium");
        XYChart.Series<String, Number> hardGoals_LC = new XYChart.Series<>();
        hardGoals_LC.setName("Hard");

        easyGoals_LC.getData().clear();
        mediumGoals_LC.getData().clear();
        hardGoals_LC.getData().clear();

        /* Deriving all the required calculations to build our line chart accurately */
        LocalDate startFormatDate_LC = dateFormatting(historicalChartController.getStartYear(),
                historicalChartController.getStartMonth(), historicalChartController.getStartDate());
        LocalDate endFormatDate_LC = dateFormatting(historicalChartController.getEndYear(),
                historicalChartController.getEndMonth(), historicalChartController.getEndDate());
        List<Goal> filt_easyGoals_LC= filteredGoalList(historicalChartModel.easyGoals(), startFormatDate_LC, endFormatDate_LC);
        List<Goal> filt_mediumGoals_LC = filteredGoalList(historicalChartModel.mediumGoals(), startFormatDate_LC, endFormatDate_LC);
        List<Goal> filt_hardGoals_LC = filteredGoalList(historicalChartModel.hardGoals(), startFormatDate_LC, endFormatDate_LC);

        /* Counting each of the goals per day based on the creation dates */
        Map<LocalDate, Integer> easyGoalsCount = new TreeMap<>();
        Map<LocalDate, Integer> mediumGoalsCount = new TreeMap<>();
        Map<LocalDate, Integer> hardGoalsCount = new TreeMap<>();

        for (Goal easyGoals: filt_easyGoals_LC){
            easyGoalsCount.put(easyGoals.getStartDate(), easyGoalsCount.getOrDefault(easyGoals.getStartDate(), 0) + 1);
        }
        for (Goal mediumGoals: filt_mediumGoals_LC){
            mediumGoalsCount.put(mediumGoals.getStartDate(), mediumGoalsCount.getOrDefault(mediumGoals.getStartDate(), 0) + 1);
        }
        for (Goal hardGoals: filt_hardGoals_LC){
            hardGoalsCount.put(hardGoals.getStartDate(), hardGoalsCount.getOrDefault(hardGoals.getStartDate(), 0) + 1);
        }

        /* Adding the data into our chart */
        for (Map.Entry<LocalDate, Integer> entry: easyGoalsCount.entrySet()){
            easyGoals_LC.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }
        for (Map.Entry<LocalDate, Integer> entry: mediumGoalsCount.entrySet()){
            mediumGoals_LC.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }
        for (Map.Entry<LocalDate, Integer> entry: hardGoalsCount.entrySet()){
            hardGoals_LC.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        lineChart.getData().addAll(easyGoals_LC, mediumGoals_LC, hardGoals_LC);

        /* Adding all the elements into the VBox */
        lineChart_display.getChildren().addAll(lineChart, curatedLegendLineChart());
        lineChart_display.setAlignment(Pos.CENTER);
        getChildren().add(lineChart_display);

        colorLineChart(lineChart);
        lineChart.setLegendVisible(false);
        return lineChart_display;
    }

    private void colorLineChart(LineChart lineChart) {
        String colorChosen_LC = colorChosen();
        String[] shadesofColor_LC = colorpreferenceShades(colorChosen_LC);
        int current_index_LC = 0;
        for (Object series: lineChart.getData()){
            XYChart.Series<String, Number> series_LC = (XYChart.Series<String, Number>) series;
            for (XYChart.Data<String, Number> data: series_LC.getData()){
                String colorObserved = shadesofColor_LC[current_index_LC % shadesofColor_LC.length];
                Node node = data.getNode();
                node.setStyle("-fx-background-color: " + colorObserved + ";");
                current_index_LC++;
            }
        }
    }

    private HBox curatedLegendLineChart(){
        String colorChosen_LC = colorChosen();
        String[] shadesofColor_LC = colorpreferenceShades(colorChosen_LC);
        /* Working on the legend box of pie chart to make sure that the color alignment matches well */
        /* Working with the default one is tricky - we will hide the default one and only show our created */
        /* legend box instead to the user. */
        HBox curatedLegend_LC = new HBox(10);
        int current_index_again_LC = 0;
        for (Object series: lineChart.getData()){
            XYChart.Series<String, Number> series_LC = (XYChart.Series<String, Number>) series;
            Rectangle colorbox_LC = new Rectangle(20, 20); // Square to store the color
            colorbox_LC.setFill(Color.web(shadesofColor_LC[current_index_again_LC % shadesofColor_LC.length]));
            Text label_info = new Text(series_LC.getName());
            curatedLegend_LC.getChildren().addAll(colorbox_LC, label_info);
            current_index_again_LC ++;
        }
        curatedLegend_LC.setAlignment(Pos.CENTER);
        return curatedLegend_LC;
    }

    private VBox drawScatterCharts(){
        VBox scatterChart_display = new VBox(10);
        return scatterChart_display;
    }

    private VBox generateDescriptiveStatistics(){
        descriptiveStatisticsTable = new VBox(10);


        /* Label for each respective line to ensure that the spacing and all the elements are well-placed */
        Label introductionL = new Label("These are your achievements so far: ");
        Label startedGoalsL = new Label("Number of goals created: ");
        Label easyGoalsL= new Label("Percentage of easy goals: ");
        Label mediumGoalsL = new Label("Percentage of medium goals: ");
        Label hardGoalL = new Label("Percentage of hard goals: ");
        Label generalGoalL = new Label("Percentage of general goals: ");
        Label fitnessGoalL = new Label("Percentage of fitness goals: ");
        Label personalGoalL = new Label("Percentage of personal goals: ");
        Label meanDailyGoalL = new Label("Mean number of goals per day: ");

        /* Working on the styling for the Labels on the left-hand side */
        colorDescriptiveStatisticsL(introductionL);
        colorDescriptiveStatisticsL(startedGoalsL);
        colorDescriptiveStatisticsL(easyGoalsL);
        colorDescriptiveStatisticsL(mediumGoalsL);
        colorDescriptiveStatisticsL(hardGoalL);
        colorDescriptiveStatisticsL(generalGoalL);
        colorDescriptiveStatisticsL(fitnessGoalL);
        colorDescriptiveStatisticsL(personalGoalL);
        colorDescriptiveStatisticsL(meanDailyGoalL);

        /* Creating the HBox and the required responses to the users */
        HBox introductionHB = new HBox(10);
        HBox startedGoalsHB = new HBox(10);
        HBox easyGoalsHB = new HBox(10);
        HBox mediumGoalsHB = new HBox(10);
        HBox hardGoalsHB = new HBox(10);
        HBox generalGoalsHB = new HBox(10);
        HBox fitnessGoalsHB = new HBox(10);
        HBox personalGoalsHB = new HBox(10);
        HBox meanDailyGoalsHB = new HBox(10);

        /* Adding the labels and the calculations to my HBox respectively */
        introductionHB.getChildren().addAll(introductionL);
        startedGoalsHB.getChildren().addAll(startedGoalsL);
        easyGoalsHB.getChildren().addAll(easyGoalsL);
        mediumGoalsHB.getChildren().addAll(mediumGoalsL);
        hardGoalsHB.getChildren().addAll(hardGoalL);
        generalGoalsHB.getChildren().addAll(generalGoalL);
        fitnessGoalsHB.getChildren().addAll(fitnessGoalL);
        personalGoalsHB.getChildren().addAll(personalGoalL);
        meanDailyGoalsHB.getChildren().addAll(meanDailyGoalL);

        HBox[] all_HB = new HBox[]{introductionHB, startedGoalsHB, easyGoalsHB,mediumGoalsHB, hardGoalsHB, generalGoalsHB,
                                    fitnessGoalsHB, personalGoalsHB, meanDailyGoalsHB};
        for (HBox oneHB: all_HB){
            oneHB.setStyle("-fx-alignment: center;");
        }
        /* Putting all the elements together into the main descriptive statistics table */
        descriptiveStatisticsTable.getChildren().addAll(introductionHB, startedGoalsHB, easyGoalsHB, mediumGoalsHB,
                                                        hardGoalsHB, generalGoalsHB, fitnessGoalsHB, personalGoalsHB,
                                                        meanDailyGoalsHB);

        descriptiveStatisticsTable.setStyle("-fx-padding: 10px;");
        descriptiveStatisticsTable.setPadding(new Insets(10));
        descriptiveStatisticsTable.setAlignment(Pos.CENTER); // TO ensure that the labels are well centered
        colorDescriptiveStatistics(descriptiveStatisticsTable);
        getChildren().add(descriptiveStatisticsTable);
        return descriptiveStatisticsTable;
    }

    private void colorDescriptiveStatistics(VBox descriptiveStatisticsTable){
        String colorChosen_DS = colorChosen();
        String[] shadesofColorDS = colorpreferenceShades(colorChosen_DS);
        descriptiveStatisticsTable.setStyle("-fx-background-color: " + shadesofColorDS[0] + ";" + "-fx-background-radius: 10px;");
    }

    private void colorDescriptiveStatisticsL(Label descriptiveStatisticsL){
        String colorChosen_DS_HB = colorChosen();
        String[] shadesofColorDS_HB = colorpreferenceShades(colorChosen_DS_HB);
        descriptiveStatisticsL.setTextFill(Color.WHITE);
        descriptiveStatisticsL.setFont(Font.font("Sans-serif"));
        descriptiveStatisticsL.setPrefWidth(Control.USE_COMPUTED_SIZE); // the HBox will only surrond the label now
        descriptiveStatisticsL.setStyle("-fx-background-color: " + shadesofColorDS_HB[1] + ";" + "-fx-padding: 5px;" +
                                         "-fx-text-fill: white;" + "-fx-font-size: 14px;" + "-fx-background-radius: 5px;" +
                                         "-fx-font-weight: bold;");
        descriptiveStatisticsL.setAlignment(Pos.CENTER);

    }

    void updatePieCharts(){}

    void updateLineCharts(){}

    void updateScatterCharts(){}

    private void updateDescriptiveStatistics(){}

    private void updateColorPreferences(){}

    private String colorChosen(){
        if (redcolorPreference.isSelected()) return "Red";
        if (purplecolorPreference.isSelected()) return "Purple";
        if (orangecolorPreference.isSelected()) return "Orange";
        else return "Blue";
    }

    private String[] colorpreferenceShades(String colorChosen){
        if (colorChosen.equals("Red")){
            return new String[]{"#FFB3B3","#a32821", "#FF7F7F", "#FF9999","#FFCCCC", "#FFA0A0", "#F4C1C1", "#FDAAAA",
                                "#EE6969", "#F97C7C"};
        }
        else if (colorChosen.equals("Purple")){
            return new String[]{"#AA98A9", "#9F2B68", "#800020", "#702963", "#483248", "#CBC3E3", "#915F6D", "#770737",
                                "#673147", "#A95C68", "#800080"};
        }
        else if (colorChosen.equals("Orange")){
            return new String[]{"#FDC883", "#b0622a", "#FAD9C4", "#FFD68A", "#FFB52E", "#FF6C8B", "#FFC55C", "#FF9138",
                                "#FF681F", "#FF681F"};
        }
        // Otherwise, it will be Blue
        return new String[]{"#A7C7E7", "#404073", "#B6D0E2", "#96DED1", "#87CEEB", "#89CFF0", "#ADD8E6", "#9ACEEB",
                            "#B3CEE5", "#6699CC"};
    }

    private LocalDate dateFormatting(String year, String month, String day) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (Integer.parseInt(day) > 0 && Integer.parseInt(day) <= 9) {
            return LocalDate.parse(year + "-" + historicalChartModel.numericalMonth(month) + "-" +
                                                                        historicalChartModel.numericalDay(day), format);
        }
        return LocalDate.parse(year + "-" + historicalChartModel.numericalMonth(month) + "-" + day, format);
    }

    /**
     * The method is used to filter out all the goals based on the starting and ending dates that the user picks. It can
     * be used by all the graphs - Pie Chart, Line Graphs and Scatter Charts.
     * @param goals: The list of goals in the user's computer
     * @param startDate: The starting date of the goals we want to assess
     * @param endDate: The ending date of the goals we want to assess
     * @return: A list of goals that belong to a specific timeframe given the start date and the end date by the user.
     */
    private List<Goal> filteredGoalList(List<Goal> goals, LocalDate startDate, LocalDate endDate){
        return goals.stream().filter(goal -> !goal.getStartDate().isBefore(startDate) &&
                                                        !goal.getEndDate().isAfter(endDate)).collect(Collectors.toList());
    }


    /**
     * This function focuses on the arrangement and the display of the graphs. If a specific option of the graphs is
     * selected by the user, only those graphs will be displayed by the user. ALl the other graphs will not be displayed
     * and will be marked as a No-Show.
     */
    private void resultsPageView(){
        /* Adds all the charts on the top of the page - Pie Chart, Line Chart and Scatter Graph */
        HBox allCharts = new HBox(20);
        VBox noLineChart = new VBox(new Label("No Line Chart was selected by the user! "));
        noLineChart.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 16px;" + "-fx-background-color: #a3a2a2;" +
                             "-fx-padding: 10px;" + "-fx-background-radius: 5px;" + "-fx-wrap-text: true;");
        noLineChart.setAlignment(Pos.CENTER);
        VBox noScatterChart = new VBox(new Label("No Scatter Chart was selected by the user! "));
        noScatterChart.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 16px;" + "-fx-background-color: #a3a2a2;" +
                                "-fx-padding: 10px;" + "-fx-background-radius: 5px;" + "-fx-wrap-text: true;");
        noScatterChart.setAlignment(Pos.CENTER);
        VBox noPieChart = new VBox(new Label("No Pie Chart was selected by the user! "));
        noPieChart.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 16px;" + "-fx-background-color: #a3a2a2;" +
                            "-fx-padding: 10px;" + "-fx-background-radius: 5px;" + "-fx-wrap-text: true;" );
        noPieChart.setAlignment(Pos.CENTER);
        HBox noDescriptiveStatistics = new HBox(new Label("No Descriptive Statistics was selected by the user! "));
        noDescriptiveStatistics.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 16px;" + "-fx-background-color: #a3a2a2;" +
                                         "-fx-padding: 10px;" + "-fx-background-radius: 5px;" + "-fx-wrap-text: true;");
        noDescriptiveStatistics.setAlignment(Pos.CENTER);

        /* Assessing cases to figure out what can be displayed to the user based on the parameters selected */
        if (checkboxPieChart.isSelected() && !checkboxLineChart.isSelected() && !checkboxScatterGraph.isSelected()){
            allCharts.getChildren().addAll(drawPieCharts(), noLineChart, noScatterChart);
        }
        else if (checkboxLineChart.isSelected() && !checkboxScatterGraph.isSelected() && !checkboxPieChart.isSelected()){
            allCharts.getChildren().addAll(noPieChart, drawLineCharts(), noScatterChart);
        }
        else if (checkboxScatterGraph.isSelected() && !checkboxPieChart.isSelected() && !checkboxLineChart.isSelected()){
            allCharts.getChildren().addAll(noPieChart, noLineChart, drawScatterCharts());
        }
        else if (checkboxPieChart.isSelected() && checkboxLineChart.isSelected() && !checkboxScatterGraph.isSelected()){
            allCharts.getChildren().addAll(drawPieCharts(), drawLineCharts(), noScatterChart);
        }
        else if (checkboxPieChart.isSelected() && checkboxScatterGraph.isSelected() && !checkboxLineChart.isSelected()){
            allCharts.getChildren().addAll(drawPieCharts(), noLineChart, drawScatterCharts());
        }
        else if (checkboxLineChart.isSelected() && checkboxScatterGraph.isSelected() && !checkboxPieChart.isSelected()){
            allCharts.getChildren().addAll(noPieChart, drawLineCharts(), drawScatterCharts());
        }
        else if (checkboxLineChart.isSelected() && checkboxScatterGraph.isSelected() && checkboxPieChart.isSelected()){
            allCharts.getChildren().addAll(drawPieCharts(), drawLineCharts(), drawScatterCharts());
        }
        allCharts.setAlignment(Pos.TOP_CENTER);
        getChildren().add(allCharts);

        /* The VBox containing the wholeDisplay will have everything we need which includes the three graphs
        * as well as the descriptive statistics at the bottom. */
        VBox wholeDisplay = new VBox(30);
        if (notincludeDescriptiveStatistics.isSelected()){
            wholeDisplay.getChildren().addAll(allCharts, noDescriptiveStatistics);
        }
        else {
            wholeDisplay.getChildren().addAll(allCharts, generateDescriptiveStatistics());
        }
        wholeDisplay.setAlignment(Pos.CENTER);
        getChildren().add(wholeDisplay);

        Dialog<Void> popupDialog = new Dialog<>();
        popupDialog.setTitle("Your Charts and Descriptive Statistics!");
        popupDialog.getDialogPane().setContent(wholeDisplay);
        popupDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        popupDialog.showAndWait();
    }

    private void updateGenerateView(){
        /* If user doesn't select anything, nothing can be generated as well. */
        if (isNoneSelected()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No parameters have been selected! ");
            alert.setHeaderText(null);
            alert.setContentText("Since you haven't selected any parameters yet, nothing can be generated!");
            alert.showAndWait();
            return;
        }

        LocalDate startDate = dateFormatting(yearSelector_left.getValue(), leftmonth_grid_selector.getValue(),
                                                                                        leftgrid_dates.get().getText());
        LocalDate endDate = dateFormatting(yearSelector_right.getValue(), rightmonth_grid_selector.getValue(),
                                                                                        rightgrid_dates.get().getText());
        /* If user doesn't pick the accurate startDate or endDate*/
        if ((rightgrid_dates.get() == null) || (leftgrid_dates.get() == null)){
            Alert alert_three= new Alert(Alert.AlertType.WARNING);
            alert_three.setTitle("No dates have been selected! ");
            alert_three.setHeaderText(null);
            alert_three.setContentText("You might have missed selecting either one of the dates! ");
            alert_three.showAndWait();
            return;
        }

        /* If the range of the dates chosen by the users are out of bound */
        if (endDate.isBefore(startDate) || startDate.isAfter(endDate)){
            Alert alert_two = new Alert(Alert.AlertType.WARNING);
            alert_two.setTitle("Date ranges are inaccurate! ");
            alert_two.setHeaderText(null);
            alert_two.setContentText("The date ranges you have selected are inaccurate. Please pick a start date prior " +
                                     "to the end date. ");
            alert_two.showAndWait();
            return;
        }

        /* If the user picked a timeframe that had no goals inside the timeframe */
        LocalDate beginDate = dateFormatting(historicalChartController.getStartYear(),
                                    historicalChartController.getStartMonth(), historicalChartController.getStartDate());
        LocalDate endingDate = dateFormatting(historicalChartController.getEndYear(),
                                        historicalChartController.getEndMonth(), historicalChartController.getEndDate());
        if (filteredGoalList(historicalChartModel.getGoals(), beginDate , endingDate).isEmpty()){
            Alert alert_four = new Alert(Alert.AlertType.WARNING);
            alert_four.setTitle("No treasures found for you.");
            alert_four.setHeaderText(null);
            alert_four.setContentText("No goals were set during this time period so no statistical analysis can be " +
                                      "performed.");
            alert_four.showAndWait();
            return;
        }

        /* If the user forgets to pick a graph choice, color preference or inclusion of descriptive statistics, they
        * will be asked to select either one. */
        if ((!checkboxPieChart.isSelected() && !checkboxLineChart.isSelected() && !checkboxScatterGraph.isSelected()) ||
                (!redcolorPreference.isSelected() && !purplecolorPreference.isSelected() && !orangecolorPreference.isSelected() && !bluecolorPreference.isSelected()) ||
                (!includeDescriptiveStatistics.isSelected() && !notincludeDescriptiveStatistics.isSelected())) {
            Alert alert_five = new Alert(Alert.AlertType.WARNING);
            alert_five.setTitle("Missed either of the parameters! ");
            alert_five.setHeaderText(null);
            alert_five.setContentText("Please set all of the parameters carefully to allow the generation of the graph accurately! ");
            alert_five.showAndWait();
            return;
        }
        /* Creating a visual page for all the charts and descriptive statistics */
        resultsPageView();
    }

    /**
     * Once the "Reset" button is clicked, we need to make sure that everything goes back into its default mode. This
     * includes all the default options including the checkboxes and radio options as well.
     */
    private void updateResetView(){
        /* If user selected nothing, we need to inform user that they are already in default mode! */
        if (isNoneSelected()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Already in DEFAULT mode!");
            alert.setHeaderText(null);
            alert.setContentText("You are already in DEFAULT mode! There is nothing that can be reset! ");
            alert.showAndWait();
            return;
        }
        yearSelector_left.setValue("2025");
        yearSelector_right.setValue("2025");
        setButtonOutlook(leftgrid_dates.get(), false);
        setButtonOutlook(rightgrid_dates.get(), false);
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
     * @param is_left_grid: It is to classify if the dateButtons that we are creating is for the left or the right grid
     */
    private void updateGridPane(GridPane grid, String currentmonth, boolean is_left_grid){
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
            /* By default, we assume that the dateButton is for the right grid pane. */
            Button dateButton = new Button(String.valueOf(day));

            dateButton.setStyle("-fx-background-color: #ebebeb;" + "-fx-background-radius: 10px;" +
                        "-fx-text-fill: #4A4A4A;" + "-fx-font-weight: bold;");

            /* Default of the dateButton */
            dateButton.setStyle("-fx-background-color: #ebebeb;" + "-fx-background-radius: 10px;" +
                    "-fx-text-fill: #4A4A4A;" + "-fx-font-weight: bold;");

            /* Working with the colors of the button and how they react with each other */
            dateButton.setOnMouseEntered(e -> {if((is_left_grid && leftgrid_dates.get() != dateButton) ||
                                                  (!is_left_grid && rightgrid_dates.get() != dateButton)){
                                                            dateButton.setStyle("-fx-background-color: white;" +
                                                            "-fx-background-radius: 10px;" +
                                                            "-fx-text-fill: #4A4A4A;" + "-fx-font-weight: bold;");
                                                }});
            dateButton.setOnMouseExited(e -> {if((is_left_grid && leftgrid_dates.get() != dateButton) ||
                                                 (!is_left_grid && rightgrid_dates.get() != dateButton)){
                                                            dateButton.setStyle("-fx-background-color: #ebebeb;" +
                                                            "-fx-background-radius: 10px;" +
                                                            "-fx-text-fill: #4A4A4A;" + "-fx-font-weight: bold;");
                                                }});
            dateButton.setOnMousePressed(e -> {if (is_left_grid) {
                                                    changeChosenButton(leftgrid_dates, dateButton);
                                               } else {
                                                    changeChosenButton(rightgrid_dates, dateButton);
                                               }});
            dateButton.setOnMouseReleased(e -> {});

            int finalDay = day;
            /* Everytime the user clicks, it will print out the following on the terminal. */

            if (is_left_grid){
                dateButton.setOnAction(e -> System.out.println("The selected is: " + currentmonth + " on " + finalDay));
                leftmonth_grid.add(dateButton, col, row);
            }
            else{
                dateButton.setOnAction(e -> System.out.println("The selected is: " + currentmonth + " on " + finalDay));
                rightmonth_grid.add(dateButton, col, row);
            }
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

    private void changeChosenButton(AtomicReference<Button> currentButton, Button newButton){
        if (currentButton.get() != newButton){
            if (currentButton.get() != null){
                setButtonOutlook(currentButton.get(), false);
            }
            setButtonOutlook(newButton, true);
            currentButton.set(newButton);
        }
        else{
            setButtonOutlook(newButton, true);
        }
    }

    private boolean isNoneSelected(){
        return ((yearSelector_left.getValue().equals("2025")) && (yearSelector_right.getValue().equals("2025")) &&
                ((rightmonth_grid_selector.getValue().equals("January"))) && ((leftmonth_grid_selector.getValue().equals("January")))
                && (leftgrid_dates.get() == null) && (rightgrid_dates.get() == null) && !checkboxPieChart.isSelected()
                && !checkboxScatterGraph.isSelected() && !checkboxLineChart.isSelected() && !redcolorPreference.isSelected()
                && !orangecolorPreference.isSelected() && !bluecolorPreference.isSelected() && !purplecolorPreference.isSelected()
                && !includeDescriptiveStatistics.isSelected() && !notincludeDescriptiveStatistics.isSelected());
    }
}
