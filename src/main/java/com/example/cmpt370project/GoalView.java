package com.example.cmpt370project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View to handle organization of UI elements and page(s) related to viewing Goals.
 */
public class GoalView extends StackPane implements Subscriber {

    // ********* The models that this view requires data from are to be added here! *********

    /**
     * The goal model that this view gets goal data from.
     */
    private GoalModel goalModel;
    
    private GoalController goalController; // Controller need to display button

    /**
     * The goal plan model that this view gets goal data from.
     */
    private GoalPlanModel goalPlanModel;

    /**
     * The user data model that this view gets goals completed data.
     */
    private UserHistoryDataModel historyModel;

    /**
     * The root of this view.
     */
    private VBox root;

    // ********* Add other ui elements as attributes here if needed (i.e. if they need to change in drawView()) *********

    private ListView<Goal> goalListView;

    // private ListView<Goal> goalListViewGoal;

    private Label progressFeedback;
    private Label welcomeLabel;

    private ComboBox<String> difficultyComboBox;

    private Button completeGoalButton;

    private Button editGoalButton;

    
    

    /**
     * Create a new goal view page.
     */
    public GoalView() {
        /*UserHistoryDataModel historyModel = new UserHistoryDataModel();
        GoalModel goalModel = new GoalModel(historyModel);
        goalModel.setUserHistoryDataModel(historyModel);*/


        root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        Label goalsLabel = new Label("My Goals:");
        goalListView = new ListView<>();
        //goalListViewGoal = new ListView<>();goalListViewGoal
        root.getChildren().addAll(goalsLabel, goalListView);

        // Add the root UI element to this view
        this.getChildren().add(root);

        // Initialize all other UI elements
        progressFeedback = new Label();
    }

    

    public void setGoalController(GoalController controller) {
    this.goalController = controller;
    }


    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        //setGoalController(goalController);
        
        if (goalModel == null) return;

        goalListView.getItems().clear();
        //goalListViewGoal.getItems().clear();
        /*for (Goal goal : goalModel.getGoals()) {
            goalListView.getItems().add(goal.toString());
        }*/

        for (Goal goal : goalModel.getGoals()) {
            
            if (goal.isCompleted()) {
            //setStyle("-fx-text-fill: green;");

            

            }
            goalListView.getItems().add(goal);
        }

        

        root.getChildren().clear();

        welcomeLabel = new Label("Here's Your Current Goals:");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        // ********* MOTIVATIONAL FEEDBACK MODULE *********
        VBox goalProgressModule = new VBox(20);
        goalProgressModule.setAlignment(Pos.CENTER_LEFT);
        goalProgressModule.setStyle("-fx-background-color: lightgray; -fx-background-radius: 5;");
        goalProgressModule.setPadding(new Insets(20));

        if (goalPlanModel.goalPlanExists()) {

            // Get the completed goal values whether it is the week/day
            String planTimelineString = "";
            int timelineCompleted = 0;

            switch(goalPlanModel.getGoalPlanTimeline()) {
                case DAILY -> {
                    planTimelineString = "DAY";
                    timelineCompleted = historyModel.getDailyCompletedGoals();
                }
                case WEEKLY -> {
                    planTimelineString = "WEEK";
                    timelineCompleted = historyModel.getWeeklyCompletedGoals();
                    System.out.println(timelineCompleted + " 99");

                }
            }

            int progressDiff = goalPlanModel.getGoalPlan().getGoalPlanCurrent() - timelineCompleted;
            String progressMessage = "";

            if (progressDiff > 0) {
                progressMessage = "You need to complete " + progressDiff + " more goals today to stay on track with your goal plan. Time to complete some goals!";
            } else if (progressDiff == 0){
                progressMessage = "You have met your target for the " + planTimelineString + " and are currently on track with you goal plan. Props to you!";
            } else {
                progressMessage = "You have completed " + -progressDiff + " more goals than your target number of goals for your goal plan. Overachiever!";
            }

            progressFeedback = new Label(progressMessage);

        } else {
            progressFeedback.setText("Want to be motivated to complete more goals? Head over to the goal plan tab to set up a goal plan!");
        }

