package com.example.cmpt370project;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import java.util.List;
import java.util.ArrayList;

public class GoalController {
    private GoalModel goalModel;
    private GoalView goalView;

    public GoalController(GoalModel goalModel, GoalView goalView) {
        this.goalModel = goalModel;
        this.goalView = goalView;
        // Subscribe the view to model changes if necessary
        goalModel.addSubscriber(goalView);
        setupEvents();
    }

    /**
     * Set up event handlers, for example, filtering by difficulty.
     */
    private void setupEvents() {
        // Assume GoalView has a public getter for the difficulty ComboBox.
        ComboBox<String> difficultyComboBox = goalView.getDifficultyComboBox();

        // When the difficulty selection changes, update the goal list.
        difficultyComboBox.setOnAction((ActionEvent e) -> {
            String selectedDifficulty = difficultyComboBox.getValue();
            List<Goal> filteredGoals = goalModel.getGoalsByDifficulty(selectedDifficulty);
            List<String> goalStrings = new ArrayList<>();
            for (Goal goal : filteredGoals) {
                goalStrings.add(goal.toString());
            }
            goalView.updateGoalList(goalStrings);
        });
    }

    // Other methods to handle add, delete, update events can be added here
}
