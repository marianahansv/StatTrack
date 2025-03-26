package com.example.cmpt370project;


import java.time.LocalDate;

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
    public void handleDeleteGoalPlan() {
        goalPlanModel.clearGoalPlan();
    }

}
