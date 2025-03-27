package com.example.cmpt370project;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration (UI) tests for the goal motivation feature functionality.
 * (i.e. tests for Persona 5, User Story 2)
 */
public class GoalMotivationIntegrationTests extends AppIntegrationTest {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
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
        clickOn(".dialog-pane .button:label('OK')");

        // Verify that the userHistoryDataModel has the updated username
        assertEquals("OverlyOrganizedOmar", root.userHistoryDataModel.getUserName(), "User name should be updated to 'OverlyOrganizedOmar'");
    }

    /**
     * Test 4: Receive Goal Plan Progress Motivation – Under Goal Plan Target
     */
    @Test
    public void P5_US2_TC04() {
        Platform.runLater(() -> {
            root.goalPlanModel.setGoalPlan(new MaintainGoalPlan(8, IGoalPlan.Timeline.WEEKLY));
            root.userHistoryDataModel.setWeeklyCompletedGoals(1);
        });

        clickOn(root.goalsButton);

        String expectedString = "You need to complete 7 more goals for the WEEK to stay on track with your goal plan. Time to complete some goals!";
        String outputString = root.goalsPage.progressFeedback.getText();

        // Verify correct motivational message is shown
        assertEquals(expectedString, outputString, "Message should tell user they are below their current goal plan target.");
    }

    /**
     * Test 5: Receive Goal Plan Progress Motivation – Met Goal Plan Target
     */
    @Test
    public void P5_US2_TC05() {
        Platform.runLater(() -> {
            root.goalPlanModel.setGoalPlan(new MaintainGoalPlan(3, IGoalPlan.Timeline.DAILY));
            root.userHistoryDataModel.setDailyCompletedGoals(3);
        });

        clickOn(root.goalsButton);

        String expectedString = "You have met your target for the DAY and are currently on track with you goal plan. Props to you!";
        String outputString = root.goalsPage.progressFeedback.getText();

        // Verify correct motivational message is shown
        assertEquals(expectedString, outputString, "Message should tell user they have met their current goal plan target.");
    }

    /**
     * Test 6: Receive Goal Plan Progress Motivation – Met Goal Plan Target
     */
    @Test
    public void P5_US2_TC06() {
        Platform.runLater(() -> {
            root.goalPlanModel.setGoalPlan(new IncreaseGoalPlan(3, 6, IGoalPlan.Timeline.DAILY, LocalDate.now().plusDays(3)));
            root.userHistoryDataModel.setDailyCompletedGoals(6);
            root.goalPlanModel.syncGoalPlanToDate(LocalDate.now().plusDays(1));
        });

        clickOn(root.goalsButton);

        String expectedString = "You have completed 2 more goals than your target number of goals for the DAY. Overachiever!";
        String outputString = root.goalsPage.progressFeedback.getText();

        // Verify correct motivational message is shown
        assertEquals(expectedString, outputString, "Message should tell user they have met their current goal plan target.");
    }

    /**
     * Test 7: Receive Goal Plan Progress Motivation – Reset for New Week
     */
    @Test
    public void P5_US2_TC07() {
        Platform.runLater(() -> {
            root.goalPlanModel.setGoalPlan(new MaintainGoalPlan(8, IGoalPlan.Timeline.WEEKLY));
            root.userHistoryDataModel.setWeeklyCompletedGoals(1);
        });

        clickOn(root.goalsButton);

        String expectedString = "You need to complete 7 more goals for the WEEK to stay on track with your goal plan. Time to complete some goals!";
        String outputString = root.goalsPage.progressFeedback.getText();

        // Verify correct motivational message is shown
        assertEquals(expectedString, outputString, "Message should tell user they are below their current goal plan target.");

        Platform.runLater(() -> {
            root.userHistoryDataModel.hardSetWeeklyData(LocalDate.now().plusDays(7));

            String expectedString2= "You need to complete 8 more goals for the WEEK to stay on track with your goal plan. Time to complete some goals!";
            String outputString2 = root.goalsPage.progressFeedback.getText();

            // Verify correct motivational message is shown
            assertEquals(expectedString2, outputString2, "Message should tell user they are below their current goal plan target.");
        });
    }

    /**
     * Test 8: Receive Goal Plan Progress Motivation – Reset for New Day
     */
    @Test
    public void P5_US2_TC08() {
        Platform.runLater(() -> {
            root.goalPlanModel.setGoalPlan(new MaintainGoalPlan(8, IGoalPlan.Timeline.DAILY));
            root.userHistoryDataModel.setDailyCompletedGoals(4);
        });

        clickOn(root.goalsButton);

        String expectedString = "You need to complete 4 more goals for the DAY to stay on track with your goal plan. Time to complete some goals!";
        String outputString = root.goalsPage.progressFeedback.getText();

        // Verify correct motivational message is shown
        assertEquals(expectedString, outputString, "Message should tell user they are below their current goal plan target.");

        Platform.runLater(() -> {
            root.userHistoryDataModel.hardSetDayData(LocalDate.now().plusDays(1));

            String expectedString2= "You need to complete 8 more goals for the DAY to stay on track with your goal plan. Time to complete some goals!";
            String outputString2 = root.goalsPage.progressFeedback.getText();

            // Verify correct motivational message is shown
            assertEquals(expectedString2, outputString2, "Message should tell user they are below their current goal plan target.");
        });
    }

    /**
     * Test 9: Receive Goal Plan Progress Motivation – Update Plan Details
     */
    @Test
    public void P5_US2_TC09() {
        Platform.runLater(() -> {
            root.goalPlanModel.setGoalPlan(new MaintainGoalPlan(8, IGoalPlan.Timeline.WEEKLY));
            root.userHistoryDataModel.setWeeklyCompletedGoals(4);
            root.userHistoryDataModel.setDailyCompletedGoals(3);
        });

        clickOn(root.goalsButton);

        String expectedString = "You need to complete 4 more goals for the WEEK to stay on track with your goal plan. Time to complete some goals!";
        String outputString = root.goalsPage.progressFeedback.getText();

        // Verify correct motivational message is shown
        assertEquals(expectedString, outputString, "Message should tell user they are below their current goal plan target.");

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        // Edit plan
        clickOn(root.goalPlanPage.endGoalNumberInput);
        for (int i = 0; i < 5; i++) {
            clickOn(".decrement-arrow-button");
        }

        clickOn(root.goalPlanPage.timelineSelectBox);
        clickOn("DAILY Basis");

        // Save plan
        clickOn(root.goalPlanPage.savePlanChangesButton);

        clickOn(root.goalsButton);

        expectedString = "You have met your target for the DAY and are currently on track with you goal plan. Props to you!";
        outputString = root.goalsPage.progressFeedback.getText();

        // Verify correct motivational message is shown
        assertEquals(expectedString, outputString, "Message should tell user they have met their current goal plan target.");
    }

    /**
     * Test 10: Receive Goal Plan Progress Motivation – No Plan Exists
     */
    @Test
    public void P5_US2_TC10() {
        Platform.runLater(() -> {
            root.goalPlanModel.clearGoalPlan();
        });

        clickOn(root.goalsButton);

        String expectedString = "Want to be motivated to complete more goals? Head over to the goal plan tab to set up a goal plan!";
        String outputString = root.goalsPage.progressFeedback.getText();

        // Verify correct motivational message is shown
        assertEquals(expectedString, outputString, "Message should tell user they should go make a goal plan.");
    }

}
