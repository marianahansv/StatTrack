package com.example.cmpt370project;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Integration tests for multi-page scenarios using the DashboardView.
 * These tests cover editing a preexisting goal on the Goal View page and verifying updates on both
 * the goal view and the graphs (Goal Visuals) pages.
 */
public class DashboardIntegrationTests extends ApplicationTest {

    private DashboardView dashboardView;

    @Override
    public void start(Stage stage) throws Exception {
        // Instantiate the DashboardView as the root of the scene.
        dashboardView = new DashboardView();
        stage.setScene(new javafx.scene.Scene(dashboardView, 1024, 768));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // For each test we may want to ensure the default view is visible.
        // Assuming the home page is the default, navigate to the Goals page when needed.
    }

    // Helper methods for navigation
    private void goToGoalsPage() {
        // Navigate to the goal view page (My Goals)
        clickOn("#goalsButton");
    }

    private void goToGoalVisualsPage() {
        // Navigate to the goal visuals (data visualization) page
        clickOn("#goalVisButton");
    }

    // ---------------------------------------------------
    // Goal View Editing Tests (Test Cases 1 to 5 & 9-10)
    // ---------------------------------------------------

    /**
     * Test Case 1: Edit goal and have new name appear on the goal view.
     * Scenario: Go to goal view, select goal, edit name, save, and verify update.
     */
    @Test
    public void testEditGoalNameOnGoalView() {
        goToGoalsPage();
        // Select the preexisting goal (assumed to be labeled "Existing Goal")
        clickOn("Existing Goal");
        clickOn("#editButton");
        // Edit the name field: erase the old text and enter new name.
        clickOn("#nameField").eraseText(12).write("Updated Goal Name");
        clickOn("#saveButton");
        // Verify that the goal view label displays the updated name.
        Label nameLabel = lookup("#goalNameLabel").query();
        assertEquals("Updated Goal Name", nameLabel.getText(), "Goal name should be updated on the goal view.");
    }

    /**
     * Test Case 2: Edit goal and have new difficulty appear on the goal view.
     */
    @Test
    public void testEditGoalDifficultyOnGoalView() {
        goToGoalsPage();
        clickOn("#goalListView").clickOn("Existing Goal");
        clickOn("#editButton");
        // Edit the difficulty via a ComboBox (select "Hard").
        clickOn("#difficultyComboBox").clickOn("Hard");
        clickOn("#saveButton");
        Label difficultyLabel = lookup("#goalDifficultyLabel").query();
        assertEquals("Hard", difficultyLabel.getText(), "Goal difficulty should be updated on the goal view.");
    }

    /**
     * Test Case 3: Edit goal and have new section appear on the goal view.
     */
    @Test
    public void testEditGoalSectionOnGoalView() {
        goToGoalsPage();
        clickOn("#goalListView").clickOn("Existing Goal");
        clickOn("#editButton");
        // Edit the section.
        clickOn("#sectionField").eraseText(10).write("New Section");
        clickOn("#saveButton");
        Label sectionLabel = lookup("#goalSectionLabel").query();
        assertEquals("New Section", sectionLabel.getText(), "Goal section should be updated on the goal view.");
    }

    /**
     * Test Case 4: Edit goal and have new start time appear on the goal view.
     */
    @Test
    public void testEditGoalStartTimeOnGoalView() {
        goToGoalsPage();
        clickOn("#goalListView").clickOn("Existing Goal");
        clickOn("#editButton");
        // Set a new start date.
        DatePicker startDatePicker = lookup("#startDatePicker").query();
        interact(() -> startDatePicker.setValue(LocalDate.now().minusDays(5)));
        clickOn("#saveButton");
        Label startDateLabel = lookup("#goalStartDateLabel").query();
        assertEquals(LocalDate.now().minusDays(5).toString(), startDateLabel.getText(),
                "Start time should be updated on the goal view.");
    }

