package com.example.cmpt370project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

/**
 * View to handle organization UI elements of the home page.
 */
public class HomeView extends StackPane implements Subscriber {
    /**
     * The goal model that this view gets goal data from.
     */
    private GoalModel goalModel;
    /**
     * List of sections
     */
    private ArrayList<String> sectionsList;

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
    private final Button clearGoalsButton;
    private final Button addGoalButton;
    private final Label welcomeLabel;
    // ********* ADD GOAL PAGE ELEMENTS *********
    private final TextField titleInput;
    private final Button cancelAddGoalButton;
    private final Button submitGoalButton;
    private final ComboBox<String> difficultyComboBox;
    private HBox sectionButtons;
    private ToggleGroup sectionToggleGroup;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;

    // New button for creating a new section
    private final Button createSectionButton;

    /**
     * Create a new home view page.
     */
    public HomeView() {
        root = new VBox();
        // Home page element
        addGoalButton = new Button("Add Goal"); //on main page
        welcomeLabel = new Label("Welcome to the Home Page!");
        clearGoalsButton = new Button("Clear Goals");

        // Add goal page element
        submitGoalButton = new Button("Add Goal"); //to database
        titleInput = new TextField();
        cancelAddGoalButton = new Button("Cancel");
        difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(welcomeLabel, addGoalButton);

        // ToggleGroup for sections
        sectionsList = new ArrayList<String>();
        sectionsList.add("General");
        sectionsList.add("Personal");
        sectionsList.add("Fitness");
        // Group for toggle buttons for sections
        sectionToggleGroup = new ToggleGroup();
        sectionButtons = new HBox(10);
        sectionButtons.setAlignment(Pos.CENTER);
        for (String section : sectionsList) {
            ToggleButton sectionButton = new ToggleButton(section);
            sectionButton.setToggleGroup(sectionToggleGroup);
            sectionButtons.getChildren().add(sectionButton);
        }
        // DatePickers for start and end date
        startDatePicker = new DatePicker(LocalDate.now());
        endDatePicker = new DatePicker(LocalDate.now().plusDays(7));

        //  "Create New Section" feature
        createSectionButton = new Button("Create New Section");
        createSectionButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New Section");
            dialog.setHeaderText("Create a New Section");
            dialog.setContentText("Enter section name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().isBlank()) {
                String newSection = result.get();
                sectionsList.add(newSection);
                ToggleButton sectionButton = new ToggleButton(newSection);
                sectionButton.setToggleGroup(sectionToggleGroup);
                sectionButtons.getChildren().add(sectionButton);
            }
        });

        // Add the root UI element to this view
        this.getChildren().add(root);

        // ********* Wire up page change events non-controller based events *********
        addGoalButton.setOnAction(e -> changePage(HomeViewPage.ADD_GOAL));
        cancelAddGoalButton.setOnAction(e -> changePage(HomeViewPage.HOME));

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
    public void setupEvents(HomeController c) {
        // ********* HOME PAGE EVENTS *********
        // No events needed for the home page yet.

        // ********* ADD GOAL PAGE EVENTS *********
        submitGoalButton.setOnAction(e -> {
            // Handle the submission of a new goal (pass to the controller)
            String difficulty = difficultyComboBox.getValue();
            Toggle selectedToggle = sectionToggleGroup.getSelectedToggle();
            String section = ((ToggleButton)selectedToggle).getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            c.handleButtonPress(e, titleInput.getText(), difficulty, section, startDate, endDate);
            changePage(HomeViewPage.HOME); // Return to the summary page after submission
        });

        clearGoalsButton.setOnAction(c::removeButtonPress);
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

        // create a box to display sections
        VBox sectionBox = new VBox();
        sectionBox.setSpacing(10);
        sectionBox.setPadding(new Insets(10));
        sectionBox.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-background-color: #f9f9f9;");
        Label sectionLabel = new Label("Sections:");
        sectionBox.getChildren().addAll(sectionLabel, sectionButtons);

        // add the section box and the create section button separately to the root
        root.getChildren().addAll(welcomeLabel, addGoalButton, clearGoalsButton, sectionBox, createSectionButton);
        this.getChildren().add(root);
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
        root.getChildren().addAll(
                new Label("Goal Title:"), titleInput,
                new Label("Difficulty:"), difficultyComboBox,
                new Label("Sections:"), sectionButtons,
                new Label("Start Date:"), startDatePicker,
                new Label("End Date:"), endDatePicker,
                submitGoalButton, cancelAddGoalButton
        );
    }
}
