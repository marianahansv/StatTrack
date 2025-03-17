package com.example.cmpt370project;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class SuggestionsController{
    private GoalModel goalModel;
    /**
     * Initialize the Suggestion Controller
     */
    public SuggestionsController(){}

    public void onGoalCompletedUpdate(Goal goal_completed){

    }

    public String getDifficultySuggestion(Goal new_goal){
        return "";
    }
    public String getTimelineSuggestion(Goal new_goal){
        return "";
    }

}
