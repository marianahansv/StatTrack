package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A dashboard view that displays the goals in a central List View
 * has a sidebar for navigation, and a footer with control buttons.
 */
public class DashboardView extends BorderPane implements Subscriber {
    private GoalModel gm;
    private Button addGoalButton;
    private Button removeGoalButton;
    private ListView<String> goalListView;

    // man this dashboard silly af
    public DashboardView() {
        // left sidebar
        VBox sidebar = new VBox();
        sidebar.setSpacing(10);
        sidebar.setAlignment(Pos.CENTER);
        Button homeButton = new Button("Home");
        Button GoalsButton = new Button("Goals");
        sidebar.getChildren().addAll(homeButton, GoalsButton);
        sidebar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px;");
        this.setLeft(sidebar);

        // main content area
        VBox mainContent = new VBox();
        mainContent.setSpacing(10);
        mainContent.setAlignment(Pos.TOP_CENTER);
        Label goalsLabel = new Label("My Goals:");
        goalListView = new ListView<>();
        mainContent.setSpacing(5);
        mainContent.setPadding(new Insets(10, 10, 10, 10));
        mainContent.getChildren().addAll(goalsLabel, goalListView);
        this.setCenter(mainContent);

        // footer
        HBox footer = new HBox();
        footer.setSpacing(20);
        footer.setAlignment(Pos.CENTER);
        addGoalButton = new Button("Add Goal");
        removeGoalButton = new Button("Clear Goals");
        footer.getChildren().addAll(addGoalButton, removeGoalButton);
        footer.setStyle("-fx-background-color: lightgray; -fx-padding: 10px;");
        this.setBottom(footer);
    }

    public void setModel(GoalModel goalModel) {
        this.gm = goalModel;
        update();
    }

    public void setupEvents(Controller c) {
        addGoalButton.setOnAction(c::handleButtonPress);
        removeGoalButton.setOnAction(c::removeButtonPress);
    }

    @Override
    public void modelUpdated() {
        update();
    }

    /**
     * Update the List View with the current list of goals from the model.
     */
    public void update() {
        goalListView.getItems().clear();
        for (Goal goal : gm.getGoals()) {
            goalListView.getItems().add(goal.toString());
        }
    }
}