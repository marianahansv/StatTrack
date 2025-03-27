package com.example.cmpt370project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GoalSectionModelTests {

    private GoalModel goalModel;

    @BeforeEach
    public void setup() {
        UserHistoryDataModel historyModel = new UserHistoryDataModel();
        goalModel = new GoalModel(historyModel);
        goalModel.clearGoals();
    }

    @Test
    public void testAssignGoalToSection() {
        goalModel.addGoal("Workout", "Personal", "Medium", LocalDate.now(), LocalDate.now().plusDays(1), false);
        Goal goal = goalModel.getGoals().get(0);
        assertEquals("Personal", goal.getSection());
    }

    @Test
    public void testEditGoalSection() {
        goalModel.addGoal("Study", "School", "Hard", LocalDate.now(), LocalDate.now().plusDays(2), false);

        Goal goal = goalModel.getGoals().get(0);
        goal.setSection("Business");
        goalModel.updateGoal(goal.getTitle(), goal);

        Goal updated = goalModel.getGoals().get(0);
        assertEquals("Business", updated.getSection());
    }

    @Test
    public void testGetGoalsBySection() {
        goalModel.addGoal("Read Book", "Personal", "Easy", LocalDate.now(), LocalDate.now().plusDays(3), false);
        goalModel.addGoal("Finish Essay", "School", "Hard", LocalDate.now(), LocalDate.now().plusDays(1), false);

        List<Goal> personalGoals = goalModel.getGoalsForSection("Personal");
        assertEquals(1, personalGoals.size());
        assertEquals("Read Book", personalGoals.get(0).getTitle());
    }

    @Test
    public void testDeleteGoalsInSection() {
        goalModel.addGoal("Do Taxes", "Business", "Medium", LocalDate.now(), LocalDate.now().plusDays(5), false);
        goalModel.addGoal("Meet Client", "Business", "Medium", LocalDate.now(), LocalDate.now().plusDays(5), false);

        goalModel.deleteGoalsInSection("Business");

        List<Goal> businessGoals = goalModel.getGoalsForSection("Business");
        assertTrue(businessGoals.isEmpty());
    }

    @Test
    public void testGeneralSectionDefault() {
        goalModel.addGoal("Unlabeled Goal", LocalDate.now(), LocalDate.now().plusDays(2));
        Goal g = goalModel.getGoals().get(0);

        assertNotNull(g.getSection(), "Section should not be null");
        assertEquals("General", g.getSection(), "Default section should be 'General'");
    }

}
