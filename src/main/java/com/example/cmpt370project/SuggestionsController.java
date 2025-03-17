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
    public void handleButtonPress(String timelineSuggestion, String taskBreakdownSuggestion){
        //creating a little dialog!
        Alert alert = new Alert(Alert.AlertType.valueOf("CONFIRMATION"));
        alert.setWidth(200);
        alert.setHeight(300);
        alert.setTitle("Your Suggestions");
        alert.setHeaderText(null);
        alert.setContentText(timelineSuggestion + "\n" + taskBreakdownSuggestion);
        //ButtonType buttonYesChanges = new ButtonType("Accept Changes");
        //ButtonType buttonNoChanges = new ButtonType("No Changes");
        //alert.getButtonTypes().setAll(buttonYesChanges,buttonNoChanges);
        alert.showAndWait();
    }


    public void onGoalCompletedUpdate(Goal goal_completed){

    }

}
