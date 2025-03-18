package com.example.cmpt370project;

import com.google.gson.Gson;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SuggestionsController{
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
    public String handleButtonPress(Goal newGoal) {
        String timelineSuggestion = suggestionsModel.getTimelineSuggestion(newGoal);
        String taskDifficultySuggestion = suggestionsModel.getDifficultySuggestion(newGoal);
        String taskBreakdownSuggestion = suggestionsModel.getTaskBreakdownSuggestion(newGoal);
        String isRealisticGoal = suggestionsModel.checkUnrealisticDeadline(newGoal);
        return "Here is what I found...\n" +
                "* "+ timelineSuggestion +
                "\n* " + taskDifficultySuggestion +
                "\n* " + taskBreakdownSuggestion +
                "\n* " + isRealisticGoal;
    }


    public void onGoalCompletedUpdate(Goal goal_completed){

    }

}
