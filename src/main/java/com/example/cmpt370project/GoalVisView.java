package com.example.cmpt370project;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
        root.getChildren().add(welcomeLabel);

        VBox goalVisualizations = new VBox();
        goalVisualizations.setAlignment(Pos.CENTER);

        goalChartView = new GoalProgress(goalModel);
        goalVisualizations.getChildren().add(goalChartView);
        root.getChildren().add(goalVisualizations);

        // SCROLL BAR because too much content not enough space 🔥🔥🔥
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.getChildren().add(scrollPane);
    }

    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        // Add here later...
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
