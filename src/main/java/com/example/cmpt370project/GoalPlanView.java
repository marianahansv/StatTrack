package com.example.cmpt370project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * View to handle organization of page(s) related to the Goal Plan feature.
 */
public class GoalPlanView extends StackPane implements Subscriber {

    /**
     * The goal plan model that this view gets goal data from.
     */
    private GoalPlanModel goalPlanModel;

    /**
     * The user data model that this view gets goals completed data.
     */
    private UserHistoryDataModel userHistoryDataModel;

    /**
     * All the possible pages of the goal plan view.
     */
    private enum GoalPlanViewPage {SUMMARY, CREATE_EDIT}

    /**
     * The current page that the goal plan view should show.
     */
    private GoalPlanViewPage currentViewPage = GoalPlanViewPage.SUMMARY;

    // ****************** INTERACTIVE UI ELEMENTS ******************

    // ********* SUMMARY PAGE ELEMENTS *********
    private Button goCreateEditGoalPlanButton;

    // ********* EDIT/CREATE PAGE ELEMENTS *********
    private ToggleGroup planStyleSelect;
    private RadioButton maintainPlan;
    private RadioButton increasePlan;

    private Spinner<Integer> endGoalNumberInput;
    private Spinner<Integer> startGoalNumberInput;

    private Button cancelEditGoalPlanButton;
    private Button savePlanChangesButton;
    private Button deletePlanButton;

    private ComboBox<String> timelineSelectBox;
    /**
     * Create a new goal plan page.
     */
    public GoalPlanView() {
        // ********* Initialize Interactive UI Components *********
        goCreateEditGoalPlanButton = new Button();

        planStyleSelect = new ToggleGroup();
        maintainPlan = new RadioButton();
        increasePlan = new RadioButton();

        endGoalNumberInput = new Spinner<>();
        startGoalNumberInput = new Spinner<>();

        cancelEditGoalPlanButton = new Button();
        savePlanChangesButton = new Button();
        deletePlanButton = new Button();

        timelineSelectBox = new ComboBox<>();

        // ********* Draw the initial view *********

//        drawView();

        // ********* Wire up page change events non-controller based events *********
        goCreateEditGoalPlanButton.setOnAction(e-> { changePage(GoalPlanViewPage.CREATE_EDIT); });
        cancelEditGoalPlanButton.setOnAction(e-> { changePage(GoalPlanViewPage.SUMMARY); });
    }

    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        // Clear whatever elements were previously stored in this view
        getChildren().clear();

