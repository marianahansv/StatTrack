package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View to handle all fields related to new Goal creation.
 */
public class AddGoalView extends StackPane implements Subscriber{

    /**
     * The model that this view gets goal data from.
     */
    GoalModel goalModel;

    /**
     * Create a new add goal page.
     */
    public AddGoalView() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Label welcomeLabel = new Label("Add Goal!");
        root.getChildren().add(welcomeLabel);

        this.getChildren().add(root);
    }
    @Override
    public void modelUpdated() {

    }

}
