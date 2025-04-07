package com.example.cmpt370project;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
/**
 * Integration tests for the GoalView functionality.
 * Test cases cover checking goal title updates based on due date,
 * verifying visualization page navigation, switching between visualization types,
 * adding new goals, and clearing goals.
 */
public class GoalVisViewIntegrationTests extends AppIntegrationTest {

    GoalModel goalModel;
    UserHistoryDataModel UH;

    @Override
    public void start(Stage stage) throws Exception {
        // Start the main application UI from AppIntegrationTest (DashboardView)
        super.start(stage);
    }

    @BeforeEach
    public void setUp() {
    goalModel = new GoalModel(UH); // Initialize before using
    }


    /**
     * Test Case 1: Ensuring the method to change title on the graphs when the goal is due today works.
     * Creates a goal due today and checks if its title is updated correctly.
     */
    @Test
    public void testTitleChangeDueToday() {
        // Create a goal that starts yesterday and ends today (i.e. due today)
        Goal g1 = new Goal("Learn how to cook soup", LocalDate.now().minusDays(1), LocalDate.now());
        // Simulate updating the title based on due status.
        // Assuming GoalController.isDue(Goal) returns the new title string.
        g1.setTitle(GoalProgress.isDue(g1));
        assertEquals("Learn how to cook soup (Due Today!)", g1.getTitle(), "Goal title should indicate 'Due Today!'");
    }

    /**
     * Test Case 2: Ensuring the method to change title when the goal is due before today works.
     */
    @Test
    public void testTitleChangePastDue() {
        // Create a goal that starts two days ago and ended yesterday (i.e. past due)
        Goal g2 = new Goal("Learn how to cook soup", LocalDate.now().minusDays(2), LocalDate.now().minusDays(1));
        g2.setTitle(GoalProgress.isDue(g2));
        assertEquals("Learn how to cook soup (Past Due!)", g2.getTitle(), "Goal title should indicate 'Past Due!'");
    }

