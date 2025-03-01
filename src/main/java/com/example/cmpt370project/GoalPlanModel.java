package com.example.cmpt370project;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The GoalPlanModel holds all the GoalPlan data in the application.
 */
public class GoalPlanModel {

    /**
     * JSON serialize and deserialize.
     */

    // Same code as what Mari used for the GoalModel (we should make this an interface/abstract class later?)

    private static final String fileName = System.getProperty("user.home") + "/GoalApplication/goalPlan.json";
    private static final DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE; //YYYY-MM-DD
    //this creates a custom gson instance (with the format for the date) I'll try to find a better way to do this but stack overflow told me to do this :(
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
            context.serialize(src.format(format))).registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
            LocalDate.parse(json.getAsJsonPrimitive().getAsString(), format)).create(); //basically this is saying...
    //when saving a LocalDate, convert it to a string ("2025-02-21") and when loading a LocalDate, read the string and convert it back

    /**
     * The current goalPlan the user has setup
     */
    private GoalPlan goalPlan;

    /**
     * The subscriber list (i.e. the view), which will update when the view changes.
     */
    private List<Subscriber> subscribers;

    public GoalPlanModel() {
        subscribers = new ArrayList<Subscriber>();
        loadDataFromFile();
    }

    public boolean goalPlanExists() {
        return goalPlan != null;
    }

    public void setGoalPlan(GoalPlan goalPlan) {
        this.goalPlan = goalPlan;
        saveDataToFile();
        notifySubscribers();
    }

    public GoalPlan getGoalPlan() {
        return goalPlan;
    }

    public void setGoalPlanCurrent(int goalPlanCurrent) throws IllegalStateException {
        if (goalPlanExists()) {
            this.goalPlan.setGoalPlanCurrent(goalPlanCurrent);
            saveDataToFile();
            notifySubscribers();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }

    }

    public void setGoalPlanMax(int goalPlanMax) throws IllegalStateException {
        if (goalPlanExists()) {
            this.goalPlan.setGoalPlanMax(goalPlanMax);
            saveDataToFile();
            notifySubscribers();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }

    }

    public int getGoalPlanCurrent() throws IllegalStateException {
        if (goalPlanExists()) {
            return this.goalPlan.getGoalPlanCurrent();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }
    }

    public int getGoalPlanMax() {
        if (goalPlanExists()) {
            return this.goalPlan.getGoalPlanMax();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }
    }

    /**
     * Notify the subscribers of this model that the data has changed.
     */
    public void notifySubscribers() {
        subscribers.forEach(Subscriber::modelUpdated);
    }

    // **************** JSON serialize and deserialize methods ****************

    /**
     * Checks if file exists before reading or writing to it.
     * If it doesn't, it creates an empty file
     */
    private void checkIfFileExists(){
        try {
            Path filePath = Paths.get(fileName);
            //check if the GoalApplication directory exists
            if (Files.notExists(filePath.getParent())){
                Files.createDirectories(filePath.getParent());
            }
            //check if goals.json exists
            if (Files.notExists(filePath)){
                Files.createFile(filePath);
            }
        }
        catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Save data to file (in JSON format).
     */
    public void saveDataToFile() {
        checkIfFileExists();
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(goalPlan, writer);
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    /**
     * Load goals from file (in JSON format).
     */
    public void loadDataFromFile() {
        checkIfFileExists();
        //handling empty file case... (only load if file is not empty, else skip bc there's nothing to read)
        File file = new File(fileName);
        if (file.exists() && file.length() != 0) {
            try (Reader reader = new FileReader(fileName)){

//                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
//                String type = jsonObject.get("type").getAsString();
//
//                GoalPlan dataFromJson = null;
//
//                // Choose the appropriate class based on the type field
//                if ("ConcreteGoalPlan1".equals(type)) {
//                    dataFromJson = gson.fromJson(jsonObject, ConcreteGoalPlan1.class);
//                } else if ("ConcreteGoalPlan2".equals(type)) {
//                    dataFromJson = gson.fromJson(jsonObject, ConcreteGoalPlan2.class);
//                } else {
//                    System.err.println("Unknown goal plan type: " + type);
//                }

                // Okay with this stuff here, you need to make the goal class thing from above abstract me thinks,
                // so have goal plan type attribute common to both
                // cause json data needs to be loaded in based on attribute (need to load concrete class)

                // then adjust the json test script for this class so it works everytime and loads data
                // ensure all methods that need to load do it properly


                GoalPlan dataFromJson = gson.fromJson(reader,GoalPlan.class);
                setGoalPlan(dataFromJson);
            }
            catch (IOException e){
                System.err.println("Error loading goals from file: " + e.getMessage());
            }
        }
    }


    /**
     * Unit and Regression Testing for this class and the Goal Plan features.
     */
    public static void main(String[] args) {

        GoalPlanModel goalPlanModel = new GoalPlanModel();

        // *************************************** UNIT TESTING ***************************************

        // Test 1: Plan Exists
        if (goalPlanModel.goalPlanExists()) {
            System.out.println("Test 1 Error: Goal Plan should not exist");
        }

        // Test 2: Set current goal number on empty plan
        try {
            goalPlanModel.setGoalPlanCurrent(10);
            System.out.println("Test 2 Error: Exception expected on calling setter for null goal plan.");
        } catch (IllegalStateException e) {
            // Expected
        } catch (Exception e) {
            System.out.println("Test 2 Error: Unexpected on calling setter for null goal plan.");
        }

        // Test 3: Set max goal number on empty plan
        try {
            goalPlanModel.setGoalPlanMax(10);
            System.out.println("Test 3 Error: Exception expected on calling setter for null goal plan.");
        } catch (IllegalStateException e) {
            // Expected
        } catch (Exception e) {
            System.out.println("Test 3 Error: Unexpected on calling setter for null goal plan.");
        }

        // Test 4: Get current goal number on empty plan
        try {
            goalPlanModel.getGoalPlanCurrent();
            System.out.println("Test 4 Error: Exception expected on calling getter for null goal plan.");
        } catch (IllegalStateException e) {
            // Expected
        } catch (Exception e) {
            System.out.println("Test 4 Error: Unexpected on calling getter for null goal plan.");
        }

        // Test 5: Get max goal number on empty plan
        try {
            goalPlanModel.getGoalPlanMax();
            System.out.println("Test 5 Error: Exception expected on calling getter for null goal plan.");
        } catch (IllegalStateException e) {
            // Expected
        } catch (Exception e) {
            System.out.println("Test 5 Error: Unexpected on calling getter for null goal plan.");
        }

        // Test 6: Get goal plan on empty plan
        if (goalPlanModel.getGoalPlan() != null) {
            System.out.println("Test 6 Error: Expected null on calling getter for null goal plan.");
        }

        // Test 7: Set goal plan
        GoalPlan mgp = new MaintainGoalPlan(10);
        goalPlanModel.setGoalPlan(mgp);

        if (!goalPlanModel.goalPlanExists()) {
            System.out.println("Test 7 Error: Goal Plan was not created.");
        }

        System.out.println("Unit Tests Complete.");

        // Test JSON file save and load

//        // Test save and load methods (saving should have happened when adding/updating goals)
//        System.out.println("Goals loaded from file:");
//        GoalModel emptyModel = new GoalModel();
//        for (Goal goal: emptyModel.getGoals()) {
//            System.out.println(goal.toString());
//        }

    }
}
