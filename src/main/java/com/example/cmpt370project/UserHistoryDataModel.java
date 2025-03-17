package com.example.cmpt370project;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Holds all data related to the user and their current/previous goa behaviours.
 */
public class UserHistoryDataModel {

    /**
     * The file where the json goal plan data will be stored.
     */
    private static final String fileName = System.getProperty("user.home") + "/GoalApplication/userHistoryData.json";

    /**
     * Handles JSON serialization and deserialization.
     */
    private static final Gson gson = new GsonBuilder().create();

    /**
     * The name of the user.
     */
    private String userName;

    /**
     * The number of completed goals for the current day, and week.
     */
    private int dailyCompletedGoals, weeklyCompletedGoals;

    /**
     * The current day, and week, that this model holds the current completed numbers for.
     */
    private LocalDate dataDay, dataWeek;

    /**
     * The subscriber list (i.e. the view(s)), which will update when the model changes.
     */
    private List<Subscriber> subscribers;

    /**
     * Create a new goal plan model.
     */
    public UserHistoryDataModel() {
        subscribers = new ArrayList<Subscriber>();

        // set default values in case there is no data to load
        dailyCompletedGoals = 0;
        weeklyCompletedGoals = 0;
        dataDay = LocalDate.now();
        dataWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        userName = "User";

        loadDataFromFile();
    }

    /**
     * Checks if the username has been set.
     * @return true if the username is not null, false otherwise.
     */
    public boolean userNameExists() {
        return userName != null;
    }

    /**
     * Gets the username if it has been set.
     * @return the username
     * @throws IllegalStateException if the username has not been set yet.
     */
    public String getUserName() throws IllegalStateException {
        if (!userNameExists()) {
            throw new IllegalStateException("There is no user name set yet.");
        }
        return userName;
    }

    /**
     * Sets the username.
     * @param userName the username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
        notifySubscribers();
        saveDataToFile();
    }

    /**
     * Gets the number of goals completed today.
     * @return the number of goals completed today.
     */
    public int getDailyCompletedGoals() {
        return dailyCompletedGoals;
    }

    /**
     * Sets the number of goals completed today (i.e. for the date stored in this model).
     * @param dailyCompletedGoals the number of goals completed today.
     */
    public void setDailyCompletedGoals(int dailyCompletedGoals) {
        this.dailyCompletedGoals = dailyCompletedGoals;
        notifySubscribers();
        saveDataToFile();
    }

    /**
     * Gets the number of goals completed this week.
     * @return the number of goals completed this week.
     */
    public int getWeeklyCompletedGoals() {
        return weeklyCompletedGoals;
    }

    /**
     * Sets the number of goals completed this week (i.e. for the date stored in this model).
     * @param weeklyCompletedGoals the number of goals completed this week.
     */
    public void setWeeklyCompletedGoals(int weeklyCompletedGoals) {
        this.weeklyCompletedGoals = weeklyCompletedGoals;
        notifySubscribers();
        saveDataToFile();
    }

    /**
     * Gets the date of the last data update for daily data.
     * @return the date when the daily data was last updated.
     */
    public LocalDate getDataDay() {
        return dataDay;
    }

    /**
     * Gets the date for when the weekly data is stored for.
     * @return the date for when the weekly data is stored for.
     */
    public LocalDate getDataWeek() {
        return dataWeek;
    }

    /**
     * Hard resets the daily data to the specified current date.
     * @param currentDate the date to set as the new data day and reset the daily goals.
     */
    public void hardSetDayData(LocalDate currentDate) {
        dailyCompletedGoals = 0;
        dataDay = currentDate;
        notifySubscribers();
        saveDataToFile();
    }

    /**
     * Hard resets the weekly data to the specified current date, setting the data week to the most recent Sunday.
     * @param currentDate the date to set as the new data week and reset the weekly goals.
     */
    public void hardSetWeeklyData(LocalDate currentDate) {
        weeklyCompletedGoals = 0;

        // Get the Sunday (start of the week) for the new start of week
        dataWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        notifySubscribers();
        saveDataToFile();
    }

    /**
     * Hard sets all the dates for which the data will be accurate for.
     * @param currentDate the current date for which the data should now begin to reflect.
     */
    public void hardSetDataDates(LocalDate currentDate) {
        hardSetDayData(currentDate);
        hardSetWeeklyData(currentDate);

        notifySubscribers();
        saveDataToFile();
    }

    /**
     * Updates the daily and weekly completed goal values based on the current date.
     * @param currentDate the current date to compare against the data day and data week.
     * @return true if any update was made (either daily or weekly), false if no update was needed.
     */
    public boolean updateCompletedValues(LocalDate currentDate) {
        boolean updateMade = false;

        if (currentDate.isAfter(dataDay)) {
            hardSetDayData(currentDate);
            updateMade = true;
        }

        LocalDate currWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        if (currWeek.isAfter(dataWeek)) {
            hardSetWeeklyData(currentDate);
            updateMade = true;
        }

        notifySubscribers();
        saveDataToFile();

        return updateMade;
    }