        progressFeedback.setWrapText(true);
        goalProgressModule.getChildren().add(progressFeedback);
        // ********* END OF MOTIVATIONAL FEEDBACK MODULE *********

        // 🔥 Goal difficulty filtering button 🔥
        goalModel.addSubscriber(this);
        // instantiate and configure the ComboBox for filtering difficulties
        difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll("All", "Easy", "Medium", "Hard");
        difficultyComboBox.setValue("Filter"); // default
        // updates the goals list when the selection changes
        difficultyComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateFilteredGoals());
        
        completeGoalButton = new Button("Complete");

        editGoalButton = new Button("Edit Goal");

        VBox dashboardControls = new VBox();
        dashboardControls.setAlignment(Pos.TOP_CENTER);
        dashboardControls.setSpacing(5);
        //goalListView.setStyle("-fx-text-fill: red;");
       


        dashboardControls.getChildren().addAll(goalListView, goalProgressModule, difficultyComboBox, completeGoalButton, editGoalButton);
        //goalListView.setStyle("-fx-text-fill: red;");

        // Add all UI elements to this UI view


        //root.getChildren().addAll(goalListView, goalProgressModule, difficultyComboBox, completeGoalButton, editGoalButton);
        //System.out.println(goalListView.getItems());

        //root.getChildren().addAll(goalProgressModule, difficultyComboBox, completeGoalButton, editGoalButton);
        //System.out.println(goalListView.getItems());

        goalListView.getItems();
    
        
        completeGoalButton.setOnAction(e -> {
            
             Goal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
             // System.out.println(selectedGoal);
             goalModel.completeGoal(selectedGoal);
             // selectedGoal.setCompleted(true);
             // goalModel.notifySubscribers();

             /**if (selectedGoal != null && !selectedGoal.isCompleted() && goalController != null) {
                goalController.completeGoal(selectedGoal); // Controller not needed
           
                } **/
        });


        editGoalButton.setOnAction(e -> {
            
            Goal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
            // Goal oldGoal = goalListView.getSelectionModel().getSelectedItem();
            // System.out.println(selectedGoal);
            selectedGoal.setEndDate(selectedGoal.getEndDate().plusDays(1));
            goalModel.updateGoal(selectedGoal.getTitle(), selectedGoal);
            goalModel.notifySubscribers();
            drawEditGoalView(selectedGoal);
            //System.out.println(4);
       });

        root.getChildren().addAll(welcomeLabel, dashboardControls);
    }
    private void drawEditGoalView(Goal goal) { // cant edit twive in a row?
        // Clear current page
        this.getChildren().clear();

        SectionModel sectionModel = new SectionModel();

        

        // Create a new VBox to hold the edit form
        VBox editRoot = new VBox(20);
        editRoot.setAlignment(Pos.TOP_LEFT);
        editRoot.setPadding(new Insets(20));
    
        // Create a TextField for editing the goal title and use prior title
        TextField titleField = new TextField(goal.getTitle());
    
        // The difficulty combo box
        ComboBox<String> editDifficultyComboBox = new ComboBox<>();
        editDifficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        editDifficultyComboBox.setValue(goal.getDifficulty());
    
        // Create DatePickers for the start and end dates and set their values
        DatePicker editStartDatePicker = new DatePicker(goal.getStartDate());
        DatePicker editEndDatePicker = new DatePicker(goal.getEndDate());
    
        // Create an HBox and a new ToggleGroup for the section buttons
        HBox editSectionButtons = new HBox(10);
        ToggleGroup editSectionToggleGroup = new ToggleGroup();
    
        // Get the sections from the model and create ToggleButtons for each
        List<String> sections = new ArrayList<>(sectionModel.getSections());
        for (String section : sections) {
            //System.out.println(section);
            ToggleButton sectionButton = new ToggleButton(section);
            sectionButton.setToggleGroup(editSectionToggleGroup);
            if (section.equals(goal.getSection())) {
                sectionButton.setSelected(true);
            }
            editSectionButtons.getChildren().add(sectionButton);
        }
    
        // Create buttons for saving or canceling the edit
        Button submitEditButton = new Button("Save Changes");
        Button cancelEditButton = new Button("Cancel");
    
        // Set up the event handler for the Save button
        submitEditButton.setOnAction(e -> {
            String OGTitle = (goal.getTitle());
            String newTitle = titleField.getText();
            String newDifficulty = editDifficultyComboBox.getValue();
            Toggle selectedToggle = editSectionToggleGroup.getSelectedToggle();
            if (selectedToggle == null) {
                // Handle the error, e.g., alert the user that a section must be selected
                return;
            }
            String newSection = ((ToggleButton) selectedToggle).getText();
            LocalDate newStartDate = editStartDatePicker.getValue();
            LocalDate newEndDate = editEndDatePicker.getValue();
            
            // Update the goal object with new values
            goal.setTitle(newTitle);
            goal.setDifficulty(newDifficulty);
            goal.setSection(newSection);
            goal.setStartDate(newStartDate);
            goal.setEndDate(newEndDate);
            
            // Update the model and notify subscribers of the change
            goalModel.updateGoal(OGTitle, goal);
            goalModel.notifySubscribers();
            //OGTitle = goal.getTitle();

            historyModel.notifySubscribers();
            historyModel.completeGoal(LocalDate.now());
            historyModel.saveDataToFile();
            
            this.getChildren().clear();
            this.getChildren().add(root);
            drawView();
        });
    
        // Set up the event handler for the Cancel button to return to the home view
        cancelEditButton.setOnAction(e -> {
            //getChildren().clear();
            this.getChildren().clear();
            this.getChildren().add(root);
            drawView();
        });
    
        // Build the edit form view
        editRoot.getChildren().addAll(
            new Label("Edit Goal Title:"), titleField,
            new Label("Difficulty:"), editDifficultyComboBox,
            new Label("Sections:"), editSectionButtons,
            new Label("Start Date:"), editStartDatePicker,
            new Label("End Date:"), editEndDatePicker,
            submitEditButton, cancelEditButton
        );
    
        // Add the edit view to the scene
        this.getChildren().add(editRoot);
    }
    




    /**
     * Set the goal model of this view.
     * @param goalModel the goal model that this view will pull data from.
     */
    public void setGoalModel(GoalModel goalModel) {
        this.goalModel = goalModel;
        goalModel.addSubscriber(this);

        if (historyModel != null && goalPlanModel != null) {
            modelUpdated();
        }
    }

    /**
     * Set the goal plan Model for this view.
     * @param gpModel the goal plan Model for this view.
     */
    public void setGoalPlanModel(GoalPlanModel gpModel) {
        this.goalPlanModel = gpModel;

        if (historyModel != null && goalModel != null) {
            modelUpdated();
        }
    }

    /**
     * Set the user data model for this view.
     * @param userHistoryDataModel the user data model for this view.
     */
    public void setUserHistoryDataModel(UserHistoryDataModel historyModel) {
        this.historyModel = historyModel;

        if (goalPlanModel != null && goalModel != null) {
            modelUpdated();
        }
    }

    /**
     * Updates the goal list based on the currently selected difficulty filter
     */
    private void updateFilteredGoals() {
        if (goalModel == null) return;

        String selectedDifficulty = difficultyComboBox.getValue();
        List<Goal> filteredGoals = goalModel.getGoalsByDifficulty(selectedDifficulty);

        goalListView.getItems().clear();
        for (Goal goal : filteredGoals) {
            goalListView.getItems().add(goal); //toString()
        }
    }

    /**
     * Set up interaction with a controller for this view.
     * @param controller the controller that will handle changing model data for user interactions on this page.
     */
    public void setFilterChangeListener(GoalController controller) {
        difficultyComboBox.setOnAction(event -> {
            controller.filterGoals(difficultyComboBox.getValue());
        });
    }

    @Override
    public void modelUpdated() {
        drawView();
    }

    /**
     * Set up interaction with a controller for this view.
     * @param c the controller that will handle changing model data for user interactions on this page.
     */
    public void setupEvents(HomeController c) {

        // See HomeView class for what to put here.
        // i.e. when ready to change data in interface based on user interactions, make a new controller class
        // and pass the button handler methods (and other interactive ui elements) of the buttons in this view to the controller

    }
}
