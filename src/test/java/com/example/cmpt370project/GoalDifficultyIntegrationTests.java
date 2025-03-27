package com.example.cmpt370project;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GoalDifficultyIntegrationTests extends AppIntegrationTest {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        FxToolkit.setupFixture(() -> root.goalModel.clearGoals());
    }

    @Test
    public void testCreateGoalWithDifficulty() throws Exception {
        FxToolkit.setupFixture(() ->
                root.goalModel.addGoal("Test Goal - Hard", "Personal", "Hard",
                        LocalDate.now(), LocalDate.now().plusDays(5), false)
        );

        List<Goal> allGoals = root.goalModel.getGoals();
        assertFalse(allGoals.isEmpty(), "Goal list should not be empty");
        assertEquals("Hard", allGoals.get(0).getDifficulty());
    }

    @Test
    public void testEditGoalDifficulty() throws Exception {
        FxToolkit.setupFixture(() ->
                root.goalModel.addGoal("Test Edit", "Personal", "Medium",
                        LocalDate.now(), LocalDate.now().plusDays(5), false)
        );

        FxToolkit.setupFixture(() -> {
            Goal goal = root.goalModel.getGoals().get(0);
            goal.setDifficulty("Easy");
            root.goalModel.updateGoal(goal.getTitle(), goal);
        });

        assertEquals("Easy", root.goalModel.getGoals().get(0).getDifficulty());
    }

    @Test
    public void testDeleteGoal() throws Exception {
        FxToolkit.setupFixture(() ->
                root.goalModel.addGoal("Delete Me", "School", "Easy",
                        LocalDate.now(), LocalDate.now().plusDays(1), false)
        );

        FxToolkit.setupFixture(() ->
                root.goalModel.deleteGoal("Delete Me")
        );

        assertEquals(0, root.goalModel.getGoals().size());
    }

    @Test
    public void testFilterGoalsByDifficulty() throws Exception {
        FxToolkit.setupFixture(() -> {
            root.goalModel.addGoal("Goal 1", "Business", "Easy", LocalDate.now(), LocalDate.now().plusDays(2), false);
            root.goalModel.addGoal("Goal 2", "Business", "Medium", LocalDate.now(), LocalDate.now().plusDays(2), false);
        });

        List<Goal> filtered = root.goalModel.getGoalsByDifficulty("Medium");
        assertEquals(1, filtered.size());
        assertEquals("Medium", filtered.get(0).getDifficulty());
    }

    @Test
    public void testDefaultDifficultyAssigned() throws Exception {
        FxToolkit.setupFixture(() ->
                root.goalModel.addGoal("No Difficulty", LocalDate.now(), LocalDate.now().plusDays(1))
        );

        Goal goal = root.goalModel.getGoals().get(0);
        assertNotNull(goal.getDifficulty(), "Default difficulty should be assigned");
    }

    @Test
    public void testEmptyStateWithNoGoals() throws Exception {
        FxToolkit.setupFixture(() -> root.goalModel.clearGoals());
        List<Goal> filtered = root.goalModel.getGoalsByDifficulty("Easy");
        assertTrue(filtered.isEmpty());
    }

    @Test
    public void testPersistenceAfterRefresh() throws Exception {
        FxToolkit.setupFixture(() ->
                root.goalModel.addGoal("Persistent Goal", "Personal", "Medium",
                        LocalDate.now(), LocalDate.now().plusDays(3), false)
        );

        FxToolkit.setupFixture(() -> {
            root.goalModel.save_goals_to_file();
            root.goalModel.load_goals_from_file();
        });

        assertTrue(root.goalModel.getGoals().stream().anyMatch(g -> g.getTitle().equals("Persistent Goal")));
    }

    @Test
    public void testCompletedGoalShowsUpWhenFiltered() throws Exception {
        FxToolkit.setupFixture(() -> {
            Goal g = new Goal("Complete Me", "Personal", "Hard", LocalDate.now(), LocalDate.now().plusDays(2), false);
            root.goalModel.addGoal(g);
            root.goalModel.completeGoal(g);
        });

        List<Goal> filtered = root.goalModel.getGoalsByDifficulty("Hard");
        assertFalse(filtered.isEmpty());
        assertTrue(filtered.get(0).isCompleted());
    }

    @Test
    public void testResettingFilterShowsAllGoals() throws Exception {
        FxToolkit.setupFixture(() -> {
            root.goalModel.addGoal("Easy One", "Personal", "Easy", LocalDate.now(), LocalDate.now().plusDays(2), false);
            root.goalModel.addGoal("Hard One", "Personal", "Hard", LocalDate.now(), LocalDate.now().plusDays(2), false);
        });

        List<Goal> allGoals = root.goalModel.getGoalsByDifficulty("All");
        assertEquals(2, allGoals.size());
    }
}
