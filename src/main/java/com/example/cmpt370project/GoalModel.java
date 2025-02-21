package com.example.cmpt370project;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The GoalModel holds all the Goals data in the application. It handles the CRUD operations, and
 * serialization (storage) of Goals.
 */
public class GoalModel {
    private static final String FILE_NAME = "goals_data.json";
    private HashMap<String, Goal> goals; // I went with a dictionary for faster and easier lookup
    public GoalModel(){
        goals = new HashMap<>();
        load_goals_from_file();
    }
    /**
     * Create a new goal and add it to the dictionary.
     */
    public void addGoal(Goal newGoal){
        goals.put(newGoal.getTitle(),newGoal);
        save_goals_to_file();
    }
    /**
     * Update an existing goal by replacing the goal with the same title.
     * @param title The title of the goal to update.
     * @param updatedGoal The updated goal object.
     * @return true if the goal was updated successfully, false if not found.
     */
    public boolean updateGoal(String title, Goal updatedGoal) {
        if (goals.containsKey(title)) {
            goals.put(title, updatedGoal);
            save_goals_to_file();
            return true;
        }
        return false;
    }
    /**
     * Delete a goal by its title. (I'm just thinking of what would be easier but we can always change it as we need)
     * @param title of goal to delete.
     * @return true if goal was deleted successfully, false otherwise.
     */
    public boolean deleteGoal(String title) {
        if (goals.containsKey(title)) {
            goals.remove(title);
            save_goals_to_file();
            return true;
        }
        return false;
    }
    /**
     * @return list of all goals. (I'm thinking of future sorting/filtering operations for which we'll need a list)
     */
    public List<Goal> listGoals(){
        return new ArrayList<>(goals.values());
    }
    /**
     * Save goals to file (in JSON format).
     */
    public void save_goals_to_file() {
        try (Writer writer = new FileWriter(FILE_NAME)) {
            Gson gson = new Gson();
            gson.toJson(goals.values(), writer);
        } catch (IOException e) {
            System.err.println("Error saving goals to file: " + e.getMessage());
        }
    }
    /**
     * Load goals from file (in JSON format).
     */
    public void load_goals_from_file() {
        try (Reader reader = new FileReader(FILE_NAME)){
            Gson gson = new Gson();
            Goal[] goalListFromJson = gson.fromJson(reader,Goal[].class);
            if (goalListFromJson != null){
                for (Goal goal: goalListFromJson){
                    //add goals to the dictionary :)
                    goals.put(goal.getTitle(),goal);
                }
            }
        }
        catch (IOException e){
            System.err.println("Error loading goals from file: " + e.getMessage());
        }
    }
    

}
