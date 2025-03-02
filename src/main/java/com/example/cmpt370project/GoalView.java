package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GoalView extends StackPane implements Subscriber {

    // Add the models that this view requires here
    private GoalModel goalModel;

    private VBox root;
    private ListView<String> goalListView;

    // Add other ui elements (buttons, labels) as attributes here if needed (aka if they need to change in drawView)


    public GoalView() {
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(5);
        root.setPadding(new Insets(10));

        Label goalsLabel = new Label("My Goals:");
        goalListView = new ListView<>();
        root.getChildren().addAll(goalsLabel, goalListView);

        // Add the root to this view
        this.getChildren().add(root);
    }

    private void drawView() {
        if (goalModel == null) return;

        goalListView.getItems().clear();
        for (Goal goal : goalModel.getGoals()) {
            goalListView.getItems().add(goal.toString());
        }
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
        // Make a separate controller for this view? (not use the same one?)

//        addGoalButton.setOnAction(c::handleButtonPress);
//        removeGoalButton.setOnAction(c::removeButtonPress);

    }
}
