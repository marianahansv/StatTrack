package com.example.cmpt370project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

/**
 * Note that in order for the test cases to successfully work without any errors, we need to activate an option for the
 * VM machine which would be written in this manner: --add-opens=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED.
 * It ensures that all the errors related to the module access is resolved.
 */
public class UserHistoryProgressTest extends AppIntegrationTest {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
    }

    // Testing the starting year is preselected
    @Test
    public void checking_right_year(){
        root.historicalChartProgress.yearSelector_right.setValue("2025");

        // Check if ComboBox is properly initialized
        if (root.historicalChartProgress.yearSelector_right == null) {
            System.out.println("Test failed: The right year selector is null.");
        } else {
            System.out.println("Test success: The right year selector is not null.");
        }
    }

    // Testing the ending year is preselected
    @Test
    void checking_left_year(){
        root.historicalChartProgress.yearSelector_left.setValue("2025");

        if (root.historicalChartProgress.yearSelector_left == null) {
            System.out.println("Test failed: The left year selector is null.");
        } else {
            System.out.println("Test success: The left year selector is not null.");
        }
    }

    // Testing the starting month is preselected
    @Test
    void checking_left_month(){
        root.historicalChartProgress.leftmonth_grid_selector.setValue("January");
        if (root.historicalChartProgress.yearSelector_left == null) {
            System.out.println("Test failed: The left month selector is null.");
        } else {
            System.out.println("Test success: The left month selector is not null.");
        }
    }

    // Testing the ending month is preselected
    @Test
    void checking_right_month(){
        root.historicalChartProgress.rightmonth_grid_selector.setValue("January");
        root.historicalChartProgress.rightmonth_grid_selector.setValue("February");
        if (root.historicalChartProgress.yearSelector_right.getValue() == "February") {
            System.out.println("Test success: The right month selector is correct.");
        } else {
            System.out.println("Test failure: The right month selector is not correct.");
        }
    }

    //                                     Testing when the checkboxes are selected                                  //
    @Test
    void checking_scatter_graph(){
        root.historicalChartProgress.checkboxScatterGraph.isSelected();
        if (root.historicalChartProgress.checkboxScatterGraph.isSelected()) {
            System.out.println("Test success: The checkbox scatter graph is validly selected.");
        } else {
            System.out.println("Test failure: The checkbox scatter graph is not validly selected.");
        }

    }

    @Test
    void checking_pie_chart(){
        root.historicalChartProgress.checkboxPieChart.isSelected();
        if (root.historicalChartProgress.checkboxPieChart.isSelected()) {
            System.out.println("Test success: The checkbox pie chart is validly selected.");
        } else {
            System.out.println("Test failure: The checkbox pie chart is not validly selected.");
        }
    }

    @Test
    void checking_line_chart(){
        root.historicalChartProgress.checkboxLineChart.isSelected();
        if (root.historicalChartProgress.checkboxLineChart.isSelected()) {
            System.out.println("Test success: The checkbox pie chart is validly selected.");
        } else {
            System.out.println("Test failure: The checkbox pie chart is not validly selected.");
        }
    }

    @Test
    void checking_red_color(){
        root.historicalChartProgress.redcolorPreference.isSelected();
        if (root.historicalChartProgress.redcolorPreference.isSelected()) {
            System.out.println("Test success: The red colored option is validly selected.");
        } else {
            System.out.println("Test failure: The red colored option is not validly selected.");
        }
    }

    @Test
     void checking_orange_color(){
        root.historicalChartProgress.orangecolorPreference.isSelected();
        if (root.historicalChartProgress.orangecolorPreference.isSelected()) {
            System.out.println("Test success: The orange colored option is validly selected.");
        } else {
            System.out.println("Test failure: The orange colored option is not validly selected.");
        }
    }

    @Test
    void checking_blue_color(){
        root.historicalChartProgress.bluecolorPreference.isSelected();
        if (root.historicalChartProgress.bluecolorPreference.isSelected()) {
            System.out.println("Test success: The blue colored option is validly selected.");
        } else {
            System.out.println("Test failure: The blue colored option is not validly selected.");
        }
    }

    @Test
    void checking_purple_color(){
        root.historicalChartProgress.purplecolorPreference.isSelected();
        if (root.historicalChartProgress.purplecolorPreference.isSelected()) {
            System.out.println("Test success: The purple colored option is validly selected.");
        } else {
            System.out.println("Test failure: The purple colored option is not validly selected.");
        }
    }

    @Test
    void checking_descrip_stat(){
        root.historicalChartProgress.includeDescriptiveStatistics.isSelected();
        if (root.historicalChartProgress.includeDescriptiveStatistics.isSelected()) {
            System.out.println("Test success: The descriptive statistics option is validly selected.");
        } else {
            System.out.println("Test failure: The descriptive statistics option is not validly selected.");
        }
    }

    @Test
    void checking_no_descip_stats(){
        root.historicalChartProgress.notincludeDescriptiveStatistics.isSelected();
        if (root.historicalChartProgress.notincludeDescriptiveStatistics.isSelected()) {
            System.out.println("Test success: The descriptive statistics option is validly selected.");
        } else {
            System.out.println("Test failure: The descriptive statistics option is not validly selected.");
        }
    }
}
