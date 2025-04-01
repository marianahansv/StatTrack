package com.example.cmpt370project;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

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
     * The current number of goals the user is to be completing (with partial decimal values).
     */
    private double currGoalNumberDouble;

    /**
     * The eventual number of goals the user wants to get to completing.
     */
    private int maxGoalNumber;

    /**
     * Intermediary dates to use for calculation of incrementing goal target.
     */
    private LocalDate startDate, nextIncrementDate;

    /**
     * The date at which the increase plan will end (target should be reached at this time).
     */
    private LocalDate endDate;

    /**
     * The amount that the target goal number will be continually incremented.
     */
    private double goalNumberIncrement;

    /**
     * Create this goal plan.
     * @param currentGoalNumber current number of goals the user is to be completing.
     * @param maxGoalNumber eventual number of goals the user wants to get to completing.
     */
    public IncreaseGoalPlan(int currentGoalNumber, int maxGoalNumber, Timeline timeline, LocalDate endDate) {
        this.currentGoalNumber = currentGoalNumber;
        this.currGoalNumberDouble = currentGoalNumber;
        this.maxGoalNumber = maxGoalNumber;
        this.timeline = timeline;
        this.endDate = endDate;

        setupIncreaseLogic();
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

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the intermediary dates and goal increment number.
     */
    private void setupIncreaseLogic() {
        startDate = LocalDate.now();

        switch (timeline) {
            case DAILY -> {
                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
                goalNumberIncrement = (double) (maxGoalNumber - currentGoalNumber) / daysBetween;

                nextIncrementDate = startDate.plusDays(1);
            }

            case WEEKLY -> {
                LocalDate startSun = startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                LocalDate endSun = endDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

                long weeksBetween = ChronoUnit.WEEKS.between(startSun, endSun);
                goalNumberIncrement = (double) (maxGoalNumber - currentGoalNumber) / weeksBetween;

                nextIncrementDate = startSun.plusWeeks(1);
            }
        }
    }

    @Override
    public void syncGoalPlanToNow() {
        syncGoalPlanToDate(LocalDate.now());
    }

    @Override
    public void syncGoalPlanToDate(LocalDate date) {

        LocalDate currDate = date;

        while ((currDate.isEqual(nextIncrementDate) || currDate.isAfter(nextIncrementDate)) && (nextIncrementDate.isBefore(endDate) || nextIncrementDate.isEqual(endDate))) {

            switch (timeline) {
                case DAILY -> {
                    nextIncrementDate = nextIncrementDate.plusDays(1); // always will be set to the next sunday
                    currGoalNumberDouble += goalNumberIncrement;
                }

                case WEEKLY -> {
                    nextIncrementDate = nextIncrementDate.plusWeeks(1); // always will be set to the next sunday
                    currGoalNumberDouble += goalNumberIncrement;
                }
            }
        }

        currentGoalNumber = (int) Math.round(currGoalNumberDouble);
    }

    @Override
    public boolean isPlanFinished() {
        return LocalDate.now().isAfter(endDate) || LocalDate.now().equals(endDate);
    }

    @Override
    public boolean isPlanFinished(LocalDate date) {
        return date.isAfter(endDate) || date.equals(endDate);
    }

    @Override
    public LocalDate getPlanStartDate() {
        return startDate;
    }

    @Override
    public LocalDate getPlanEndDate() {
        return endDate;
    }

    @Override
    public boolean hasEndDate() {
        return true;
    }

    /**
     * Get the next increment date.
     * @return the next day that the plan will increment.
     */
    public LocalDate getNextIncrementDate() {
        return nextIncrementDate;
    }

    /**
     * Get the new current goal number to completing at the next increment date.
     * @return the new current goal number to completing at the next increment date.
     */
    public int getNextIncrementValue() {
        return (int) Math.round(currGoalNumberDouble + goalNumberIncrement);
    }

    /**
     * Unit testing for increase plan increment logic.
     */
    public static void main(String[] args) {

        // Note: Future plan of dev is to update this test script to it won't 'expire' and become invalid as time moves on

        // Test 1: setUpIncreaseLogic() with weekly plan (called in constructor)
        IncreaseGoalPlan testGoalPlan = new IncreaseGoalPlan(3, 6, Timeline.WEEKLY, LocalDate.of(2025, 04, 3));

        if (testGoalPlan.goalNumberIncrement != 0.75 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 03, 9))) {
            System.out.println("Test 1: Error in setup of goal increment logic.");
        }

        // Test 2: syncGoalPlanToDate with weekly plan
        testGoalPlan.syncGoalPlanToDate(LocalDate.of(2025, 03, 9));

        if (testGoalPlan.currGoalNumberDouble != 3.75 || testGoalPlan.currentGoalNumber != 4 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 03, 16))) {
            System.out.println("Test 2: Error in syncing goal plan to a week later.");
        }

        testGoalPlan.syncGoalPlanToDate(LocalDate.of(2025, 03, 27));

        if (testGoalPlan.currGoalNumberDouble != 5.25 || testGoalPlan.currentGoalNumber != 5 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 03, 30))) {
            System.out.println("Test 2: Error in syncing goal plan to more than a week later.");
        }

        testGoalPlan.syncGoalPlanToDate(LocalDate.of(2025, 04, 3));

        if (testGoalPlan.currGoalNumberDouble != 6 || testGoalPlan.currentGoalNumber != 6 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 04, 6))) {
            System.out.println("Test 2: Error in syncing goal plan to last day.");
        }

        testGoalPlan.syncGoalPlanToDate(LocalDate.of(2025, 04, 14));

        if (testGoalPlan.currGoalNumberDouble != 6 || testGoalPlan.currentGoalNumber != 6 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 04, 6))) {
            System.out.println("Test 2: Error in syncing goal plan beyond last day.");
        }

        // Test 3: setUpIncreaseLogic() with daily plan (called in constructor)
        testGoalPlan = new IncreaseGoalPlan(1, 10, Timeline.DAILY, LocalDate.of(2025, 03, 15));

        if (Math.round(testGoalPlan.goalNumberIncrement) != Math.round(9/7) || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 03, 9))) {
            System.out.println("Test 3: Error in setup of goal increment logic.");
        }

        // Test 4: syncGoalPlanToDate with daily plan

        testGoalPlan.syncGoalPlanToDate(LocalDate.of(2025, 03, 9));

        if (testGoalPlan.currentGoalNumber != 2 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 03, 10))) {
            System.out.println("Test 4: Error in syncing goal plan to a day later.");
        }

        testGoalPlan.syncGoalPlanToDate(LocalDate.of(2025, 03, 13));

        if (testGoalPlan.currentGoalNumber != 7 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 03, 14))) {
            System.out.println("Test 4: Error in syncing goal plan to more than a day later.");
        }

        testGoalPlan.syncGoalPlanToDate(LocalDate.of(2025, 03, 13));

        if (testGoalPlan.currentGoalNumber != 7 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 03, 14))) {
            System.out.println("Test 4: Error in syncing goal plan more than once for same day.");
        }

        testGoalPlan.syncGoalPlanToDate(LocalDate.of(2025, 03, 15));

        if (testGoalPlan.currGoalNumberDouble != 10|| testGoalPlan.currentGoalNumber != 10 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 03, 16))) {
            System.out.println("Test 4: Error in syncing goal plan to last day.");
        }

        testGoalPlan.syncGoalPlanToDate(LocalDate.of(2025, 04, 16));

        if (testGoalPlan.currGoalNumberDouble != 10 || testGoalPlan.currentGoalNumber != 10 || !testGoalPlan.nextIncrementDate.equals(LocalDate.of(2025, 03, 16))) {
            System.out.println("Test 4: Error in syncing goal plan beyond last day.");
        }

        System.out.println("Tests Completed.");
    }
}
