package com.example.cmpt370project;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for the goal plan feature functionality (i.e. testing the GoalPlanModel operations).
 */
public class GoalPlanUnitTests {

    private static GoalPlanModel goalPlanModel;

    @BeforeAll
    public static void setUp() {
        goalPlanModel = new GoalPlanModel();
        goalPlanModel.clearGoalPlan(); // Start testing with empty goal plan
    }

    /**
     * Test initialization of goalPlanModel.
     */
    @Test
    public void testEmptyPlan() {
        // Test the initial value of the userName
        assertFalse(goalPlanModel.goalPlanExists(), "Goal Plan should not exist");
    }
}
