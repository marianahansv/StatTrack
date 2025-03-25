package com.example.cmpt370project;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuggestionsModel {
    private List<Goal> goals = new ArrayList<>();

    /*Stats constants used in score calculations for the suggestions*/
    private static final double OUTLIER_THRESHOLD_MULTIPLIER = 1.5;
    private static final double SIGNIFICANTLY_LONGER_STD_DEV = 1.5;
    private static final double SIGNIFICANTLY_SHORTER_STD_DEV = -1.5;
    private static final double LATE_COMPLETION_THRESHOLD_PERCENTAGE = 10.0;
    private static final double EARLY_COMPLETION_THRESHOLD_PERCENTAGE = -10.0;
    private static final int MIN_GOALS_FOR_STATS = 3;
    private static final double HIGH_INCOMPLETE_GOAL_RATIO = 0.5;
    private static final int HIGH_INCOMPLETE_GOAL_COUNT = 5;
    private static final int DEADLINE_OVERLAP_WINDOW = 3;

    public SuggestionsModel() {
    }

    public void initializeSuggestionsModel(List<Goal> goals) {
        this.goals = goals;
    }

    public void updateGoalList(List<Goal> goals) {
        this.goals = goals;
    }

    /**
     * Generates a task breakdown suggestion based on historical goal completion data.
     */
    public String getTaskBreakdownSuggestion(Goal newGoal) {
        String difficulty = newGoal.getDifficulty();
        List<Goal> completedGoalsOfDifficulty = goals.stream()
                .filter(Goal::isCompleted)
                .filter(g -> g.getDifficulty().equals(difficulty))
                .toList();
        if (completedGoalsOfDifficulty.size() < MIN_GOALS_FOR_STATS) {
            return "I don't have enough data to suggest a goal breakdown!";
        }
        List<Long> durations = completedGoalsOfDifficulty.stream()
                .map(g -> ChronoUnit.DAYS.between(g.getStartDate(), g.getEndDate()))
                .collect(Collectors.toList());
        List<Long> nonOutlierDurations = excludeOutliers(durations);
        if (nonOutlierDurations.isEmpty()) {
            return "I don't have enough reliable data suggest a goal breakdown!";
        }
        double averageDuration = nonOutlierDurations.stream().mapToLong(Long::longValue).average().orElse(0);
        double stdDev = calculateStandardDeviation(nonOutlierDurations, averageDuration);
        long newGoalDuration = ChronoUnit.DAYS.between(newGoal.getStartDate(), newGoal.getEndDate());
        double stdDevsAway = (newGoalDuration - averageDuration) / stdDev;

        if (stdDev > 0 && stdDevsAway > SIGNIFICANTLY_LONGER_STD_DEV) {
            long suggestedTasks = Math.max(2, Math.round((double) newGoalDuration / averageDuration));
            return "This goal is significantly longer than your typical " + difficulty + " goals. Consider breaking it down into " + suggestedTasks + " smaller goals.";
        } else if (stdDev > 0 && stdDevsAway < SIGNIFICANTLY_SHORTER_STD_DEV && completedGoalsOfDifficulty.size() > MIN_GOALS_FOR_STATS * 2) {
            return "This goal is significantly shorter than your typical " + difficulty + " goals. You might consider combining it with another goal if possible.";
        } else {
            return "The size of this goal seems to align with your typical " + difficulty + " completed goals.";
        }
    }

    /**
     * Generates a timeline suggestion for a new goal based on historical goal completion data.
     */
    public String getTimelineSuggestion(Goal newGoal) {
        String difficulty = newGoal.getDifficulty();
        List<Goal> completedGoalsOfDifficulty = goals.stream()
                .filter(Goal::isCompleted)
                .filter(g -> g.getDifficulty().equals(difficulty))
                .toList();
        if (completedGoalsOfDifficulty.size() < MIN_GOALS_FOR_STATS) {
            return "I don't have enough data for timeline suggestions! Let's complete some goals :D";
        }
        List<Double> completionDifferencePercentages = completedGoalsOfDifficulty.stream()
                .map(goal -> {
                    long duration = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate());
                    long timeTaken = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getCompletionDate());
                    return duration == 0 ? 0 : ((double) timeTaken - duration) / duration * 100;
                })
                .collect(Collectors.toList());
        List<Double> nonOutlierPercentages = excludeOutlierDouble(completionDifferencePercentages);
        if (nonOutlierPercentages.isEmpty()) {
            return "Your behaviour is still unpredictable for me...let's finish more goals!";
        }

        double averagePercentage = nonOutlierPercentages.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        long newGoalDuration = ChronoUnit.DAYS.between(newGoal.getStartDate(), newGoal.getEndDate());
        long suggestedAdjustmentDays = Math.round(newGoalDuration * (averagePercentage / 100.0));
        LocalDate suggestedDeadline = newGoal.getEndDate().plusDays(suggestedAdjustmentDays);

        if (averagePercentage > LATE_COMPLETION_THRESHOLD_PERCENTAGE) {
            return "Based on your past " + difficulty + " goals, you tend to finish around " + String.format("%.1f", averagePercentage) + "% late. Consider setting your deadline to " + suggestedDeadline.toString() + " (add " + suggestedAdjustmentDays + " days).";
        } else if (averagePercentage < EARLY_COMPLETION_THRESHOLD_PERCENTAGE) {
            return "Based on your past " + difficulty + " goals, you tend to finish around " + String.format("%.1f", Math.abs(averagePercentage)) + "% early. You might be able to set your deadline to " + suggestedDeadline.toString() + " (minus " + Math.abs(suggestedAdjustmentDays) + " days).";
        } else {
            return "Based on your past behaviour, your initial deadline it's perfect!";
        }
    }

    /**
     * Checks if the deadline for a new goal is unrealistic.
     */
    public String checkUnrealisticDeadline(Goal newGoal) {
        long incompleteGoalsCount = goals.stream().filter(goal -> !goal.isCompleted()).count();
        if (incompleteGoalsCount > goals.size() * HIGH_INCOMPLETE_GOAL_RATIO || incompleteGoalsCount > HIGH_INCOMPLETE_GOAL_COUNT) {
            return "Warning: You have a significant number of incomplete goals (" + incompleteGoalsCount + "). Consider finishing some before adding more!";
        }
        // Simple check based on overall lateness
        double overallAverageLateness = goals.stream()
                .filter(Goal::isCompleted)
                .mapToLong(goal -> ChronoUnit.DAYS.between(goal.getEndDate(), goal.getCompletionDate()))
                .average()
                .orElse(0);
        long newGoalDuration = ChronoUnit.DAYS.between(newGoal.getStartDate(), newGoal.getEndDate());
        if (overallAverageLateness > 2 && newGoalDuration < overallAverageLateness) {
            return "Warning: You typically finish goals a few days late. This deadline might be a bit too ambitious :0";
        }
        return "You've been doing a great job at keeping up with your goals! You're a star :)";
    }

    /**
     * Generates a difficulty suggestion based on the user's data for similar goals
     */
    public String getDifficultySuggestion(Goal newGoal) {
        Map<String, Long> completedCounts = goals.stream()
                .filter(Goal::isCompleted)
                .collect(Collectors.groupingBy(Goal::getDifficulty, Collectors.counting()));

        Map<String, Long> totalCounts = goals.stream()
                .collect(Collectors.groupingBy(Goal::getDifficulty, Collectors.counting()));

        //working with rations for more accurate and sensible suggestions :)
        Map<String, Double> successRates = new HashMap<>();
        totalCounts.forEach((difficulty, total) -> {
            long completed = completedCounts.getOrDefault(difficulty, 0L);
            successRates.put(difficulty, total == 0 ? 0 : (double) completed / total);
        });

        String newGoalDifficulty = newGoal.getDifficulty();
        double newGoalSuccessRate = successRates.getOrDefault(newGoalDifficulty, 0.0);
        String bestDifficulty = null;
        double bestSuccessRate = -1.0;
        for (Map.Entry<String, Double> entry : successRates.entrySet()) {
            if (!entry.getKey().equals(newGoalDifficulty) && entry.getValue() > bestSuccessRate) {
                bestSuccessRate = entry.getValue();
                bestDifficulty = entry.getKey();
            }
        }

        if (bestDifficulty != null && bestSuccessRate > newGoalSuccessRate + 0.2 && totalCounts.getOrDefault(newGoalDifficulty, 0L) >= MIN_GOALS_FOR_STATS) {
            return "You've had a higher success rate with '" + bestDifficulty + "' goals. Consider if that might be a better fit for this goal!";
        } else if (newGoalDifficulty.equals("easy") && successRates.getOrDefault("medium", 0.0) > 0.7 && totalCounts.getOrDefault("medium", 0L) >= MIN_GOALS_FOR_STATS) {
            return "You've been very successful with easy goals, and also have a good track record with medium ones. Consider challenging yourself with a hard goal next time!";
        }
        return "You've been killing it! Your chosen difficulty aligns with your past behaviour.";
    }

    /**
     * Helper method to exclude outliers from a list of numerical values using the IQR method. (STATS245<3)
     *
     * @param data The list of numerical values.
     * @return A new list containing the values with outliers removed.
     */
    private List<Long> excludeOutliers(List<Long> data) {
        if (data.size() < 3) {
            return new ArrayList<>(data);
        }
        List<Long> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);

        int q1Index = sortedData.size() / 4;
        int q3Index = sortedData.size() * 3 / 4;
        long q1 = sortedData.get(q1Index);
        long q3 = sortedData.get(q3Index);
        long iqr = q3 - q1;

        long lowerBound = q1 - (long) (iqr * OUTLIER_THRESHOLD_MULTIPLIER);
        long upperBound = q3 + (long) (iqr * OUTLIER_THRESHOLD_MULTIPLIER);

        return sortedData.stream()
                .filter(value -> value >= lowerBound && value <= upperBound)
                .collect(Collectors.toList());
    }

    private List<Double> excludeOutlierDouble(List<Double> data) {
        if (data.size() < 3) {
            return new ArrayList<>(data);
        }
        List<Double> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);

        int q1Index = sortedData.size() / 4;
        int q3Index = sortedData.size() * 3 / 4;
        double q1 = sortedData.get(q1Index);
        double q3 = sortedData.get(q3Index);
        double iqr = q3 - q1;

        double lowerBound = q1 - (iqr * OUTLIER_THRESHOLD_MULTIPLIER);
        double upperBound = q3 + (iqr * OUTLIER_THRESHOLD_MULTIPLIER);

        return sortedData.stream()
                .filter(value -> value >= lowerBound && value <= upperBound)
                .collect(Collectors.toList());
    }

    private double calculateStandardDeviation(List<Long> data, double mean) {
        if (data.size() < 2) {
            return 0;
        }
        double sumOfSquares = 0;
        for (long val : data) {
            sumOfSquares += Math.pow(val - mean, 2);
        }
        return Math.sqrt(sumOfSquares / (data.size() - 1));
    }

}