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
     * The homeController of this application (goes with the home page).
     */
    private HomeController homeController;

    /**
     * The goalPlanController of this application (goes with the goal plan page).
     */
    private GoalPlanController goalPlanController;

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

    /**
     * The goal individual visualization page of the application.
     */
    private GoalVisView goalVisPage;


    /**
     *  The user progress historical visualization page of the application
     */
    private UserProgressHistoryVisView userprogresshistoryVisPage;

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
     * The button for going to the goal plan page.
     */
    private Button goalVisButton;

    /**
     * Construct the dashboard view and MVC structure of the application.
     */
    public DashboardView(GoalModel goalModel) {



        this.goalModel = goalModel;






        // ************************* MVC CONFIGURATION *************************

        // ********* 1. Create all the MVC components *********

        // MODElS
        goalModel = new GoalModel();
        goalPlanModel = new GoalPlanModel();

        // CONTROLLERS
        homeController = new HomeController();
        goalPlanController = new GoalPlanController();
        chartController = new GoalChartController(goalChartView, goalModel);

        // VIEWS
        this.homePage = new HomeView();
        this.goalsPage = new GoalView();
        this.goalPlanPage = new GoalPlanView();
        this.goalVisPage = new GoalVisView(goalModel);
        //goalChartView = new GoalProgress(goalModel);

        // ********* 2. Add subscribers to models *********

        // GOAL MODEL SUBS
        goalModel.addSubscriber(goalsPage);
        goalModel.addSubscriber(homePage);

        // GOAL PLAN MODEL SUBS
        goalPlanModel.addSubscriber(goalPlanPage);

        // ********* 3. Setup controller with each view *********

        // HOMEPAGE CONTROLLER
        homePage.setupEvents(homeController);

        // GOAL PAGE CONTROLLER

        // GOAL PLAN PAGE CONTROLLER
        goalPlanPage.setupEvents(goalPlanController);

        // ********* 4. Set models of each Controller *********

        // SET HOME PAGE CONTROLLER MODEL
        homeController.setModel(goalModel);

        // SET GOAL PLAN PAGE CONTROLLER MODEL
        goalPlanController.setModel(goalPlanModel);

        // ********* 5. Set the required models of each view *********
        goalPlanPage.setGoalPlanModel(goalPlanModel);
        goalsPage.setGoalModel(goalModel);
        homePage.setGoalModel(goalModel);
        goalVisPage.setGoalPlanModel(goalPlanModel);

        // ************************* END MVC CONFIGURATION *************************

        // Set up the UI components of the dashboard
        setupDashboardViewUI();

        // Set up page change interactions on button press
        homeButton.setOnAction(e -> this.setCenter(homePage));
        goalsButton.setOnAction(e -> this.setCenter(goalsPage));
        goalPlanButton.setOnAction(e -> {
            goalPlanPage.setPageToSummaryView();
            this.setCenter(goalPlanPage);
        });
        goalVisButton.setOnAction(e -> this.setCenter(goalVisPage));
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
        goalVisButton = new Button("Goal Vis");
        sidebar.getChildren().addAll(homeButton, goalsButton, goalPlanButton, goalVisButton);
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