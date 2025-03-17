package com.example.cmpt370project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;

public class SuggestionsModel {
    private static final String FILE_NAME = System.getProperty("user.home") + "/GoalApplication/goalSuggestionsData.json";
    private HashMap<String, String> counters;
    private static final Gson gson = new Gson();
    /**
     * Initialize the Suggestion Model
     * try -> load file from user disk
     * if fail -> create empty file with no data
     */
    /*public SuggestionsModel(){
        checkIfFileExists();
        try (Writer writer = new FileWriter(FILE_NAME)) {
           // gson.toJson(G.values(), writer);
        } catch (IOException e) {
            System.err.println("Error saving goals to file: " + e.getMessage());
        }
    }*/
    /**
     * Checks if file exists before reading or writing to it.
     * If it doesn't, it creates an empty file intialized with zero counters!
     */
    private void checkIfFileExists(){
        try {
            Path pathToGoals = Paths.get(FILE_NAME);
            //check if the GoalApplication directory exists
            if (Files.notExists(pathToGoals.getParent())){
                Files.createDirectories(pathToGoals.getParent());
            }
            //check if goals.json exists
            if (Files.notExists(pathToGoals)) {
                Files.createFile(pathToGoals);
                //now we created the file so we can set all counters to 0


            }
        }
        catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }
    public void onGoalCompletedUpdate(Goal goal_completed){

    }

    public String getDifficultySuggestion(Goal new_goal){
        return "";
    }
    public String getTimelineSuggestion(Goal new_goal){
        return "";
    }

}
