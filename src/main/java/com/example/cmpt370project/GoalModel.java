
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
    private HashMap<String, Goal> goals;
    private static final DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE; //YYYY-MM-DD
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
            context.serialize(src.format(format))).registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
            LocalDate.parse(json.getAsJsonPrimitive().getAsString(), format)).create();
    /**
     * The subscriber list (i.e. the view), which will update when the view changes.
     */
    private List<Subscriber> subscribers;

    private String currentFilter = "All"; // Stores the currently selected difficulty filter

    public GoalModel(){
        goals = new HashMap<>();
        subscribers = new ArrayList<Subscriber>();
        load_goals_from_file();
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * add goal to the dictionary.
     * Note: this method does not handle the creation of the goal
     * @param newGoal : Goal to be added
     */
    public void addGoal(Goal newGoal){
        goals.put(newGoal.getTitle(),newGoal);
        save_goals_to_file();
        notifySubscribers();
    }

    /**
     * Create a new goal and add it to the dictionary.
     * Note: This method handles the creation of the goal
     * @param title title of the goal to be added
     * @param section section the goal belongs to
     * @param difficulty difficulty (easy-medium-hard)
     * @param startDate start date of goal
     * @param endDate end date of goal
     */
    public void addGoal(String title, String section, String difficulty, LocalDate startDate, LocalDate endDate, boolean completed){
        Goal newGoal = new Goal(title,section,difficulty,startDate,endDate,completed);
        goals.put(newGoal.getTitle(),newGoal);
        save_goals_to_file();
        notifySubscribers();
    }

    /**
     * Create a new goal and add it to the dictionary.
     * Note: This method handles the creation of the goal
     * @param title title of the goal to be added
     * @param startDate start date of goal
     * @param endDate end date of goal
     */
    public void addGoal(String title, LocalDate startDate, LocalDate endDate){
        Goal newGoal = new Goal(title,startDate,endDate);
        goals.put(newGoal.getTitle(),newGoal);
        save_goals_to_file();
        notifySubscribers();
    }
    /**
     * Empties the goal dictionary.
     */
    public void clearGoals() {
        goals.clear();
        save_goals_to_file();
        notifySubscribers();
    }


  
    

    /**
     * Update an existing goal by replacing the goal with the same title.
     * @param title The title of the goal to update.
     * @param updatedGoal The updated goal object.
     * @return true if the goal was updated successfully, false if not found.
     */
    public boolean updateGoal(String title, Goal updatedGoal) {
        if (goals.containsKey(title)) {
            System.out.println("Updating goal: " + title);
            goals.put(title, updatedGoal);
            save_goals_to_file();
            return true;
        }
        System.out.println("Goal not found for update: " + title);
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
    public List<Goal> getGoals(){
        return new ArrayList<>(goals.values());
    }

    /**
     * Return the number of goals.
     * @return number of goals
     */
    public int getGoalCount() {
        return goals.size();
    }
      /**
     * Complete a goal;
     * @param goal The goal to complete.
     */
    public void completeGoal(Goal goal) {
        goal.setCompleted(true);
        //System.out.println(1111111);
        notifySubscribers(); // Make sure the view refreshes
        updateGoal(goal.getTitle(), goal);
        //save_goals_to_file();
        //deleteGoal(goal.getTitle());
        
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

    /**
     * Returns a list of goals that belong to the given section.
     * @param section the section name
     * @return list of goals in that section
     */
    public List<Goal> getGoalsForSection(String section) {
        List<Goal> sectionGoals = new ArrayList<>();
        for (Goal goal : goals.values()) {
            if (goal.getSection().equalsIgnoreCase(section)) {
                sectionGoals.add(goal);
            }
        }
        return sectionGoals;
    }

    /**
     * Sets the difficulty filter and notifies subscribers (View).
     * The View will update itself based on the new filter.
     */
    public void setFilteredDifficulty(String difficulty) {
        this.currentFilter = difficulty;
        notifySubscribers();
    }

    /**
     * Returns a list of goals filtered by difficulty.
     * @param difficulty the difficulty level to filter by (e.g., "Easy", "Medium", "Hard").
     *                   If "All" is passed, it returns all goals.
     * @return list of goals that match the given difficulty.
     */
    public List<Goal> getGoalsByDifficulty(String difficulty) {
        if ("All".equalsIgnoreCase(difficulty)) {
            return getGoals();
        }
        List<Goal> filteredGoals = new ArrayList<>();
        for (Goal goal : goals.values()) {
            if (goal.getDifficulty() != null && goal.getDifficulty().equalsIgnoreCase(difficulty)) {
                filteredGoals.add(goal);
            }
        }
        return filteredGoals;
    }


    /**
     * Notify the subscribers of this model that the data has changed.
     */
    public void notifySubscribers() {
        // create a copy of the subscribers list before iterating
        List<Subscriber> subscribersCopy = new ArrayList<>(subscribers);

        for (Subscriber subscriber : subscribersCopy) {
            subscriber.modelUpdated();  // update each subscriber safely
        }
    }

    // Unit Testing :)
    public static void main(String[] args) {
        GoalModel model = new GoalModel();
        // Create some goals
        Goal goal1 = new Goal("Run a marathon", "Fitness", "Hard",
                LocalDate.of(2025, 2, 1), LocalDate.of(2025, 6, 1), false);
        Goal goal2 = new Goal("Read 10 books","Personal", "Medium", LocalDate.of(2025, 3, 1), LocalDate.of(2025, 7, 31), false);

        //test addGoal
        model.addGoal(goal1);
        model.addGoal(goal2);

        //list all goals
        System.out.println("Goals:");
        for (Goal goal : model.getGoals()) {
            System.out.println(goal);
        }

        // test deleteGoal
        model.deleteGoal("Run a marathon");
        // list goals again to see if the goal is deleted
        System.out.println("\nGoals after deletion:");
        for (Goal goal : model.getGoals()) {
            System.out.println(goal);
        }

        //test updateGoal
        Goal newGoal = new Goal("Learn how to cook soup", LocalDate.now(), LocalDate.now().plusDays(3));
        model.updateGoal("Read 10 books", newGoal);

        //test save_to_file and load_from_file (saving should have happened when adding/updating goals)
        System.out.println("Goals loaded from file:");
        GoalModel emptyModel = new GoalModel();
        for (Goal goal: emptyModel.getGoals()) {
            System.out.println(goal.toString());
        }
    }

}

