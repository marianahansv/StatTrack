package com.example.cmpt370project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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
    enum HomeViewPage {HOME, ADD_GOAL}

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
    private Label quickActionsHeading;
    private Label sectionHeading;
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
    private VBox upcomingGoalsModule;
    private ScrollPane upcomingGoalsScroll;
    private Label upcomingGoalsHeading;

    // ********* ADD GOAL PAGE ELEMENTS *********
    private final Label addGoalTitleLabel;
    private final TextField titleInput;
    private final Button cancelAddGoalButton;
    private final Button submitGoalButton;
    final Button changeNameButton;
    private final ComboBox<String> difficultyComboBox;
    private FlowPane sectionButtons;
    private HBox addGoalFormRow1;
    private HBox addGoalFormRow2;
    private HBox addGoalFormRow3;
    private HBox addGoalFormRow4;
    private VBox suggestionsBox;
    private VBox addGoalForm;
    private ToggleGroup sectionToggleGroup;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final Button createSectionButton;
    private final Button deleteSectionButton;
    private final Button giveMeSuggestionsButton;
    private final Label suggestionsContentLabel;
    private final Label goalDataOverviewLabel;
    private HBox goalDataOverviewModule;


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
        addGoalButton.getStyleClass().add("cbutton");
//        addGoalButton.getStyleClass().add("add-goal-button");

        welcomeLabel = new Label("Welcome to StatTrack!");
        welcomeLabel.getStyleClass().add("welcome-label");

        sectionHeading = new Label("Goal Sections");
        quickActionsHeading = new Label("Quick Actions");
        upcomingGoalsHeading = new Label("Upcoming Goals");
        goalDataOverviewLabel = new Label("Goal Data Overview");
        goalDataOverviewModule = new HBox();

        sectionHeading.getStyleClass().add("heading-level-2");
        quickActionsHeading.getStyleClass().add("heading-level-2");
        upcomingGoalsHeading.getStyleClass().add("heading-level-2");
        goalDataOverviewLabel.getStyleClass().add("heading-level-2");

        clearGoalsButton = new Button("Clear Goals");
        clearGoalsButton.getStyleClass().add("cbutton");
        clearGoalsButton.getStyleClass().add("critical-button");

//        clearGoalsButton.getStyleClass().add("clear-goals-button");

        changeNameButton = new Button("Change Name");
        changeNameButton.getStyleClass().add("cbutton");
//        changeNameButton.getStyleClass().add("change-name-button");

        // Motivational Message module
        motivationModule = new VBox(20);
        motivationModule.setAlignment(Pos.CENTER_LEFT);
        motivationModule.getStyleClass().add("module");

        // Greeting text configuration
        userGreeting = new Label();
        userGreeting.getStyleClass().add("user-greeting");
        motivationModule.getChildren().add(userGreeting);

        // Get randomized motivational message and greeting
        Random random = new Random();
        int randomIndex = random.nextInt(motivationalMessages.length);
        motivationalLabel = new Label();
        motivationalLabel.setText(motivationalMessages[randomIndex]);
        motivationalLabel.getStyleClass().add("bigger-paragraph-text");
        motivationModule.getChildren().add(motivationalLabel);

        randomIndex = random.nextInt(greetings.length);
        currentGreeting = greetings[randomIndex];
        userGreeting.setText(currentGreeting + ", User! Let's complete some goals.");

        // Add goal page elements
        addGoalTitleLabel = new Label("Let's Add a New Goal:");
        addGoalTitleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        submitGoalButton = new Button("Add Goal");
        submitGoalButton.getStyleClass().add("cbutton");
        giveMeSuggestionsButton = new Button("Give Me Suggestions");
        giveMeSuggestionsButton.getStyleClass().add("cbutton");

        titleInput = new TextField();
        cancelAddGoalButton = new Button("Cancel");
        cancelAddGoalButton.getStyleClass().add("cbutton");

        difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyComboBox.setValue("Medium"); //default value

        addGoalFormRow1 = new HBox(20);
        addGoalFormRow2 = new HBox(20);
        addGoalFormRow3 = new HBox(20);
        addGoalFormRow4 = new HBox(20);
        addGoalForm = new VBox(30);

        suggestionsContentLabel = new Label("");

        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(welcomeLabel, motivationModule, addGoalButton);

        // upcoming goal
        upcomingGoalsModule = new VBox(10);
        upcomingGoalsModule.setAlignment(Pos.TOP_LEFT);
        upcomingGoalsModule.getStyleClass().add("upcoming-goals-module");
        upcomingGoalsScroll = new ScrollPane();
        upcomingGoalsScroll.setFitToWidth(true);
        upcomingGoalsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        upcomingGoalsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        upcomingGoalsScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        upcomingGoalsModule.getChildren().add(upcomingGoalsScroll);

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
            sectionButton.getStyleClass().add("cbutton");
