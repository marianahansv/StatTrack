package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View to handle organization UI elements of the home page.
 */
public class HomeView extends StackPane implements Subscriber {

    // ********* The models that this view requires data from are to be added here! *********

    /**
     * The goal model that this view gets goal data from.
     */
    private GoalModel goalModel;

    /**
     * The root of this view.
     */
    private VBox root;

    // ********* Add other ui elements as attributes here if needed (i.e. if they need to change in drawView()) *********

    private Button addGoalButton;
    private Button removeGoalButton;

    /**
     * Create a new home view page.
     */
    public HomeView() {
        root = new VBox();
        addGoalButton = new Button("Add Goal");
        removeGoalButton = new Button("Clear Goals");

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Label welcomeLabel = new Label("Welcome to the Home Page!");
        root.getChildren().addAll(welcomeLabel, addGoalButton, removeGoalButton);

        // Add the root UI element to this view
        this.getChildren().add(root);
    }

    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        // Make changes to ui elements in here by calling attributes and changing based on model data.
        // See GoalView as example for what will go here.
    }

    /**
     * Set the goal model of this view.
     * @param goalModel the goal model that this view will pull data from.
     */
    public void setGoalModel(GoalModel goalModel) {
        this.goalModel = goalModel;
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

        // Right now wew have this HomeController class just named generally, I think we should rename so its specific to
        // the home page. (other views get other controllers)

        addGoalButton.setOnAction(c::handleButtonPress);
        removeGoalButton.setOnAction(c::removeButtonPress);

    }
}
