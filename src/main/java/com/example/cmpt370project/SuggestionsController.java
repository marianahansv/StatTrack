package com.example.cmpt370project;

import com.google.gson.Gson;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.InputMismatchException;
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
        if (goalModel.getGoals().stream().filter(Goal::isCompleted).count() < 3) {
            return "Here is what I found...\n" +
                    "This looks like a great start for you! But...I don't have enough data to make suggestions yet (I need at least 3 goals in you history!)";
        }
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
