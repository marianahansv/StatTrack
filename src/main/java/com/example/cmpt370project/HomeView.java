package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * View to handle organization UI elements of the home page.
 */
public class HomeView extends StackPane implements Subscriber {
    /**
     * The goal model that this view gets goal data from.
     */
    private GoalModel goalModel;
    /**
     * The root of this view.
     */
    private VBox root;
    /**
     * All the possible pages of the home view.
     */
    private enum HomeViewPage {HOME, ADD_GOAL}
    /**
     * The current page that the home view should show.
     */
    private HomeViewPage currentViewPage = HomeViewPage.HOME;


    // ********* INTERACTIVE UI ELEMENTS (i.e. they change in drawView()) *********
    // ********* HOME PAGE ELEMENTS *********
    private Button addGoalButton;
    private Label welcomeLabel;
    // ********* ADD GOAL PAGE ELEMENTS *********
    private Button submitGoalButton;

    /**
     * Create a new home view page.
     */
    public HomeView() {
        root = new VBox();
        addGoalButton = new Button("Add Goal"); //on main page
        submitGoalButton = new Button("Add Goal");
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        welcomeLabel = new Label("Welcome to the Home Page!");
        root.getChildren().addAll(welcomeLabel, addGoalButton);

        // Add the root UI element to this view
        this.getChildren().add(root);

        // ********* Wire up page change events non-controller based events *********
        addGoalButton.setOnAction(e -> changePage(HomeViewPage.ADD_GOAL));

        drawView();
    }
    /**
     * Update the UI elements of this page when the model changes.
     */
    private void drawView() {
        getChildren().clear();
        switch (currentViewPage){
            case HOME -> drawHomeView();
            case ADD_GOAL -> drawAddGoalView();
        }
    }
    /**
     * Switch to a different page of this view.
     * @param newPage the page this view should switch to.
     */
    private void changePage(HomeViewPage newPage) {
        this.currentViewPage = newPage;
        drawView();
    }
    /**
     * Sets the current page of this view to the home view.
     */
    public void setPageToHomeView() {
        changePage(HomeViewPage.HOME);
    }
    /**
     * Set the goal model of this view.
     * @param goalModel the goal model that this view will pull data from.
     */
    public void setGoalModel(GoalModel goalModel) {
        this.goalModel = goalModel;
        modelUpdated();
    }
    @Override
    public void modelUpdated() {
        drawView();
    }
    /**
     * Set up interaction with a controller for this view.
     * @param c the controller that will handle changing model data for user interactions on this page.
     */
    public void setupEvents(Controller c) {
        // ********* HOME PAGE EVENTS *********
        // No events needed for the home page yet.

        // ********* ADD GOAL PAGE EVENTS *********
        submitGoalButton.setOnAction(e -> {
            // Handle the submission of a new goal (pass to the controller)
            c.handleButtonPress(e);
            changePage(HomeViewPage.HOME); // Return to the summary page after submission
        });
    }
    /**
     * Draws the UI of the Home page.
     */
    private void drawHomeView() {
        // Set up root element for page
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        this.getChildren().add(root);
        root.getChildren().addAll(welcomeLabel, addGoalButton);
    }
    /**
     * Draws the UI of the Add Goal page.
     */
    private void drawAddGoalView() {
        // Set up root element for page
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        this.getChildren().add(root);
        root.getChildren().addAll(submitGoalButton);
    }
}
