package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DashboardView extends BorderPane implements Subscriber {
    private GoalModel gm;

    // buttons
    private Button homeButton;
    private Button goalsButton;
    private Button addGoalButton;
    private Button removeGoalButton;

    // pages (for the center region)
    private VBox homePage;
    private VBox goalsPage;

    // goals UI
    private ListView<String> goalListView;

    public DashboardView() {
        // --- header ---
        HBox header = new HBox(new Label("Goal Tracker Dashboard"));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: lightblue; -fx-padding: 5px;");
        this.setTop(header);

        // --- sidebar ---
        VBox sidebar = new VBox();
        sidebar.setSpacing(10);
        sidebar.setAlignment(Pos.CENTER);
        homeButton = new Button("Home");
        goalsButton = new Button("Goals");
        sidebar.getChildren().addAll(homeButton, goalsButton);
        sidebar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px;");
        this.setLeft(sidebar);

        // --- center ---
        createHomePage();
        createGoalsPage();
        this.setCenter(homePage);

        // --- footer ---
        HBox footer = new HBox();
        footer.setSpacing(20);
        footer.setAlignment(Pos.CENTER);
        addGoalButton = new Button("Add Goal");
        removeGoalButton = new Button("Clear Goals");
        footer.getChildren().addAll(addGoalButton, removeGoalButton);
        footer.setStyle("-fx-background-color: lightgray; -fx-padding: 10px;");
        this.setBottom(footer);

        homeButton.setOnAction(e -> this.setCenter(homePage));
        goalsButton.setOnAction(e -> this.setCenter(goalsPage));
    }

    /**
     * Creates a simple Home page (blank for now).
     */
    private void createHomePage() {
        homePage = new VBox();
        homePage.setAlignment(Pos.CENTER);
        homePage.setSpacing(10);
        homePage.setPadding(new Insets(10));

        Label welcomeLabel = new Label("Welcome to the Home Page!");
        homePage.getChildren().add(welcomeLabel);
    }

    /**
     * Creates the Goals page with a ListView of goals.
     */
    private void createGoalsPage() {
        goalsPage = new VBox();
        goalsPage.setAlignment(Pos.TOP_CENTER);
        goalsPage.setSpacing(5);
        goalsPage.setPadding(new Insets(10));

        Label goalsLabel = new Label("My Goals:");
        goalListView = new ListView<>();
        goalsPage.getChildren().addAll(goalsLabel, goalListView);
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
     * Updates the ListView of goals on the Goals page.
     */
    private void update() {
        if (gm == null) return;

        goalListView.getItems().clear();
        for (Goal goal : gm.getGoals()) {
            goalListView.getItems().add(goal.toString());
        }
    }
}