package com.example.cmpt370project;


import java.util.*;

public class SuggestionsController {
    private GoalModel goalModel;
    private SuggestionsModel suggestionsModel;
    private HomeView homeView;

    /**
     * Initialize the Suggestion Controller
     */
    public SuggestionsController(GoalModel goalModel, SuggestionsModel suggestionsModel){
        this.goalModel = goalModel;
        this.suggestionsModel = suggestionsModel;

        updateSuggestionsModel();
        suggestionsModel.initializeSuggestionsModel(goalModel.getGoals());
    }

    private void updateSuggestionsModel(){
        suggestionsModel.updateGoalList(goalModel.getGoals());
    }

    /**
     * Handles user choice between accepting suggested changes or not!*/
    public List<String> handleButtonPress(Goal newGoal){
        ArrayList<String> suggestions = new ArrayList<>();
        suggestions.add(suggestionsModel.getTimelineSuggestion(newGoal));
        suggestions.add(suggestionsModel.getTaskBreakdownSuggestion(newGoal));
        suggestions.add(suggestionsModel.getDifficultySuggestion(newGoal));
        suggestions.add(suggestionsModel.checkUnrealisticDeadline(newGoal));
        return suggestions;
    }

    public void validateInput(Goal newGoal) throws InputMismatchException {
        if (newGoal.getTitle().isBlank()){
            throw new InputMismatchException("Title cannot be empty.");
        }
        if (newGoal.getTitle().length() > 40){
            throw new InputMismatchException("Title cannot be more than 40 characters.");
        }
        //section has pre-set value
        //difficulty has pre-set value
        if(newGoal.getEndDate().isBefore(newGoal.getStartDate())){
            throw new InputMismatchException("End date cannot be before start date.");
        }
    }

    public void updateModel(){
        suggestionsModel.updateGoalList(goalModel.getGoals());
    }

}
