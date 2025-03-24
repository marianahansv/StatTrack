package com.example.cmpt370project;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoalPlanIntegrationTests extends AppIntegrationTest {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
    }

    /**
     * Test making a new goal plan.
     */
    @Test
    public void testGoalPlanCreate() {
        root.goalPlanModel.clearGoalPlan();

        // Go to edit the goal plan
        clickOn(root.goalPlanButton);
        clickOn(root.goalPlanPage.goCreateEditGoalPlanButton);

        clickOn(root.goalPlanPage.maintainPlan);
        clickOn(root.goalPlanPage.endGoalNumberInput);
        for (int i = 0; i < 4; i++) {
            clickOn(".increment-arrow-button");
        }

        clickOn(root.goalPlanPage.timelineSelectBox);
        clickOn("DAILY Basis");

        clickOn(root.goalPlanPage.savePlanChangesButton);

        // Verify that the goal plan has been updated
        assertEquals("Maintain", root.goalPlanModel.getGoalPlan().getPlanName(), "Goal Plan should be set to 'Maintain'");
        assertEquals(5, root.goalPlanModel.getGoalPlanCurrent(), "Goal Plan should be set to maintain 5 goals");
        assertEquals(IGoalPlan.Timeline.DAILY, root.goalPlanModel.getGoalPlan().getTimeline(), "Goal Plan should be set to daily timeline");
    }
}