//            sectionButton.getStyleClass().add("section-button");
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
        createSectionButton.getStyleClass().add("cbutton");
//        createSectionButton.getStyleClass().add("create-section-button");

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
                    sectionButton.getStyleClass().add("cbutton");
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
        deleteSectionButton.getStyleClass().add("cbutton");
        deleteSectionButton.getStyleClass().add("critical-button");
//        deleteSectionButton.getStyleClass().add("delete-section-button");

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

    void changePage(HomeViewPage newPage) {
        this.currentViewPage = newPage;
        drawView();
    }

    public void setGoalModel(GoalModel goalModel) {
        this.goalModel = goalModel;
        if (goalModel != null) {
            updateUpcomingGoals();
        }
        if (userHistoryDataModel != null) {
            modelUpdated();
        }
    }

    /**
     * Set the suggestions model of this view.
     *
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
            drawView();
            String selectedSection = ((ToggleButton) sectionToggleGroup.getSelectedToggle()).getText();
            updateGoalsDisplay(selectedSection);
            if (userHistoryDataModel != null) {
                userGreeting.setText(currentGreeting + ", " + userHistoryDataModel.getUserName() + "! Let's complete some goals.");
            }
        } else {
            drawView();
        }
        if (goalModel != null) {
            updateUpcomingGoals();
        }
    }

    /**
     * Set up interaction with a controller for this view.
     *
     * @param c the controller that will handle changing model data for user interactions on this page.
     */
    public void setupEvents(HomeController c, SuggestionsController s) {
        // ********* HOME PAGE EVENTS *********
        // No events needed for the home page yet.

        // ********* ADD GOAL PAGE EVENTS *********
        //pulling data from input fields
        submitGoalButton.setOnAction(e -> handleGoalSubmission(c,s));
        giveMeSuggestionsButton.setOnAction(e -> handleSuggestions(s));
        clearGoalsButton.setOnAction(c::removeButtonPress);
        changeNameButton.setOnAction(c::handleChangeName);
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
    private void handleGoalSubmission(HomeController c, SuggestionsController s) {
        try {
            Goal newGoal = getGoalFromInput();
            c.handleButtonPressValidateInput(null, newGoal.getTitle(), newGoal.getSection(), newGoal.getDifficulty(), newGoal.getStartDate(), newGoal.getEndDate(), newGoal.isCompleted());
            c.handleGoalSubmissionButton(newGoal.getTitle(), newGoal.getSection(), newGoal.getDifficulty(), newGoal.getStartDate(), newGoal.getEndDate(), newGoal.isCompleted());
            s.updateModel();
            changePage(HomeViewPage.HOME);
            resetAddGoalPage();
        } catch (InputMismatchException e) {
            showErrorAlert(e.getMessage());
        }
    }

    /**
     * Method to encapsulate logic to give user input data to the suggestions controller
     */
    private void handleSuggestions(SuggestionsController s) {
        try {
            suggestionsBox.getChildren().clear();
            Goal newGoal = getGoalFromInput();
            s.validateInput(newGoal);
            List<String> suggestionsInText = s.handleButtonPress(newGoal);
            for (String suggestion: suggestionsInText){
                suggestionsBox.getChildren().add(createSuggestionBox(suggestion));
            }
            suggestionsContentLabel.setText("Here's what I found...");
        } catch (InputMismatchException e) {
            showErrorAlert(e.getMessage());
        }
    }

    /**
     * Helper method to handle the creation of the error alert message
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Oops!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetAddGoalPage() {
        titleInput.clear();
        difficultyComboBox.setValue("Medium");
        sectionToggleGroup.selectToggle(sectionToggleGroup.getToggles().get(0));
    }

    private void drawHomeView() {
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_LEFT);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        FlowPane sectionButtonsBox = new FlowPane();
        sectionButtonsBox.getStyleClass().add("flow-pane");
        sectionButtonsBox.setHgap(10);
        sectionButtonsBox.setVgap(10);
        sectionButtonsBox.setAlignment(Pos.TOP_LEFT);

        // section button
        for (String section : sectionsList) {
            ToggleButton sectionButton = new ToggleButton(section);
            sectionButton.setToggleGroup(sectionToggleGroup);
            sectionButton.getStyleClass().add("cbutton");
            sectionButton.getStyleClass().add("section-button");

            sectionButton.setOnAction(e -> {
                currentSelectedSection = section;
                updateGoalsDisplay(section);
            });

            sectionButtonsBox.getChildren().add(sectionButton);
        }

        VBox mySectionsBox = new VBox(15);
        mySectionsBox.setAlignment(Pos.CENTER);
        mySectionsBox.getChildren().addAll(sectionButtonsBox);

        VBox sectionsAndGoalsBox = new VBox(20);
        sectionsAndGoalsBox.getChildren().addAll(mySectionsBox, goalsBox);

        if (userHistoryDataModel != null) {
            userGreeting.setText(currentGreeting + ", " + userHistoryDataModel.getUserName() + "! Let's complete some goals.");
        }

        // Make quick actions grouped
        HBox quickActionsGroup = new HBox(10);
        Region spacer1 = new Region();
        spacer1.setPrefWidth(20);
        Region spacer2 = new Region();
        spacer2.setPrefWidth(20);
        quickActionsGroup.getChildren().addAll(addGoalButton, clearGoalsButton, spacer1, createSectionButton, deleteSectionButton, spacer2, changeNameButton);

        // Goal Overview Module
        goalDataOverviewModule = new HBox(20);
        goalDataOverviewModule.getStyleClass().add("b-module");

        if (goalModel != null) {

            // Goals Completed
            VBox goalsCompletedBox = new VBox(10);
            goalsCompletedBox.setAlignment(Pos.CENTER);
            Label goalsCompletedLabel = new Label("Total Goals Completed: ");
            Label goalsCompletedNumberLabel = new Label("" + goalModel.getGoals().stream().filter(Goal::isCompleted).collect(Collectors.toList()).size());
            goalsCompletedLabel.getStyleClass().add("bigger-paragraph-text");
            goalsCompletedNumberLabel.getStyleClass().add("bigger-paragraph-text");
            goalsCompletedNumberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

            goalsCompletedBox.getStyleClass().add("goal-card");
            goalsCompletedBox.getChildren().addAll(goalsCompletedLabel, goalsCompletedNumberLabel);

            goalDataOverviewModule.getChildren().addAll(goalsCompletedBox);
            HBox.setHgrow(goalsCompletedBox, Priority.ALWAYS);

            // Goals In-Progress
            VBox goalsInProgressBox = new VBox(10);
            goalsInProgressBox.setAlignment(Pos.CENTER);
            Label goalsInProgressLabel = new Label("Total Goals In-Progress: ");

            Label goalsInProgressNumberLabel = new Label("" + goalModel.getGoals().stream()
                    .filter(goal -> (!goal.getEndDate().isBefore(LocalDate.now()))
                            && (!goal.getStartDate().isAfter(LocalDate.now()) || goal.getStartDate().isEqual(LocalDate.now()))
                            && !goal.isCompleted())
                    .count());
            goalsInProgressLabel.getStyleClass().add("bigger-paragraph-text");
            goalsInProgressNumberLabel.getStyleClass().add("bigger-paragraph-text");
            goalsInProgressNumberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

            goalsInProgressBox.getStyleClass().add("goal-card");
            goalsInProgressBox.getChildren().addAll(goalsInProgressLabel, goalsInProgressNumberLabel);

            goalDataOverviewModule.getChildren().addAll(goalsInProgressBox);
            HBox.setHgrow(goalsInProgressBox, Priority.ALWAYS);


            // Goals Expired
            VBox goalsExpiredBox = new VBox(10);
            goalsExpiredBox.setAlignment(Pos.CENTER);
            Label goalsExpiredLabel = new Label("Total Goals Expired: ");

            Label goalsExpiredNumberLabel = new Label("" + goalModel.getGoals().stream()
                    .filter(goal -> (goal.getEndDate().isBefore(LocalDate.now())))
                    .count());
            goalsExpiredLabel.getStyleClass().add("bigger-paragraph-text");
            goalsExpiredNumberLabel.getStyleClass().add("bigger-paragraph-text");
            goalsExpiredNumberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

            goalsExpiredBox.getStyleClass().add("goal-card");
            goalsExpiredBox.getChildren().addAll(goalsExpiredLabel, goalsExpiredNumberLabel);

            goalDataOverviewModule.getChildren().addAll(goalsExpiredBox);
            HBox.setHgrow(goalsExpiredBox, Priority.ALWAYS);


            // Goal Avg. Timeline
            VBox goalTimelineAvgBox = new VBox(10);
            goalTimelineAvgBox.setAlignment(Pos.CENTER);
            Label goalTimelineAvgLabel = new Label("Average Goal Duration: ");

            long totalDays = goalModel.getGoals().stream()
                    .mapToLong(goal -> ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate().plusDays(1)))
                    .sum();

            Label goalTimelineAvgNumberLabel = new Label("" + (long) Math.ceil(((double) totalDays / goalModel.getGoals().size())) + " days");
            goalTimelineAvgLabel.getStyleClass().add("bigger-paragraph-text");
            goalTimelineAvgNumberLabel.getStyleClass().add("bigger-paragraph-text");
            goalTimelineAvgNumberLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

            goalTimelineAvgBox.getStyleClass().add("goal-card");
            goalTimelineAvgBox.getChildren().addAll(goalTimelineAvgLabel, goalTimelineAvgNumberLabel);

            goalDataOverviewModule.getChildren().addAll(goalTimelineAvgBox);
            HBox.setHgrow(goalTimelineAvgBox, Priority.ALWAYS);


            // Goal Most Popular Difficulty
            VBox goalDifficultyPopBox = new VBox(10);
            goalDifficultyPopBox.setAlignment(Pos.CENTER);
            Label goalDifficultyPopLabel = new Label("Most Popular Difficulty: ");

            Map<String, Long> difficultyCounts = goalModel.getGoals().stream()
                    .collect(Collectors.groupingBy(Goal::getDifficulty, Collectors.counting()));

            // Find the difficulty with the most goals
            String maxDifficulty = difficultyCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue()) // Compare by count
                    .map(Map.Entry::getKey) // Get the difficulty key
                    .orElse(null); // In case there are no goals
            if (maxDifficulty == null) {
                maxDifficulty = "None";
            }

            Label goalDifficultyPopCategoryLabel = new Label(maxDifficulty);
            goalDifficultyPopLabel.getStyleClass().add("bigger-paragraph-text");
            goalDifficultyPopCategoryLabel.getStyleClass().add("bigger-paragraph-text");
            goalDifficultyPopCategoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

            goalDifficultyPopBox.getStyleClass().add("goal-card");
            goalDifficultyPopBox.getChildren().addAll(goalDifficultyPopLabel, goalDifficultyPopCategoryLabel);

            goalDataOverviewModule.getChildren().addAll(goalDifficultyPopBox);
            HBox.setHgrow(goalDifficultyPopBox, Priority.ALWAYS);
        }

        int homeViewSpaceSize = 20;

        Region homeSpacer1 = new Region();
        homeSpacer1.setPrefWidth(homeViewSpaceSize);

        Region homeSpacer2 = new Region();
        homeSpacer2.setPrefWidth(homeViewSpaceSize);

        Region homeSpacer3 = new Region();
        homeSpacer3.setPrefWidth(homeViewSpaceSize);

        Region homeSpacer4 = new Region();
        homeSpacer4.setPrefWidth(homeViewSpaceSize);

        Label quickActionsSubtitle = new Label("What would you like to do next in your goal planning?");
        quickActionsSubtitle.getStyleClass().add("bigger-paragraph-text");
        Label overviewGoalsSubtitle = new Label("Here are some summary stats for your currently planned goals:");
        overviewGoalsSubtitle.getStyleClass().add("bigger-paragraph-text");
        Label upcomingGoalsSubtitle = new Label("Let's see which goals you should focus on completing next:");
        upcomingGoalsSubtitle.getStyleClass().add("bigger-paragraph-text");
        Label sectionGoalsSubtitle = new Label("Here are your current sections and their goals:");
        sectionGoalsSubtitle.getStyleClass().add("bigger-paragraph-text");

        root.getChildren().addAll(welcomeLabel, motivationModule, homeSpacer1, new VBox(5, quickActionsHeading, quickActionsSubtitle), quickActionsGroup, homeSpacer2,
                new VBox(5, goalDataOverviewLabel, overviewGoalsSubtitle), goalDataOverviewModule, homeSpacer4, new VBox(5, upcomingGoalsHeading, upcomingGoalsSubtitle),
                upcomingGoalsModule, homeSpacer3, new VBox(5, sectionHeading, sectionGoalsSubtitle), sectionsAndGoalsBox);
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
        root.setSpacing(35);
        this.getChildren().add(root);

        //clear to avoid dupes
        addGoalFormRow1.getChildren().clear();
        addGoalFormRow2.getChildren().clear();
        addGoalFormRow3.getChildren().clear();
        addGoalFormRow4.getChildren().clear();
        addGoalForm.getChildren().clear();

        submitGoalButton.getStyleClass().add("button");
        submitGoalButton.getStyleClass().add("add-goal-button");
        giveMeSuggestionsButton.getStyleClass().add("button");
        giveMeSuggestionsButton.getStyleClass().add("suggestions-button");
        cancelAddGoalButton.getStyleClass().add("button");
        cancelAddGoalButton.getStyleClass().add("cancel-button");
        Label sectionLabel = new Label("Want Suggestions on Your New Goal, " + userHistoryDataModel.getUserName() + "?");
        sectionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        //organize UI elements
        addGoalFormRow1.getChildren().addAll(new Label("Goal Title:"), titleInput, new Label("Sections:"), sectionButtons);
        addGoalFormRow2.getChildren().addAll(new Label("Difficulty:"), difficultyComboBox,
                new Label("Start Date:"), startDatePicker,
                new Label("End Date:"), endDatePicker);
        addGoalFormRow3.getChildren().addAll(giveMeSuggestionsButton,submitGoalButton, cancelAddGoalButton);
        addGoalFormRow4.getChildren().addAll(sectionLabel);

        addGoalForm.getChildren().addAll(addGoalFormRow1, addGoalFormRow2, addGoalFormRow3, addGoalFormRow4);
        titleInput.setPrefWidth(275);

        if (sectionToggleGroup.getSelectedToggle() == null && !sectionToggleGroup.getToggles().isEmpty()) {
            sectionToggleGroup.selectToggle(sectionToggleGroup.getToggles().get(0));
        }

        String suggestionsContent = "Click the button to find out!";
        suggestionsContentLabel.setText(suggestionsContent);
        suggestionsContentLabel.setStyle("-fx-font-size: 16px;");

        suggestionsBox = new VBox();
        suggestionsBox.setSpacing(4);
        //suggestionsBox.setPadding(new Insets(10));
//        suggestionsBox.setStyle("-fx-border-color: grey; -fx-border-width: 2px; -fx-background-color: #f9f9f9;");
        suggestionsBox.getStyleClass().add("module");
        suggestionsBox.getChildren().addAll(suggestionsContentLabel);

        // Spacer (to separate Form from suggestions)

        //Region spacer1 = new Region();

        root.getChildren().addAll(
                addGoalTitleLabel,
                addGoalForm,
                //spacer1,
                suggestionsBox
        );

        //selecting general section toggle
        Toggle default_toggle = sectionToggleGroup.getToggles().get(0);
        for (Toggle t : sectionToggleGroup.getToggles()) {
            if (default_toggle.equals(t)) t.setSelected(true);
            else {
                t.setSelected(false);
            }

        }
    }
    /**
     * Helper method to create each suggestion box
     * */
    private HBox createSuggestionBox(String content) {
        Label suggestionContentLabel = new Label(content);
        suggestionContentLabel.setStyle("-fx-font-size: 14px;");
        suggestionContentLabel.setWrapText(true);

        HBox suggestionBox = new HBox(30); // 10px spacing between elements
        suggestionBox.getChildren().addAll(suggestionContentLabel);
        if (content.contains("you tend to finish around"))
        {
            Button acceptButton = new Button("Change it! ✅");
            acceptButton.setPrefWidth(300);
            acceptButton.setOnAction(e -> {
                acceptTimelineSuggestion(content);
                acceptButton.setDisable(true);
            });
            suggestionBox.getChildren().add(acceptButton);
        }
        suggestionBox.setSpacing(10);
        suggestionBox.getStyleClass().add("module");

        return suggestionBox;
    }

    /**
     * Updates the view timeline for the goal after user accepts changes
     * */
    public void acceptTimelineSuggestion(String suggestion){
        if (suggestion.contains("add")){
            String[] allStrings = suggestion.split(" ");
            String numberOfDays = allStrings[allStrings.length - 2];
            endDatePicker.setValue(endDatePicker.getValue().plusDays(Long.parseLong(numberOfDays)));
        }
        else if (suggestion.contains("minus")){
            String[] allStrings = suggestion.split(" ");
            String numberOfDays = allStrings[allStrings.length - 2];
            endDatePicker.setValue(endDatePicker.getValue().minusDays(Long.parseLong(numberOfDays)));
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

    private VBox createGoalCard(Goal goal, LocalDate today) {
        VBox goalCard = new VBox(5);
        goalCard.setPadding(new Insets(10));
        goalCard.getStyleClass().add("upcoming-goal-card");

        //calculate days remaining until due date
        long daysRemaining = ChronoUnit.DAYS.between(today, goal.getEndDate());
        double progress = 1.0 - (Math.min(7.0, Math.max(0.0, daysRemaining)) / 7.0);

        // title and due date
        Label titleLabel = new Label(goal.getTitle());
        titleLabel.getStyleClass().add("upcoming-goal-title");

        Label dateLabel = new Label("Due: " +
                goal.getEndDate().format(DateTimeFormatter.ofPattern("MMM d")));
        dateLabel.getStyleClass().add("upcoming-goal-date");

        // progress bar
        ProgressBar progressBar = new ProgressBar(progress);
        progressBar.setPrefWidth(200);
        progressBar.getStyleClass().add("upcoming-goals-progress");

        // days remaining
        String daysText = daysRemaining == 1 ? "day" : "days";
        Label daysLabel = new Label(String.format("%d %s left", daysRemaining, daysText));
        daysLabel.getStyleClass().add("upcoming-goals-days-label");

        // difficulty
        Label difficultyLabel = new Label("Difficulty: " + goal.getDifficulty());
        difficultyLabel.getStyleClass().add("upcoming-goal-difficulty");

        goalCard.getChildren().addAll(
                titleLabel,
                dateLabel,
                progressBar,
                daysLabel,
                difficultyLabel
        );

        return goalCard;
    }

    private void updateUpcomingGoals() {
        if (goalModel == null) {
            return;
        }

        VBox scrollContent = new VBox();
        scrollContent.setPadding(new Insets(5));

        upcomingGoalsScroll = new ScrollPane();
        upcomingGoalsScroll.setContent(scrollContent);
        upcomingGoalsScroll.setFitToHeight(true);
        upcomingGoalsScroll.setFitToWidth(false);
        upcomingGoalsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        upcomingGoalsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        upcomingGoalsScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // set fixed height to show exactly 4 goals
        upcomingGoalsScroll.setPrefHeight(220);

        // get goals due in the next 7 days
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<Goal> upcomingGoals = goalModel.getGoals().stream()
                .filter(goal -> !goal.isCompleted())
                .filter(goal ->
                        !goal.getEndDate().isBefore(today) &&
                                !goal.getEndDate().isAfter(nextWeek)
                )
                .sorted(Comparator.comparing(Goal::getEndDate))
                .toList();

        if (upcomingGoals.isEmpty()) {
            Label noGoalsLabel = new Label("No goals due in the next 7 days!");
            noGoalsLabel.getStyleClass().add("upcoming-goals-label");
            noGoalsLabel.setStyle("-fx-font-size: 16");
            scrollContent.getChildren().add(noGoalsLabel);
        } else {
            HBox goalsRow = new HBox(15);
            goalsRow.setPadding(new Insets(10));
            goalsRow.setAlignment(Pos.TOP_LEFT);
            goalsRow.setFillHeight(true);

            for (Goal goal : upcomingGoals) {
                VBox goalCard = createGoalCard(goal, today);
                goalsRow.getChildren().add(goalCard);
            }

            scrollContent.getChildren().add(goalsRow);

            int maxVisibleGoals = 4;
            if (upcomingGoals.size() > maxVisibleGoals) {
                int remaining = upcomingGoals.size() - maxVisibleGoals;
                String moreText = remaining == 1 ? "1 more goal" : remaining + " more goals";
                Label moreLabel = new Label(moreText);
                moreLabel.getStyleClass().add("upcoming-goals-more-label");
                scrollContent.getChildren().add(moreLabel);
            }
        }

        upcomingGoalsModule.getChildren().clear();
        upcomingGoalsModule.getChildren().add(upcomingGoalsScroll);
    }
}
