package com.example.cmpt370project;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Handles changing GoalPlanModel according to the users actions.
 */
public class GoalPlanController {

    /**
     * The GoalPlanModel that this controller modifies.
     */
    private GoalPlanModel goalPlanModel;

    public GoalPlanController() {}

    /**
     * Set the model of this controller.
     * @param goalPlanModel the GoalPlanModel that this controller will modify.
     */
    public void setModel(GoalPlanModel goalPlanModel) {
        this.goalPlanModel = goalPlanModel;
    }

    /**
     * Handle updating the user's Goal Plan when they edit/create a Maintain Goal Plan.
     * @param inputEndGoalNumber the input number of goals that the user wants to maintain on the regular.
     */
    public void handleSaveMaintainGoalPlan(int inputEndGoalNumber, IGoalPlan.Timeline timeline) {
        MaintainGoalPlan newPlan = new MaintainGoalPlan(inputEndGoalNumber, timeline);
        goalPlanModel.setGoalPlan(newPlan);
    }

    /**
     * Handle updating the user's Goal Plan when they edit/create an Increase Goal Plan.
     * @param inputEndGoalNumber the current number of goals the user will be completing.
     * @param inputStartGoalNumber the eventual number of goals the user wants to get to completing.
     */
    public void handleSaveIncreaseGoalPlan(int inputEndGoalNumber, int inputStartGoalNumber, IGoalPlan.Timeline timeline, LocalDate endDate) {
        IncreaseGoalPlan newPlan = new IncreaseGoalPlan(inputStartGoalNumber, inputEndGoalNumber, timeline, endDate);
        goalPlanModel.setGoalPlan(newPlan);
    }

    /**
     * Handle deleting the user's Goal Plan.
     */
    public boolean handleDeleteGoalPlan() {

        // Create an alert of type CONFIRMATION
        AtomicBoolean planDeleted = new AtomicBoolean(false);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Goal Plan");
        alert.setHeaderText("Are you sure you want to delete your goal plan?");
        alert.setContentText("This action cannot be undone.");

        // Show the dialog and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                goalPlanModel.clearGoalPlan();
                planDeleted.set(true);
            }
        });
        return planDeleted.get();
    }

}
