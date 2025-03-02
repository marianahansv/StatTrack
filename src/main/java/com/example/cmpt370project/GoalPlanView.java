package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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

    /**
     * Create a new goal plan page.
     */
    public GoalPlanView() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Label welcomeLabel = new Label("Welcome to the Goal Plan Page!");
        root.getChildren().add(welcomeLabel);

        this.getChildren().add(root);
    }

    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        // Add here later...
    }

    /**
     * Set the goal plan Model for this view.
     * @param gpModel the goal plan Model for this view.
     */
    public void setGoalPlanModel(GoalPlanModel gpModel) {
        this.goalPlanModel = gpModel;
    }

    @Override
    public void modelUpdated() {
        drawView();
    }

    /**
     * Set up interaction with a controller for this view.
     * @param c the controller that will handle changing model data for user interactions on this page.
     */
    public void setupEvents(Controller c) {

        // See HomeView class for what to put here.
        // i.e. when ready to change data in interface based on user interactions, make a new controller class
        // and pass the button handler methods (and other interactive ui elements) of the buttons in this view to the controller

    }
}
