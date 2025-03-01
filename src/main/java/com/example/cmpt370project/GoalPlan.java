package com.example.cmpt370project;

/**
 * An interface to represent a specific goal plan for the system.
 */
public interface GoalPlan {

    String getPlanName();
    int getGoalPlanCurrent();
    int getGoalPlanMax();
    void setGoalPlanCurrent(int goalPlanCurrent);
    boolean canEditMax();

}
