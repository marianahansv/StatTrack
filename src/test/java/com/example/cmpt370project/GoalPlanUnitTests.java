package com.example.cmpt370project;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoalPlanUnitTests {

    private static GoalPlanModel goalPlanModel;

    @BeforeAll
    public static void setUp() {
        goalPlanModel = new GoalPlanModel();
        goalPlanModel.clearGoalPlan(); // Start testing with empty goal plan
    }

    @Test
    public void testEmptyPlan() {
        // Test the initial value of the userName
        assertEquals(false, goalPlanModel.goalPlanExists(), "Goal Plan should not exist");
    }
}
