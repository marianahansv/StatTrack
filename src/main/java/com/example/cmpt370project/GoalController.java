package com.example.cmpt370project;

public class GoalController {
    private GoalModel goalModel;
    private GoalView goalView;

    /**
     * Create a GoalController that manages interactions between the model and the view.
     */
    public GoalController(GoalModel goalModel, GoalView goalView) {
        this.goalModel = goalModel;
        this.goalView = goalView;
        goalModel.addSubscriber(goalView); // MAKES sure view updates when model changes
        goalView.setFilterChangeListener(this); // view notifies controller on change
    }

    /**
     * Controller updates the Model based on selected difficulty.
     * The Model will notify the View automatically.
     */
    public void filterGoals(String difficulty) {
        goalModel.setFilteredDifficulty(difficulty); // updates the model
    }

    /**
     * Compeletes goal and updates the Suggestions Model
     */
    public void completeGoal(Goal goal) {
        goalModel.completeGoal(goal);
    }

    /**
     * Deletes a goal from the model by its title. This removes the goal from:
     * @param title The title of the goal to be deleted
     * @return true if the goal was found and deleted successfully, false otherwise
     */
    public boolean deleteGoal(String title) {
        return goalModel.deleteGoal(title);
    }

}
