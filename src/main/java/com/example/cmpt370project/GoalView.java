package com.example.cmpt370project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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

    /**
     * The goal Controller
     */
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

    ListView<Goal> goalListView;
    Label progressFeedback;
    private Label welcomeLabel;
    ComboBox<String> difficultyComboBox;
    ComboBox<String> completionStatusComboBox;
    Button completeGoalButton;
    Button editGoalButton;
    Button deleteGoalButton;
    
    

    /**
     * Create a new goal view page.
     */
    public GoalView() {
        // Create the main container
        root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        // Label for the goals list
        Label goalsLabel = new Label("My Goals:");

        // Initialize the list view for goals
        goalListView = new ListView<>();

        // Add the goals label and list view to the root container
        root.getChildren().addAll(goalsLabel, goalListView);

        // Add the root container to the main view (StackPane)
        this.getChildren().add(root);

        // Initialize the feedback label (will be updated in drawView)
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

        for (Goal goal : goalModel.getGoals()) {
            goalListView.getItems().add(goal);
        }
    
        // Yo Cell Factories are poggers
    goalListView.setCellFactory(lv -> new ListCell<Goal>() {  
    protected void updateItem(Goal goal, boolean empty) {
        super.updateItem(goal, empty);
        if (empty || goal == null) {
            setText(null);
            setStyle("");
        } else {
            setText(goal.toString());
            if (goal.isCompleted()) {
                // Change the text color to green if the goal is complete
                setStyle("-fx-text-fill: green;");
            } else if (!goal.isCompleted() && goal.getEndDate().isBefore(LocalDate.now())) {
                // Change the text color to red if the goal is incomplete and past due
                setStyle("-fx-text-fill: red;");
            } else {
                // Otherwise, use the default text color
                setStyle("-fx-text-fill: black;");
            }
        }
        }
        });

        root.getChildren().clear();

        // Welcome label for dashboard
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

                }
            }

            int progressDiff = goalPlanModel.getGoalPlan().getGoalPlanCurrent() - timelineCompleted;
            String progressMessage = "";

            if (progressDiff > 0) {
                progressMessage = "You need to complete " + progressDiff + " more goals for the " + planTimelineString + " to stay on track with your goal plan. Time to complete some goals!";
            } else if (progressDiff == 0){
                progressMessage = "You have met your target for the " + planTimelineString + " and are currently on track with your goal plan. Props to you!";
            } else {
                progressMessage = "You have completed " + -progressDiff + " more goals than your target number of goals for the " + planTimelineString + ". Overachiever!";
            }

            progressFeedback = new Label(progressMessage);

        } else {
            progressFeedback.setText("Want to be motivated to complete more goals? Head over to the goal plan tab to set up a goal plan!");
        }

        progressFeedback.setWrapText(true);
        progressFeedback.getStyleClass().add("bigger-paragraph-text");
        progressFeedback.setStyle("-fx-font-weight: bold;");
        goalProgressModule.getChildren().add(progressFeedback);
        goalProgressModule.setAlignment(Pos.CENTER);
        goalProgressModule.getStyleClass().add("module");
        // ********* END OF MOTIVATIONAL FEEDBACK MODULE *********

        // 🔥 Goal difficulty filtering button 🔥
        goalModel.addSubscriber(this);
        // instantiate and configure the ComboBox for filtering difficulties
        difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll("All", "Easy", "Medium", "Hard");
        difficultyComboBox.setValue("All"); // default
        // updates the goals list when the selection changes
        difficultyComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateFilteredGoals());

        // 🔥 Goal completion status filtering button 🔥
        completionStatusComboBox = new ComboBox<>();
        completionStatusComboBox.getItems().addAll("All", "Completed", "Uncompleted");
        completionStatusComboBox.setValue("All"); // default
        completionStatusComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateFilteredGoals());
        difficultyComboBox.getStyleClass().add("filter-combo");
        completionStatusComboBox.getStyleClass().add("filter-combo");

        // Container for filters
        HBox filtersContainer = new HBox(10);
        filtersContainer.setAlignment(Pos.CENTER);

        Label filterLabel = new Label("Filter Your Goals:");
        filterLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        Region filterActionsSpacer = new Region();

        filtersContainer.getChildren().addAll(
                filterLabel,
                filterActionsSpacer,
                difficultyComboBox,
                completionStatusComboBox
        );

        // ********* Action Buttons *********
        completeGoalButton = new Button("Complete Goal");
        completeGoalButton.getStyleClass().add("cbutton");
        completeGoalButton.getStyleClass().add("add-goal-button");
        completeGoalButton.setId("completeGoalButton");
        editGoalButton = new Button("Edit Goal");
        editGoalButton.getStyleClass().add("cbutton");
        editGoalButton.getStyleClass().add("edit-button");
        editGoalButton.setId("editButton");
        deleteGoalButton = new Button("Delete Goal");
        deleteGoalButton.getStyleClass().add("cbutton");
        deleteGoalButton.getStyleClass().add("cancel-button");
        deleteGoalButton.setId("deleteButton");

        Label goalActionsLabel = new Label("Selected Goal Quick Actions:");
        goalActionsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        Region goalActionsSpacer = new Region();

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
                goalActionsLabel,
                goalActionsSpacer,
                completeGoalButton,
                editGoalButton,
                deleteGoalButton
        );

        // Container for dashboard controls (list view, feedback, filtering, buttons)
        VBox dashboardControls = new VBox();
        dashboardControls.setAlignment(Pos.TOP_CENTER);
        dashboardControls.setSpacing(25);
        dashboardControls.getChildren().addAll(
                goalListView,
                goalProgressModule,
                buttonBox,
                filtersContainer
        );

        goalListView.getItems();
        
        //On Complete button pressed complete the goal
        completeGoalButton.setOnAction(e -> {
            
             Goal selectedGoal = goalListView.getSelectionModel().getSelectedItem();

             if (!selectedGoal.isCompleted()) {
                 goalModel.completeGoal(selectedGoal);
             }
        });
        
        // On edit button pressed take to editing page
        editGoalButton.setOnAction(e -> {
            
            Goal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
            goalModel.updateGoal(selectedGoal.getTitle(), selectedGoal);
            goalModel.notifySubscribers();
            drawEditGoalView(selectedGoal);
       });

        // On delete button pressed, delete the selected goal
        deleteGoalButton.setOnAction(e -> {
            Goal selectedGoal = goalListView.getSelectionModel().getSelectedItem();
            if (selectedGoal != null) {

                // Confirm to make sure the user want to delete their goal
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Goal Deletion");
                alert.setHeaderText("Are you sure you want to delete this goal from the system?");
                alert.setContentText("This action cannot be undone.");

                // Show the dialog and wait for the user's response
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        goalModel.deleteGoal(selectedGoal.getTitle());
                        goalModel.notifySubscribers();
                    }
                });
            }
        });

        root.getChildren().addAll(welcomeLabel, dashboardControls);
        goalModel.addSubscriber(this);
    }
      /**
     * Draws the edit view for a selected goal.
     * Provides form fields to modify the goal details and save or cancel changes.
     * @param goal the goal to be edited.
     */
    private void drawEditGoalView(Goal goal) {
        // Clear current view
        this.getChildren().clear();

        // Create a new container for the edit form
        VBox editRoot = new VBox(12);
        editRoot.setAlignment(Pos.TOP_LEFT);
        editRoot.setPadding(new Insets(20));

        // Text field for editing the goal title (pre-populated with current title)
        TextField titleField = new TextField(goal.getTitle());
        titleField.setId("goalNameField");

        // ComboBox for selecting the difficulty (pre-populated with current difficulty)
        ComboBox<String> editDifficultyComboBox = new ComboBox<>();
        editDifficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        editDifficultyComboBox.setValue(goal.getDifficulty());
        editDifficultyComboBox.setId("goalDifficulty");

        // DatePickers for editing start and end dates
        DatePicker editStartDatePicker = new DatePicker(goal.getStartDate());
        editStartDatePicker.setId("startDate");
        DatePicker editEndDatePicker = new DatePicker(goal.getEndDate());
        editEndDatePicker.setId("endDate");

        // Create section selection buttons using ToggleGroup
        HBox editSectionButtons = new HBox(10);
        ToggleGroup editSectionToggleGroup = new ToggleGroup();

        // Retrieve available sections from SectionModel and create ToggleButtons
        SectionModel sectionModel = new SectionModel();
        List<String> sections = new ArrayList<>(sectionModel.getSections());
        for (String section : sections) {
            ToggleButton sectionButton = new ToggleButton(section);
            sectionButton.setToggleGroup(editSectionToggleGroup);
            if (section.equals(goal.getSection())) {
                sectionButton.setSelected(true);
            }
            editSectionButtons.getChildren().add(sectionButton);
            sectionButton.getStyleClass().add("cbutton");
            sectionButton.setId(section.substring(0));
        }

        // Buttons to submit or cancel the edit
        Button submitEditButton = new Button("Save Changes");
        submitEditButton.setId("saveGoalButton");
        Button cancelEditButton = new Button("Cancel");
        cancelEditButton.setId("cancelButton");

        // Event handler for saving changes
        submitEditButton.setOnAction(e -> {
            String originalTitle = goal.getTitle();
            String newTitle = titleField.getText();
            String newDifficulty = editDifficultyComboBox.getValue();
            Toggle selectedToggle = editSectionToggleGroup.getSelectedToggle();
            if (selectedToggle == null) {
                // If no section is selected, exit the handler (could show an error dialog)
                return;
            }
            String newSection = ((ToggleButton) selectedToggle).getText();
            LocalDate newStartDate = editStartDatePicker.getValue();
            LocalDate newEndDate = editEndDatePicker.getValue();

            // Update the goal with new values
            goal.setTitle(newTitle);
            goal.setDifficulty(newDifficulty);
            goal.setSection(newSection);
            goal.setStartDate(newStartDate);
            goal.setEndDate(newEndDate);

            // Update the model and notify subscribers
            goalModel.updateGoal(originalTitle, goal);
            goalModel.notifySubscribers();

            // Update user history data
            historyModel.notifySubscribers();
            historyModel.saveDataToFile();

            // Return to the main view
            this.getChildren().clear();
            this.getChildren().add(root);
            drawView();
        });

        // Event handler for canceling the edit and returning to the main view
        cancelEditButton.setOnAction(e -> {
            this.getChildren().clear();
            this.getChildren().add(root);
            drawView();
        });

        // Title for Goal Edit
        Label editGoalTitle = new Label("Let's Edit Your Goal:");
        editGoalTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        // Build the edit form view by adding all UI elements
        editRoot.getChildren().addAll(
                editGoalTitle,
                new Region(),
                new Label("Edit Goal Title:"), titleField,
                new Region(),
                new Label("Difficulty:"), editDifficultyComboBox,
                new Region(),
                new Label("Sections:"), editSectionButtons,
                new Region(),
                new Label("Start Date:"), editStartDatePicker,
                new Region(),
                new Label("End Date:"), editEndDatePicker,
                new Region(),
                new HBox(10, submitEditButton, cancelEditButton)
        );

        // Styling of the form
        titleField.setMaxWidth(300);
        submitEditButton.getStyleClass().add("cbutton");
        submitEditButton.getStyleClass().add("add-goal-button");
        cancelEditButton.getStyleClass().add("cbutton");
        cancelEditButton.getStyleClass().add("cancel-button");


        // Display the edit form in the view
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
     * @param historyModel the user data model for this view.
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
        String selectedCompletionStatus = completionStatusComboBox.getValue();

        System.out.println("Filtering goals - Difficulty: " + selectedDifficulty + ", Completion: " + selectedCompletionStatus);

        List<Goal> allGoals = goalModel.getGoals();
        List<Goal> filteredGoals = new ArrayList<>();

        for (Goal goal : allGoals) {
            String goalDifficulty = goal.getDifficulty() != null ? goal.getDifficulty().trim() : "";
            boolean matchesDifficulty = selectedDifficulty.equals("All") || goalDifficulty.equalsIgnoreCase(selectedDifficulty);

            boolean matchesCompletion = selectedCompletionStatus.equals("All")
                    || (selectedCompletionStatus.equals("Completed") && goal.isCompleted())
                    || (selectedCompletionStatus.equals("Uncompleted") && !goal.isCompleted());

            if (matchesDifficulty && matchesCompletion) {
                filteredGoals.add(goal);
            }
        }

        System.out.println("Filtered goals found: " + filteredGoals.size());
        goalListView.getItems().clear();
        goalListView.getItems().addAll(filteredGoals);
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
