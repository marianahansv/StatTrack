package com.example.cmpt370project;

/**
 * Represents a goal plan where the user's goal is to increase the number of goals they complete.
 */
public class IncreaseGoalPlan implements GoalPlan{

    /**
     * The current number of goals the user is to be completing.
     */
    private int currentGoalNumber;

    /**
     * The eventual number of goals the user wants to get to completing.
     */
    private int maxGoalNumber;

    /**
     * Create this goal plan.
     * @param currentGoalNumber current number of goals the user is to be completing.
     * @param maxGoalNumber eventual number of goals the user wants to get to completing.
     */
    public IncreaseGoalPlan(int currentGoalNumber, int maxGoalNumber) {
        this.currentGoalNumber = currentGoalNumber;
        this.maxGoalNumber = maxGoalNumber;
    }

    @Override
    public String getPlanName() {
        return "Increase";
    }

    @Override
    public int getGoalPlanCurrent() {
        return currentGoalNumber;
    }

    @Override
    public int getGoalPlanMax() {
        return maxGoalNumber;
    }

    @Override
    public void setGoalPlanCurrent(int goalPlanCurrent) {
        this.currentGoalNumber = goalPlanCurrent;
    }

    @Override
    public boolean canEditMax() {
        return true;
    }

    /**
     * Set the goals the user want to get to maintaining.
     * @param maxGoalNumber the number of goals the user want to get to maintaining.
     */
    public void setMaxGoalNumber(int maxGoalNumber) {
        this.maxGoalNumber = maxGoalNumber;
    }
}
