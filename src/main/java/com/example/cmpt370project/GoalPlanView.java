package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View to handle organization of page(s) related to the Goal Plan feature.
 */
public class GoalPlanView extends StackPane implements Subscriber {

    /**
     * The goal plan model that this view gets goal data from.
     */
    private GoalPlanModel goalPlanModel;

    private enum GoalPlanViewPage {SUMMARY, CREATE_EDIT}

    private GoalPlanViewPage currentViewPage = GoalPlanViewPage.SUMMARY;

    private Button createEditGoalPlanButton;

    /**
     * Create a new goal plan page.
     */
    public GoalPlanView() {
        // ********* Initialize Interactive UI Components *********
        createEditGoalPlanButton = new Button();

        // ********* Draw the initial view *********
        drawView();

        // ********* Wire up events for Interactive UI Components *********
        createEditGoalPlanButton.setOnAction(e-> { changePage(GoalPlanViewPage.CREATE_EDIT);
        });
    }

    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        // Clear whatever elements were previously stored in this view
        getChildren().clear();

        switch(currentViewPage) {
            case SUMMARY -> drawGoalPlanSummaryView();
            case CREATE_EDIT -> drawGoalPlanCreateEditView();
        }
    }

    private void changePage(GoalPlanViewPage newPage) {
        currentViewPage = newPage;
        drawView();
    }

    public void setPageToSummaryView() {
        changePage(GoalPlanViewPage.SUMMARY);
    }

    /**
     * Set the goal plan Model for this view.
     * @param gpModel the goal plan Model for this view.
     */
    public void setGoalPlanModel(GoalPlanModel gpModel) {
        this.goalPlanModel = gpModel;
        modelUpdated();
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

    private void drawGoalPlanSummaryView() {
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(10);
        root.setPadding(new Insets(20));
        this.getChildren().add(root);

        Label titleLabel = new Label("Here's Your Personalized Goal Plan:");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        root.getChildren().add(titleLabel);

        if (goalPlanModel != null) {

            // ********* GOAL PLAN NOT EXIST VIEW *********
            if (!goalPlanModel.goalPlanExists()) {

                VBox startGoalPlanModule = new VBox();
                VBox.setVgrow(startGoalPlanModule, Priority.ALWAYS);
                startGoalPlanModule.setAlignment(Pos.CENTER);
                startGoalPlanModule.setSpacing(20);
                startGoalPlanModule.setPadding(new Insets(20));

                startGoalPlanModule.getChildren().add(new Label("You do not currently have a goal plan set up yet. Create one to get started!"));

                createEditGoalPlanButton.setText("Set Up Your Goal Plan");
                startGoalPlanModule.getChildren().add(createEditGoalPlanButton);

                root.getChildren().add(startGoalPlanModule);
            }

            // ********* GOAL PLAN EXIST VIEW *********

            else {

            }
        }
    }

    private void drawGoalPlanCreateEditView() {
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        this.getChildren().add(root);

        if (goalPlanModel != null) {

            String titleString;
            if (!goalPlanModel.goalPlanExists()) {
                titleString = "Set Up";
            } else {
                titleString = "Edit";
            }

            Label titleLabel = new Label("Let's " + titleString + " Your Personalized Goal Plan:");
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            root.getChildren().add(titleLabel);

            // form

            VBox formGoalPlanModule = new VBox();
            VBox.setVgrow(formGoalPlanModule, Priority.ALWAYS);
            formGoalPlanModule.setAlignment(Pos.TOP_LEFT);
            formGoalPlanModule.setSpacing(30);

            VBox planSelectLayout = new VBox(10);
            Label planStyleLabel = new Label("Select your plan style:");

            // Create a ToggleGroup for the radio buttons
            ToggleGroup toggleGroup = new ToggleGroup();

            // Radio Buttons
            RadioButton maintainPlan = new RadioButton("Maintain a Set Number of Goals");
            maintainPlan.setToggleGroup(toggleGroup);
            toggleGroup.selectToggle(maintainPlan); // set as default
            RadioButton increasePlan = new RadioButton("Increase the Amount of Goals You Complete");
            increasePlan.setToggleGroup(toggleGroup);

            HBox toggleGroupLayout = new HBox(20); // 20px of spacing
            toggleGroupLayout.getChildren().addAll(maintainPlan, increasePlan);
            planSelectLayout.getChildren().addAll(planStyleLabel,toggleGroupLayout);

            // Additional fields (start off hidden)
            VBox currentGoalNumberInputLayout = new VBox(10);
            Label goalsPerDayLabel = new Label("How many goals would you like to complete every day?");
            Spinner<Integer> endingGoalsSpinner = new Spinner<>(1, 100, 1);
            currentGoalNumberInputLayout.getChildren().addAll(goalsPerDayLabel, endingGoalsSpinner);

            VBox maxGoalNumberInputLayout = new VBox(10);
            Label startingGoalsLabel = new Label("How many goals would you like to start with completing every day?");
            Spinner<Integer> startingGoalsSpinner = new Spinner<>(1, 100, 1);
            maxGoalNumberInputLayout.getChildren().addAll(startingGoalsLabel, startingGoalsSpinner);

            HBox planNumbersInputLayout = new HBox(20); // 20px of spacing
            planNumbersInputLayout.getChildren().addAll(currentGoalNumberInputLayout, maxGoalNumberInputLayout);

            // Button at the bottom
            HBox buttonGroupLayout = new HBox(20); // 20px of spacing
            Button createPlanButton = new Button("Create My Plan");
            Button cancelButton = new Button("Cancel Plan " + titleString);
            buttonGroupLayout.getChildren().addAll(createPlanButton, cancelButton);

            // Initially hide the fields based on the selection
            goalsPerDayLabel.setVisible(true);
            endingGoalsSpinner.setVisible(true);
            startingGoalsLabel.setVisible(false);
            startingGoalsSpinner.setVisible(false);

            // Change visibility based on the selected radio button
            toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == maintainPlan) {
                    goalsPerDayLabel.setVisible(true);
                    endingGoalsSpinner.setVisible(true);
                    startingGoalsLabel.setVisible(false);
                    startingGoalsSpinner.setVisible(false);
                } else if (newValue == increasePlan) {
                    goalsPerDayLabel.setVisible(true);
                    endingGoalsSpinner.setVisible(true);
                    startingGoalsLabel.setVisible(true);
                    startingGoalsSpinner.setVisible(true);
                }
            });

            // Add components to the layout
            formGoalPlanModule.getChildren().addAll(planSelectLayout,
                    planNumbersInputLayout,
                    buttonGroupLayout);

            root.getChildren().add(formGoalPlanModule);
        }

    }

}
