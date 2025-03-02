package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class DashboardView extends BorderPane {
    private GoalModel goalModel;
    private GoalPlanModel goalPlanModel;

    private Controller controller;

    // pages (for the center region)
    private HomeView homePage;
    private GoalView goalsPage;
    private GoalPlanView goalPlanPage;

    // Ui Elements for switching between views (should not adjust models)
    private Button homeButton;
    private Button goalsButton;
    private Button goalPlanButton;

    public DashboardView() {

        // Create MVC components
        goalModel = new GoalModel();
        goalPlanModel = new GoalPlanModel();
        controller = new Controller();

        // create the pages
        this.homePage = new HomeView();
        this.goalsPage = new GoalView();
        this.goalPlanPage = new GoalPlanView();

        goalPlanModel.addSubscriber(goalPlanPage);

        goalModel.addSubscriber(goalsPage);
        goalModel.addSubscriber(homePage);

        homePage.setupEvents(controller);

        // change this later
        controller.setModel(goalModel);

        goalPlanPage.setGpModel(goalPlanModel);
        goalsPage.setModel(goalModel);
        homePage.setModel(goalModel);

        // set up this view
        setupDashboardViewUI();
    }

    private void setupDashboardViewUI() {
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
        goalPlanButton = new Button("Goal Plan");
        sidebar.getChildren().addAll(homeButton, goalsButton, goalPlanButton);
        sidebar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px;");
        this.setLeft(sidebar);

        // --- center ---
        this.setCenter(homePage);

        // --- footer ---
        HBox footer = new HBox();
        footer.setSpacing(20);
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-background-color: lightgray; -fx-padding: 10px;");
        this.setBottom(footer);

        homeButton.setOnAction(e -> this.setCenter(homePage));
        goalsButton.setOnAction(e -> this.setCenter(goalsPage));
        goalPlanButton.setOnAction(e -> this.setCenter(goalPlanPage));
    }
}