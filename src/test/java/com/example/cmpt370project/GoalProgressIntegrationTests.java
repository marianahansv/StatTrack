package com.example.cmpt370project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Integration tests for the GoalProgress view.
 */
public class GoalProgressIntegrationTests extends ApplicationTest {

    private GoalModel testModel;
    private GoalProgress goalProgressView;
    public UserHistoryDataModel UH;

    /**
     * A simple dummy goal model to supply sample data.
     */
    private static class TestGoalModel extends GoalModel {
        private List<Goal> goals = new ArrayList<>();

        public TestGoalModel(UserHistoryDataModel UH) {
            super(UH);
            // Add two sample goals:
            // One that is due today and one that is due in the future.
            goals.add(new Goal("Due Today Goal", LocalDate.now().minusDays(1), LocalDate.now()));
            goals.add(new Goal("Future Goal", LocalDate.now(), LocalDate.now().plusDays(5)));
        }

        @Override
        public List<Goal> getGoals() {
            return goals;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Instantiate our dummy model and the view that uses it.
        testModel = new TestGoalModel(UH);
        goalProgressView = new GoalProgress(testModel);
        VBox root = new VBox(goalProgressView);
        stage.setScene(new javafx.scene.Scene(root, 800, 600));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // Ensure the view is updated before each test.
        interact(() -> goalProgressView.updateChart());
    }

    /**
     * Test that the default chart type is "Pie Chart" and that for each goal a PieChart is added.
     */
    @Test
    public void testDefaultChartTypeShowsPieCharts() {
        // Since the default selection is "Pie Chart", the updateChart method should add a PieChart per goal.
        // The view's children include the ChoiceBox and all added charts.
        long pieChartCount = goalProgressView.getChildren().stream()
                .filter(node -> node instanceof PieChart)
                .count();
        // Expect at least as many PieCharts as there are goals in the model.
        assertTrue(pieChartCount >= testModel.getGoals().size(), 
                "Default chart type should produce a PieChart for each goal.");
    }

    /**
     * Test switching to Bar Chart using the ChoiceBox.
     */
    @Test
    public void testSwitchToBarChart() {
        // Lookup the ChoiceBox for chart type selection.
        ChoiceBox<String> chartTypeSelector = (ChoiceBox<String>) lookup(".choice-box").query();
        assertNotNull(chartTypeSelector, "Chart type selector should be present.");

        // Change the selection to "Bar Chart" and simulate an action.
        interact(() -> chartTypeSelector.setValue("Bar Chart"));
        interact(() -> goalProgressView.updateChart());

        // Verify that a BarChart is now present among the children.
        Node barChart = goalProgressView.getChildren().stream()
                .filter(node -> node instanceof BarChart)
                .findFirst().orElse(null);
        assertNotNull(barChart, "BarChart should be displayed when 'Bar Chart' is selected.");
    }

    /**
     * Test switching to Line Chart using the ChoiceBox.
     */
    @Test
    public void testSwitchToLineChart() {
        ChoiceBox<String> chartTypeSelector = (ChoiceBox<String>) lookup(".choice-box").query();
        assertNotNull(chartTypeSelector, "Chart type selector should be present.");

        interact(() -> chartTypeSelector.setValue("Line Chart"));
        interact(() -> goalProgressView.updateChart());

        Node lineChart = goalProgressView.getChildren().stream()
                .filter(node -> node instanceof LineChart)
                .findFirst().orElse(null);
        assertNotNull(lineChart, "LineChart should be displayed when 'Line Chart' is selected.");
    }
}
