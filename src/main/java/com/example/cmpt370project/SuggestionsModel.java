package com.example.cmpt370project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SuggestionsModel {
    private List<Goal> goals = new ArrayList<>();
    private int timelineSuggestionPlusMinusDays = 2;
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

        return "";
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
            return "Based on your history consider adding " + Math.abs(Math.ceil(averageDays)) + " days to your deadline. Good job completing those goals!";
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

}
