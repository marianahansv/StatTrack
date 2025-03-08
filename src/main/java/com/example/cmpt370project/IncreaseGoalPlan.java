package com.example.cmpt370project;

/**
 * Represents a goal plan where the user's goal is to increase the number of goals they complete.
 */
public class IncreaseGoalPlan implements IGoalPlan {

    /**
     * How is this goal plan progress measured?
     */
    private Timeline timeline;

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
    public IncreaseGoalPlan(int currentGoalNumber, int maxGoalNumber, Timeline timeline) {
        this.currentGoalNumber = currentGoalNumber;
        this.maxGoalNumber = maxGoalNumber;
        this.timeline = timeline;
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
    public void setGoalPlanMax(int goalPlanMax) throws UnsupportedOperationException {
        if (canEditMax()) {
            this.maxGoalNumber = goalPlanMax;
        } else {
            throw new UnsupportedOperationException("You can not change the maximum goal number for this goal plan.");
        }
    }

    @Override
    public boolean canEditMax() {
        return true;
    }

    @Override
    public String toString() {
        return "Goal Plan Type: " + getClass().getSimpleName() +
                " Current Goal Number: " + currentGoalNumber +
                " Max Goal Number: " + maxGoalNumber;
    }

    @Override
    public Timeline getTimeline() {
        return timeline;
    }
}
