package com.example.cmpt370project;

import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.util.InputMismatchException;

/**
 * Handles changing model according to the users actions. Only calls model using its public API!
 * (i.e. call Model methods INSTEAD OF ITS ATTRIBUTES DIRECTLY as will likely miss notifying subscribers when needed.)
 */
public class HomeController {
    private GoalModel gm;

    // Could consider adding states for a state machine if that comes up later, depending on what our application requires...
    // (i.e. if there are different interaction states for a single view...)

    public HomeController() {}

    public void setModel(GoalModel goalModel) {
        this.gm = goalModel;
    }

    /**
     * Handles making the model change when the user adds a goal using the button.
     * @param actionEvent the event that happens when the button is pressed.
     */
    public void handleButtonPress(ActionEvent actionEvent, String title, String section, String difficulty, LocalDate start, LocalDate end, boolean completed) throws InputMismatchException {
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
        try {
            gm.addGoal(title, section, difficulty, start, end, completed);
        } catch (InputMismatchException e){
            throw new InputMismatchException(e.getMessage());
        }

    }


    /**
     * Handles making the model change when the user clears their goals using the button.
     * @param actionEvent the event that happens when the button is pressed.
     */
    public void removeButtonPress(ActionEvent actionEvent) {
        gm.clearGoals();
    }


}
