package com.example.cmpt370project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuggestionsModel {
    private List<Goal> goals = new ArrayList<>();
    private int timelineSuggestionPlusMinusDays = 5; // +-10 days in completion calculations
    public SuggestionsModel(){}
    public void initializeSuggestionsModel(List<Goal> goals){
        this.goals = goals;
    }
    public void updateGoalList(List<Goal> goals){
        this.goals = goals;
    }
    /**
    * Generate suggestion based on difficulty!
    * **/
    public String getDifficultySuggestion(Goal new_goal) {
        // Count completed goals by difficulty
        Map<String, Long> difficultyCounters = goals.stream()
                .filter(Goal::isCompleted)
                .collect(Collectors.groupingBy(Goal::getDifficulty, Collectors.counting()));

        long easyGoals = difficultyCounters.getOrDefault("easy", 0L);
        long mediumGoals = difficultyCounters.getOrDefault("medium", 0L);
        long hardGoals = difficultyCounters.getOrDefault("hard", 0L);

        // Count goals by early, on-time, over-time based on actual dates
        long earlyGoals = goals.stream().filter(goal -> calculateDifferenceInDates(goal) < 0).count();
        long ontimeGoals = goals.stream().filter(goal -> calculateDifferenceInDates(goal) == 0).count();
        long overtimeGoals = goals.stream().filter(goal -> calculateDifferenceInDates(goal) > 0).count();

        // Calculate user tendencies for early, on-time, overtime (just like "average behavior")
        String mostCommonCompletionStatus = getMostCommonCompletionStatus(earlyGoals, ontimeGoals, overtimeGoals);

        // Compare new goal to past data and generate suggestions
        String newGoalDifficulty = new_goal.getDifficulty();

        // If user matches their past behavior, encourage them
        if (newGoalDifficulty.equals(getMostCommonDifficulty(easyGoals, mediumGoals, hardGoals))) {
            return "Based on your history, this goal is perfect for you! Keep it up :)";
        }

        // Handle specific cases based on goal completion patterns
        if (mostCommonCompletionStatus.equals("overtime")) {
            return handleOvertimeGoals(easyGoals, mediumGoals, hardGoals);
        } else if (mostCommonCompletionStatus.equals("early")) {
            return handleEarlyGoals(easyGoals, mediumGoals, hardGoals);
        }

        // Default message if no clear pattern is found
        return "Your goal seems unique. Keep going and refine your strategy as you go!";
    }

    private String getMostCommonCompletionStatus(long earlyGoals, long ontimeGoals, long overtimeGoals) {
        if (earlyGoals >= Math.max(ontimeGoals, overtimeGoals)) return "early";
        else if (ontimeGoals >= Math.max(earlyGoals, overtimeGoals)) return "ontime";
        else return "overtime";
    }

    private String getMostCommonDifficulty(long easyGoals, long mediumGoals, long hardGoals) {
        if (easyGoals >= mediumGoals && easyGoals >= hardGoals) return "easy";
        else if (mediumGoals >= easyGoals && mediumGoals >= hardGoals) return "medium";
        else return "hard";
    }

    private String handleOvertimeGoals(long easyGoals, long mediumGoals, long hardGoals) {
        // Suggestions for users who tend to finish goals late
        if (easyGoals > mediumGoals && easyGoals > hardGoals) {
            return "You tend to delay easy goals. Try to group them into bigger tasks!";
        } else if (mediumGoals > easyGoals && mediumGoals > hardGoals) {
            return "You often delay medium goals. Consider breaking them down into smaller chunks!";
        } else {
            return "You often delay hard goals. Break them down into smaller tasks for better success!";
        }
    }

    private String handleEarlyGoals(long easyGoals, long mediumGoals, long hardGoals) {
        // Suggestion for early goal completions
        if (easyGoals > mediumGoals && easyGoals > hardGoals) {
            return "You tend to finish easy goals too quickly! Consider aiming for more challenging goals!";
        } else if (mediumGoals > easyGoals && mediumGoals > hardGoals) {
            return "You finish medium goals early! Time to take on some harder goals!";
        } else {
            return "You finish hard goals early! Maybe try making them even bigger or more complex!";
        }
    }

    /**
     * Generate suggestion based on timeline!
     * **/
    public String getTimelineSuggestion(Goal new_goal){
        long thisGoalDuration = calculateScheduledDaysforGoalCompletion(new_goal);

        // Find similar goals based on a threshold of scheduled days
        List<Goal> similarGoals = goals.stream()
                .filter(goal -> Math.abs(calculateScheduledDaysforGoalCompletion(goal) - thisGoalDuration) <= timelineSuggestionPlusMinusDays)
                .toList();

        // Find all the differences in the actual completion dates
        List<Long> allCompletionDifferences = similarGoals.stream().map(this::calculateDifferenceInDates).toList();

        // Calculate the average completion difference
        long averageDays;
        if (allCompletionDifferences.isEmpty()) {
            averageDays = 0;
        } else {
            averageDays = (allCompletionDifferences.stream().mapToLong(Long::longValue).sum()) / allCompletionDifferences.size();
        }

        System.out.println(averageDays);
        // Generate the suggestion based on the average difference
        if (Math.abs(averageDays) < -3) {
            return "Based on your history, this deadline looks great! Keep it up!";
        } else if (averageDays > 0) {
            return "Based on your history, consider adding " + Math.ceil(averageDays) + " days to your deadline to give yourself more flexibility!";
        } else {
            // Avoid recommending subtracting time unless it's too easy
            if (averageDays < -5) {
                return "Based on your history, you might be able to subtract " + Math.abs(Math.ceil(averageDays)/2) + " days from your deadline. You’ve been completing goals early!";
            }
            return "Based on your history, your timeline seems to be working well. You might want to consider sticking to the current deadline!";
        }
    }

    /**
     * Helper method to calculate number of days scheduled for goal completion
     * (does NOT consider actual completion date)
     */
    private long calculateScheduledDaysforGoalCompletion(Goal goal){
        LocalDate newGoalStartDate = goal.getStartDate();
        LocalDate newGoalEndDate = goal.getEndDate();
        return ChronoUnit.DAYS.between(newGoalStartDate, newGoalEndDate);
    }

    /**
     * Helper method to calculate the difference in days between the scheduled deadline
     * and actual completion date.
     */
    private long calculateDifferenceInDates(Goal goal){
        if (goal.isCompleted()) {
            return ChronoUnit.DAYS.between(goal.getEndDate(), goal.getCompletionDate());
        }
        // If not completed yet, there's no difference
        return 0;
    }



    //unit testing (this is good to understand how it works!)


    public static void main(String[] args) {

        //Testing Timeline Suggestions
        // Goal 1: Completed early (suggest subtract days)
        Goal goal1 = new Goal("1", "a", "medium", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 10), true);
        goal1.setCompletionDate(LocalDate.of(2025, 3, 8));  // Completed 2 days early

        // Goal 2: Completed late (suggest add days)
        Goal goal2 = new Goal("2", "a", "medium", LocalDate.of(2025, 2, 5), LocalDate.of(2025, 2, 10), true);
        goal2.setCompletionDate(LocalDate.of(2025, 2, 15));  // Completed 5 days late

        // Goal 3: Not completed yet (no suggestion)
        Goal goal3 = new Goal("3", "c", "medium", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), false);

        // Goal 4: Completed on time (keep days the same)
        Goal goal4 = new Goal("4", "b", "medium", LocalDate.of(2025, 3, 2), LocalDate.of(2025, 3, 9), true);
        goal4.setCompletionDate(LocalDate.of(2025, 3, 9));  // Completed on time

        // Goal List 1: Goals where we want to **add days** to the timeline
        List<Goal> goalListAdd = new ArrayList<>();
        goalListAdd.add(goal2); // Goal 2 is completed late

        // Goal List 2: Goals where we want to **subtract days** from the timeline
        List<Goal> goalListSubtract = new ArrayList<>();
        goalListSubtract.add(goal1); // Goal 1 is completed early

        // Goal List 3: Goals where we want to **keep the same** timeline
        List<Goal> goalListKeep = new ArrayList<>();
        goalListKeep.add(goal4); // Goal 4 is completed on time

        // Initialize SuggestionsModel
        SuggestionsModel suggestionsModel = new SuggestionsModel();

        // Initialize goal lists into the SuggestionsModel
        suggestionsModel.initializeSuggestionsModel(goalListAdd);
        Goal newGoalAdd = new Goal("test1", "b", "medium", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), false);
        String timelineSuggestionAdd = suggestionsModel.getTimelineSuggestion(newGoalAdd);
        System.out.println("Add Days Suggestion: " + timelineSuggestionAdd);

        suggestionsModel.initializeSuggestionsModel(goalListSubtract);
        Goal newGoalSubtract = new Goal("test2", "b", "medium", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), false);
        String timelineSuggestionSubtract = suggestionsModel.getTimelineSuggestion(newGoalSubtract);
        System.out.println("Subtract Days Suggestion: " + timelineSuggestionSubtract);

        suggestionsModel.initializeSuggestionsModel(goalListKeep);
        Goal newGoalKeep = new Goal("test3", "b", "medium", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), false);
        String timelineSuggestionKeep = suggestionsModel.getTimelineSuggestion(newGoalKeep);
        System.out.println("Keep the Same Days Suggestion: " + timelineSuggestionKeep);

        //Testing Task Breakdown Suggestions
        // Test Case 1: User completes easy goals late
        goal1 = new Goal("1", "a", "easy", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 10), true);
        goal2 = new Goal("2", "a", "easy", LocalDate.of(2025, 2, 5), LocalDate.of(2025, 2, 10), true);
        goal3 = new Goal("3", "c", "medium", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), true);
        goal1.setCompletionDate(LocalDate.of(2025, 3, 15));  // Late
        goal2.setCompletionDate(LocalDate.of(2025, 2, 20));  // Late
        goal3.setCompletionDate(LocalDate.of(2025, 3, 18));  // Late

        List<Goal> goalList1 = new ArrayList<>();
        goalList1.add(goal1);
        goalList1.add(goal2);
        goalList1.add(goal3);

        SuggestionsModel suggestionsModel1 = new SuggestionsModel();
        suggestionsModel1.initializeSuggestionsModel(goalList1);

        Goal newGoal1 = new Goal("test1", "a", "easy", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), false);
        System.out.println("Test 1 Suggestion: " + suggestionsModel1.getDifficultySuggestion(newGoal1));

        // Test Case 2: User completes easy goals early
        goal4 = new Goal("4", "b", "easy", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 10), true);
        Goal goal5 = new Goal("5", "b", "easy", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10), true);
        Goal goal6 = new Goal("6", "d", "hard", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 10), true);
        goal4.setCompletionDate(LocalDate.of(2025, 3, 5));  // Early
        goal5.setCompletionDate(LocalDate.of(2025, 2, 5));  // Early
        goal6.setCompletionDate(LocalDate.of(2025, 3, 3));  // Early

        List<Goal> goalList2 = new ArrayList<>();
        goalList2.add(goal4);
        goalList2.add(goal5);
        goalList2.add(goal6);

        SuggestionsModel suggestionsModel2 = new SuggestionsModel();
        suggestionsModel2.initializeSuggestionsModel(goalList2);

        Goal newGoal2 = new Goal("test2", "d", "hard", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), false);
        System.out.println("Test 2 Suggestion: " + suggestionsModel2.getDifficultySuggestion(newGoal2));

        // Test Case 3: User completes all goals on time and new goal is medium
        Goal goal7 = new Goal("7", "e", "easy", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 10), true);
        Goal goal8 = new Goal("8", "f", "medium", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 10), true);
        Goal goal9 = new Goal("9", "g", "hard", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 10), true);
        goal7.setCompletionDate(LocalDate.of(2025, 3, 10));  // On time
        goal8.setCompletionDate(LocalDate.of(2025, 2, 10));  // On time
        goal9.setCompletionDate(LocalDate.of(2025, 1, 10));  // On time

        List<Goal> goalList3 = new ArrayList<>();
        goalList3.add(goal7);
        goalList3.add(goal8);
        goalList3.add(goal9);

        SuggestionsModel suggestionsModel3 = new SuggestionsModel();
        suggestionsModel3.initializeSuggestionsModel(goalList3);

        Goal newGoal3 = new Goal("test3", "g", "medium", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 20), false);
        System.out.println("Test 3 Suggestion: " + suggestionsModel3.getDifficultySuggestion(newGoal3));

        // Test Case 4: User completes medium and hard goals late, easy goals early
        Goal goal10 = new Goal("10", "h", "easy", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 10), true);
        Goal goal11 = new Goal("11", "i", "medium", LocalDate.of(2025, 2, 5), LocalDate.of(2025, 2, 15), true);
        Goal goal12 = new Goal("12", "j", "hard", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 10), true);
        goal10.setCompletionDate(LocalDate.of(2025, 3, 2));  // Early
        goal11.setCompletionDate(LocalDate.of(2025, 2, 20));  // Late
        goal12.setCompletionDate(LocalDate.of(2025, 1, 5));  // Late

        List<Goal> goalList4 = new ArrayList<>();
        goalList4.add(goal10);
        goalList4.add(goal11);
        goalList4.add(goal12);

        SuggestionsModel suggestionsModel4 = new SuggestionsModel();
        suggestionsModel4.initializeSuggestionsModel(goalList4);

        Goal newGoal4 = new Goal("test4", "h", "medium", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 15), false);
        System.out.println("Test 4 Suggestion: " + suggestionsModel4.getDifficultySuggestion(newGoal4));
    }

}