        // Draw the current page that is to be shown
        switch(currentViewPage) {
            case SUMMARY -> drawGoalPlanSummaryView();
            case CREATE_EDIT -> drawGoalPlanCreateEditView();
        }
    }

    /**
     * Switch to a different page of this view.
     * @param newPage the page this view should switch to.
     */
    private void changePage(GoalPlanViewPage newPage) {
        currentViewPage = newPage;
        drawView();
    }

    /**
     * Sets the current page of this view to the summary view.
     */
    public void setPageToSummaryView() {
        changePage(GoalPlanViewPage.SUMMARY);
    }

    /**
     * Set the goal plan Model for this view.
     * @param gpModel the goal plan Model for this view.
     */
    public void setGoalPlanModel(GoalPlanModel gpModel) {
        this.goalPlanModel = gpModel;

        if (userHistoryDataModel != null) {
            modelUpdated();
        }
    }

    /**
     * Set the user data model for this view.
     * @param userHistoryDataModel the user data model for this view.
     */
    public void setUserHistoryDataModel(UserHistoryDataModel userHistoryDataModel) {
        this.userHistoryDataModel = userHistoryDataModel;

        if (goalPlanModel != null) {
            modelUpdated();
        }
    }

    @Override
    public void modelUpdated() {
        drawView();
    }

    /**
     * Set up events and interaction that need interaction with a controller for this view.
     * @param controller the controller that will handle changing model data for user interactions on this page.
     */
    public void setupEvents(GoalPlanController controller) {
        // ********* SUMMARY PAGE EVENTS *********


        // ********* CREATE/EDIT PAGE EVENTS *********

        // GOAL PLAN CREATE/EDIT
        savePlanChangesButton.setOnAction(e-> {
            // Create whichever goal plan is selected (pass to the controller)
            if (planStyleSelect.getSelectedToggle() == maintainPlan) {
                if (timelineSelectBox.getValue().equals("DAILY Basis")) {
                    controller.handleSaveMaintainGoalPlan(endGoalNumberInput.getValue(), IGoalPlan.Timeline.DAILY);
                } else {
                    controller.handleSaveMaintainGoalPlan(endGoalNumberInput.getValue(), IGoalPlan.Timeline.WEEKLY);
                }

            } else {
                if (timelineSelectBox.getValue().equals("DAILY Basis")) {
                    controller.handleSaveIncreaseGoalPlan(endGoalNumberInput.getValue(), startGoalNumberInput.getValue(), IGoalPlan.Timeline.DAILY);
                } else {
                    controller.handleSaveIncreaseGoalPlan(endGoalNumberInput.getValue(), startGoalNumberInput.getValue(), IGoalPlan.Timeline.WEEKLY);
                }
            }

            changePage(GoalPlanViewPage.SUMMARY);
        });

        // GOAL PLAN DELETE
        deletePlanButton.setOnAction(e-> {
            controller.handleDeleteGoalPlan();
            changePage(GoalPlanViewPage.SUMMARY);
        });
    }

    /**
     * Draws the UI of the Summary page.
     */
    private void drawGoalPlanSummaryView() {
        // Set up root element for page
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        this.getChildren().add(root);

        if (goalPlanModel != null) {
            // Add page title
            Label titleLabel = new Label("Here's Your Personalized Goal Plan:");
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            root.getChildren().add(titleLabel);

            // ********* GOAL PLAN NOT EXIST VIEW *********
            if (!goalPlanModel.goalPlanExists()) {

                // Populate data if the user has no goal plan
                VBox startGoalPlanModule = new VBox();
                VBox.setVgrow(startGoalPlanModule, Priority.ALWAYS);
                startGoalPlanModule.setAlignment(Pos.TOP_LEFT);
                startGoalPlanModule.setSpacing(20);

                // Info Label
                startGoalPlanModule.getChildren().add(new Label("You do not currently have a goal plan set up yet. Create one to get started!"));

                // Set Goal Plan Button
                goCreateEditGoalPlanButton.setText("Set Up Your Goal Plan");
                startGoalPlanModule.getChildren().add(goCreateEditGoalPlanButton);

                root.getChildren().add(startGoalPlanModule);
            }

            // ********* GOAL PLAN EXIST VIEW *********
            else {

                HBox goalSummaryHeaderModule = new HBox(20);
                goalSummaryHeaderModule.setAlignment(Pos.CENTER_LEFT);
                Label currentPlanTitle = new Label();
                currentPlanTitle.setMinWidth(300);
                currentPlanTitle.setMaxWidth(500);
                currentPlanTitle.setWrapText(true);

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

                if (goalPlanModel.getGoalPlan() instanceof MaintainGoalPlan) {
                    currentPlanTitle.setText("Your current plan is to maintain completing " +
                            goalPlanModel.getGoalPlan().getGoalPlanCurrent() + " goals each " + planTimelineString + " .");
                }

                else if (goalPlanModel.getGoalPlan() instanceof IncreaseGoalPlan) {
                    currentPlanTitle.setText("Your current plan is to increase the number of goals you complete each "
                            + planTimelineString + " until you get to completing " +
                            goalPlanModel.getGoalPlan().getGoalPlanMax() + " goals each " + planTimelineString + " .");
                }

                goCreateEditGoalPlanButton.setText("Change Your Plan Details");

                goalSummaryHeaderModule.getChildren().addAll(currentPlanTitle, goCreateEditGoalPlanButton);
                root.getChildren().add(goalSummaryHeaderModule);

                Label progressTitle = new Label("Here's your current progress on your plan:");
                progressTitle.setStyle("-fx-font-weight: bold;");
                root.getChildren().add(progressTitle);

                VBox goalProgressModule = new VBox(20);
                goalProgressModule.setAlignment(Pos.CENTER_LEFT);
                goalProgressModule.setMaxWidth(670);
                goalProgressModule.setStyle("-fx-background-color: lightgray; -fx-background-radius: 5;");
                goalProgressModule.setPadding(new Insets(20));


                Label currentProgressSum = new Label("Your current number of goals completed for the " + planTimelineString + " : " + timelineCompleted);
                Label targetProgressSum = new Label("Your current target number of goals to complete for the "
                        + planTimelineString + " : " + goalPlanModel.getGoalPlan().getGoalPlanCurrent());

                int progressDiff = goalPlanModel.getGoalPlan().getGoalPlanCurrent() - timelineCompleted;
                String progressMessage = "";

                if (progressDiff > 0) {
                    progressMessage = "You need to complete " + progressDiff + " more goals today to stay on track with you goal plan. Time to complete some goals!";
                } else if (progressDiff == 0){
                    progressMessage = "You have met your target for the " + planTimelineString + " and are currently on track with you goal plan. Props to you!";
                } else {
                    progressMessage = "You have completed " + -progressDiff + " more goals than your target number of goals. Overachiever!";
                }

                System.out.println(-progressDiff);
                System.out.println(goalPlanModel.getGoalPlan().getGoalPlanCurrent());

                Label progressFeedback = new Label(progressMessage);
                progressFeedback.setWrapText(true);

                goalProgressModule.getChildren().addAll(currentProgressSum, targetProgressSum, progressFeedback);
                root.getChildren().add(goalProgressModule);
            }
        }
    }

    /**
     * Draws the UI of the Create/Edit page.
     */
    private void drawGoalPlanCreateEditView() {
        // Set up root element for page
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        this.getChildren().add(root);

        if (goalPlanModel != null) {

            // Add page title (different wording based on if editing/creating)
            String titleString;
            if (!goalPlanModel.goalPlanExists()) {
                titleString = "Set Up";
            } else {
                titleString = "Edit";
            }

            Label titleLabel = new Label("Let's " + titleString + " Your Personalized Goal Plan:");
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            root.getChildren().add(titleLabel);

            // ********* GOAL PLAN EDIT/CREATE FORM *********

            // Create base form container
            VBox formGoalPlanModule = new VBox();
            VBox.setVgrow(formGoalPlanModule, Priority.ALWAYS);
            formGoalPlanModule.setAlignment(Pos.TOP_LEFT);
            formGoalPlanModule.setSpacing(30);

            // Create container for plan selection form element
            VBox planSelectLayout = new VBox(10);
            Label planStyleLabel = new Label("Select your plan style:");

            // Options for plan selection
            maintainPlan.setText("Maintain a Set Number of Goals");
            maintainPlan.setToggleGroup(planStyleSelect);
            planStyleSelect.selectToggle(maintainPlan); // Set Maintain Plan as default option

            increasePlan.setText("Increase the Amount of Goals You Complete");
            increasePlan.setToggleGroup(planStyleSelect);

            // Put radio button options in a group
            HBox toggleGroupLayout = new HBox(20); // 20px of spacing
            toggleGroupLayout.getChildren().addAll(maintainPlan, increasePlan);

            planSelectLayout.getChildren().addAll(planStyleLabel,toggleGroupLayout);

            // Create containers for goal plan timeline input
            VBox dayWeekSelectInputLayout = new VBox(10);
            Label dayWeekLabel = new Label("How would you like your goal plan to measure and improve your goal completion habits?");

            // Create ComboBox for Weekly/ Daily selection
            timelineSelectBox = new ComboBox<>();
            timelineSelectBox.getItems().addAll("DAILY Basis", "WEEKLY Basis");
            timelineSelectBox.setValue("DAILY Basis"); // Default value

            dayWeekSelectInputLayout.getChildren().addAll(dayWeekLabel, timelineSelectBox);


            // Create container for goal number input form element
            VBox endGoalNumberInputLayout = new VBox(10);
            Label goalsPerDayLabel = new Label("How many goals would you like to complete every DAY?");
            endGoalNumberInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
            endGoalNumberInputLayout.getChildren().addAll(goalsPerDayLabel, endGoalNumberInput);

            // Create container for starting goal number input form element
            VBox startGoalNumberInputLayout = new VBox(10);
            Label startingGoalsLabel = new Label("How many goals would you like to start with completing every DAY?");
            startGoalNumberInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
            startGoalNumberInputLayout.getChildren().addAll(startingGoalsLabel, startGoalNumberInput);

            // Put all goal input fields in a container
            HBox planNumbersInputLayout = new HBox(20); // 20px of spacing
            planNumbersInputLayout.getChildren().addAll(endGoalNumberInputLayout, startGoalNumberInputLayout);


            // Create containers for goal plan timeline input
            VBox endDateSelectLayout = new VBox(10);
            Label endDateLabel= new Label("When would you like to reach your target number of goals to complete?");

            DatePicker endDatePicker = new DatePicker();

            endDateSelectLayout.getChildren().addAll(endDateLabel, endDatePicker);


            // Create container for submission and cancel buttons
            HBox buttonGroupLayout = new HBox(20); // 20px of spacing
            if (!goalPlanModel.goalPlanExists()) {
                savePlanChangesButton.setText("Create My Plan");
            } else {
                savePlanChangesButton.setText("Update My Plan");
            }

            cancelEditGoalPlanButton.setText("Cancel Plan " + titleString);
            deletePlanButton.setText("Delete My Plan");

            if (!goalPlanModel.goalPlanExists()) {
                deletePlanButton.setVisible(false);
            } else {
                deletePlanButton.setVisible(true);
            }

            // Add space between delete button and the others
            Region spacer = new Region();
            spacer.setPrefWidth(110);

            buttonGroupLayout.getChildren().addAll(savePlanChangesButton, cancelEditGoalPlanButton, spacer, deletePlanButton);

            // Initially hide the fields unapplicable fields based on the plan selection
            goalsPerDayLabel.setVisible(true);
            endGoalNumberInput.setVisible(true);
            startingGoalsLabel.setVisible(false);
            startGoalNumberInput.setVisible(false);
            endDateSelectLayout.setVisible(false);

            // Control change in visibility based on the selected radio button
            planStyleSelect.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == maintainPlan) {
                    goalsPerDayLabel.setVisible(true);
                    endGoalNumberInput.setVisible(true);
                    startingGoalsLabel.setVisible(false);
                    startGoalNumberInput.setVisible(false);
                    endDateSelectLayout.setVisible(false);
                } else if (newValue == increasePlan) {
                    goalsPerDayLabel.setVisible(true);
                    endGoalNumberInput.setVisible(true);
                    startingGoalsLabel.setVisible(true);
                    startGoalNumberInput.setVisible(true);
                    endDateSelectLayout.setVisible(true);
                }
            });

            // Listener to update labels based on ComboBox selection
            timelineSelectBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if ("DAILY Basis".equals(newValue)) {
                    goalsPerDayLabel.setText("How many goals would you like to complete every DAY?");
                    startingGoalsLabel.setText("How many goals would you like to start with completing every DAY?");
                } else if ("WEEKLY Basis".equals(newValue)) {
                    goalsPerDayLabel.setText("How many goals would you like to complete every WEEK?");
                    startingGoalsLabel.setText("How many goals would you like to start with completing every WEEK?");
                }
            });

            // Set the fields to the previous value if a goal plan already exists (i.e. if editing the plan)
            if (goalPlanModel.goalPlanExists()) {
                // Set Maintain Plan previous values
                if (goalPlanModel.getGoalPlan() instanceof MaintainGoalPlan) {
                    planStyleSelect.selectToggle(maintainPlan);
                    endGoalNumberInput.getValueFactory().setValue(goalPlanModel.getGoalPlanCurrent());
                }

                // Set Increase Plan previous values
                else if (goalPlanModel.getGoalPlan() instanceof IncreaseGoalPlan) {
                    planStyleSelect.selectToggle(increasePlan);
                    endGoalNumberInput.getValueFactory().setValue(goalPlanModel.getGoalPlanMax());
                    startGoalNumberInput.getValueFactory().setValue(goalPlanModel.getGoalPlanCurrent());
                }

                switch(goalPlanModel.getGoalPlanTimeline()) {
                    case DAILY -> {
                        timelineSelectBox.setValue("DAILY Basis");
                    }
                    case WEEKLY -> {
                        timelineSelectBox.setValue("WEEKLY Basis");
                    }
                }
            }

            // Add all form elements to the form layout
            formGoalPlanModule.getChildren().addAll(planSelectLayout, dayWeekSelectInputLayout,
                    planNumbersInputLayout, endDateSelectLayout,
                    buttonGroupLayout);

            // Add the form to the root page
            root.getChildren().add(formGoalPlanModule);
        }
    }
}
