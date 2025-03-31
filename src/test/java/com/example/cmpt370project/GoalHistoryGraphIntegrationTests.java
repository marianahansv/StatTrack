package com.example.cmpt370project;

import javafx.scene.control.Alert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoalHistoryGraphIntegrationTests extends AppIntegrationTest {
    @Test
    /**
     * Test 1: Timeframe selected where no goals are created.
     */
    void P4_US1_TC01() {
        if (root.historicalChartButton.getScene() == null) {
            throw new IllegalStateException("historicalChartButton is not attached to a scene.");
        };

        clickOn(root.historicalChartButton);
        clickOn(root.historicalChartProgress.checkboxLineChart);
        clickOn(root.historicalChartProgress.bluecolorPreference);
        clickOn(root.historicalChartProgress.redcolorPreference);
        clickOn(root.historicalChartProgress.includeDescriptiveStatistics);
        clickOn(root.historicalChartProgress.generate_visualizaton);
        Alert alert_four = new Alert(Alert.AlertType.WARNING);
        alert_four.setContentText("No goals were set during this time period so no statistical analysis can be " +
                "performed.");
        assertEquals("No goals were set during this time period so no statistical analysis can be performed.",
                alert_four.getContentText(), "Expected: Alert message when no goals are during the timeframe");
    }

    /**
     * Test 2: No parameters were selected to view the graphs.
     */
    @Test
    public void P4_US1_TC02() {
        assertTrue(root.historicalChartProgress.isNoneSelected(), "Expected: Initially nothing to be selected");
    }
}

//
//    /**
//     * Test 3: Exactly one of the parameters is missing.
//     */
//    public void P4_US1_TC03(){
//    }
//
//    /**
//     * Test 4: The starting date selected by the user is bigger than the end date. [date focused]
//     */
//    public void P4_US1_TC04(){
//    }
//
//    /**
//     * Test 5: The starting date selected by the user is bigger than the end date. [month focused]
//     */
//    public void P4_US1_TC05(){}
//
//    /**
//     * Test 6: The starting date selected by the user is bigger than the end date. [year focused]
//     */
//    public void P4_US1_TC06(){}
//
//    /**
//     * Test 7: Resetting the parameters and achieving expected result from the reselected parameters.
//     */
//    public void P4_US1_TC07(){}
//
//    /**
//     * Test 8: View the historical goals without any descriptive statistics.
//     */
//    public void P4_US1_TC08(){}
//
//    /**
//     * Test 9: View the historical goals with descriptive statistics included.
//     */
//    public void P4_US1_TC09(){
//    }
//
//    /**
//     * Test 10: Viewing the results in the expected color display.
//     */
//    public void P4_US1_TC10(){
//    }