    /**
     * Mark a goal as completed, and track it to the history.
     * @param currentDate the date when this goal was  marked as completed.
     */
    public void completeGoal(LocalDate currentDate) {
        updateCompletedValues(currentDate);

        if (currentDate.equals(dataDay)) {
            dailyCompletedGoals += 1;
            //System.out.println(1);
        }
        //System.out.println(2);

        LocalDate currWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        if (currWeek.equals(dataWeek)) {
            weeklyCompletedGoals += 1;
            //System.out.println(3);
        }
        //System.out.println(4);
        //updateCompletedValues(currentDate);

        notifySubscribers();
        saveDataToFile();
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
            JsonObject jsonObject = new JsonObject();

            // Save userName, dailyCompletedGoals, weeklyCompletedGoals, dataDay, dataWeek
            jsonObject.addProperty("userName", userName);
            jsonObject.addProperty("dailyCompletedGoals", dailyCompletedGoals);
            jsonObject.addProperty("weeklyCompletedGoals", weeklyCompletedGoals);

            // Convert LocalDate to String (using ISO_LOCAL_DATE format for simplicity)
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            jsonObject.addProperty("dataDay", dataDay.format(formatter));
            jsonObject.addProperty("dataWeek", dataWeek.format(formatter));

            // Serialize the JsonObject to JSON and write it to the file
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
            try (Reader reader = new FileReader(fileName)) {

                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

                // Load the attributes from the JSON object
                userName = jsonObject.get("userName").getAsString();
                dailyCompletedGoals = jsonObject.get("dailyCompletedGoals").getAsInt();
                weeklyCompletedGoals = jsonObject.get("weeklyCompletedGoals").getAsInt();

                // Convert string back to LocalDate using the ISO_LOCAL_DATE format
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                dataDay = LocalDate.parse(jsonObject.get("dataDay").getAsString(), formatter);
                dataWeek = LocalDate.parse(jsonObject.get("dataWeek").getAsString(), formatter);

            } catch (IOException e) {
                System.err.println("Error loading data from file: " + e.getMessage());
            }
        }
    }

    /**
     * Unit and Regression Testing for this class's data, methods, and JSON methods.
     */
    public static void main(String[] args) {

        // *************************************** UNIT TESTING ***************************************

        UserHistoryDataModel dataModel = new UserHistoryDataModel();
        dataModel.hardSetDataDates(LocalDate.of(2020, 1, 1));

        // Test 1: Test initialization of values on creation
        if (dataModel.getDailyCompletedGoals() != 0 || dataModel.getWeeklyCompletedGoals() != 0) {
            System.out.println("Test 1 Error: completed goal numbers not zero on initialization.");
        }

        if (!dataModel.getDataDay().equals(LocalDate.of(2020, 1, 1))) {
            System.out.println("Test 1 Error: data day does not match initialization date.");
        }

        if (!dataModel.getDataWeek().equals(LocalDate.of(2019, 12, 29))) {
            System.out.println("Test 1 Error: data week does not initialize to correct data week.");
        }

        // Test 2: Test mark goal as completed, and updating of day and week if the date in the model is outdated
        dataModel.completeGoal(LocalDate.of(2019, 12, 29));

        if (dataModel.getDailyCompletedGoals() != 0 || dataModel.getWeeklyCompletedGoals() != 1) {
            System.out.println("Test 2 Error: goal increment did not work properly for week only.");
        }

        dataModel.hardSetDataDates(LocalDate.of(2020, 1, 1));
        dataModel.completeGoal(LocalDate.of(2020, 1, 2));

        if (dataModel.getDailyCompletedGoals() != 1 || dataModel.getWeeklyCompletedGoals() != 1 || !dataModel.getDataDay().equals(LocalDate.of(2020, 1, 2))) {
            System.out.println("Test 2 Error: goal increment did not work properly for week and day, where day must be updated.");
        }

        dataModel.hardSetDataDates(LocalDate.of(2020, 1, 1));
        dataModel.completeGoal(LocalDate.of(2020, 1, 6));

        if (dataModel.getDailyCompletedGoals() != 1 || dataModel.getWeeklyCompletedGoals() != 1 ||
                !dataModel.getDataDay().equals(LocalDate.of(2020, 1, 6)) || !dataModel.getDataWeek().equals(LocalDate.of(2020, 1, 5))) {
            System.out.println("Test 2 Error: goal increment did not work properly for week and day, where day and week must be updated.");
        }

        // *************************************** JSON TESTING ***************************************

        // Test 3: Test save of data after hard setting dates and completing a goal
        dataModel = new UserHistoryDataModel();
        dataModel.hardSetDataDates(LocalDate.of(2020, 1, 1));

        if (dataModel.getDailyCompletedGoals() != 0 || dataModel.getWeeklyCompletedGoals() != 0 ||
                !dataModel.getDataDay().equals(LocalDate.of(2020, 1, 1)) || !dataModel.getDataWeek().equals(LocalDate.of(2019, 12, 29))) {
            System.out.println("Test 3 Error: JSON file save did not work correctly for setting the data dates.");
        }

        dataModel.completeGoal(LocalDate.of(2020, 1, 2));

        // Test loading from JSON
        dataModel = new UserHistoryDataModel();

        if (dataModel.getDailyCompletedGoals() != 1 || dataModel.getWeeklyCompletedGoals() != 1 || !dataModel.getDataDay().equals(LocalDate.of(2020, 1, 2))) {
            System.out.println("Test 3 Error: JSON file save did not work correctly for goal increment where day must be updated.");
        }

        System.out.println("Test Script Completed.");
    }
}
