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
    private int timelineSuggestionPlusMinusDays = 10; // +-10 days in completion calculations
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
    public String getDifficultySuggestion(Goal new_goal){
        //count completed goals by difficulty!
        Map<String, Long> difficultyCounters = goals.stream().filter(Goal::isCompleted).collect(Collectors.groupingBy(Goal::getDifficulty,Collectors.counting()));
        long easyGoals = difficultyCounters.getOrDefault("easy", 0L);
        long mediumGoals = difficultyCounters.getOrDefault("medium", 0L);
        long hardGoals = difficultyCounters.getOrDefault("hard",0L);
        //count goals by early, ontime, overtime
        long earlyGoals = goals.stream().filter(goal-> calculateDifferenceInDates(goal) < 0).count();
        long ontimeGoals = goals.stream().filter(goal -> calculateDifferenceInDates(goal) == 0).count();
        long overtimeGoals = goals.stream().filter(goal -> calculateDifferenceInDates(goal) > 0).count();

        //get most common difficulty
        String mostCommonDifficulty;
        if (easyGoals>=mediumGoals && easyGoals >= hardGoals) mostCommonDifficulty = "easy";
        if (mediumGoals >= easyGoals && mediumGoals >= hardGoals) mostCommonDifficulty = "medium";
        else mostCommonDifficulty = "hard";

        //get most common completion
        int mostCommonCompletion = (int) Math.max(earlyGoals,Math.max(ontimeGoals,overtimeGoals));
        //compare new goal to past data and generate suggestions
        String newGoalDifficulty = new_goal.getDifficulty();
        //if user matches thei past behaviour, keep it up!
        if (newGoalDifficulty.equals(mostCommonDifficulty)){
            return "Based on your history, this goal is perfect for you! Keep it up :)";
        }
        //if user takes longer than the deadline
        if (overtimeGoals == mostCommonCompletion){
            //we have too many easy goals, suggest making bigger (harder ones)
            if (newGoalDifficulty.equals("easy") && hardGoals > easyGoals){
                return "You often delay easy tasks. Consider consolidating " + (int) easyGoals*0.5 + " easy tasks into a bigger one!";
            }
            //otherwise just let them know!
            return "You tend to delay similar goals. Try grouping smaller tasks!";
        }
        //if user finishes too early
        if (earlyGoals==mostCommonCompletion){
            //completes too many easy ones, suggest they keep doing the same
            if (newGoalDifficulty.equals("hard") && easyGoals > hardGoals){
                return "You're on fire! You often finish easy goals early. Consider breaking this big goal into smaller ones!";
            }
            //otherwise just let them know!
            return "You tend to complete goals quickly! It's time to level up ;)";
        }
        //else anything medium should stay the way it is :)
        return "You've achieved balance! This goal difficulty should be fine! Keep up the good work :D";
    }

    /**
     * Generate suggestion based on timeline!
     * **/
    public String getTimelineSuggestion(Goal new_goal){
        long thisGoalDuration = calculateScheduledDaysforGoalCompletion(new_goal);
        //find similar length goals within a threshold
        List<Goal> similarGoals = goals.stream().filter(goal -> Math.abs(calculateScheduledDaysforGoalCompletion(goal)-thisGoalDuration) <= timelineSuggestionPlusMinusDays)
                .toList();
        //find all deadline vs actual completion date differences in # of days
        List<Long> allCompletionDifferences = similarGoals.stream().map(this::calculateDifferenceInDates).toList();
        //calculate average completion difference (this will give the days to add/minus)
        long averageDays;
        if (allCompletionDifferences.isEmpty()) averageDays = 0;
        //sum all completion differences in days and divide by total number of goals
        else averageDays = (allCompletionDifferences.stream().mapToLong(Long::longValue).sum()) / allCompletionDifferences.size();

        //generate the suggestion!
        if (Math.abs(averageDays) < 1){
            return "Based on your history this deadline is great!";
        } else if (averageDays > 0) {
            return "Based on your history consider adding " + Math.ceil(averageDays) + " days to your deadline. We want to make sure you complete this goal!";
        } else {
            return "Based on your history consider subtracting " + Math.abs(Math.ceil(averageDays)) + " days to your deadline. Good job completing those goals!";
        }
    }
    /**
     * Helper method to calculate number of days scheduled to complete a goal
     * (does NOT consider actual completion date)
     * */
    private long calculateScheduledDaysforGoalCompletion(Goal goal){
        LocalDate newGoalStartDate = goal.getStartDate();
        LocalDate newGoalEndDate = goal.getEndDate();
        return ChronoUnit.DAYS.between(newGoalStartDate,newGoalEndDate);
    }
    /**
     * Helper method to calculate the difference in days between scheduled deadline
     * and actual completion date.
     * */
    private long calculateDifferenceInDates(Goal goal){
        if (goal.isCompleted()){
            return ChronoUnit.DAYS.between(goal.getEndDate(),goal.getCompletionDate());
        }
        //if not completed yet, there's no difference
        return 0;
    }

    //unit testing (this is good to understand how it works!)
    public static void main(String[] args) {
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
    }
}
