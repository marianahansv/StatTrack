package com.example.cmpt370project;

/**
 * An interface to represent a specific goal plan for the system.
 */
public interface GoalPlan {

    String getPlanName();
    int getGoalPlanCurrent();
    int getGoalPlanMax();
    void setGoalPlanCurrent(int goalPlanCurrent);
    void setGoalPlanMax(int goalPlanMax) throws UnsupportedOperationException ;
    boolean canEditMax();

}
