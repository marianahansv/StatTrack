package com.example.cmpt370project;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Optional;

/**
 * Handles changing model according to the users actions. Only calls model using its public API!
 * (i.e. call Model methods INSTEAD OF ITS ATTRIBUTES DIRECTLY as will likely miss notifying subscribers when needed.)
 */
public class HomeController {
    private GoalModel gm;
    private UserHistoryDataModel userHistoryDataModel;

    // Could consider adding states for a state machine if that comes up later, depending on what our application requires...
    // (i.e. if there are different interaction states for a single view...)

    public HomeController() {}

    public void setModel(GoalModel goalModel, UserHistoryDataModel userHistoryDataModel) {
        this.gm = goalModel;
        this.userHistoryDataModel = userHistoryDataModel;
    }

    /**
     * Handles making the model change when the user adds a goal using the button.
     * @param actionEvent the event that happens when the button is pressed.
     */
    public void handleButtonPressValidateInput(ActionEvent actionEvent, String title, String section, String difficulty, LocalDate start, LocalDate end, boolean completed) throws InputMismatchException {
        if (title.isBlank()){
            throw new InputMismatchException("Title cannot be empty.");
        }
        if (title.length() > 40){
            throw new InputMismatchException("Title cannot be more than 40 characters.");
        }
        //section has pre-set value
        //difficulty has pre-set value
        if(end.isBefore(start)){
            throw new InputMismatchException("End date cannot be before start date.");
        }
    }

    public void handleGoalSubmissionButton(String title, String section, String difficulty, LocalDate start, LocalDate end, boolean completed){
        try {
            gm.addGoal(title,section,difficulty,start,end,completed);
        } catch (InputMismatchException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles making the model change when the user clears their goals using the button.
     * @param actionEvent the event that happens when the button is pressed.
     */
    public void removeButtonPress(ActionEvent actionEvent) {

        // Create an alert of type CONFIRMATION
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Clear Goals");
        alert.setHeaderText("Are you sure you want to clear all your goals?");
        alert.setContentText("This action cannot be undone.");

        // Show the dialog and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                gm.clearGoals();
            }
        });


    }

    public void handleChangeName(ActionEvent actionEvent) {
        // Create a TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change your name.");
        dialog.setHeaderText("Please enter your new user name:");
        dialog.setContentText("Name:");

        // Show the dialog and capture the input
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isBlank()) {
            userHistoryDataModel.setUserName(result.get().trim());
        } else if (result.get().isBlank()) {
            userHistoryDataModel.setUserName("User");
        }
    }

}
