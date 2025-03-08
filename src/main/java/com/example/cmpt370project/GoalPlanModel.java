package com.example.cmpt370project;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The GoalPlanModel holds all the goal plan data in the application.
 */
public class GoalPlanModel {

    /**
     * The file where the json goal plan data will be stored.
     */
    private static final String fileName = System.getProperty("user.home") + "/GoalApplication/goalPlan.json";

    /**
     * Handles JSON serialization and deserialization.
     */
    private static final Gson gson = new GsonBuilder().create();

    /**
     * The current IGoalPlan the user has set up
     */
    private IGoalPlan IGoalPlan;

    /**
     * The subscriber list (i.e. the view(s)), which will update when the model changes.
     */
    private List<Subscriber> subscribers;

    /**
     * Create a new goal plan model.
     */
    public GoalPlanModel() {
        subscribers = new ArrayList<Subscriber>();
        loadDataFromFile();
    }

    /**
     * Is there a current active goal plan?
     * @return true if a goal plan exists, false otherwise.
     */
    public boolean goalPlanExists() {
        return IGoalPlan != null;
    }

    /**
     * Set the goal plan of this model.
     * @param IGoalPlan the new goal plan to be followed.
     */
    public void setGoalPlan(IGoalPlan IGoalPlan) {
        this.IGoalPlan = IGoalPlan;
        saveDataToFile();
        notifySubscribers();
    }

    /**
     * Get the current goal plan.
     * @return the current goal plan.
     */
    public IGoalPlan getGoalPlan() {
        return IGoalPlan;
    }

    /**
     * Clear the current goal plan.
     */
    public void clearGoalPlan() {
        IGoalPlan = null;
        saveDataToFile();
        notifySubscribers();
    }

    /**
     * Set the current number of goals to be working towards for the active goal plan.
     * @param goalPlanCurrent the new current number of goals to be working towards.
     * @throws IllegalStateException if !goalPlanExists()
     */
    public void setGoalPlanCurrent(int goalPlanCurrent) throws IllegalStateException {
        if (goalPlanExists()) {
            this.IGoalPlan.setGoalPlanCurrent(goalPlanCurrent);
            saveDataToFile();
            notifySubscribers();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }

    }

    /**
     * Set the maximum number of goals to be working towards for the active goal plan.
     * @param goalPlanMax the new maximum number of goals to be working towards for the active goal plan.
     * @throws IllegalStateException if !goalPlanExists() or !canEditMax() for the goal plan.
     */
    public void setGoalPlanMax(int goalPlanMax) throws IllegalStateException {
        if (goalPlanExists()) {
            if (this.IGoalPlan.canEditMax()) {
                this.IGoalPlan.setGoalPlanMax(goalPlanMax);
                saveDataToFile();
                notifySubscribers();
            } else {
                throw new IllegalStateException("The max goal number can not be set for this plan.");
            }

        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }
    }

    /**
     * Get the current number of goals to be working towards for the active goal plan.
     * @return the current number of goals to be working towards for the active goal plan.
     * @throws IllegalStateException if !goalPlanExists()
     */
    public int getGoalPlanCurrent() throws IllegalStateException {
        if (goalPlanExists()) {
            return this.IGoalPlan.getGoalPlanCurrent();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }
    }

    /**
     * Get the maximum number of goals to be working towards for the active goal plan.
     * @return the maximum number of goals to be working towards for the active goal plan.
     */
    public int getGoalPlanMax() {
        if (goalPlanExists()) {
            return this.IGoalPlan.getGoalPlanMax();
        } else {
            throw new IllegalStateException("Goal plan does not yet exist.");
        }
    }

