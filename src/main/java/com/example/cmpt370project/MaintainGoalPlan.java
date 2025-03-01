package com.example.cmpt370project;

/**
 * Represents a goal plan where the user's goal is to maintain a consistent number of goals.
 */
public class MaintainGoalPlan implements GoalPlan {

    /**
     * The number of goals the user wants to be complete on the regular.
     */
    private int goalMaintenanceNumber;

    /**
     * Create this goal plan.
     * @param goalMaintenanceNumber The number of goals the user wants to maintain on the regular.
     */
    public MaintainGoalPlan(int goalMaintenanceNumber) {
        this.goalMaintenanceNumber = goalMaintenanceNumber;
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

    // Probably should change the top class to abstract class that does these methods and attributes
    @Override
    public void setGoalPlanMax(int goalPlanMax) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("You can not change the maximum goal number for this goal plan.");
    }

    @Override
    public boolean canEditMax() {
        return false;
    }
}
