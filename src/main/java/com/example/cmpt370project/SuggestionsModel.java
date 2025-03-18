package com.example.cmpt370project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SuggestionsModel {
    private List<Goal> goals = new ArrayList<>();
    private static final double OUTLIER_THRESHOLD_MULTIPLIER = 1.5; // Adjust as needed

    public SuggestionsModel() {
    }

    public void initializeSuggestionsModel(List<Goal> goals) {
        this.goals = goals;
    }

    public void updateGoalList(List<Goal> goals) {
        this.goals = goals;
    }

    /**
     * Generates a suggestion based on the user's historical completion trends for goals of different difficulties,
     * excluding outliers in completion time.
     */
    public String getDifficultySuggestion(Goal newGoal) {
        var difficultyCounters = goals.stream()
                .filter(Goal::isCompleted)
                .collect(Collectors.groupingBy(Goal::getDifficulty, Collectors.counting()));

        long easyGoalsCompleted = difficultyCounters.getOrDefault("easy", 0L);
        long mediumGoalsCompleted = difficultyCounters.getOrDefault("medium", 0L);
        long hardGoalsCompleted = difficultyCounters.getOrDefault("hard", 0L);

        var completionStatusCounts = goals.stream()
                .filter(Goal::isCompleted)
                .collect(Collectors.groupingBy(this::getCompletionStatus, Collectors.counting()));

        long earlyCompletions = completionStatusCounts.getOrDefault("early", 0L);
        long onTimeCompletions = completionStatusCounts.getOrDefault("on_time", 0L);
        long lateCompletions = completionStatusCounts.getOrDefault("late", 0L);

        String mostCommonCompletionStatus = getMostCommonStatus(earlyCompletions, onTimeCompletions, lateCompletions);
        String newGoalDifficulty = newGoal.getDifficulty();
        String mostCommonDifficulty = getMostCommonDifficulty(easyGoalsCompleted, mediumGoalsCompleted, hardGoalsCompleted);

        if (newGoalDifficulty.equals(mostCommonDifficulty)) {
            return "Based on your history, this goal difficulty aligns with what you typically complete. Keep going!";
        }

        if (mostCommonCompletionStatus.equals("late")) {
            return handleLateCompletions(easyGoalsCompleted, mediumGoalsCompleted, hardGoalsCompleted);
        } else if (mostCommonCompletionStatus.equals("early")) {
            return handleEarlyCompletions(easyGoalsCompleted, mediumGoalsCompleted, hardGoalsCompleted);
        }

        return "Your goal difficulty is different from your usual. Observe your progress to refine your strategy.";
    }

    private String getCompletionStatus(Goal goal) {
        long difference = ChronoUnit.DAYS.between(goal.getEndDate(), goal.getCompletionDate());
        if (difference < 0) return "early";
        else if (difference == 0) return "on_time";
        else return "late";
    }

    private String getMostCommonStatus(long early, long onTime, long late) {
        if (early >= onTime && early >= late) return "early";
        else if (onTime >= early && onTime >= late) return "on_time";
        else return "late";
    }

    private String getMostCommonDifficulty(long easy, long medium, long hard) {
        if (easy >= medium && easy >= hard) return "easy";
        else if (medium >= easy && medium >= hard) return "medium";
        else return "hard";
    }

    private String handleLateCompletions(long easy, long medium, long hard) {
        if (easy > medium && easy > hard) {
            return "You often finish easy goals late. Consider grouping similar easy tasks.";
        } else if (medium > easy && medium > hard) {
            return "You often finish medium goals late. Breaking them down might help.";
        } else {
            return "You often finish hard goals late. Try breaking them into smaller, more manageable steps.";
        }
    }

    private String handleEarlyCompletions(long easy, long medium, long hard) {
        if (easy > medium && easy > hard) {
            return "You tend to complete easy goals quickly. Consider taking on slightly more challenging tasks.";
        } else if (medium > easy && medium > hard) {
            return "You complete medium goals early. You might be ready for harder goals.";
        } else {
            return "You complete hard goals early. Consider making them more complex or ambitious.";
        }
    }

    /**
     * Generates a suggestion for adjusting the deadline of a new goal based on historical completion times,
     * excluding outlier completion times.
     */
    public String getTimelineSuggestion(Goal newGoal) {
        long newGoalDuration = ChronoUnit.DAYS.between(newGoal.getStartDate(), newGoal.getEndDate());
        long durationTolerance = Math.max(1, Math.round(newGoalDuration * 0.3));
        double maxAdjustmentFactor = 0.5;

        List<Long> completionDifferences = goals.stream()
                .filter(Goal::isCompleted)
                .filter(goal -> {
                    long pastGoalDuration = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate());
                    return Math.abs(pastGoalDuration - newGoalDuration) <= durationTolerance;
                })
                .map(goal -> ChronoUnit.DAYS.between(goal.getEndDate(), goal.getCompletionDate()))
                .collect(Collectors.toList());

        if (completionDifferences.isEmpty()) {
            return "No past completed goals with a similar planned duration found for deadline suggestions.";}

        List<Long> nonOutlierDifferences = excludeOutliers(completionDifferences);

        if (nonOutlierDifferences.isEmpty()) {
            return "Insufficient reliable past completion data for deadline suggestions on similar length goals after excluding outliers.";}

        double averageDifference = nonOutlierDifferences.stream().mapToLong(Long::longValue).average().orElse(0);
        long suggestedAdjustment = Math.round(averageDifference);

        // Limit the suggested adjustment
        long maxAbsoluteAdjustment = Math.round(newGoalDuration * maxAdjustmentFactor);
        if (Math.abs(suggestedAdjustment) > maxAbsoluteAdjustment) {
            suggestedAdjustment = (suggestedAdjustment > 0) ? maxAbsoluteAdjustment : -maxAbsoluteAdjustment;}

        LocalDate suggestedDeadline = newGoal.getEndDate().plusDays(suggestedAdjustment);

        // Prevent suggesting a deadline before the start date
        if (suggestedDeadline.isBefore(newGoal.getStartDate())) {
            suggestedDeadline = newGoal.getStartDate();
            suggestedAdjustment = ChronoUnit.DAYS.between(newGoal.getEndDate(), suggestedDeadline);
            return "Based on your past completion times, you tend to finish similar goals early. However, a drastic change isn't suggested. Consider setting your deadline closer to your start date.";
        }

        if (suggestedAdjustment > 2) {
            return "Based on your typical completion times for similar length goals, consider setting your deadline to " + suggestedDeadline.toString() + " (add " + suggestedAdjustment + " days).";
        } else if (suggestedAdjustment < -2) {
            return "Based on your typical completion times for similar length goals, you might be able to set your deadline to " + suggestedDeadline.toString() + " (minus " + Math.abs(suggestedAdjustment) + " days).";
        } else {
            return "Based on your typical completion times for similar length goals, your initial deadline seems reasonable.";
        }
    }
    /**
     * Checks if the deadline for a new goal might be unrealistic based on the number of currently incomplete goals
     * and the user's past completion times (excluding outliers).
     */
    public String checkUnrealisticDeadline(Goal newGoal) {
        long incompleteGoals = goals.stream().filter(goal -> !goal.isCompleted()).count();

        List<Long> completionDifferences = goals.stream()
                .filter(Goal::isCompleted)
                .map(goal -> ChronoUnit.DAYS.between(goal.getEndDate(), goal.getCompletionDate()))
                .collect(Collectors.toList());

        List<Long> nonOutlierDifferences = excludeOutliers(completionDifferences);

        if (incompleteGoals > goals.size()*0.75  && !nonOutlierDifferences.isEmpty()) {
            double averageDifference = nonOutlierDifferences.stream().mapToLong(Long::longValue).average().orElse(0);
            LocalDate expectedCompletion = newGoal.getEndDate().plusDays(Math.round(averageDifference));
            if (expectedCompletion.isBefore(LocalDate.now())) {
                return "Warning: You have " + incompleteGoals + " goals in progress. Your new deadline seems very ambitious based on your typical completion times.";
            }
        } else if (incompleteGoals > 4) {
            return "Warning: You currently have a large number of incomplete goals (" + incompleteGoals + "). Consider finishing some before adding more.";
        }
        return null;
    }

    /**
     * Generates a suggestion on whether to break down a goal based on the user's history of completing goals
     * of different durations, excluding outliers in duration.
     */
    public String getTaskBreakdownSuggestion(Goal newGoal) {
        long newGoalDuration = ChronoUnit.DAYS.between(newGoal.getStartDate(), newGoal.getEndDate());
        long durationTolerance = Math.max(1, Math.round(newGoalDuration * 0.3)); // Adjust tolerance as needed

        List<Long> similarCompletedGoalDurations = goals.stream()
                .filter(Goal::isCompleted)
                .filter(goal -> {
                    long pastGoalDuration = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate());
                    return Math.abs(pastGoalDuration - newGoalDuration) <= durationTolerance;
                })
                .map(goal -> ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate()))
                .collect(Collectors.toList());

        if (similarCompletedGoalDurations.isEmpty()) {
            return "No past completed goals with a similar planned duration found for task breakdown suggestions.";}

        List<Long> nonOutlierDurations = excludeOutliers(similarCompletedGoalDurations);

        if (nonOutlierDurations.isEmpty()) {
            return "Insufficient reliable past goal duration data for similar length goals after excluding outliers.";}

        double averageSimilarCompletedDuration = nonOutlierDurations.stream().mapToLong(Long::longValue).average().orElse(0);
        if (newGoalDuration > averageSimilarCompletedDuration * OUTLIER_THRESHOLD_MULTIPLIER) {
            long suggestedTasks = Math.max(2, Math.round((double) newGoalDuration / averageSimilarCompletedDuration));
            return "This goal is significantly longer than your typical similar length completed goals. Consider breaking it down into approximately " + suggestedTasks + " smaller tasks.";
        } else if (newGoalDuration < averageSimilarCompletedDuration / OUTLIER_THRESHOLD_MULTIPLIER && nonOutlierDurations.size() > 3) {
            return "This goal is significantly shorter than your typical similar length completed goals. You might consider combining it with another related goal if possible.";
        } else {
            return "The size of this goal seems to align with your typical similar length completed goals.";
        }
    }

    /**
     * Helper method to exclude outliers from a list of numerical values using the IQR method.
     *
     * @param data The list of numerical values.
     * @return A new list containing the values with outliers removed.
     */
    private List<Long> excludeOutliers(List<Long> data) {
        if (data.size() < 3) {
            return new ArrayList<>(data); // Not enough data to reliably identify outliers
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

    public static void main(String[] args) {
        List<Goal> pastGoals = new ArrayList<>();
        Goal goal1 = new Goal("1", "Learn Python Basics", "easy", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 7), true);
        goal1.setCompletionDate(LocalDate.of(2025, 1, 5));
        pastGoals.add(goal1);
        Goal goal2 = new Goal("2", "Read a Novel", "medium", LocalDate.of(2025, 1, 10), LocalDate.of(2025, 1, 31), true);
        goal2.setCompletionDate(LocalDate.of(2025, 2, 5)); // Outlier (late)
        pastGoals.add(goal2);
        Goal goal3 = new Goal("3", "Plan a Trip", "hard", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28), false);
        pastGoals.add(goal3);
        Goal goal4 = new Goal("4", "Exercise 3 times", "easy", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 7), true);
        goal4.setCompletionDate(LocalDate.of(2025, 3, 3)); // Outlier (early)
        pastGoals.add(goal4);
        Goal goal5 = new Goal("5", "Write a Blog Post", "medium", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 17), true);
        goal5.setCompletionDate(LocalDate.of(2025, 3, 18));
        pastGoals.add(goal5);
        Goal goal6 = new Goal("6", "Small Task 1", "easy", LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 2), true);
        goal6.setCompletionDate(LocalDate.of(2025, 4, 2));
        pastGoals.add(goal6);
        Goal goal7 = new Goal("7", "Small Task 2", "easy", LocalDate.of(2025, 4, 3), LocalDate.of(2025, 4, 4), true);
        goal7.setCompletionDate(LocalDate.of(2025, 4, 3)); // Outlier (very early completion relative to goal 2)
        pastGoals.add(goal7);
        Goal goal8 = new Goal("8", "Very Long Project", "hard", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 6, 30), false);
        pastGoals.add(goal8);

        SuggestionsModel model = new SuggestionsModel();
        model.initializeSuggestionsModel(pastGoals);

        Goal newGoalTimeline = new Goal("9", "Learn a new library", "medium", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 4, 1), false);
        System.out.println("Timeline Suggestion: " + model.getTimelineSuggestion(newGoalTimeline));
        String unrealisticWarning = model.checkUnrealisticDeadline(newGoalTimeline);
        if (unrealisticWarning != null) {
            System.out.println(unrealisticWarning);
        }

        Goal newGoalBreakdown = new Goal("10", "Build a complete application", "hard", LocalDate.of(2025, 4, 5), LocalDate.of(2025, 5, 30), false);
        System.out.println("Task Breakdown Suggestion: " + model.getTaskBreakdownSuggestion(newGoalBreakdown));

        Goal newGoalDifficulty = new Goal("11", "Do laundry", "easy", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 3, 26), false);
        System.out.println("Difficulty Suggestion: " + model.getDifficultySuggestion(newGoalDifficulty));
    }
}