    /**
     * Check if the maximum number of goals to be working towards for the active goal plan can be changed.
     * @return true if the maximum number of goals to be working towards for the active goal plan can be changed.
     */
    public boolean canEditGoalPlanMax() {
        if (goalPlanExists()) {
            return this.IGoalPlan.canEditMax();
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

    /**
     * Add a subscriber of this model.
     */
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
        notifySubscribers();
    }

    // **************** JSON serialization and deserialization methods ****************

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
            //check if goalPlan.json exists
            if (Files.notExists(filePath)){
                Files.createFile(filePath);
            }
        }
        catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Checks if file the JSON file is empty.
     * @return true if the file has no data, false otherwise.
     */
    public boolean isJsonFileEmpty(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            // If the parsed element is null or empty, return true (empty file)
            return jsonElement.isJsonNull() || jsonElement.getAsJsonObject().size() == 0;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return true;  // Treat as empty in case of an error (file not readable)
        }
    }

    /**
     * Save the data to the file of this class (in JSON format).
     */
    public void saveDataToFile() {
        checkIfFileExists();
        try (Writer writer = new FileWriter(fileName)) {
            // Save the Goal Plan to the file

            // Is there a current goal pla active?
            if (IGoalPlan == null) {
                // If IGoalPlan is null, write an empty JSON object to clear the file
                gson.toJson(new JsonObject(), writer);
                return;
            }

            JsonObject jsonObject = new JsonObject();

            // Add a "type" field based on the class of the IGoalPlan (to be used for deserialization)
            if (IGoalPlan instanceof MaintainGoalPlan) {
                jsonObject.addProperty("type", "MaintainGoalPlan");
            } else if (IGoalPlan instanceof IncreaseGoalPlan) {
                jsonObject.addProperty("type", "IncreaseGoalPlan");
            }

            // Serialize the IGoalPlan object and add all other properties to the jsonObject
            JsonObject goalPlanJson = gson.toJsonTree(IGoalPlan).getAsJsonObject();

            // Add all properties from goalPlanJson to the jsonObject (so "type" attribute appears first)
            for (Map.Entry<String, JsonElement> entry : goalPlanJson.entrySet()) {
                jsonObject.add(entry.getKey(), entry.getValue());
            }

            gson.toJson(jsonObject, writer);

        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    /**
     * Load goals from file (in JSON format).
     */
    public void loadDataFromFile() {
        checkIfFileExists();
        // Only load if file is not empty, otherwise skip because there's nothing to read
        if (!isJsonFileEmpty(fileName)) {
            try (Reader reader = new FileReader(fileName)){

                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                String type = jsonObject.get("type").getAsString();

                IGoalPlan dataFromJson = null;

                // Choose the appropriate class based on the "type" field
                if ("MaintainGoalPlan".equals(type)) {
                    dataFromJson = gson.fromJson(jsonObject, MaintainGoalPlan.class);
                } else if ("IncreaseGoalPlan".equals(type)) {
                    dataFromJson = gson.fromJson(jsonObject, IncreaseGoalPlan.class);
                } else {
                    System.err.println("Unknown goal plan type: " + type);
                }

                // Load into the model data
                setGoalPlan(dataFromJson);
            }
            catch (IOException e){
                System.err.println("Error loading goals from file: " + e.getMessage());
            }
        }
    }


    /**
     * Unit and Regression Testing for this class's and the Goal Plan features and JSON methods.
     */
    public static void main(String[] args) {

        GoalPlanModel goalPlanModel = new GoalPlanModel();
        goalPlanModel.clearGoalPlan(); // Start testing with empty goal plan

        // *************************************** UNIT TESTING ***************************************

        // Test 1: Test Plan Exists on empty plan
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
        IGoalPlan mgp = new MaintainGoalPlan(10);
        goalPlanModel.setGoalPlan(mgp);

        if (!goalPlanModel.goalPlanExists()) {
            System.out.println("Test 7 Error: Goal Plan was not created.");
        }

        // *************************************** JSON TESTING ***************************************

        // Test 8: Test save of maintain goal plan (write and read)
        GoalPlanModel emptyGoalPlanModel = new GoalPlanModel();
        String expectedOutput = "Goal Plan Type: MaintainGoalPlan Goals to Maintain: 10";
        if (!emptyGoalPlanModel.goalPlanExists() || !Objects.equals(emptyGoalPlanModel.getGoalPlan().toString(), expectedOutput)) {
            System.out.println("Test 8 Error: Did not find expected goal plan.");
        }

        // Test 9: Test save of maintain goal plan (write and read)
        IGoalPlan igp = new IncreaseGoalPlan(3, 8);
        emptyGoalPlanModel.setGoalPlan(igp);

        emptyGoalPlanModel = new GoalPlanModel();
        expectedOutput = "Goal Plan Type: IncreaseGoalPlan Current Goal Number: 3 Max Goal Number: 8";
        if (!emptyGoalPlanModel.goalPlanExists() || !Objects.equals(emptyGoalPlanModel.getGoalPlan().toString(), expectedOutput)) {
            System.out.println("Test 9 Error: Did not find expected goal plan.");
        }

        // Test 10: Test clear goal plan (write and read)
        emptyGoalPlanModel.clearGoalPlan();

        emptyGoalPlanModel = new GoalPlanModel();
        if (emptyGoalPlanModel.goalPlanExists()) {
            System.out.println("Test 10 Error: Goal Plan was not cleared.");
        }


        System.out.println("Test Script Complete.");
    }
}
