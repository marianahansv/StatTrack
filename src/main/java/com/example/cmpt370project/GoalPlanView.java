package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View to handle organization of views related to the Goal Plan feature.
 */
public class GoalPlanView extends StackPane implements Subscriber {

    /**
     * The Goal Plan model from which the view needs data from.
     */
    private GoalPlanModel gpModel;




    public GoalPlanView() {
        drawView();
    }

    /**
     * Creates and organizes the elements of this view.
     */
    public void drawView() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Label welcomeLabel = new Label("Welcome to the Goal Plan Page!");
        root.getChildren().add(welcomeLabel);

        this.getChildren().add(root);
    }

    /**
     * Set the Goal Plan Model for this view.
     * @param gpModel the Goal Plan Model for this view.
     */
    public void setGpModel(GoalPlanModel gpModel) {
        this.gpModel = gpModel;
    }

    @Override
    public void modelUpdated() {
        drawView();
    }
}