    /**
     * Test Case 5: Edit failed goal and have new end time appear on the goal view, changing the colour.
     */
    @Test
    public void testEditGoalEndTimeAndColorOnGoalView() {
        goToGoalsPage();
        clickOn("#goalListView").clickOn("Existing Goal");
        clickOn("#editButton");
        // Set the end date to a past date to simulate a past-due goal.
        DatePicker endDatePicker = lookup("#endDatePicker").query();
        interact(() -> endDatePicker.setValue(LocalDate.now().minusDays(1)));
        clickOn("#saveButton");
        Label endDateLabel = lookup("#goalEndDateLabel").query();
        assertEquals(LocalDate.now().minusDays(1).toString(), endDateLabel.getText(),
                "End time should be updated on the goal view.");
        // Verify that the colour style is updated (assumes that the style includes 'black' when updated).
        String style = endDateLabel.getStyle();
        assertTrue(style.contains("black"), "Goal end time color should change from red to black when edited.");
    }

    /**
     * Test Case 9: Being able to complete a goal.
     */
    @Test
    public void testCompleteGoal() {
        goToGoalsPage();
        clickOn("#goalListView").clickOn("Existing Goal");
        // Simulate clicking the complete button.
        clickOn("#completeButton");
        Label statusLabel = lookup("#goalStatusLabel").query();
        // Verify that the goal status is updated to "Completed" and that the style includes green text.
        assertEquals("Completed", statusLabel.getText(), "Goal status should indicate completion.");
        String style = statusLabel.getStyle();
        assertTrue(style.contains("green"), "Goal completion text should be green.");
    }

    /**
     * Test Case 10: Being able to cancel changes.
     */
    @Test
    public void testCancelGoalEdit() {
        goToGoalsPage();
        clickOn("#goalListView").clickOn("Existing Goal");
        clickOn("#editButton");
        // Store original name.
        TextField nameField = lookup("#nameField").query();
        String originalName = nameField.getText();
        // Make a temporary change.
        clickOn("#nameField").eraseText(originalName.length()).write("Temporary Change");
        clickOn("#cancelButton");
        // Verify that the goal view still shows the original name.
        Label nameLabel = lookup("#goalNameLabel").query();
        assertEquals(originalName, nameLabel.getText(), "Goal should remain unchanged after cancelling the edit.");
    }

    // ---------------------------------------------------
    // Graphs Update Editing Tests (Test Cases 6 to 8)
    // ---------------------------------------------------

    /**
     * Test Case 6: Edit goal and have new name appear on the graphs.
     */
    @Test
    public void testEditGoalNameOnGraphs() {
        // First, edit the goal name on the goal view.
        goToGoalsPage();
        clickOn("#goalListView").clickOn("Existing Goal");
        clickOn("#editButton");
        clickOn("#nameField").eraseText(12).write("Graph Updated Name");
        clickOn("#saveButton");
        // Then, navigate to the Goal Visuals page.
        goToGoalVisualsPage();
        Label graphNameLabel = lookup("#graphGoalNameLabel").query();
        assertEquals("Graph Updated Name", graphNameLabel.getText(), "Graph should display updated goal name.");
    }

    /**
     * Test Case 7: Edit goal and have new start time appear on the graphs.
     */
    @Test
    public void testEditGoalStartTimeOnGraphs() {
        goToGoalsPage();
        clickOn("#goalListView").clickOn("Existing Goal");
        clickOn("#editButton");
        DatePicker startDatePicker = lookup("#startDatePicker").query();
        interact(() -> startDatePicker.setValue(LocalDate.now().minusDays(7)));
        clickOn("#saveButton");
        goToGoalVisualsPage();
        Label graphStartDateLabel = lookup("#graphStartDateLabel").query();
        assertEquals(LocalDate.now().minusDays(7).toString(), graphStartDateLabel.getText(),
                "Graph should display updated start time.");
    }

    /**
     * Test Case 8: Edit goal and have new end time appear on the graphs.
     */
    @Test
    public void testEditGoalEndTimeOnGraphs() {
        goToGoalsPage();
        clickOn("#goalListView").clickOn("Existing Goal");
        clickOn("#editButton");
        DatePicker endDatePicker = lookup("#endDatePicker").query();
        interact(() -> endDatePicker.setValue(LocalDate.now().plusDays(10)));
        clickOn("#saveButton");
        goToGoalVisualsPage();
        Label graphEndDateLabel = lookup("#graphEndDateLabel").query();
        assertEquals(LocalDate.now().plusDays(10).toString(), graphEndDateLabel.getText(),
                "Graph should display updated end time.");
    }
}
