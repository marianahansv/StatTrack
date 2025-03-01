package com.example.cmpt370project;

import java.util.ArrayList;
import java.util.List;

/**
 * The GoalPlanModel holds all the GoalPlan data in the application.
 */
public class GoalPlanModel {

    /**
     * The current goalPlan the user has setup
     */
    private GoalPlan goalPlan;

    /**
     * The subscriber list (i.e. the view), which will update when the view changes.
     */
    private List<Subscriber> subscribers;

    public GoalPlanModel() {
        subscribers = new ArrayList<Subscriber>();
    }

    public boolean goalPlanExists() {
        return goalPlan != null;
    }

    public void setGoalPlan(GoalPlan goalPlan) {
        this.goalPlan = goalPlan;
        notifySubscribers();
    }

    public GoalPlan getGoalPlan() {
        return goalPlan;
    }

    public void setGoalPlanCurrent(int goalPlanCurrent) throws IllegalStateException {
        if (goalPlanExists()) {
            this.goalPlan.setGoalPlanCurrent(goalPlanCurrent);
            notifySubscribers();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }

    }

    public void setGoalPlanMax(int goalPlanMax) throws IllegalStateException {
        if (goalPlanExists()) {
            this.goalPlan.setGoalPlanMax(goalPlanMax);
            notifySubscribers();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }

    }

    public int getGoalPlanCurrent() throws IllegalStateException {
        if (goalPlanExists()) {
            return this.goalPlan.getGoalPlanCurrent();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }
    }

    public int getGoalPlanMax() {
        if (goalPlanExists()) {
            return this.goalPlan.getGoalPlanMax();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }
    }

    /**
     * Notify the subscribers of this model that the data has changed.
     */
    public void notifySubscribers() {
        subscribers.forEach(Subscriber::modelUpdated);
    }

    public static void main(String[] args) {

        GoalPlanModel goalPlanModel = new GoalPlanModel();

        // *************************************** UNIT TESTING ***************************************

        // Test 1: Plan Exists
        if (goalPlanModel.goalPlanExists()) {
            System.out.println("Test 1 Error: Goal Plan should not exist");
        }

        // Test 2: Set current goal number on empty plan
        try {
            goalPlanModel.setGoalPlanCurrent(10);
            System.out.println("Test 2 Error: Exception expected on calling setter for null goal plan.");
        } catch (IllegalStateException e) {
            // Expected
        } catch (Exception e) {
            System.out.println("Test 2 Error: Unexpected on calling setter for null goal plan.");
        }

        // Test 3: Set max goal number on empty plan
        try {
            goalPlanModel.setGoalPlanMax(10);
            System.out.println("Test 3 Error: Exception expected on calling setter for null goal plan.");
        } catch (IllegalStateException e) {
            // Expected
        } catch (Exception e) {
            System.out.println("Test 3 Error: Unexpected on calling setter for null goal plan.");
        }

        // Test 4: Get current goal number on empty plan
        try {
            goalPlanModel.getGoalPlanCurrent();
            System.out.println("Test 4 Error: Exception expected on calling getter for null goal plan.");
        } catch (IllegalStateException e) {
            // Expected
        } catch (Exception e) {
            System.out.println("Test 4 Error: Unexpected on calling getter for null goal plan.");
        }

        // Test 5: Get max goal number on empty plan
        try {
            goalPlanModel.getGoalPlanMax();
            System.out.println("Test 5 Error: Exception expected on calling getter for null goal plan.");
        } catch (IllegalStateException e) {
            // Expected
        } catch (Exception e) {
            System.out.println("Test 5 Error: Unexpected on calling getter for null goal plan.");
        }

        // Test 6: Get goal plan on empty plan
        if (goalPlanModel.getGoalPlan() != null) {
            System.out.println("Test 6 Error: Expected null on calling getter for null goal plan.");
        }

        // Test 7: Set goal plan
        GoalPlan mgp = new MaintainGoalPlan(10);
        goalPlanModel.setGoalPlan(mgp);

        if (!goalPlanModel.goalPlanExists()) {
            System.out.println("Test 7 Error: Goal Plan was not created.");
        }

        System.out.println("Unit Tests Complete.");

    }
}
