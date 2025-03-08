package com.example.cmpt370project;

/**
 * Represents a goal plan where the user's goal is to maintain a consistent number of goals.
 */
public class MaintainGoalPlan implements IGoalPlan {

    /**
     * How is this goal plan progress measured?
     */
    private Timeline timeline;

    /**
     * The number of goals the user wants to be complete on the regular.
     */
    private int goalMaintenanceNumber;

    /**
     * Create this goal plan.
     * @param goalMaintenanceNumber The number of goals the user wants to maintain on the regular.
     */
    public MaintainGoalPlan(int goalMaintenanceNumber, Timeline timeline) {
        this.goalMaintenanceNumber = goalMaintenanceNumber;
        this.timeline = timeline;
    }

    @Override
    public String getPlanName() {
        return "Maintain";
    }

    @Override
    public int getGoalPlanCurrent() {
        return goalMaintenanceNumber;
    }

    @Override
    public int getGoalPlanMax() {
        return goalMaintenanceNumber;
    }

    @Override
    public void setGoalPlanCurrent(int goalPlanCurrent) {
        this.goalMaintenanceNumber = goalPlanCurrent;
    }

    @Override
    public void setGoalPlanMax(int goalPlanMax) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("You can not change the maximum goal number for this goal plan.");
    }

    @Override
    public boolean canEditMax() {
        return false;
    }

    @Override
    public String toString() {
        return "Goal Plan Type: " + getClass().getSimpleName() +
                " Goals to Maintain: " + goalMaintenanceNumber;
    }

    @Override
    public Timeline getTimeline() {
        return timeline;
    }
}