    /**
     * Test Case 3: Ensuring the method does not change title when the goal due date is in the future.
     */
    @Test
    public void testTitleNoChangeFutureDue() {
        // Create a goal that starts two days ago and ends in the future
        Goal g3 = new Goal("Learn how to cook soup", LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));
        g3.setTitle(GoalProgress.isDue(g3));
        assertEquals("Learn how to cook soup", g3.getTitle(), "Goal title should remain unchanged when not due yet");
    }

    /**
     * Test Case 4: Goal Visualization Page Opens.
     * This test simulates clicking the “Goal Visualization” button to navigate to the goal view.
     */
    @Test
    public void testGoalVisualizationPageOpens() {
        // Here, we click on it and check that the goalView is now visible.
        //WaitForAsyncUtils.waitForFxEvents(4);
        clickOn("#goalVisButton");
        // Check for a UI element unique to GoalView, for example the list view of goals.
        ListView<?> goalList = lookup(".list-view").query();
        
        assertTrue(goalList.isVisible(), "Open the goal Visualization page to ensure it loads");
    }

    /**
     * Test Case 5: Switching between visualization types.
     * This test simulates using a drop-down menu to switch chart types.
     */
    @Test
    public void testSwitchVisualizationTypes() {
        // Navigate to the goal visualization view
        clickOn("#goalVisButton");
        // Assume there is a ComboBox for visualization type with fx:id "visualizationTypeComboBox"
        ComboBox<String> vizComboBox = lookup("#chartTypeSelector").queryComboBox();

        // Switch to "Bar Chart"
        clickOn(vizComboBox);
        doubleClickOn("Bar Chart");
        // Switch to "Pie Chart"
        clickOn(vizComboBox);
        doubleClickOn("Pie Chart");
        // Switch to "Line Chart"
        clickOn(vizComboBox);
        doubleClickOn("Line Chart");
        // Switch back to "Pie Chart"
        clickOn(vizComboBox);
        doubleClickOn("Pie Chart");
        // Switch to "Bar Chart" again
        clickOn(vizComboBox);
        doubleClickOn("Bar Chart");
        // Switch to "Line Chart" again
        clickOn(vizComboBox);
        doubleClickOn("Line Chart");
        // Finally, switch back to "Bar Chart"
        clickOn(vizComboBox);
        doubleClickOn("Bar Chart");
        
        // Verify that the current selection is "Bar Chart"
        assertEquals("Bar Chart", vizComboBox.getValue(), "Visualization should be set to 'Bar Chart' after switching.");
    }

    /**
     * Test Case 6: Adding a new goal to update the visualization.
     */
    @Test
    public void testAddingNewGoal() {
        // From the home page, simulate clicking the "Add Goal" button.
        clickOn("#clearGoalsButton");
        clickOn("OK");
        clickOn("#addGoalButton");
        // Fill out the form fields
        clickOn("#goalTitleField");
        write("Fortnite poggers");
        // Add start and end dates
        clickOn("#startDatePicker");
        doubleClickOn("#startDatePicker");
        write(LocalDate.now().toString());
        clickOn("#endDatePicker");
        doubleClickOn("#endDatePicker");
        write(LocalDate.now().plusDays(5).toString());
        // Click the save button 
        clickOn("#saveGoalButton");
        String title = "Fortnite poggers";
        // Navigate to Goal Visualization page
        clickOn("#goalVisButton");
        ListView<?> goalList = lookup(".list-view").query();
        assertTrue(goalList.isVisible(), "New goal is added to visualization");
    }

    /**
     * Test Case 7: Ensure goals with the same start and end date are visible in the line graph.
     */
    @Test
    public void testSameStartAndEndDatesVisible() {
        // Add two goals with the same start and end date.
        Goal goal1 = new Goal("Goal Same Date 1", LocalDate.now(), LocalDate.now().plusWeeks(1));
        Goal goal2 = new Goal("Goal Same Date 2", LocalDate.now(), LocalDate.now().plusWeeks(1));
        Platform.runLater(() -> {
            goalModel.addGoal(goal1);
            goalModel.addGoal(goal2);
        });
        // Navigate to goal visualization and select "Line Chart"
        clickOn("#goalVisButton");
        ComboBox<String> vizComboBox = lookup("#chartTypeSelector").queryComboBox();
        clickOn(vizComboBox);
        clickOn("Line Chart");
        sleep(2000);
        
        // Verify both goals are visible
        ListView<Goal> goalList = lookup(".list-view").query();
        boolean found1 = goalList.getItems().stream().anyMatch(g -> g.getTitle().contains("Goal Same Date 1"));
        boolean found2 = goalList.getItems().stream().anyMatch(g -> g.getTitle().contains("Goal Same Date 2"));
        assertTrue(found1 && found2, "Both goals with same start and end date should be visible in the line chart.");
    }

    /**
     * Test Case 8: Ensure goals with the same start but different end dates are visible.
     */
    @Test
    public void testSameStartDifferentEndVisible() {
        Goal goal1 = new Goal("Goal Same Start Diff End 1", LocalDate.now(), LocalDate.now().plusDays(2));
        Goal goal2 = new Goal("Goal Same Start Diff End 2", LocalDate.now(), LocalDate.now().plusDays(5));
        Platform.runLater(() -> {
            goalModel.addGoal(goal1);
            goalModel.addGoal(goal2);
        });
        clickOn("#goalsButton");
        ComboBox<String> vizComboBox = lookup("#chartTypeSelector").queryComboBox();
        clickOn(vizComboBox);
        clickOn("Line Chart");
        
        ListView<Goal> goalList = lookup(".list-view").query();
        boolean found1 = goalList.getItems().stream().anyMatch(g -> g.getTitle().contains("Goal Same Start Diff End 1"));
        boolean found2 = goalList.getItems().stream().anyMatch(g -> g.getTitle().contains("Goal Same Start Diff End 2"));
        assertTrue(found1 && found2, "Both goals with same start and different end dates should be visible in the line chart.");
    }

    /**
     * Test Case 9: Ensure goals with different start but same end dates are visible.
     */
    @Test
    public void testDifferentStartSameEndVisible() {
        Goal goal1 = new Goal("Goal Diff Start Same End 1", LocalDate.now().minusDays(2), LocalDate.now());
        Goal goal2 = new Goal("Goal Diff Start Same End 2", LocalDate.now().minusDays(1), LocalDate.now());
        Platform.runLater(() -> {
            goalModel.addGoal(goal1);
            goalModel.addGoal(goal2);
        });
        clickOn("#goalsButton");
        ComboBox<String> vizComboBox = lookup("#chartTypeSelector").queryComboBox();
        clickOn(vizComboBox);
        clickOn("Line Chart");
        
        ListView<Goal> goalList = lookup(".list-view").query();
        boolean found1 = goalList.getItems().stream().anyMatch(g -> g.getTitle().contains("Goal Diff Start Same End 1"));
        boolean found2 = goalList.getItems().stream().anyMatch(g -> g.getTitle().contains("Goal Diff Start Same End 2"));
        assertTrue(found1 && found2, "Both goals with different start but same end dates should be visible in the line chart.");
    }

    /**
     * Test Case 10: Clearing Goals.
     * This test ensures that when goals are cleared, they no longer appear in the visualization.
     */
    @Test
    public void testClearingGoals() {
        // Assume at least one goal exists
        Goal sampleGoal = new Goal("Sample Goal", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Platform.runLater(() -> {
            goalModel.addGoal(sampleGoal);
        });
        // Navigate to goal visualization and verify the goal is shown
        clickOn("#goalsButton");
        ListView<?> goalList = lookup(".list-view").query();
        assertTrue(goalList.isVisible(), "Goals should be visible before clearing.");

        // Go back to the home page and clear goals.
        clickOn("#homeButton");
        clickOn("#clearGoalsButton");
        clickOn("OK");

        // Return to goal visualization and check that no goals are visible.
        clickOn("#goalsButton");
        // Pause briefly to allow UI update
        assertTrue(goalList.getItems().isEmpty(), "Goals should be cleared and not visible in the visualization.");
    }
}

