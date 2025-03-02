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

    // Add the models that this view requires here
    private GoalModel goalModel;

    private VBox root;
    private Button addGoalButton;
    private Button removeGoalButton;

    // Add other ui elements (buttons, labels) as attributes here if needed (aka if they need to change in drawView)


    public HomeView() {
        root = new VBox();
        addGoalButton = new Button("Add Goal");
        removeGoalButton = new Button("Remove Goal");

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Label welcomeLabel = new Label("Welcome to the Home Page!");
        root.getChildren().addAll(welcomeLabel, addGoalButton, removeGoalButton);

        // Add the root to this view
        this.getChildren().add(root);
    }

    private void drawView() {
        // make changes to ui elements in here by calling attributes and changing based on model data
    }

    public void setModel(GoalModel goalModel) {
        this.goalModel = goalModel;
        drawView();
    }

    @Override
    public void modelUpdated() {
        drawView();
    }

    public void setupEvents(Controller c) {

        // Do something like this for whatever ui control elements there are.
        // Make a separate controller for this view?

        addGoalButton.setOnAction(c::handleButtonPress);
        removeGoalButton.setOnAction(c::removeButtonPress);

    }
}
