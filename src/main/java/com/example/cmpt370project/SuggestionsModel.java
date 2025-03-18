package com.example.cmpt370project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SuggestionsModel {
    private List<Goal> goals = new ArrayList<>();

    public SuggestionsModel() {
    }

    public void initializeSuggestionsModel(List<Goal> goals) {
        this.goals = goals;
    }

    public void updateGoalList(List<Goal> goals) {
        this.goals = goals;
    }

    /**
     * Generates a suggestion based on the user's historical completion trends for goals of different difficulties.
     */
    public String getDifficultySuggestion(Goal newGoal) {
        // Count completed goals by difficulty
        var difficultyCounters = goals.stream()
                .filter(Goal::isCompleted)
                .collect(Collectors.groupingBy(Goal::getDifficulty, Collectors.counting()));

        long easyGoalsCompleted = difficultyCounters.getOrDefault("easy", 0L);
        long mediumGoalsCompleted = difficultyCounters.getOrDefault("medium", 0L);
        long hardGoalsCompleted = difficultyCounters.getOrDefault("hard", 0L);

        // Count goals completed early, on time, or late
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
     * Generates a suggestion for adjusting the deadline of a new goal based on historical completion times.
     */
    public String getTimelineSuggestion(Goal newGoal) {
        List<Long> completionDifferences = goals.stream()
                .filter(Goal::isCompleted)
                .map(goal -> ChronoUnit.DAYS.between(goal.getEndDate(), goal.getCompletionDate()))
                .toList();

        if (completionDifferences.isEmpty()) {
            return "No past completion data available for deadline suggestions.";
        }

        double averageDifference = completionDifferences.stream().mapToLong(Long::longValue).average().orElse(0);
        LocalDate suggestedDeadline = newGoal.getEndDate().plusDays(Math.round(averageDifference));

        if (averageDifference > 2) {
            return "Based on your history, you typically finish goals about " + Math.round(averageDifference) + " days late. Consider setting your deadline to " + suggestedDeadline.toString() + ".";
        } else if (averageDifference < -2) {
            return "Based on your history, you often finish goals about " + Math.abs(Math.round(averageDifference)) + " days early. You might be able to set your deadline to " + suggestedDeadline.toString() + ".";
        } else {
            return "Based on your history, your initial deadline seems reasonable.";
        }
    }

    /**
     * Checks if the deadline for a new goal might be unrealistic based on the number of currently incomplete goals
     * and the user's past completion times.
     */
    public String checkUnrealisticDeadline(Goal newGoal) {
        long incompleteGoals = goals.stream().filter(goal -> !goal.isCompleted()).count();

        List<Long> completionDifferences = goals.stream()
                .filter(Goal::isCompleted)
                .map(goal -> ChronoUnit.DAYS.between(goal.getEndDate(), goal.getCompletionDate()))
                .toList();

        if (incompleteGoals > ((int) goals.size()/2 ) && !completionDifferences.isEmpty()) { // Threshold for incomplete goals -> You have more than half your goal uncompleted
            double averageDifference = completionDifferences.stream().mapToLong(Long::longValue).average().orElse(0);
            LocalDate expectedCompletion = newGoal.getEndDate().plusDays(Math.round(averageDifference));
            if (expectedCompletion.isBefore(LocalDate.now())) {
                return "Warning: You have " + incompleteGoals + " goals in progress. Your new deadline seems very ambitious based on your past completion times.";
            }
        } else if (incompleteGoals > 4) { // Another simpler warning based solely on the number of incomplete goals
            return "Warning: You currently have a large number of incomplete goals (" + incompleteGoals + "). Consider finishing some before adding more.";
        }
        return null; // No warning
    }

    /**
     * Generates a suggestion on whether to break down a goal based on the user's history of completing goals of different durations.
     */
    public String getTaskBreakdownSuggestion(Goal newGoal) {
        long newGoalDuration = ChronoUnit.DAYS.between(newGoal.getStartDate(), newGoal.getEndDate());

        List<Long> completedGoalDurations = goals.stream()
                .filter(Goal::isCompleted)
                .map(goal -> ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate()))
                .toList();

        if (completedGoalDurations.isEmpty()) {
            return "No past goal completion data to suggest task breakdown.";
        }

        double averageCompletedDuration = completedGoalDurations.stream().mapToLong(Long::longValue).average().orElse(0);

        if (newGoalDuration > averageCompletedDuration * 2) { // If the new goal is significantly longer than average
            long suggestedTasks = Math.max(2, Math.round((double) newGoalDuration / averageCompletedDuration));
            return "This goal is longer than your typical completed goals. Consider breaking it down into approximately " + suggestedTasks + " smaller tasks.";
        } else if (newGoalDuration < averageCompletedDuration / 2 && completedGoalDurations.size() > 3) { // If significantly shorter and there's enough history
            return "This goal is shorter than your typical completed goals. You might consider combining it with another related goal if possible.";
        } else {
            return "The size of this goal seems to align with your past completed goals.";
        }
    }

    // Example usage in a main method (for testing purposes)
    public static void main(String[] args) {
        // Sample goal data
        List<Goal> pastGoals = new ArrayList<>();
        Goal goal1 = new Goal("1", "Learn Python Basics", "easy", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 7), true);
        goal1.setCompletionDate(LocalDate.of(2025, 1, 5));
        pastGoals.add(goal1);
        Goal goal2 = new Goal("2", "Read a Novel", "medium", LocalDate.of(2025, 1, 10), LocalDate.of(2025, 1, 31), true);
        goal2.setCompletionDate(LocalDate.of(2025, 2, 5));
        pastGoals.add(goal2);
        Goal goal3 = new Goal("3", "Plan a Trip", "hard", LocalDate.of(2025, 2, 1), LocalDate.of(2025, 2, 28), false);
        pastGoals.add(goal3);
        Goal goal4 = new Goal("4", "Exercise 3 times", "easy", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 7), true);
        goal4.setCompletionDate(LocalDate.of(2025, 3, 6));
        pastGoals.add(goal4);
        Goal goal5 = new Goal("5", "Write a Blog Post", "medium", LocalDate.of(2025, 3, 10), LocalDate.of(2025, 3, 17), true);
        goal5.setCompletionDate(LocalDate.of(2025, 3, 20));
        pastGoals.add(goal5);

        SuggestionsModel model = new SuggestionsModel();
        model.initializeSuggestionsModel(pastGoals);

        // Test Timeline Suggestion
        Goal newGoalTimeline = new Goal("6", "Learn a new library", "medium", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 4, 1), false);
        System.out.println("Timeline Suggestion: " + model.getTimelineSuggestion(newGoalTimeline));
        String unrealisticWarning = model.checkUnrealisticDeadline(newGoalTimeline);
        if (unrealisticWarning != null) {
            System.out.println(unrealisticWarning);
        }

        // Test Task Breakdown Suggestion
        Goal newGoalBreakdown = new Goal("7", "Build a complete application", "hard", LocalDate.of(2025, 4, 5), LocalDate.of(2025, 5, 30), false);
        System.out.println("Task Breakdown Suggestion: " + model.getTaskBreakdownSuggestion(newGoalBreakdown));

        // Test Difficulty Suggestion
        Goal newGoalDifficulty = new Goal("8", "Do laundry", "easy", LocalDate.of(2025, 3, 25), LocalDate.of(2025, 3, 26), false);
        System.out.println("Difficulty Suggestion: " + model.getDifficultySuggestion(newGoalDifficulty));
    }
}

