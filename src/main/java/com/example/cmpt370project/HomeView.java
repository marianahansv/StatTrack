package com.example.cmpt370project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
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
     * The suggestions data model that this view gets suggestions from.
     */
    private SuggestionsModel suggestionsModel;
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
    private final Label addGoalTitleLabel;
    private final TextField titleInput;
    private final Button cancelAddGoalButton;
    private final Button submitGoalButton;
    private final ComboBox<String> difficultyComboBox;
    private FlowPane sectionButtons;
    private HBox addGoalFormRow1;
    private HBox addGoalFormRow2;
    private HBox addGoalFormRow3;
    private VBox addGoalForm;
    private ToggleGroup sectionToggleGroup;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final Button createSectionButton;
    private final Button deleteSectionButton;
    private final Button giveMeSuggestionsButton;
    private final Label suggestionsContentLabel;


    /**
     * The container that displays goals for the selected section.
     */
    private final VBox goalsBox;

    /**
     * Create a new home view page.
     */
    public HomeView() {
        root = new VBox();
        // Load the CSS file
        this.getStylesheets().add(getClass().getResource("/homepage.css").toExternalForm());

        // Home page elements
        addGoalButton = new Button("Add Goal");
        addGoalButton.getStyleClass().add("button");
        addGoalButton.getStyleClass().add("add-goal-button");

        welcomeLabel = new Label("Welcome to Your Personal Goal Tracker!");
        welcomeLabel.getStyleClass().add("welcome-label");

        clearGoalsButton = new Button("Clear Goals");
        clearGoalsButton.getStyleClass().add("button");
        clearGoalsButton.getStyleClass().add("clear-goals-button");

        // Motivational Message module
        motivationModule = new VBox(20);
        motivationModule.setAlignment(Pos.CENTER_LEFT);
        motivationModule.getStyleClass().add("motivation-module");

        // Greeting text configuration
        userGreeting = new Label();
        userGreeting.getStyleClass().add("user-greeting");
        motivationModule.getChildren().add(userGreeting);

        // Get randomized motivational message and greeting
        Random random = new Random();
        int randomIndex = random.nextInt(motivationalMessages.length);
        motivationalLabel = new Label();
        motivationalLabel.setText(motivationalMessages[randomIndex]);
        motivationalLabel.getStyleClass().add("motivational-label");
        motivationModule.getChildren().add(motivationalLabel);

        randomIndex = random.nextInt(greetings.length);
        currentGreeting = greetings[randomIndex];
        userGreeting.setText(currentGreeting + ", User! Let's complete some goals.");

        // Add goal page elements
        addGoalTitleLabel = new Label("Add a Goal!");
        addGoalTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        submitGoalButton = new Button("Add Goal");
        giveMeSuggestionsButton = new Button("Give me suggestions");

        titleInput = new TextField();
        cancelAddGoalButton = new Button("Cancel");
        cancelAddGoalButton.getStyleClass().add("button");

        difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyComboBox.setValue("Medium"); //default value

        addGoalFormRow1 = new HBox(20);
        addGoalFormRow2 = new HBox(20);
        addGoalFormRow3 = new HBox(20);
        addGoalForm = new VBox(20);

        suggestionsContentLabel = new Label("");

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(welcomeLabel, motivationModule, addGoalButton);

        // Initialize SectionModel and load sections from file
        sectionModel = new SectionModel();
        sectionsList = new ArrayList<>(sectionModel.getSections());

        sectionButtons = new FlowPane();

        sectionButtons.setHgap(10);
        sectionButtons.setVgap(10);
        sectionToggleGroup = new ToggleGroup();
        sectionButtons.setAlignment(Pos.CENTER);

        for (String section : sectionsList) {
            ToggleButton sectionButton = new ToggleButton(section);
            sectionButton.setToggleGroup(sectionToggleGroup);
            sectionButton.getStyleClass().add("button");
            sectionButton.getStyleClass().add("section-button");
            sectionButton.setOnAction(e -> {
                currentSelectedSection = section; // update the currently selected section
                updateGoalsDisplay(section);
            });
            sectionButtons.getChildren().add(sectionButton);
        }

        // DatePickers for start and end date
        startDatePicker = new DatePicker(LocalDate.now());
        endDatePicker = new DatePicker(LocalDate.now().plusDays(7));

        // "Create New Section" feature
        createSectionButton = new Button("Create New Section");
        createSectionButton.getStyleClass().add("button");
        createSectionButton.getStyleClass().add("create-section-button");

        createSectionButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New Section");
            dialog.setHeaderText("Create a New Section");
            dialog.setContentText("Enter section name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && !result.get().isBlank()) {
                String newSection = result.get().trim();

                // check if the section already exists
                if (!sectionModel.addSection(newSection)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Duplicate Section");
                    alert.setHeaderText(null);
                    alert.setContentText("A section with this name already exists.");
                    alert.showAndWait();
                } else {
                    // add section to UI only if it was successfully added to the model
                    sectionsList.add(newSection);
                    ToggleButton sectionButton = new ToggleButton(newSection);
                    sectionButton.setToggleGroup(sectionToggleGroup);
                    sectionButton.getStyleClass().add("button");
                    sectionButton.getStyleClass().add("section-button");
                    sectionButton.setOnAction(ev -> {
                        currentSelectedSection = newSection;
                        updateGoalsDisplay(newSection);
                    });
                    sectionButtons.getChildren().add(sectionButton);
                    drawView();
                }
            }
        });

        // "Delete Section" feature
        deleteSectionButton = new Button("Delete Section");
        deleteSectionButton.getStyleClass().add("button");
        deleteSectionButton.getStyleClass().add("delete-section-button");

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
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Can't delete the only section left!");
                    alert.showAndWait();
                } else {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Delete Section");
                    confirmationAlert.setHeaderText("Are you sure?");
                    confirmationAlert.setContentText("Warning: This will delete all goals in this section!");

                    Optional<ButtonType> confirmationResult = confirmationAlert.showAndWait();
                    if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
                        boolean deleted = sectionModel.deleteSection(selectedSection, userHistoryDataModel);
                        if (deleted) {
                            sectionsList.remove(selectedSection);
                            sectionButtons.getChildren().removeIf(node ->
                                    node instanceof ToggleButton && ((ToggleButton) node).getText().equals(selectedSection)
                            );
                            drawView();
                            goalModel.notifySubscribers();
                        }
                    }
                }
            });
        });

        // initialize the goals display container
        goalsBox = new VBox();
        goalsBox.setSpacing(10);
        goalsBox.setPadding(new Insets(10));
        goalsBox.getStyleClass().add("goals-box");
        goalsBox.setVisible(false);
        goalsBox.setManaged(false);

        this.getChildren().add(root);

        // ********* Wire up page change events non-controller based events *********
        addGoalButton.setOnAction(e -> changePage(HomeViewPage.ADD_GOAL));
        cancelAddGoalButton.setOnAction(e -> changePage(HomeViewPage.HOME));

        drawView();
    }

    private void drawView() {
        getChildren().clear();
        switch (currentViewPage) {
            case HOME -> drawHomeView();
            case ADD_GOAL -> drawAddGoalView();
        }
    }

    private void changePage(HomeViewPage newPage) {
        this.currentViewPage = newPage;
        drawView();
    }

    public void setGoalModel(GoalModel goalModel) {
        this.goalModel = goalModel;

        if (userHistoryDataModel != null) {
            modelUpdated();
        }
    }

    /**
     * Set the suggestions model of this view.
     * @param suggestionsModel the suggestion model that this view will pull data from.
     */
    public void setSuggestionsModel(SuggestionsModel suggestionsModel) {
        this.suggestionsModel = suggestionsModel;
    }

    /**
     * Set the user data model for this view.
     *
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
    public void setupEvents(HomeController c, SuggestionsController s) {
        // ********* HOME PAGE EVENTS *********
        // No events needed for the home page yet.

        // ********* ADD GOAL PAGE EVENTS *********
        //pulling data from input fields
        submitGoalButton.setOnAction(e -> handleGoalSubmission(c));
        giveMeSuggestionsButton.setOnAction(e -> handleSuggestions(s));
        clearGoalsButton.setOnAction(c::removeButtonPress);
    }

    /**
     * Helper method that handles getting goal from user input to pass it to the controller
    **/
    private Goal getGoalFromInput() {
        String difficulty = difficultyComboBox.getValue();
        Toggle selectedToggle = sectionToggleGroup.getSelectedToggle();
        String section = selectedToggle != null ? ((ToggleButton) selectedToggle).getText() : "Uncategorized";
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        return new Goal(titleInput.getText(), section, difficulty, startDate, endDate, false);
    }

    /**
     * Method to encapsulate logic to give user input data to the home controller
    */
    private void handleGoalSubmission(HomeController c){
        try {
            Goal newGoal = getGoalFromInput();
            c.handleButtonPressValidateInput(null, newGoal.getTitle(),newGoal.getSection(),newGoal.getDifficulty(),newGoal.getStartDate(),newGoal.getEndDate(),newGoal.isCompleted());
            c.handleGoalSubmissionButton(newGoal.getTitle(),newGoal.getSection(),newGoal.getDifficulty(),newGoal.getStartDate(),newGoal.getEndDate(),newGoal.isCompleted());
            changePage(HomeViewPage.HOME);
            resetAddGoalPage();
        } catch (InputMismatchException e){
            showErrorAlert(e.getMessage());
        }
    }
    /**
     * Method to encapsulate logic to give user input data to the suggestions controller
     */
    private void handleSuggestions(SuggestionsController s){
        Goal newGoal = getGoalFromInput();
        String suggestionsInText = s.handleButtonPress(newGoal);
        suggestionsContentLabel.setText(suggestionsInText);
    }
     /**
     * Helper method to handle the creation of the error alert message
     * */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oops!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void resetAddGoalPage(){
        titleInput.clear();
        difficultyComboBox.setValue("Medium");
        sectionToggleGroup.selectToggle(sectionToggleGroup.getToggles().getFirst());
    }

    private void drawHomeView() {
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        Label sectionTitle = new Label("My Sections");
        sectionTitle.getStyleClass().add("section-title");

        // hbox for title and sections
        HBox titleAndButtons = new HBox(10);
        titleAndButtons.setAlignment(Pos.CENTER_LEFT);
        titleAndButtons.getChildren().addAll(sectionTitle, createSectionButton, deleteSectionButton);

        FlowPane sectionButtonsBox = new FlowPane();
        sectionButtonsBox.getStyleClass().add("flow-pane");
        sectionButtonsBox.setHgap(10);
        sectionButtonsBox.setVgap(10);
        sectionButtonsBox.setAlignment(Pos.TOP_LEFT);

        // section button
        for (String section : sectionsList) {
            ToggleButton sectionButton = new ToggleButton(section);
            sectionButton.setToggleGroup(sectionToggleGroup);
            sectionButton.getStyleClass().add("button");
            sectionButton.getStyleClass().add("section-button");

            sectionButton.setOnAction(e -> {
                currentSelectedSection = section;
                updateGoalsDisplay(section);
            });

            sectionButtonsBox.getChildren().add(sectionButton);
        }

        VBox mySectionsBox = new VBox(15);
        mySectionsBox.getStyleClass().add("my-sections-container");
        mySectionsBox.setAlignment(Pos.CENTER);
        mySectionsBox.getChildren().addAll(titleAndButtons, sectionButtonsBox);

        VBox sectionsAndGoalsBox = new VBox(10);
        sectionsAndGoalsBox.getChildren().addAll(mySectionsBox, goalsBox);

        if (userHistoryDataModel != null) {
            userGreeting.setText(currentGreeting + ", " + userHistoryDataModel.getUserName() + "! Let's complete some goals.");
        }
        root.getChildren().addAll(welcomeLabel, motivationModule, addGoalButton, clearGoalsButton, sectionsAndGoalsBox);
        this.getChildren().add(root);

        //restore the selected toggle if a section was previously selected.
        if (currentSelectedSection != null) {
            for (javafx.scene.Node node : sectionButtonsBox.getChildren()) {
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
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(30);
        this.getChildren().add(root);

        //clear to avoid dupes
        addGoalFormRow1.getChildren().clear();
        addGoalFormRow2.getChildren().clear();
        addGoalFormRow3.getChildren().clear();
        addGoalForm.getChildren().clear();

        //organize UI elements
        addGoalFormRow1.getChildren().addAll(new Label("Goal Title:"), titleInput, new Label("Sections:"), sectionButtons);
        addGoalFormRow2.getChildren().addAll(new Label("Difficulty:"), difficultyComboBox,
                new Label("Start Date:"), startDatePicker,
                new Label("End Date:"), endDatePicker);
        addGoalFormRow3.getChildren().addAll(giveMeSuggestionsButton, submitGoalButton, cancelAddGoalButton);

        addGoalForm.getChildren().addAll(addGoalFormRow1,addGoalFormRow2,addGoalFormRow3);
        titleInput.setPrefWidth(275);

        //box for suggestions!
        Label sectionLabel = new Label("Your Suggestions, Dani:");
        sectionLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        String suggestionsContent = "Click the button to find out!";
        suggestionsContentLabel.setText(suggestionsContent);

        VBox suggestionsBox = new VBox();
        suggestionsBox.setSpacing(10);
        suggestionsBox.setPadding(new Insets(10));
        suggestionsBox.setStyle("-fx-border-color: grey; -fx-border-width: 2px; -fx-background-color: #f9f9f9;");
        suggestionsBox.getChildren().addAll(suggestionsContentLabel);

        root.getChildren().addAll(
                addGoalTitleLabel,
                addGoalForm,
                sectionLabel,
                suggestionsBox
        );

        //selecting general section toggle
        Toggle default_toggle = sectionToggleGroup.getToggles().getFirst();
        for (Toggle t : sectionToggleGroup.getToggles()) {
            if (default_toggle.equals(t)) t.setSelected(true);
            else {
                t.setSelected(false);
            }

        }
    }

    /**
     * Updates the goalsBox to display all goals in the given section.
     * Assumes goalModel.getGoalsForSection(section) returns a List of Goal objects.
     * If there are no goals, displays a default message.
     *
     * @param section the section whose goals should be displayed.
     */
    private void updateGoalsDisplay(String section) {
        currentSelectedSection = section;
        goalsBox.getChildren().clear();

        Label goalsTitle = new Label("Goals in " + section);
        goalsTitle.getStyleClass().add("goals-title");
        goalsBox.getChildren().add(goalsTitle);
        goalsBox.setVisible(true);
        goalsBox.setManaged(true);

        List<Goal> goals = goalModel.getGoalsForSection(section);
        System.out.println("Updating goals display for section '" + section + "': " + goals.size() + " goal(s) found.");
        if (goals.isEmpty()) {
            Label noGoalsLabel = new Label("No goals in this section.");
            noGoalsLabel.getStyleClass().add("no-goals-label");
            goalsBox.getChildren().add(noGoalsLabel);
        } else {
            for (Goal goal : goals) {
                VBox goalCard = new VBox(10);
                goalCard.getStyleClass().add("goal-card");

                Label titleLabel = new Label("Title: " + goal.getTitle());
                titleLabel.getStyleClass().add("title-label");

                Label difficultyLabel = new Label("Difficulty: " + goal.getDifficulty());
                difficultyLabel.getStyleClass().add("label");

                Label datesLabel = new Label(
                        "Start: " + goal.getStartDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")) +
                                " | End: " + goal.getEndDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
                );
                datesLabel.getStyleClass().add("label");

                Label statusLabel = new Label("Completed: " + (goal.isCompleted() ? "✅" : "❌"));
                statusLabel.getStyleClass().add("label");
                if (goal.isCompleted()) {
                    statusLabel.getStyleClass().add("status-completed");
                } else {
                    statusLabel.getStyleClass().add("status-incomplete");
                }

                goalCard.getChildren().addAll(titleLabel, difficultyLabel, datesLabel, statusLabel);
                goalsBox.getChildren().add(goalCard);
            }
        }
    }
}
