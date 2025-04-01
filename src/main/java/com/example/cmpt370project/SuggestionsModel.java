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
    private static final double SIGNIFICANTLY_LONGER_STD_DEV = 2.5;
    private static final double SIGNIFICANTLY_SHORTER_STD_DEV = -2.5;
    private static final double LATE_COMPLETION_THRESHOLD_PERCENTAGE = 30;
    private static final double EARLY_COMPLETION_THRESHOLD_PERCENTAGE = -35;
    private static final int MIN_GOALS_FOR_STATS = 3;
    private static final double HIGH_INCOMPLETE_GOAL_RATIO = 0.5;
    private static final int HIGH_INCOMPLETE_GOAL_COUNT = 5;

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
            return "Your behaviour is still unpredictable for me...let's finish more goals!";
        }

        double averageDuration = nonOutlierDurations.stream().mapToLong(Long::longValue).average().orElse(0);
        double stdDev = calculateStandardDeviation(nonOutlierDurations, averageDuration);
        long newGoalDuration = ChronoUnit.DAYS.between(newGoal.getStartDate(), newGoal.getEndDate());

        if (stdDev == 0) {
            double ratio = newGoalDuration / averageDuration;

            if (ratio > 1.5) {
                int suggestedTasks = (int) Math.max(2, Math.round(ratio)*0.3);
                return "This goal is significantly longer than your typical " + difficulty + " goals. Consider breaking it down into " + suggestedTasks + " smaller goals.";
            } else if (ratio < 0.75 && completedGoalsOfDifficulty.size() > MIN_GOALS_FOR_STATS*2) {
                return "This goal is significantly shorter than your typical " + difficulty + " goals. You might consider combining it with another goal if possible.";
            }
        }

        double stdDevsAway = (newGoalDuration - averageDuration) / (stdDev);

        if (stdDevsAway > SIGNIFICANTLY_LONGER_STD_DEV && (newGoalDuration - averageDuration) > averageDuration*0.5) {
            long suggestedTasks = (long) Math.max(2, Math.round((newGoalDuration / averageDuration))*0.6);
            return String.format(
                    "This %s goal (%d days) is significantly longer than your average (%ddays). " +
                            "Consider breaking it into %d smaller goals of about %d days each.",
                    difficulty, newGoalDuration, (long)averageDuration,
                    suggestedTasks, (long)Math.ceil(averageDuration)
            );
        }
        else if (stdDevsAway < SIGNIFICANTLY_SHORTER_STD_DEV && (averageDuration-newGoalDuration) > averageDuration*0.3) {
            return String.format(
                    "This %s goal (%d days) is significantly shorter than your average (%d days). " +
                            "You might want to combine it with another goal!",
                    difficulty, newGoalDuration, (long)averageDuration
            );
        }
        else {
            return String.format(
                    "This %s goal (%d days) fits well with your typical duration range (%d days). No need to break it down!!",
                    difficulty, newGoalDuration, (long)averageDuration
            );
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
        long suggestedAdjustmentDays = Math.round(newGoalDuration * (averagePercentage/100));
        LocalDate suggestedDeadline = newGoal.getEndDate().plusDays(suggestedAdjustmentDays);

        if ( averagePercentage > LATE_COMPLETION_THRESHOLD_PERCENTAGE && suggestedAdjustmentDays != 0) {
            return "Based on your past " + difficulty + " goals, you tend to finish around " + String.format("%.1f", averagePercentage) + "% late. Consider setting your deadline to " + suggestedDeadline.toString() + " (add " + suggestedAdjustmentDays + " days).";
        } else if (averagePercentage < EARLY_COMPLETION_THRESHOLD_PERCENTAGE) {
            return "Based on your past " + difficulty + " goals, you tend to finish around " + String.format("%.1f", Math.abs(averagePercentage)) + "% early. You might be able to set your deadline to " + suggestedDeadline.toString() + " (minus " + Math.abs(suggestedAdjustmentDays) + " days).";
        } else {
            System.out.println(averagePercentage);
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
        double sumOfSquares = 0;
        for (long val : data) {
            sumOfSquares += Math.pow(val - mean, 2);
        }
        return Math.sqrt(sumOfSquares / (data.size() - 1));
    }

    /**
    * Helper method to create past goals to test this class
    * */
    private static List<Goal> createPastGoals(String difficulty, int count, int avgDurationDays, int durationVariationDays, boolean completed) {
        List<Goal> goals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int duration = avgDurationDays + (int) (Math.random() * 2 * durationVariationDays - durationVariationDays);
            if (duration <= 0) duration = 1;
            LocalDate startDate = LocalDate.now().minusDays(30 + i * 5L);
            LocalDate endDate = startDate.plusDays(duration);
            Goal goal = new Goal("Past " + difficulty + " Goal " + i, "History", difficulty, startDate, endDate, true);
            if (true) {
                goal.setCompletionDate(endDate.plusDays((int) (Math.random() * 3 - 1))); //this is to simulate some variation in completion
            }
            goals.add(goal);
        }
        return goals;
    }

    public static void main(String[] args) {
        // Create a sample list of past goals

        // Task Breakdown Test Cases
        System.out.println("--- Task Breakdown Suggestions ---");
        // Case 1: New "hard" goal significantly longer than average
        List<Goal> pastGoals = new ArrayList<>(createPastGoals("hard", 12, 15, 3, true)); // Average around 15 days
        Goal newGoalBreakdown1 = new Goal("Major Project", "Work", "hard", LocalDate.now().plusDays(1), LocalDate.now().plusDays(40), false);
        SuggestionsModel modelBreakdown1 = new SuggestionsModel();
        modelBreakdown1.initializeSuggestionsModel(pastGoals);
        System.out.println("Breakdown Suggestion 1: " + modelBreakdown1.getTaskBreakdownSuggestion(newGoalBreakdown1));

        // Case 2: New "easy" goal significantly shorter than average (with enough data)
        pastGoals.clear();
        pastGoals.addAll(createPastGoals("easy", 10, 5, 1, true)); // Average around 5 days
        Goal newGoalBreakdown2 = new Goal("Quick Task", "Personal", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), false);
        SuggestionsModel modelBreakdown2 = new SuggestionsModel();
        modelBreakdown2.initializeSuggestionsModel(pastGoals);
        System.out.println("Breakdown Suggestion 2: " + modelBreakdown2.getTaskBreakdownSuggestion(newGoalBreakdown2));

        // Case 3: New "medium" goal within the typical range
        pastGoals.clear();
        pastGoals.addAll(createPastGoals("medium", 7, 10, 2, true)); // Average around 10 days
        Goal newGoalBreakdown3 = new Goal("Standard Task", "General", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(11), false);
        SuggestionsModel modelBreakdown3 = new SuggestionsModel();
        modelBreakdown3.initializeSuggestionsModel(pastGoals);
        System.out.println("Breakdown Suggestion 3: " + modelBreakdown3.getTaskBreakdownSuggestion(newGoalBreakdown3));

        // Timeline Suggestion Test Cases
        System.out.println("\n--- Timeline Suggestions ---");
        pastGoals.clear();
        // Case 1: User tends to finish "medium" goals late
        List<Goal> lateMediumGoals = createPastGoals("medium", 5, 7, 1, true);
        for (Goal goal : lateMediumGoals) {
            goal.setCompletionDate(goal.getEndDate().plusDays(2)); // Simulate finishing late
        }
        pastGoals.addAll(lateMediumGoals);
        Goal newGoalTimeline1 = new Goal("Medium Project", "Work", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), false);
        SuggestionsModel modelTimeline1 = new SuggestionsModel();
        modelTimeline1.initializeSuggestionsModel(pastGoals);
        System.out.println("Timeline Suggestion 1: " + modelTimeline1.getTimelineSuggestion(newGoalTimeline1));

        pastGoals.clear();
        // Case 2: User tends to finish "easy" goals early
        List<Goal> earlyEasyGoals = createPastGoals("easy", 8, 5, 1, true);
        for (Goal goal : earlyEasyGoals) {
            goal.setCompletionDate(goal.getEndDate().minusDays(1)); // Simulate finishing early
        }
        pastGoals.addAll(earlyEasyGoals);
        Goal newGoalTimeline2 = new Goal("Easy Task", "Personal", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), false);
        SuggestionsModel modelTimeline2 = new SuggestionsModel();
        modelTimeline2.initializeSuggestionsModel(pastGoals);
        System.out.println("Timeline Suggestion 2: " + modelTimeline2.getTimelineSuggestion(newGoalTimeline2));

        pastGoals.clear();
        // Case 3: User finishes "hard" goals roughly on time
        pastGoals.addAll(createPastGoals("hard", 6, 14, 2, true));
        Goal newGoalTimeline3 = new Goal("Hard Project", "General", "hard", LocalDate.now().plusDays(1), LocalDate.now().plusDays(21), false);
        SuggestionsModel modelTimeline3 = new SuggestionsModel();
        modelTimeline3.initializeSuggestionsModel(pastGoals);
        System.out.println("Timeline Suggestion 3: " + modelTimeline3.getTimelineSuggestion(newGoalTimeline3));

        // Unrealistic Deadline Check Test Cases
        System.out.println("\n--- Unrealistic Deadline Checks ---");
        pastGoals.clear();
        // Case 1: Too many incomplete goals
        for (int i = 0; i < 8; i++) {
            pastGoals.add(new Goal("Incomplete Goal " + i, "Work", "medium", LocalDate.now().minusDays(10), LocalDate.now().plusDays(5), false));
        }
        Goal newGoalUnrealistic1 = new Goal("New Urgent Task", "Personal", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), false);
        SuggestionsModel modelUnrealistic1 = new SuggestionsModel();
        modelUnrealistic1.initializeSuggestionsModel(pastGoals);
        System.out.println("Unrealistic Deadline Check 1: " + modelUnrealistic1.checkUnrealisticDeadline(newGoalUnrealistic1));

        pastGoals.clear();
        // Case 2: High density of overlapping deadlines
        pastGoals.add(new Goal("Ongoing Task A", "a","medium", LocalDate.now().minusDays(5), LocalDate.now().plusDays(7),false));
        pastGoals.add(new Goal("Ongoing Task B", "a", "medium", LocalDate.now().minusDays(3), LocalDate.now().plusDays(8), false));
        Goal newGoalUnrealistic2 = new Goal("Another Task", "a", "easy", LocalDate.now().plusDays(2), LocalDate.now().plusDays(9), false);
        SuggestionsModel modelUnrealistic2 = new SuggestionsModel();
        modelUnrealistic2.initializeSuggestionsModel(pastGoals);
        System.out.println("Unrealistic Deadline Check 2: " + modelUnrealistic2.checkUnrealisticDeadline(newGoalUnrealistic2));

        pastGoals.clear();
        // Case 3: Ambitious deadline based on past lateness
        List<Goal> consistentlyLateGoals = createPastGoals("medium", 5, 7, 1, true);
        for (Goal goal : consistentlyLateGoals) {
            goal.setCompletionDate(goal.getEndDate().plusDays(3));
        }
        pastGoals.addAll(consistentlyLateGoals);
        Goal newGoalUnrealistic3 = new Goal("Quick Medium Task", "General", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), false);
        SuggestionsModel modelUnrealistic3 = new SuggestionsModel();
        modelUnrealistic3.initializeSuggestionsModel(pastGoals);
        System.out.println("Unrealistic Deadline Check 3: " + modelUnrealistic3.checkUnrealisticDeadline(newGoalUnrealistic3));

        // Goal Difficulty Suggestion Test Cases
        System.out.println("\n--- Goal Difficulty Suggestions ---");
        pastGoals.clear();
        // Case 1: Higher success rate with "medium" than "hard"
        pastGoals.addAll(createPastGoals("easy", 5, 7, 1, true));
        pastGoals.addAll(createPastGoals("medium", 7, 10, 2, true));
        pastGoals.addAll(createPastGoals("hard", 3, 15, 5, true)); // Lower success rate for hard
        SuggestionsModel modelDifficulty1 = new SuggestionsModel();
        modelDifficulty1.initializeSuggestionsModel(pastGoals);
        Goal newGoalDifficulty1 = new Goal("Tough Challenge", "Work", "hard", LocalDate.now().plusDays(1), LocalDate.now().plusDays(20), false);
        System.out.println("Difficulty Suggestion 1: " + modelDifficulty1.getDifficultySuggestion(newGoalDifficulty1));

        pastGoals.clear();
        // Case 2: Successful with "easy" and good with "medium"
        pastGoals.addAll(createPastGoals("easy", 10, 5, 1, true));
        pastGoals.addAll(createPastGoals("medium", 8, 12, 2, true));
        SuggestionsModel modelDifficulty2 = new SuggestionsModel();
        modelDifficulty2.initializeSuggestionsModel(pastGoals);
        Goal newGoalDifficulty2 = new Goal("Simple Task", "Personal", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), false);
        System.out.println("Difficulty Suggestion 2: " + modelDifficulty2.getDifficultySuggestion(newGoalDifficulty2));

        pastGoals.clear();
        // Case 3: Limited history
        pastGoals.add(new Goal("First Goal", "General", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), true));
        SuggestionsModel modelDifficulty3 = new SuggestionsModel();
        modelDifficulty3.initializeSuggestionsModel(pastGoals);
        Goal newGoalDifficulty3 = new Goal("Another Goal", "Learning", "medium", LocalDate.now().plusDays(15), LocalDate.now().plusDays(25), false);
        System.out.println("Difficulty Suggestion 3: " + modelDifficulty3.getDifficultySuggestion(newGoalDifficulty3));
    }

}