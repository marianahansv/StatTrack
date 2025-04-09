package com.example.cmpt370project;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.matcher.control.ComboBoxMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
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
public class GoalViewIntegrationTests extends AppIntegrationTest {

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


    // Utility method: Navigate to the Goal View page.
    private void navigateToGoalView() {
        // Assume clicking a button takes you to the goal view.
        clickOn("#goalsButton");
    }

    // Utility method: Open the edit view for the selected goal.
    private void openEditForGoal() {
        clickOn("#editButton");
    }

    // Utility method: Save the changes made to the goal.
    private void saveGoal() {
        doubleClickOn("#saveGoalButton");
    }

    // Utility method: Navigate to the data visualization (graphs) page.
    private void navigateToDataVisualization() {
        clickOn("#goalVisButton");
    }

    // Test Case 1: Edit goal name and check new name on goal view.
    @Test
    public void testEditGoalName() {
        navigateToGoalView();
        clickOn(300, 180);
        openEditForGoal();
        clickOn("#goalNameField");

        doubleClickOn("#goalNameField");
        write("Text");
        saveGoal();

        navigateToGoalView();
        clickOn(300, 180);
        openEditForGoal();

        verifyThat("#goalNameField", TextInputControlMatchers.hasText("Text"));
    }

    // Test Case 2: Edit goal difficulty and check new difficulty on goal view.
    @Test
    public void testEditGoalDifficulty() {
        navigateToGoalView();
        clickOn(300, 180);
        openEditForGoal();
        clickOn("#goalDifficulty");
        eraseText(10);
        clickOn("Hard");
        saveGoal();

        clickOn(300, 180);
        openEditForGoal();
        clickOn("#goalDifficulty");
        verifyThat("#goalDifficulty",  ComboBoxMatchers.hasSelectedItem("Hard"));
    }

    // Test Case 3: Edit goal section and check new section on goal view.
    @Test
    public void testEditGoalSection() {
        navigateToGoalView();
        
        clickOn(300, 180);
        openEditForGoal();
        clickOn("#General");
        saveGoal();
        ListView<Goal> goalList = lookup(".list-view").query();
        ObservableList list = goalList.getItems();
        String string = list.toString();
        
        clickOn(300, 180);
        openEditForGoal();
        clickOn("#Personal");
        saveGoal();
        ObservableList list2 = goalList.getItems();
        String string2 = list2.toString();
        
        assertTrue(!string.equals(string2), "The goal section has changed");

    }

    // Test Case 4: Edit goal start time and check new start time on goal view.
    @Test
    public void testEditGoalStartTime() {
        navigateToGoalView();

        ListView<Goal> goalList = lookup(".list-view").query();
        ObservableList list = goalList.getItems();
        String string = list.toString();

        clickOn(300, 180);
        openEditForGoal();
        clickOn("#startDate");
        doubleClickOn("#startDate");
        write("4/5/2025");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        saveGoal();
        ListView<Goal> goalList2 = lookup(".list-view").query();
        ObservableList list2 = goalList2.getItems();
        String string2 = list2.toString();

        assertTrue(!string.equals(string2), "The goal start date has changed");
    }

    // Test Case 5: Edit goal end time
    @Test
    public void testEditGoalEndTime() {
        navigateToGoalView();

        ListView<Goal> goalList = lookup(".list-view").query();
        ObservableList list = goalList.getItems();
        String string = list.toString();

        clickOn(300, 180);
        openEditForGoal();
        clickOn("#endDate");
        doubleClickOn("#endDate");
        write("6/5/2025");
        saveGoal();

        ListView<Goal> goalList2 = lookup(".list-view").query();
        ObservableList list2 = goalList2.getItems();
        String string2 = list2.toString();

        assertTrue(!string.equals(string2), "The goal end date has changed");
    }

    // Test Case 6: Edit goal name and verify the change appears on graphs.
    @Test
    public void testEditGoalNameOnGraphs() {
        navigateToDataVisualization();
        
        ListView<Goal> goalList = lookup(".list-view").query();
        String list = goalList.getId();
        String string = list.toString();

        navigateToGoalView();
        clickOn(300, 180);
        openEditForGoal();
        clickOn("#goalNameField");
        eraseText(10);
        write("www");
        saveGoal();
        ListView<Node> goalList2 = lookup(".list-view").query();
        ObservableList<Node> list2 = goalList2.getItems();
        String string2 = list2.toString();


        assertTrue(!string.equals(string2), "The goal name visually has changed");
    }

    // Test Case 7: Edit goal start time and verify the change appears on graphs.
    @Test
    public void testEditGoalStartTimeOnGraphs() {
        navigateToDataVisualization();

        ListView<Goal> goalList = lookup(".list-view").query();
        ObservableList list = goalList.getItems();
        String string = list.toString();
        navigateToGoalView();
        clickOn(300, 180);
        openEditForGoal();

        clickOn("#startDate");
        doubleClickOn("#startDate");
        write("4/5/2025");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        saveGoal();
        ListView<Goal> goalList2 = lookup(".list-view").query();
        ObservableList list2 = goalList2.getItems();
        String string2 = list2.toString();

        assertTrue(!string.equals(string2), "The goal start date has changed");
    }

    // Test Case 8: Edit goal end time and verify the change appears on all graphs.
    @Test
    public void testEditGoalEndTimeOnGraphs() {
        navigateToDataVisualization();

        ListView<Goal> goalList = lookup(".list-view").query();
        ObservableList list = goalList.getItems();
        String string = list.toString();
        navigateToGoalView();
        clickOn(300, 180);
        openEditForGoal();

        clickOn("#endDate");
        doubleClickOn("#endDate");
        write("7/5/2025");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        saveGoal();
        ListView<Goal> goalList2 = lookup(".list-view").query();
        ObservableList list2 = goalList2.getItems();
        String string2 = list2.toString();

        assertTrue(!string.equals(string2), "The goal end date has changed");
    }

    // Test Case 9: Complete a goal and check that its completed status
    @Test
    public void testCompleteGoal() {
        navigateToGoalView();
        ListView<Goal> goalList = lookup(".list-view").query();
        ObservableList list = goalList.getItems();
        String string = list.toString();
        clickOn(300, 180);
        clickOn("#completeGoalButton");
        // Verify the label text changes to "Completed".
        ListView<Goal> goalList2 = lookup(".list-view").query();
        ObservableList list2 = goalList2.getItems();
        String string2 = list2.toString();

        assertTrue(!string.equals(string2), "The goal end date has changed");
        
    }

    // Test Case 10: Cancel changes during goal editing and verify that no changes are saved.
    @Test
    public void testCancelGoalEdit() {
        navigateToGoalView();
        ListView<Goal> goalList = lookup(".list-view").query();
        ObservableList list = goalList.getItems();
        String string = list.toString();
        clickOn(300, 180);
        openEditForGoal();
        clickOn("#goalNameField");
        eraseText(10);
        write("ShouldNotSave");
        // Instead of saving, cancel the edit.
        clickOn("#cancelButton");
        ListView<Goal> goalList2 = lookup(".list-view").query();
        ObservableList list2 = goalList2.getItems();
        String string2 = list2.toString();
        assertTrue(string.equals(string2), "The goal end date has changed");
    }
}
