package com.example.cmpt370project;

/**
 * An interface to represent the allowed operations for a goal plan for the system.
 */
public interface IGoalPlan {

    /**
     * Get the name of this goal plan.
     * @return the name of this goal plan.
     */
    String getPlanName();

    /**
     * Get the current number of goals the user should be working towards completing for this plan.
     * @return the current number of goals the user should be working towards completing for this plan.
     */
    int getGoalPlanCurrent();

    /**
     * Get the maximum number of goals the user can work towards completing for this plan.
     * @return the maximum number of goals the user can work towards completing for this plan.
     */
    int getGoalPlanMax();

    /**
     * Set the current number of goals the user should be working towards completing for this plan.
     * @param goalPlanCurrent the new current number of goals the user should be working towards.
     */
    void setGoalPlanCurrent(int goalPlanCurrent);

    /**
     * Set the maximum number of goals the user can work towards completing for this plan.
     * @param goalPlanMax the new maximum number of goals the user can work towards.
     * @throws UnsupportedOperationException if !canEditMax()
     */
    void setGoalPlanMax(int goalPlanMax) throws UnsupportedOperationException ;

    /**
     * Can the maximum number of goals the user can work towards be changed for this plan?
     * @return true if it can be edited, false otherwise.
     */
    boolean canEditMax();

}
