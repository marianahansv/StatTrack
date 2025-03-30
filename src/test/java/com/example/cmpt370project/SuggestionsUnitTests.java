package com.example.cmpt370project;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SuggestionsUnitTests {
    private static SuggestionsModel suggestionsModel;
    private static List<Goal> goals;
    @BeforeAll
    public static void setUp() {
        suggestionsModel = new SuggestionsModel();
        goals = new ArrayList<>();
    }

    // Helper method to create past goals
    private List<Goal> createPastGoals(String difficulty, int count, int avgDurationDays, int durationVariationDays, boolean completed, String completionTime) {
        List<Goal> goals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int duration = avgDurationDays + (int) (Math.random() * 2 * durationVariationDays - durationVariationDays);
            if (duration <= 0) duration = 1;
            LocalDate startDate = LocalDate.now().minusDays(30 + i * 5L);
            LocalDate endDate = startDate.plusDays(duration);
            Goal goal = new Goal("Past " + difficulty + " Goal " + i, "History", difficulty, startDate, endDate, completed);
            if (completed) {
                if (completionTime.equals("early")) goal.setCompletionDate(endDate.plusDays((int) (Math.random() * - 6 + 1)));
                else if (completionTime.equals("late")) goal.setCompletionDate(endDate.plusDays((int) (Math.random() * 6 - 1)));
                else goal.setCompletionDate(endDate.plusDays((int) (Math.random() * 3 - 1)));
            }
            goals.add(goal);
        }
        return goals;
    }

    // User Story 1: Realistic Deadline Suggestions
    @Test
    public void testTimelineSuggestion_realisticDeadline() {
        goals.clear();
        goals.addAll(createPastGoals("medium", 5, 7, 1, true, "onTime"));
        Goal newGoal = new Goal("Medium Task", "Test", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(8), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTimelineSuggestion(newGoal);
        assertTrue(suggestion.contains("initial deadline it's perfect!"));
    }

    @Test
    public void testTimelineSuggestion_shorterDeadline() {
        goals.clear();
        goals.addAll(createPastGoals("easy", 5, 5, 1, true, "early"));
        for (Goal goal : goals) {
            goal.setCompletionDate(goal.getEndDate().minusDays(2));
        }
        Goal newGoal = new Goal("Easy Task", "Test", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTimelineSuggestion(newGoal);
        assertTrue(suggestion.contains("minus"));
    }

    @Test
    public void testTimelineSuggestion_longerDeadline() {
        goals.clear();
        goals.addAll(createPastGoals("hard", 5, 10, 1, true, "late"));
        for (Goal goal : goals) {
            goal.setCompletionDate(goal.getEndDate().plusDays(2));
        }
        Goal newGoal = new Goal("Hard Task", "Test", "hard", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTimelineSuggestion(newGoal);
        assertTrue(suggestion.contains("add"));
    }

    @Test
    public void testTimelineSuggestion_noSimilarGoals() {
        goals.clear();
        goals.addAll(createPastGoals("easy", 2, 5, 1, true, "onTime"));
        Goal newGoal = new Goal("hard Task", "Test", "hard", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTimelineSuggestion(newGoal);
        assertTrue(suggestion.contains("I don't have enough data"));
    }

    @Test
    public void testUnrealisticDeadline_highIncompleteGoals() {
        goals.clear();
        goals.addAll(createPastGoals("medium", 6, 7, 1, false, "onTime"));
        Goal newGoal = new Goal("Quick Task", "Test", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.checkUnrealisticDeadline(newGoal);
        assertTrue(suggestion.contains("You have a significant number of incomplete goals"));
    }

    @Test
    public void testTimelineSuggestion_noData() {
        goals.clear();
        Goal newGoal = new Goal("New Task", "Test", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(7), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTimelineSuggestion(newGoal);
        assertTrue(suggestion.contains("I don't have enough data"));
    }

    @Test
    public void testUnrealisticDeadline_allIncomplete() {
        goals.clear();
        goals.addAll(createPastGoals("easy", 3, 5, 1, false, "onTime"));
        Goal newGoal = new Goal("New Task", "Test", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.checkUnrealisticDeadline(newGoal);
        assertTrue(suggestion.contains("You have a significant number of incomplete goals"));
    }

    @Test
    public void testTimelineSuggestion_lateCompletion() {
        goals.clear();
        goals.addAll(createPastGoals("medium", 5, 7, 1, true, "late"));
        for (Goal goal : goals) {
            goal.setCompletionDate(goal.getEndDate().plusDays(2));
        }
        Goal newGoal = new Goal("Medium Task", "Test", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(7), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTimelineSuggestion(newGoal);
        assertTrue(suggestion.contains("late. Consider setting your deadline"));
    }

    @Test
    public void testTimelineSuggestion_earlyCompletion() {
        goals.clear();
        goals.addAll(createPastGoals("easy", 5, 5, 1, true,"early"));
        for (Goal goal : goals) {
            goal.setCompletionDate(goal.getEndDate().minusDays(2));
        }
        Goal newGoal = new Goal("Easy Task", "Test", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTimelineSuggestion(newGoal);
        assertTrue(suggestion.contains("early. You might be able to set your deadline"));
    }

    @Test
    public void testTimelineSuggestion_diffTimelines() {
        goals.clear();
        goals.addAll(createPastGoals("medium", 5, 7, 3, true, "onTime"));
        Goal newGoal = new Goal("Medium Task", "Test", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(7), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTimelineSuggestion(newGoal);
        assertTrue(suggestion.contains("initial deadline it's perfect!"));
    }

    // User Story 2: Breaking Down Goals

    @Test
    public void testTaskBreakdown_typicalGoalSize() {
        goals.clear();
        goals.addAll(createPastGoals("medium", 5, 7, 1, true, "onTime"));
        Goal newGoal = new Goal("Medium Task", "Test", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(7), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("align with your typical"));
    }

    @Test
    public void testTaskBreakdown_longerGoalSize() {
        goals.clear();
        goals.addAll(createPastGoals("hard", 8, 7, 1, true, "onTime"));
        Goal newGoal = new Goal("Long Hard Task", "Test", "hard", LocalDate.now().plusDays(1), LocalDate.now().plusDays(14), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("significantly longer"));
    }

    @Test
    public void testTaskBreakdown_shorterGoalSize() {
        goals.clear();
        goals.addAll(createPastGoals("easy", 8, 7, 1, true, "onTime"));
        Goal newGoal = new Goal("Short Easy Task", "Test", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("significantly shorter"));
    }

    @Test
    public void testTaskBreakdown_noSimilarDuration() {
        goals.clear();
        goals.addAll(createPastGoals("easy", 8, 7, 1, true, "onTime"));
        Goal newGoal = new Goal("Unique Task", "Test", "unique", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("I don't have enough data"));
    }

    @Test
    public void testTaskBreakdown_noUserHistory() {
        goals.clear();
        Goal newGoal = new Goal("New Task", "Test", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(7), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("I don't have enough data"));
    }

    @Test
    public void testTaskBreakdown_easyGoals() {
        goals.clear();
        List<Goal> easyGoals = createPastGoals("easy", 10, 5, 1, true, "onTime");
        goals.addAll(easyGoals);
        Goal newGoal = new Goal("Easy Task", "Test", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(6), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("align with your typical"));
    }

    @Test
    public void testTaskBreakdown_mediumGoals() {
        goals.clear();
        List<Goal> mediumGoals = createPastGoals("medium", 10, 7, 1, true, "onTime");
        goals.addAll(mediumGoals);
        Goal newGoal = new Goal("Medium Task", "Test", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(9), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("align with your typical"));
    }

    @Test
    public void testTaskBreakdown_hardGoals() {
        goals.clear();
        List<Goal> hardGoals = createPastGoals("hard", 10, 10, 1, true, "onTime");
        goals.addAll(hardGoals);
        Goal newGoal = new Goal("Hard Task", "Test", "hard", LocalDate.now().plusDays(1), LocalDate.now().plusDays(12), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("align with your typical"));
    }

    @Test
    public void testTaskBreakdown_easyGoalsLate() {
        goals.clear();
        List<Goal> easyGoals = createPastGoals("easy", 5, 5, 1, true, "late");
        goals.addAll(easyGoals);
        Goal newGoal = new Goal("Easy Task", "Test", "easy", LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("align with your typical"));
    }

    @Test
    public void testTaskBreakdown_mediumGoalsEarly() {
        goals.clear();
        List<Goal> mediumGoals = createPastGoals("medium", 5, 7, 1, true, "onTime");
        goals.addAll(mediumGoals);
        Goal newGoal = new Goal("Medium Task", "Test", "medium", LocalDate.now().plusDays(1), LocalDate.now().plusDays(6), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("align with your typical"));
    }

    @Test
    public void testTaskBreakdown_hardGoalsEarly() {
        goals.clear();
        List<Goal> hardGoals = createPastGoals("hard", 5, 10, 1, true, "onTime");
        goals.addAll(hardGoals);
        Goal newGoal = new Goal("Hard Task", "Test", "hard", LocalDate.now().plusDays(1), LocalDate.now().plusDays(9), false);
        suggestionsModel.initializeSuggestionsModel(goals);
        String suggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        assertTrue(suggestion.contains("align with your typical"));
    }
}

