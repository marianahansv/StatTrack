package com.example.cmpt370project;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View
 */
public class GoalVisView extends StackPane implements Subscriber {

    /**
     * The goal plan model that this view gets goal data from.
     */
    private GoalPlanModel goalPlanModel;
    
    /**
     * Chart for visualizing goal progress.
     */
    private GoalProgress goalChartView;

    /**
     * Create a new goal vis page.
     */
    public GoalVisView(GoalModel goalModel) {
        
        VBox root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Let's Visualize Your Goal Progress:");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label goalVisSubtitle = new Label("Use the chart-type dropdown select to visualize and compare your progress on each goal timeline for all of your goals.");
        goalVisSubtitle.getStyleClass().add("bigger-paragraph-text");

        root.getChildren().add(new VBox(25, welcomeLabel, goalVisSubtitle, new Region()));

        VBox goalVisualizations = new VBox();
        goalVisualizations.setAlignment(Pos.CENTER);

        goalChartView = new GoalProgress(goalModel);
        goalVisualizations.getChildren().add(goalChartView);
        root.getChildren().add(goalVisualizations);

        this.getChildren().add(root);
    }

    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        goalChartView.updateChart();
    }

    /**
     * Set the goal plan Model for this view.
     * @param gpModel the goal plan Model for this view.
     */
    public void setGoalPlanModel(GoalPlanModel gpModel) {
        this.goalPlanModel = gpModel;
    }

    /**
     * Model is updated
     */
    @Override
    public void modelUpdated() {
        drawView();
    }
}
