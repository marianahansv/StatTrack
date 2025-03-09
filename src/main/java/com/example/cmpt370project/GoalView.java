package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
     * The goal plan model that this view gets goal data from.
     */
    private GoalPlanModel goalPlanModel;

    /**
     * The user data model that this view gets goals completed data.
     */
    private UserHistoryDataModel userHistoryDataModel;

    /**
     * The root of this view.
     */
    private VBox root;

    // ********* Add other ui elements as attributes here if needed (i.e. if they need to change in drawView()) *********

    private ListView<String> goalListView;

    /**
     * Create a new goal view page.
     */
    public GoalView() {
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(5);
        root.setPadding(new Insets(10));

        Label goalsLabel = new Label("My Goals:");
        goalListView = new ListView<>();
        root.getChildren().addAll(goalsLabel, goalListView);

        // Add the root UI element to this view
        this.getChildren().add(root);
    }

    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        if (goalModel == null) return;

        goalListView.getItems().clear();
        for (Goal goal : goalModel.getGoals()) {
            goalListView.getItems().add(goal.toString());
        }
    }

    /**
     * Set the goal model of this view.
     * @param goalModel the goal model that this view will pull data from.
     */
    public void setGoalModel(GoalModel goalModel) {
        this.goalModel = goalModel;
        modelUpdated();
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
     * Set up interaction with a controller for this view.
     * @param c the controller that will handle changing model data for user interactions on this page.
     */
    public void setupEvents(HomeController c) {

        // See HomeView class for what to put here.
        // i.e. when ready to change data in interface based on user interactions, make a new controller class
        // and pass the button handler methods (and other interactive ui elements) of the buttons in this view to the controller

    }
}
