package com.example.cmpt370project;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration (UI) tests for the goal motivation feature functionality.
 */
public class GoalMotivationIntegrationTests extends AppIntegrationTest {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

    }

    /**
     * Test 1: Set New Username to Receive Motivational Greetings (valid data)
     */
    @Test
    public void P5_US2_TC01() {
//        // Simulate first app open
//        root.userHistoryDataModel.setFirstOpen();
//
//
//
//        // Find the text field inside the dialog and type in a name
//        write("HasAPlanFran");
//
//        // Click the "OK" button to confirm the input
//        clickOn(".dialog-pane .button:label('OK')"); // Clicking the "OK" button by its label
//
//        // Verify that the userHistoryDataModel has the updated username
//        assertEquals("HasAPlanFran", root.userHistoryDataModel.getUserName(), "User name should be updated to 'User'");
    }

    /**
     * Test 2: Set Username to Receive Motivational Greetings (choosing not to set username)
     */
    @Test
    public void P5_US2_TC02() {
//        // Simulate first app open
//        root.userHistoryDataModel.setFirstOpen();
//
//
//
//        // Find the text field inside the dialog and type in a name
//        write("HasAPlanFran");
//
//        // Click the "OK" button to confirm the input
//        clickOn(".dialog-pane .button:label('OK')"); // Clicking the "OK" button by its label
//
//        // Verify that the userHistoryDataModel has the updated username
//        assertEquals("HasAPlanFran", root.userHistoryDataModel.getUserName(), "User name should be updated to 'User'");
    }


    /**
     * Test 3: Edit Previous Username to Receive Motivational Greetings (valid data)
     */
    @Test
    public void P5_US2_TC03() {
        // Trigger the dialog by clicking the button
        clickOn(root.homePage.changeNameButton);

        // Find the text field inside the dialog and type in a name
        write("OverlyOrganizedOmar");

        // Click the "OK" button to confirm the input
        clickOn(".dialog-pane .button:label('OK')"); // Clicking the "OK" button by its label

        // Verify that the userHistoryDataModel has the updated username
        assertEquals("OverlyOrganizedOmar", root.userHistoryDataModel.getUserName(), "User name should be updated to 'OverlyOrganizedOmar'");
    }

    /**
     * Test 4: Receive Goal Plan Progress Motivation – Under Goal Plan Target
     */
    @Test
    public void P5_US2_TC04() {
        root.goalPlanModel.setGoalPlan(new MaintainGoalPlan(8, IGoalPlan.Timeline.WEEKLY));
        root.userHistoryDataModel.setWeeklyCompletedGoals(1);

        // Trigger the dialog by clicking the button
        clickOn(root.goalsButton);

        String expectedString = "";
        String outputString = "";

        // Verify that the userHistoryDataModel has the updated username
        assertEquals("OverlyOrganizedOmar", root.userHistoryDataModel.getUserName(), "User name should be updated to 'OverlyOrganizedOmar'");
    }

}
