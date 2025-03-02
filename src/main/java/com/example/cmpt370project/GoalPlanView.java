package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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


    private Button createEditGoalPlanButton;

    /**
     * Create a new goal plan page.
     */
    public GoalPlanView() {
        // INITIALIZE UI COMPONENTS
        createEditGoalPlanButton = new Button();

        drawView();
    }

    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        drawGoalPlanHomeView();
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

    private void drawGoalPlanHomeView() {
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(10);
        root.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Here's Your Personalized Goal Plan:");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        root.getChildren().add(welcomeLabel);

        this.getChildren().add(root);

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

}
