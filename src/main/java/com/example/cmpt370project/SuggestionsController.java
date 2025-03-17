package com.example.cmpt370project;

import com.google.gson.Gson;
import javafx.collections.ListChangeListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class SuggestionsController{
    private GoalModel goalModel;
    private SuggestionsModel suggestionsModel;
    private HomeView homeView;
    /**
     * Initialize the Suggestion Controller
     */
    public SuggestionsController(GoalModel goalModel, SuggestionsModel suggestionsModel, HomeView homeView){
        this.goalModel = goalModel;
        this.suggestionsModel = suggestionsModel;
        this.homeView = homeView;

        updateSuggestionsModel();
        suggestionsModel.initializeSuggestionsModel(goalModel.getGoals());
        //listen for changes in goalModel to update suggestions model
        goalModel.getGoals().addListener((ListChangeListener<? super Goal>) change -> updateSuggestionsModel());
    }

    private void updateSuggestionsModel(){
        suggestionsModel.updateGoalList(goalModel.getGoals());
    }


    public void onGoalCompletedUpdate(Goal goal_completed){

    }

}
