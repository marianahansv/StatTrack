package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.List;

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
    private UserHistoryDataModel userHistoryDataModel;

    /**
     * The root of this view.
     */
    private VBox root;

    // ********* Add other ui elements as attributes here if needed (i.e. if they need to change in drawView()) *********

    private ListView<Goal> goalListView;

    private ListView<Goal> goalListViewGoal;

    private Label progressFeedback;

    private ComboBox<String> difficultyComboBox;

    private Button completeGoalButton;

    /**
     * Create a new goal view page.
     */
    public GoalView() {
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(5);
        root.setPadding(new Insets(10));

        Label goalsLabel = new Label("My Goals:");
        goalListView = new ListView<>();
        goalListViewGoal = new ListView<>();
        root.getChildren().addAll(goalsLabel, goalListView, goalListViewGoal);

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
        if (goalModel == null) return;

        goalListView.getItems().clear();
        //goalListViewGoal.getItems().clear();
        /*for (Goal goal : goalModel.getGoals()) {
            goalListView.getItems().add(goal.toString());
        }*/

        for (Goal goal : goalModel.getGoals()) {
            goalListView.getItems().add(goal);
        }

        

        root.getChildren().clear();

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
                    timelineCompleted = userHistoryDataModel.getDailyCompletedGoals();
                }
                case WEEKLY -> {
                    planTimelineString = "WEEK";
                    timelineCompleted = userHistoryDataModel.getWeeklyCompletedGoals();
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

        // Add all UI elements to this UI view
        root.getChildren().addAll(goalListView, goalProgressModule, difficultyComboBox, completeGoalButton);
    
        
        completeGoalButton.setOnAction(e -> {
             Goal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
             if (selectedGoal != null && !selectedGoal.isCompleted() && goalController != null) {
                goalController.completeGoal(selectedGoal);
            } 
        });
    }




    /**
     * Set the goal model of this view.
     * @param goalModel the goal model that this view will pull data from.
     */
    public void setGoalModel(GoalModel goalModel) {
        this.goalModel = goalModel;

        if (userHistoryDataModel != null && goalPlanModel != null) {
            modelUpdated();
        }
    }

    /**
     * Set the goal plan Model for this view.
     * @param gpModel the goal plan Model for this view.
     */
    public void setGoalPlanModel(GoalPlanModel gpModel) {
        this.goalPlanModel = gpModel;

        if (userHistoryDataModel != null && goalModel != null) {
            modelUpdated();
        }
    }

    /**
     * Set the user data model for this view.
     * @param userHistoryDataModel the user data model for this view.
     */
    public void setUserHistoryDataModel(UserHistoryDataModel userHistoryDataModel) {
        this.userHistoryDataModel = userHistoryDataModel;

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
