package com.example.cmpt370project;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration (UI) tests for the goal plan feature functionality.
 * (i.e. tests for Persona 5, User Story 1)
 */
public class GoalPlanIntegrationTests extends AppIntegrationTest {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
    }

    /**
     * Test 1: Test making a new maintain goal plan.
     */
    @Test
    public void P5_US1_TC01() {
        root.goalPlanModel.clearGoalPlan();
        root.goalPlanModel.syncGoalPlanToNow();

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        // Make a plan
        clickOn(root.goalPlanPage.maintainPlan);
        clickOn(root.goalPlanPage.endGoalNumberInput);
        for (int i = 0; i < 4; i++) {
            clickOn(".increment-arrow-button");
        }

        clickOn(root.goalPlanPage.timelineSelectBox);
        clickOn("DAILY Basis");

        // Save plan
        clickOn(root.goalPlanPage.savePlanChangesButton);

        // Verify that the goal plan has been updated
        assertEquals("Maintain", root.goalPlanModel.getGoalPlan().getPlanName(), "Goal Plan should be set to 'Maintain'");
        assertEquals(5, root.goalPlanModel.getGoalPlanCurrent(), "Goal Plan should be set to maintain 5 goals");
        assertEquals(IGoalPlan.Timeline.DAILY, root.goalPlanModel.getGoalPlan().getTimeline(), "Goal Plan should be set to daily timeline");
    }

    /**
     * Test 2: Test making a new increase goal plan.
     */
    @Test
    public void P5_US1_TC02() {
        root.goalPlanModel.clearGoalPlan();
        root.goalPlanModel.syncGoalPlanToDate(LocalDate.of(2031, 04, 07));

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        // Make a plan
        clickOn(root.goalPlanPage.increasePlan);
        clickOn(root.goalPlanPage.endGoalNumberInput);
        for (int i = 0; i < 4; i++) {
            clickOn(root.goalPlanPage.endGoalNumberInput.lookup(".increment-arrow-button"));
        }

        clickOn(root.goalPlanPage.startGoalNumberInput);
        for (int i = 0; i < 1; i++) {
            clickOn(root.goalPlanPage.startGoalNumberInput.lookup(".increment-arrow-button"));
        }

        clickOn(root.goalPlanPage.timelineSelectBox);
        clickOn("WEEKLY Basis");

        clickOn(root.goalPlanPage.endDatePicker);
        root.goalPlanPage.endDatePicker.setValue(LocalDate.of(2031, 04, 27));

        // Save plan
        clickOn(root.goalPlanPage.savePlanChangesButton);

        // Verify that the goal plan has been updated
        assertEquals("Increase", root.goalPlanModel.getGoalPlan().getPlanName(), "Goal Plan should be set to 'Increase'");
        assertEquals(2, root.goalPlanModel.getGoalPlanCurrent(), "Goal Plan should be set to increase 2 goals");
        assertEquals(IGoalPlan.Timeline.WEEKLY, root.goalPlanModel.getGoalPlan().getTimeline(), "Goal Plan should be set to weekly timeline");
        assertEquals(5, root.goalPlanModel.getGoalPlan().getGoalPlanMax(), "Goal Plan target should be set to 5 timeline");
    }

    /**
     * Test 3: Test update previous Maintain Goal Plan (with valid data)
     */
    @Test
    public void P5_US1_TC03() {
        root.goalPlanModel.clearGoalPlan();
        root.goalPlanModel.setGoalPlan(new MaintainGoalPlan(5, IGoalPlan.Timeline.DAILY));

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        // Edit plan
        clickOn(root.goalPlanPage.endGoalNumberInput);
        for (int i = 0; i < 3; i++) {
            clickOn(".increment-arrow-button");
        }

        clickOn(root.goalPlanPage.timelineSelectBox);
        clickOn("WEEKLY Basis");

        // Save plan
        clickOn(root.goalPlanPage.savePlanChangesButton);

        // Verify that the goal plan has been updated
        assertEquals("Maintain", root.goalPlanModel.getGoalPlan().getPlanName(), "Goal Plan should be set to 'Maintain'");
        assertEquals(8, root.goalPlanModel.getGoalPlanCurrent(), "Goal Plan should be set to maintain 8 goals");
        assertEquals(IGoalPlan.Timeline.WEEKLY, root.goalPlanModel.getGoalPlan().getTimeline(), "Goal Plan should be set to weekly timeline");
    }

    /**
     * Test 4: Test update previous Increase Goal Plan (with valid data)
     */
    @Test
    public void P5_US1_TC04() {
        root.goalPlanModel.clearGoalPlan();
        root.goalPlanModel.setGoalPlan(new IncreaseGoalPlan(2, 8, IGoalPlan.Timeline.WEEKLY, LocalDate.of(2031, 04, 27)));

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        // Edit plan
        clickOn(root.goalPlanPage.endGoalNumberInput);
        for (int i = 0; i < 2; i++) {
            clickOn(".decrement-arrow-button");
        }

        clickOn(root.goalPlanPage.startGoalNumberInput);
        for (int i = 0; i < 1; i++) {
            clickOn(root.goalPlanPage.startGoalNumberInput.lookup(".decrement-arrow-button"));
        }

        clickOn(root.goalPlanPage.timelineSelectBox);
        clickOn("DAILY Basis");

        clickOn(root.goalPlanPage.endDatePicker);
        root.goalPlanPage.endDatePicker.setValue(LocalDate.of(2031, 04, 28));

        // Save plan
        clickOn(root.goalPlanPage.savePlanChangesButton);

        // Verify that the goal plan has been updated
        assertEquals("Increase", root.goalPlanModel.getGoalPlan().getPlanName(), "Goal Plan should be set to 'Increase'");
        assertEquals(1, root.goalPlanModel.getGoalPlanCurrent(), "Goal Plan should be set to increase 1 goals");
        assertEquals(IGoalPlan.Timeline.DAILY, root.goalPlanModel.getGoalPlan().getTimeline(), "Goal Plan should be set to daily timeline");
        assertEquals(6, root.goalPlanModel.getGoalPlan().getGoalPlanMax(), "Goal Plan target should be set to 6 timeline");
        assertEquals(LocalDate.of(2031, 04, 28), ((IncreaseGoalPlan) root.goalPlanModel.getGoalPlan()).getEndDate(), "Goal Plan end date should be 2031-04-28");
    }

    /**
     * Test 5: Cancel in progress changes to update a Goal Plan
     */
    @Test
    public void P5_US1_TC05() {
        root.goalPlanModel.clearGoalPlan();
        root.goalPlanModel.setGoalPlan(new MaintainGoalPlan(8, IGoalPlan.Timeline.WEEKLY));

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

        clickOn(root.goalPlanPage.endDatePicker);
        root.goalPlanPage.endDatePicker.setValue(LocalDate.of(2031, 04, 28));

        // Cancel goal plan create
        clickOn(root.goalPlanPage.cancelEditGoalPlanButton);

        // Verify that the goal plan has been updated
        assertEquals("Maintain", root.goalPlanModel.getGoalPlan().getPlanName(), "Goal Plan should be set to 'Maintain'");
        assertEquals(8, root.goalPlanModel.getGoalPlanCurrent(), "Goal Plan should be set to maintain 8 goals");
        assertEquals(IGoalPlan.Timeline.WEEKLY, root.goalPlanModel.getGoalPlan().getTimeline(), "Goal Plan should be set to weekly timeline");
    }

    /**
     * Test 6: Delete a preexisting Goal Plan
     */
    @Test
    public void P5_US1_TC06() {
        root.goalPlanModel.clearGoalPlan();
        root.goalPlanModel.setGoalPlan(new MaintainGoalPlan(8, IGoalPlan.Timeline.WEEKLY));

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        // Delete Plan
        clickOn(root.goalPlanPage.deletePlanButton);

        // Click the "OK" button to confirm the input
        clickOn(".dialog-pane .button:label('OK')"); // Clicking the "OK" button by its label

        // Verify that the goal plan has been updated
        assertEquals(null, root.goalPlanModel.getGoalPlan(), "Goal Plan should be set to null");
    }

    /**
     * Test 7: Cancel in progress changes to create a Goal Plan
     */
    @Test
    public void P5_US1_TC07() {
        root.goalPlanModel.clearGoalPlan();

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        // Make a plan
        clickOn(root.goalPlanPage.maintainPlan);
        clickOn(root.goalPlanPage.endGoalNumberInput);

        clickOn(root.goalPlanPage.timelineSelectBox);
        clickOn("DAILY Basis");

        // Cancel goal plan create
        clickOn(root.goalPlanPage.cancelEditGoalPlanButton);

        // Verify that the goal plan has been updated
        assertEquals(null, root.goalPlanModel.getGoalPlan(), "Goal Plan should be set to null");
    }

    /**
     * Test 8: Create an invalid Increase Goal Plan (invalid data)
     */
    @Test
    public void P5_US1_TC08() {
        root.goalPlanModel.clearGoalPlan();
        root.goalPlanModel.syncGoalPlanToDate(LocalDate.of(2031, 04, 07));

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        // Make a plan
        clickOn(root.goalPlanPage.increasePlan);
        clickOn(root.goalPlanPage.endGoalNumberInput);
        for (int i = 0; i < 1; i++) {
            clickOn(root.goalPlanPage.endGoalNumberInput.lookup(".increment-arrow-button"));
        }

        clickOn(root.goalPlanPage.startGoalNumberInput);
        for (int i = 0; i < 7; i++) {
            clickOn(root.goalPlanPage.startGoalNumberInput.lookup(".increment-arrow-button"));
        }

        clickOn(root.goalPlanPage.timelineSelectBox);
        clickOn("DAILY Basis");

        clickOn(root.goalPlanPage.endDatePicker);
        root.goalPlanPage.endDatePicker.setValue(LocalDate.of(2031, 04, 28));

        // Save plan
        clickOn(root.goalPlanPage.savePlanChangesButton);

        // Verify the error message is visible
        assert root.goalPlanPage.errorLabel.isVisible() : "Goal Plan should be set to null";
    }

    /**
     * Test 9: Increase Goal Plan target number increases
     */
    @Test
    public void P5_US1_TC09() {
        root.goalPlanModel.clearGoalPlan();
        root.goalPlanModel.setGoalPlan(new IncreaseGoalPlan(3, 6, IGoalPlan.Timeline.DAILY, LocalDate.now().plusDays(3)));

        root.goalPlanModel.syncGoalPlanToDate(LocalDate.now().plusDays(1));

        // Verify the error message is visible
        assertEquals(4, root.goalPlanModel.getGoalPlanCurrent(), "Goal Plan current number should have been incremented");
    }

    /**
     * Test 10: Increase Goal Plan matures and rolls over to Maintain Plan Goal
     */
    @Test
    public void P5_US1_TC10() {
        root.goalPlanModel.clearGoalPlan();
        root.goalPlanModel.setGoalPlan(new IncreaseGoalPlan(3, 6, IGoalPlan.Timeline.DAILY, LocalDate.now().plusDays(3)));

        root.goalPlanModel.syncGoalPlanToDate(LocalDate.now().plusDays(3));

        // Verify that the goal plan has been updated
        assertEquals("Maintain", root.goalPlanModel.getGoalPlan().getPlanName(), "Goal Plan should be set to 'Maintain'");
        assertEquals(6, root.goalPlanModel.getGoalPlanCurrent(), "Goal Plan should be set to maintain 6 goals");
        assertEquals(IGoalPlan.Timeline.DAILY, root.goalPlanModel.getGoalPlan().getTimeline(), "Goal Plan should be set to daily timeline");
    }
}
