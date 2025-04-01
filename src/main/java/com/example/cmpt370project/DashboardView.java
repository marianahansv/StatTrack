package com.example.cmpt370project;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the base UI of the application that holds different views and sets up the MVC structure.
 */
public class DashboardView extends BorderPane implements Subscriber {

    private GoalProgress goalChartView;
    private GoalChartController chartController;

    /**
     * Controller for the suggestions methods
     * **/
    private SuggestionsController suggestionsController;
    // ************************* APPLICATION MODELS *************************
    /**
     * The model that holds the goal data of the application.
     */
    private GoalModel goalModel;
    /**
     * The model that holds the suggestions data of the application.
     */
    private SuggestionsModel suggestionsModel;
    /**
     * The model that holds the goal plan data of the application.
     */
    GoalPlanModel goalPlanModel;

    /**
     * The model that holds the historical data of each user and their goals
     */
    private UserProgressHIstoryVisModel historicalChartModel;

    /**
     * The model that holds the user's name and current/past goal trend/behaviour data of the application.
     */
    UserHistoryDataModel userHistoryDataModel;

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
    HomeView homePage;

    /**
     * The goals page of the application.
     */
    GoalView goalsPage;

    /**
     * The goal plan page of the application.
     */
    GoalPlanView goalPlanPage;

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
    Button homeButton;

    /**
     * The button for going to the goals page.
     */
    Button goalsButton;

    /**
     * The button for going to the goal plan page.
     */
    Button goalPlanButton;

    /**
     * The button for going to the goal plan page.
     */
    private Button goalVisButton;

    /**
     * The button to go the historical data visualization page.
     */
    private Button historicalChartButton;

    /**
     * ScrollPane for adding vertical scrolling to all views.
     */
    private ScrollPane scrollPane;

    // ************************* UI Elements for Dashboard Sidebar Widgets  *************************
    private Label totalGoals;
    private Label dailyCompleted;
    private Label weeklyCompleted;
    private Label nearestDeadline;

    /**
     * Construct the dashboard view and MVC structure of the application.
     */
    public DashboardView() {

        // ************************* MVC CONFIGURATION *************************

        // ********* 1. Create all the MVC components *********

        // MODElS

        goalPlanModel = new GoalPlanModel();
        userHistoryDataModel = new UserHistoryDataModel();
        historicalChartModel = new UserProgressHIstoryVisModel(userHistoryDataModel);
        goalModel = new GoalModel(userHistoryDataModel);
        suggestionsModel = new SuggestionsModel();

        // CONTROLLERS
        homeController = new HomeController();
        goalPlanController = new GoalPlanController();
        chartController = new GoalChartController(goalChartView, goalModel);
        historicalChartController = new UserProgressHistoryController(historicalChartProgress, historicalChartModel);
        suggestionsController = new SuggestionsController(goalModel,suggestionsModel);

        // VIEWS
        this.homePage = new HomeView();
        this.goalsPage = new GoalView();
        this.goalPlanPage = new GoalPlanView();
        this.goalVisPage = new GoalVisView(goalModel);
        this.historicalChartView = new UserProgressHistoryVisView(historicalChartController);

        //goalChartView = new GoalProgress(goalModel);

        // ********* 2. Add subscribers to models *********

        // GOAL MODEL SUBS
        goalModel.addSubscriber(goalsPage);
        goalModel.addSubscriber(homePage);
        goalModel.addSubscriber(this);

        // GOAL PLAN MODEL SUBS
        goalPlanModel.addSubscriber(goalPlanPage);
        goalPlanModel.addSubscriber(goalsPage);

        // USER MODEL SUBS
        userHistoryDataModel.addSubscriber(goalPlanPage);
        userHistoryDataModel.addSubscriber(goalsPage);
        userHistoryDataModel.addSubscriber(homePage);
        userHistoryDataModel.addSubscriber(this);
        historicalChartModel.addSubscriber(historicalChartView);

        // ********* 3. Setup controller with each view *********

        // HOMEPAGE CONTROLLER
        homePage.setupEvents(homeController, suggestionsController);

        // GOAL PAGE CONTROLLER

        // GOAL PLAN PAGE CONTROLLER
        goalPlanPage.setupEvents(goalPlanController);

        // ********* 4. Set models of each Controller *********

        // SET HOME PAGE CONTROLLER MODEL
        homeController.setModel(goalModel, userHistoryDataModel);

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
        homePage.setSuggestionsModel(suggestionsModel);


        goalVisPage.setGoalPlanModel(goalPlanModel);
        historicalChartController.setGoalPlanModel(historicalChartModel);

        // ************************* END MVC CONFIGURATION *************************

        // Initialize ScrollPane for vertical scrolling
        scrollPane = new ScrollPane();
        scrollPane.setContent(homePage); // Default view is Home Page
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER); // Disable horizontal scrolling

