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

    /**
     * The model that holds the historical data of each user and their goals
     */
    private UserProgressHIstoryVisModel historicalChartModel;

    /**
     * The model that holds the user's name and current/past goal trend/behaviour data of the application.
     */
    private UserHistoryDataModel userHistoryDataModel;

    // ************************* APPLICATION CONTROLLERS *************************

    /**
     * The homeController of this application (goes with the home page).
     */
    private HomeController homeController;

    /**
     * The goalPlanController of this application (goes with the goal plan page).
     */
    private GoalPlanController goalPlanController;

    /**
     * The historicalChartController of the application (goes with the historical goal page).
     */
    private UserProgressHistoryController historicalChartController;
    // Consider removing this later since it is not really doing anything now
    //private GoalChartController chartController;

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
     * The goal plan page of the application.
     */
    private GoalVisView goalVisPage;

    //private GoalProgress goalChartView;

    /**
     *  The user progress historical visualization page of the application
     */
    private UserProgressHistoryVisView historicalChartView;

    /**
     * The visualization of the front end UI and the generation of the graphs for the application.
     */
    private UserHIstoryProgressVisuals historicalChartProgress;

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
     * The button to go the historical data visualization page.
     */
    private Button historicalChartButton;

    /**
     * Construct the dashboard view and MVC structure of the application.
     */
    public DashboardView() {

        // ************************* MVC CONFIGURATION *************************

        // ********* 1. Create all the MVC components *********

        // MODElS
        goalModel = new GoalModel();
        goalPlanModel = new GoalPlanModel();
        historicalChartModel = new UserProgressHIstoryVisModel();
        userHistoryDataModel = new UserHistoryDataModel();

        // CONTROLLERS
        homeController = new HomeController();
        goalPlanController = new GoalPlanController();
        chartController = new GoalChartController(goalChartView, goalModel);
        historicalChartController = new UserProgressHistoryController(historicalChartProgress, historicalChartModel);

        // VIEWS
        this.homePage = new HomeView();
        this.goalsPage = new GoalView();
        this.goalPlanPage = new GoalPlanView();
        this.goalVisPage = new GoalVisView(goalModel);
        this.historicalChartView = new UserProgressHistoryVisView(historicalChartModel, historicalChartController);

        //goalChartView = new GoalProgress(goalModel);

        // ********* 2. Add subscribers to models *********

        // GOAL MODEL SUBS
        goalModel.addSubscriber(goalsPage);
        goalModel.addSubscriber(homePage);

        // GOAL PLAN MODEL SUBS
        goalPlanModel.addSubscriber(goalPlanPage);
        goalPlanModel.addSubscriber(goalsPage);

        // USER MODEL SUBS
        userHistoryDataModel.addSubscriber(goalPlanPage);
        userHistoryDataModel.addSubscriber(goalsPage);
        userHistoryDataModel.addSubscriber(homePage);
        historicalChartModel.addSubscriber(historicalChartView);

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
        goalPlanPage.setUserHistoryDataModel(userHistoryDataModel);

        goalsPage.setGoalModel(goalModel);
        goalsPage.setGoalPlanModel(goalPlanModel);
        goalsPage.setUserHistoryDataModel(userHistoryDataModel);

        homePage.setGoalModel(goalModel);
        homePage.setUserHistoryDataModel(userHistoryDataModel);

        goalVisPage.setGoalPlanModel(goalPlanModel);
        historicalChartView.setGoalPlanModel(historicalChartModel);

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
        goalVisButton.setOnAction(e -> {
            //goalModel.notifySubscribers();
            this.setCenter(goalVisPage);
        });


        // ************************* POPULATE DUMMY DATA *************************
        // Here is where we can manually set data to show for testing/demo purposes!!

        // Set completed goals to test on GoalPlan page
        userHistoryDataModel.setDailyCompletedGoals(12);
        userHistoryDataModel.setWeeklyCompletedGoals(2);

        // Set username to test on the HomeView page
        userHistoryDataModel.setUserName("HasAPlanFran");
        historicalChartButton.setOnAction(e -> this.setCenter(historicalChartView));
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
        sidebar.setPrefWidth(100);
        homeButton = new Button("Home");
        goalsButton = new Button("My Goals");
        goalPlanButton = new Button("Goal Plan");
        goalVisButton = new Button("Goal Visuals");

        // Set the same preferred width for each button
        homeButton.setMaxWidth(Double.MAX_VALUE);
        goalsButton.setMaxWidth(Double.MAX_VALUE);
        goalPlanButton.setMaxWidth(Double.MAX_VALUE);
        goalVisButton.setMaxWidth(Double.MAX_VALUE);

        sidebar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px;");
        goalVisButton = new Button("Goal Vis");
        historicalChartButton = new Button("Goal His");
        sidebar.getChildren().addAll(homeButton, goalsButton, goalPlanButton, goalVisButton, historicalChartButton);
        sidebar.setStyle("-fx-background-color: lightblue; -fx-padding: 10px;");
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