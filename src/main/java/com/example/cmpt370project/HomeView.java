package com.example.cmpt370project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
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
     * The section model that this view gets section data from.
     */
    private SectionModel sectionModel;

    /**
     * The user data model that this view gets goals completed data.
     */
    private UserHistoryDataModel userHistoryDataModel;
    /**
     * List of sections.
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

    /**
     * Field to store the currently selected section.
     */
    private String currentSelectedSection = null;

    // ********* INTERACTIVE UI ELEMENTS (i.e. they change in drawView()) *********

    // ********* HOME PAGE ELEMENTS *********
    private final Button clearGoalsButton;
    private final Button addGoalButton;
    private final Label welcomeLabel;
    private VBox motivationModule;
    private Label userGreeting;
    private Label motivationalLabel;
    private String[] motivationalMessages = {
            "Every step counts, keep moving forward!",
            "Success starts with the first step—let's make it count!",
            "Your goals are within reach—stay focused and keep pushing!",
            "Dream big, work hard, and make it happen!",
            "Progress is progress, no matter how small.",
            "The journey to success begins with the decision to try.",
            "Turn your dreams into goals and your goals into achievements!",
            "Believe in yourself—every goal is possible!",
            "Small daily improvements lead to stunning results!",
            "Start today, your future self will thank you!",
            "The hardest part is starting. The rest is just consistency!",
            "Set your goals, stay determined, and embrace the process!",
            "Success is the sum of small efforts repeated day in and day out.",
            "Push yourself because no one else is going to do it for you.",
            "The only limit to your success is the amount of effort you put in!",
            "Be proud of how far you’ve come, but keep going!",
            "Goals are dreams with deadlines. Let’s make them happen!"
    };
    private String[] greetings = {
            "Hi",
            "Hello",
            "Hey",
            "Hey There",
            "Howdy",
            "Hiya"
    };
    private String currentGreeting;

    // ********* ADD GOAL PAGE ELEMENTS *********
    private final TextField titleInput;
    private final Button cancelAddGoalButton;
    private final Button submitGoalButton;
    private final ComboBox<String> difficultyComboBox;
    private HBox sectionButtons;
    private ToggleGroup sectionToggleGroup;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final Button createSectionButton;
    private final Button deleteSectionButton;


    /**
     * The container that displays goals for the selected section.
     */
    private final VBox goalsBox;

    /**
     * Create a new home view page.
     */
    public HomeView() {
        root = new VBox();
        // Home page element
        addGoalButton = new Button("Add Goal");
        welcomeLabel = new Label("Welcome to Your Personal Goal Tracker!");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        clearGoalsButton = new Button("Clear Goals");

        // Motivational Message module
        motivationModule = new VBox(20);
        motivationModule.setAlignment(Pos.CENTER_LEFT);
        motivationModule.setStyle("-fx-background-color: lightgray; -fx-background-radius: 5;");
        motivationModule.setPadding(new Insets(20));

        // Greeting text configuration
        userGreeting = new Label();
        userGreeting.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        motivationModule.getChildren().add(userGreeting);

        // Get randomized motivational message and greeting
        Random random = new Random();
        int randomIndex = random.nextInt(motivationalMessages.length);
        motivationalLabel = new Label();
        motivationalLabel.setText(motivationalMessages[randomIndex]);
        motivationModule.getChildren().add(motivationalLabel);

        randomIndex = random.nextInt(greetings.length);
        currentGreeting = greetings[randomIndex];
        userGreeting.setText(currentGreeting + ", User! Let's complete some goals.");

        // Add goal page elements
        submitGoalButton = new Button("Add Goal");

        titleInput = new TextField();
        cancelAddGoalButton = new Button("Cancel");
        difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyComboBox.setValue("Medium"); //default value

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(welcomeLabel, motivationModule, addGoalButton);

        // Initialize SectionModel and load sections from file
        sectionModel = new SectionModel();
        sectionsList = new ArrayList<>(sectionModel.getSections());

        // ToggleGroup for sections
        sectionToggleGroup = new ToggleGroup();
        sectionButtons = new HBox(10);
        sectionButtons.setAlignment(Pos.CENTER);
        for (String section : sectionsList) {
            ToggleButton sectionButton = new ToggleButton(section);
            sectionButton.setToggleGroup(sectionToggleGroup);
            sectionButton.setOnAction(e -> {
                currentSelectedSection = section; // update the currently selected section
                updateGoalsDisplay(section);
            });
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
                // Persist the new section using SectionModel
                sectionModel.addSection(newSection);
                sectionsList.add(newSection);
                ToggleButton sectionButton = new ToggleButton(newSection);
                sectionButton.setToggleGroup(sectionToggleGroup);
                sectionButton.setOnAction(ev -> {
                    currentSelectedSection = newSection;
                    updateGoalsDisplay(newSection);
                });
                sectionButtons.getChildren().add(sectionButton);
            }
        });

        // "Delete Section" feature
        deleteSectionButton = new Button("Delete Section");
        deleteSectionButton.setOnAction(e -> {
            // ChoiceDialog to let the user select a section to delete
            ChoiceDialog<String> dialog = new ChoiceDialog<>(null, new ArrayList<>(sectionModel.getSections()));
            dialog.setTitle("Delete Section");
            dialog.setHeaderText("Select a section to delete:");
            dialog.setContentText("Section:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(selectedSection -> {
                // Delete the section from the model ONLY if there's more than one left
                if (sectionModel.getSections().size() <= 1) {
                    Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
                    alert.setTitle("Warning");
                    alert.setHeaderText("Can't delete the only section left!");
                    alert.showAndWait();
                }
                else{
                    boolean deleted = sectionModel.deleteSection(selectedSection);
                    if (deleted) {
                        // Remove section from the local list and remove its toggle button from the UI
                        sectionsList.remove(selectedSection);
                        sectionButtons.getChildren().removeIf(node ->
                                node instanceof ToggleButton && ((ToggleButton)node).getText().equals(selectedSection)
                        );
                    }
                }
            });
        });

        // initialize the goals display container
        goalsBox = new VBox();
        goalsBox.setSpacing(10);
        goalsBox.setPadding(new Insets(10));
        goalsBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: #cdb6f3;");
        goalsBox.setVisible(false);
        goalsBox.setManaged(false);

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
        switch (currentViewPage) {
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

        if (userHistoryDataModel != null) {
            modelUpdated();
        }
    }

    /**
     * Set the user data model for this view.
     * @param userHistoryDataModel the user data model for this view.
     */
    public void setUserHistoryDataModel(UserHistoryDataModel userHistoryDataModel) {
        this.userHistoryDataModel = userHistoryDataModel;

        if (goalModel != null) {
            modelUpdated();
        }
    }

    @Override
    public void modelUpdated() {
        // If a section is currently selected, update its goals display; otherwise, redraw the view.
        if (currentViewPage == HomeViewPage.HOME && sectionToggleGroup.getSelectedToggle() != null) {
            String selectedSection = ((ToggleButton) sectionToggleGroup.getSelectedToggle()).getText();
            updateGoalsDisplay(selectedSection);
        } else {
            drawView();
        }
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
            String section = ((ToggleButton) selectedToggle).getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            boolean completed = false; // Why would you add a goal you completed?
            //handle any user input errors if exceptions are thrown by the controller
            try {
                c.handleButtonPress(e, titleInput.getText(), section, difficulty, startDate, endDate, completed);
                changePage(HomeViewPage.HOME); // Return to the summary page after submission
                resetAddGoalPage();
            } catch (InputMismatchException error){
                // Display error message
                Alert alert = new Alert(Alert.AlertType.valueOf("ERROR"));
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText(error.getMessage());
                alert.showAndWait();
            }
        });

        clearGoalsButton.setOnAction(c::removeButtonPress);
    }

    private void resetAddGoalPage(){
        titleInput.clear();
        difficultyComboBox.setValue("Medium");
        sectionToggleGroup.selectToggle(sectionToggleGroup.getToggles().getFirst());
    }
    /**
     * Draws the UI of the Home page.
     */
    private void drawHomeView() {
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

        // Get and population user's name (once it has been set in the model)
        if (userHistoryDataModel != null) {
            userGreeting.setText(currentGreeting + ", " + userHistoryDataModel.getUserName() + "! Let's complete some goals.");
        }

        root.getChildren().addAll(welcomeLabel, motivationModule, addGoalButton, clearGoalsButton, sectionBox, createSectionButton, deleteSectionButton, goalsBox);
        this.getChildren().add(root);

        //restore the selected toggle if a section was previously selected.
        if (currentSelectedSection != null) {
            for (javafx.scene.Node node : sectionButtons.getChildren()) {
                if (node instanceof ToggleButton) {
                    ToggleButton tb = (ToggleButton) node;
                    if (tb.getText().equalsIgnoreCase(currentSelectedSection)) {
                        tb.setSelected(true);
                        updateGoalsDisplay(currentSelectedSection);
                        break;
                    }
                }
            }
        }
    }
    /**
     * Draws the UI of the Add Goal page.
     */
    private void drawAddGoalView() {
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
        this.getChildren().add(root);
        root.getChildren().addAll(
                new Label("Goal Title:"), titleInput,
                new Label("Sections:"), sectionButtons,
                new Label("Difficulty:"), difficultyComboBox,
                new Label("Start Date:"), startDatePicker,
                new Label("End Date:"), endDatePicker,
                submitGoalButton, cancelAddGoalButton
        );
        //selecting general section toggle
        Toggle default_toggle = sectionToggleGroup.getToggles().getFirst();
        for (Toggle t: sectionToggleGroup.getToggles()) {
            if (default_toggle.equals(t)) t.setSelected(true);
            else {t.setSelected(false);}

        }
    }

    /**
     * Updates the goalsBox to display all goals in the given section.
     * Assumes goalModel.getGoalsForSection(section) returns a List of Goal objects.
     * If there are no goals, displays a default message.
     * @param section the section whose goals should be displayed.
     */
    private void updateGoalsDisplay(String section) {
        currentSelectedSection = section;
        goalsBox.getChildren().clear();

        // the section box only shows up when the section is selected
        goalsBox.setVisible(true);
        goalsBox.setManaged(true);

        List<Goal> goals = goalModel.getGoalsForSection(section);
        System.out.println("Updating goals display for section '" + section + "': " + goals.size() + " goal(s) found.");
        if (goals.isEmpty()) {
            goalsBox.getChildren().add(new Label("No goals in this section."));
        } else {
            for (Goal goal : goals) {
                Label goalLabel = new Label(goal.toString());
                goalsBox.getChildren().add(goalLabel);
            }
        }
    }
}