        // Set up the UI components of the dashboard
        setupDashboardViewUI();

        // Set up page change interactions on button press
        homeButton.setOnAction(e -> scrollPane.setContent(homePage));
        goalsButton.setOnAction(e -> scrollPane.setContent(goalsPage));
        goalPlanButton.setOnAction(e -> {
            goalPlanPage.setPageToSummaryView();
            scrollPane.setContent(goalPlanPage);
        });
        goalVisButton.setOnAction(e -> scrollPane.setContent(goalVisPage));

        // If first time running the app, get the users name
        // Do this here in this class, because not specific to any view

        Platform.runLater(() -> {
            if (userHistoryDataModel.isFirstOpen()) {
                // Create a TextInputDialog
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Welcome to StatTrack!");
                dialog.setHeaderText("Please enter your name:");
                dialog.setContentText("Name:");

                // Show the dialog and capture the input
                Optional<String> result = dialog.showAndWait();

                if (result.isPresent() && !result.get().isBlank()) {
                    userHistoryDataModel.setUserName(result.get().trim());
                } else {
                    userHistoryDataModel.setUserName("User");
                }
            }
        });


        // ************************* POPULATE DUMMY DATA *************************
        // Here is where we can manually set data to show for testing/demo purposes!!

        // Set completed goals to test on GoalPlan page
        /* userHistoryDataModel.setDailyCompletedGoals(12);
        userHistoryDataModel.setWeeklyCompletedGoals(2);

        // Set username to test on the HomeView page
        userHistoryDataModel.setUserName("HasAPlanFran");*/
        historicalChartButton.setOnAction(e -> scrollPane.setContent(historicalChartView));
    }

    /**
     * Configures the basic UI components for the dashboard.
     */
    private void setupDashboardViewUI() {
        this.getStylesheets().add(getClass().getResource("/homepage.css").toExternalForm());

        // --- header ---
        Image appLogo = new Image((Objects.requireNonNull(getClass().getResourceAsStream("/StatTrackLogo.png"))));
        ImageView appLogoView = new ImageView(appLogo);
        appLogoView.setFitHeight(32);  // Set the width of the image
        appLogoView.setPreserveRatio(true);

        HBox header = new HBox(appLogoView);
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: lightgray; -fx-padding: 7px;");
        this.setTop(header);

        // --- sidebar ---
        VBox sidebar = new VBox();
        sidebar.setSpacing(10);
        sidebar.setAlignment(Pos.CENTER);
        sidebar.setPrefWidth(180);
        homeButton = new Button("Home");
        goalsButton = new Button("My Goals");
        goalPlanButton = new Button("Goal Plan");
        goalVisButton = new Button("Goal Visuals");
        historicalChartButton = new Button("Goal History");

        // Set each button to take up the full width
        homeButton.setMaxWidth(Double.MAX_VALUE);
        goalsButton.setMaxWidth(Double.MAX_VALUE);
        goalPlanButton.setMaxWidth(Double.MAX_VALUE);
        goalVisButton.setMaxWidth(Double.MAX_VALUE);
        historicalChartButton.setMaxWidth(Double.MAX_VALUE);

        // Style the buttons
        homeButton.getStyleClass().add("cbutton");
        goalsButton.getStyleClass().add("cbutton");
        goalPlanButton.getStyleClass().add("cbutton");
        goalVisButton.getStyleClass().add("cbutton");
        historicalChartButton.getStyleClass().add("cbutton");

        // Add current date display
        Label dateLabel = new Label();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        dateLabel.setText(formatter.format(LocalDate.now()));

        Timeline timeline = new Timeline(
                // Have date update every second
                new KeyFrame(Duration.seconds(1), e -> {
                    String newTestDate = formatter.format(LocalDate.now());

                    if (!newTestDate.equals(dateLabel.getText())) {
                        dateLabel.setText(formatter.format(LocalDate.now()));
                        userHistoryDataModel.updateCompletedValues(LocalDate.now());
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        VBox dateModule = new VBox();
        Label dateIntro = new Label("\uD83D\uDCC5 Today's date is:");

        dateLabel.getStyleClass().add("bigger-paragraph-text");
        dateIntro.getStyleClass().add("bigger-paragraph-text");
        dateLabel.setStyle("-fx-font-weight: bold");
        dateModule.getStyleClass().add("date-module");

        dateModule.getChildren().addAll(dateIntro, dateLabel);

        // Add goal summary data
        VBox goalQuickSummaryModule = new VBox();
        Label goalQuickSummaryTitle = new Label("\uD83C\uDFAF Goal Quick Summary:");
        HBox totalGoalsRow = new HBox();
        Label totalGoalsLabel = new Label("Total Tracked Goals: ");
        totalGoals = new Label("" + goalModel.getGoals().size());

        HBox dailyCompletedRow = new HBox();
        Label dailyCompletedLabel = new Label("Goals Completed Today: ");
        dailyCompleted = new Label("" + userHistoryDataModel.getDailyCompletedGoals());

        HBox weeklyCompletedRow = new HBox();
        Label weeklyCompletedLabel = new Label("Goals Completed This Week: ");
        weeklyCompleted = new Label("" + userHistoryDataModel.getWeeklyCompletedGoals());

        goalQuickSummaryTitle.getStyleClass().add("bigger-paragraph-text");
        goalQuickSummaryTitle.setStyle("-fx-font-weight: bold");
        totalGoals.getStyleClass().add("bigger-paragraph-text");
        totalGoals.setStyle("-fx-font-weight: bold");
        weeklyCompleted.getStyleClass().add("bigger-paragraph-text");
        weeklyCompleted.setStyle("-fx-font-weight: bold");
        dailyCompleted.getStyleClass().add("bigger-paragraph-text");
        dailyCompleted.setStyle("-fx-font-weight: bold");
        totalGoalsLabel.getStyleClass().add("bigger-paragraph-text");
        weeklyCompletedLabel.getStyleClass().add("bigger-paragraph-text");
        dailyCompletedLabel.getStyleClass().add("bigger-paragraph-text");
        goalQuickSummaryModule.getStyleClass().add("date-module");

        totalGoalsRow.getChildren().addAll(totalGoalsLabel, totalGoals);
        weeklyCompletedRow.getChildren().addAll(weeklyCompletedLabel, weeklyCompleted);
        dailyCompletedRow.getChildren().addAll(dailyCompletedLabel, dailyCompleted);

        goalQuickSummaryModule.getChildren().addAll(goalQuickSummaryTitle, dailyCompletedRow, weeklyCompletedRow, totalGoalsRow);

        // Add nearest deadline module
        VBox nearestDeadlineModule = new VBox();
        Label nearestDeadlineTitle = new Label("⏰ Your next goal deadline is:");

        LocalDate nearestDeadlineDate = findNextDeadline();

        if (nearestDeadlineDate != null) {
            nearestDeadline = new Label("" + formatter.format(nearestDeadlineDate));
        } else {
            nearestDeadline = new Label("Never—Time to make a goal!");
        }

        nearestDeadlineTitle.getStyleClass().add("bigger-paragraph-text");
        nearestDeadline.setStyle("-fx-font-weight: bold");
        nearestDeadline.getStyleClass().add("bigger-paragraph-text");
        nearestDeadlineModule.getStyleClass().add("next-deadline-module");

        nearestDeadlineModule.getChildren().addAll(nearestDeadlineTitle, nearestDeadline);


        sidebar.getChildren().addAll(homeButton, goalsButton, goalPlanButton, goalVisButton, historicalChartButton);
        VBox.setVgrow(sidebar, Priority.ALWAYS);

        VBox sidebarParent = new VBox(10);
        sidebarParent.getChildren().addAll(dateModule, goalQuickSummaryModule, nearestDeadlineModule, sidebar);
        sidebarParent.setStyle("-fx-background-color: #92d3f5; -fx-padding: 10px;");

        this.setLeft(sidebarParent);

        // --- center ---
        this.setCenter(scrollPane);

        // --- footer ---
        HBox footer = new HBox();
        footer.setSpacing(20);
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-background-color: lightgray; -fx-padding: 10px;");
        this.setBottom(footer);
    }

    @Override
    public void modelUpdated() {
        // Update the responsive elements of the Dashboard
        if (totalGoals != null) {
            totalGoals.setText("" + goalModel.getGoals().size());
        }

        if (weeklyCompleted != null) {
            weeklyCompleted.setText("" + userHistoryDataModel.getWeeklyCompletedGoals());
        }

        if (dailyCompleted != null) {
            dailyCompleted.setText("" + userHistoryDataModel.getDailyCompletedGoals());
        }

        if (nearestDeadline != null) {
            LocalDate nearestDeadlineDate = findNextDeadline();

            if (nearestDeadlineDate != null) {
                nearestDeadline.setText("" + nearestDeadlineDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
            } else {
                nearestDeadline.setText("Never—Time to make a goal!");
            }
        }
    }

    /**
     * Helper method to calculate the nearest deadline.
     * @return the nearest goal deadline.
     */
    private LocalDate findNextDeadline() {
        LocalDate nextDeadline = null;

        for (Goal goal: this.goalModel.getGoals()) {

            if (nextDeadline == null && !goal.isCompleted()) {
                nextDeadline = goal.getEndDate();
            } else if (!goal.isCompleted()) {
                if (goal.getEndDate().isBefore(nextDeadline)) {
                    nextDeadline = goal.getEndDate();
                }
            }

        }
        return nextDeadline;
    }
}