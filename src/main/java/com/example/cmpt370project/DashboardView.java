package com.example.cmpt370project;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents the base UI of the application that holds different views and sets up the MVC structure.
 */
public class DashboardView extends BorderPane {

    private GoalProgress goalChartView;
    private GoalChartController chartController;

    // ************************* APPLICATION MODELS *************************
    /**
     * The model that holds the goal data of the application.
     */
    private GoalModel goalModel;

    /**
     * The model that holds the goal plan data of the application.
     */
    private GoalPlanModel goalPlanModel;

    // ************************* APPLICATION CONTROLLERS *************************

    /**
     * The controller of this application.
     */
    private Controller controller; // We can add/rename this one later if we want to have diff controllers for each view.

    // ************************* APPLICATION VIEWS *************************

    /**
     * The homepage of the application.
     */
    private HomeView homePage;

    /**
     * The goals page of the application.
     */
    private GoalView goalsPage;

    /**
     * The goal plan page of the application.
     */
    private GoalPlanView goalPlanPage;

    // ************************* UI ELEMENTS OF BASIC DASHBOARD VIEW *************************

    // UI elements here should not make any major changes to the models, they should only be for aesthetic or page changes

    /**
     * The button for going to the homepage.
     */
    private Button homeButton;

    /**
     * The button for going to the goals page.
     */
    private Button goalsButton;

    /**
     * The button for going to the goal plan page.
     */
    private Button goalPlanButton;

    /**
     * Construct the dashboard view and MVC structure of the application.
     */
    public DashboardView(GoalModel goalModel) {

        
        
        this.goalModel = goalModel;

        goalChartView = new GoalProgress(goalModel);
        chartController = new GoalChartController(goalChartView, goalModel);

        setCenter(goalChartView);

        // ************************* MVC CONFIGURATION *************************

        // ********* 1. Create all the MVC components *********

        // MODElS
        goalModel = new GoalModel();
        goalPlanModel = new GoalPlanModel();

        // CONTROLLERS
        controller = new Controller();

        // VIEWS
        this.homePage = new HomeView();
        this.goalsPage = new GoalView();
        this.goalPlanPage = new GoalPlanView(goalModel);

        // ********* 2. Add subscribers to models *********

        // GOAL MODEL SUBS
        goalModel.addSubscriber(goalsPage);
        goalModel.addSubscriber(homePage);

        // GOAL PLAN MODEL SUBS
        goalPlanModel.addSubscriber(goalPlanPage);

        // ********* 3. Setup controller with each view *********

        // HOMEPAGE CONTROLLER
        homePage.setupEvents(controller);

        // GOAL PAGE CONTROLLER

        // GOAL PLAN PAGE CONTROLLER

        // ********* 4. Set models of each controller *********

        // SET HOME PAGE CONTROLLER MODEL
        controller.setModel(goalModel);

        // ********* 5. Set the required models of each view *********
        goalPlanPage.setGoalPlanModel(goalPlanModel);
        goalsPage.setGoalModel(goalModel);
        homePage.setGoalModel(goalModel);

        // ************************* END MVC CONFIGURATION *************************

        // Set up the UI components of the dashboard
        setupDashboardViewUI();

        // Set up page change interactions on button press
        homeButton.setOnAction(e -> this.setCenter(homePage));
        goalsButton.setOnAction(e -> this.setCenter(goalsPage));
        goalPlanButton.setOnAction(e -> this.setCenter(goalPlanPage));
    }

    /**
     * Configures the basic UI components for the dashboard.
     */
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
    }
}