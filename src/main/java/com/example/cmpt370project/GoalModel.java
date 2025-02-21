package com.example.cmpt370project;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
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
    private static final String FILE_NAME = System.getProperty("user.home") + "/GoalApplication/goals.json";
    private HashMap<String, Goal> goals; // I went with a dictionary for easier lookup
    //okay guys apparently gson doesn't know how to convert LocalDate into json so I had to this reformatting thing to define
    //the json conversion :)
    private static final DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE; //YYYY-MM-DD
    //this creates a custom gson instance (with the format for the date) I'll try to find a better way to do this but stack overflow told me to do this :(
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
            context.serialize(src.format(format))).registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
            LocalDate.parse(json.getAsJsonPrimitive().getAsString(), format)).create(); //basically this is saying...
            //when saving a LocalDate, convert it to a string ("2025-02-21") and when loading a LocalDate, read the string and convert it back

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
     * Checks if file exists before reading or writing to it.
     * If it doesn't, it creates an empty file
     */
    private void checkIfFileExists(){
        try {
            Path pathToGoals = Paths.get(FILE_NAME);
            //check if the GoalApplication directory exists
            if (Files.notExists(pathToGoals.getParent())){
                Files.createDirectories(pathToGoals.getParent());
            }
            //check if goals.json exists
            if (Files.notExists(pathToGoals)){
                Files.createFile(pathToGoals);
            }
        }
        catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }
    /**
     * Save goals to file (in JSON format).
     */
    public void save_goals_to_file() {
        checkIfFileExists();
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(goals.values(), writer);
        } catch (IOException e) {
            System.err.println("Error saving goals to file: " + e.getMessage());
        }
    }
    /**
     * Load goals from file (in JSON format).
     */
    public void load_goals_from_file() {
        checkIfFileExists();
        //handling empty file case... (only load if file is not empty, else skip bc there's nothing to read)
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() != 0) {
            try (Reader reader = new FileReader(FILE_NAME)){
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

    // Unit Testing :)
    public static void main(String[] args) {
        GoalModel model = new GoalModel();
        // Create some goals
        Goal goal1 = new Goal("Run a marathon", "Fitness", "Hard",
                LocalDate.of(2025, 2, 1), LocalDate.of(2025, 6, 1));
        Goal goal2 = new Goal("Read 10 books","Personal", "Medium", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 7, 31));

        //test addGoal
        model.addGoal(goal1);
        model.addGoal(goal2);

        //list all goals
        System.out.println("Goals:");
        for (Goal goal : model.listGoals()) {
            System.out.println(goal);
        }

        // test deleteGoal
        model.deleteGoal("Run a marathon");
        // list goals again to see if the goal is deleted
        System.out.println("\nGoals after deletion:");
        for (Goal goal : model.listGoals()) {
            System.out.println(goal);
        }

        //test updateGoal
        Goal newGoal = new Goal("Learn how to cook soup", LocalDate.now(), LocalDate.now().plusDays(3));
        model.updateGoal("Read 10 books", newGoal);

        //test save_to_file and load_from_file (saving should have happened when adding/updating goals)
        System.out.println("Goals loaded from file:");
        GoalModel emptyModel = new GoalModel();
        for (Goal goal: emptyModel.listGoals()) {
            System.out.println(goal.toString());
        }
    }
}
