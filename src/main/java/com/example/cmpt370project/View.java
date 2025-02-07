package com.example.cmpt370project;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Handles displaying the UI of the system (we can make a different view class for different pages).
 * Different views for different pages allows management of which views to show based on the current state of the application.
 */
public class View extends StackPane implements Subscriber {

    private Model m;

    /**
     * The "Add Goal" Button.
     */
    private Button addGoalButton;

    /**
     * The "Clear Goals" Button.
     */
    private Button removeGoalButton;

    /**
     * Label to display goals.
     */
    private Label l;

    // When importing classes, MAKE SURE IT IS FROM FX, NOT JAVA AWT LIBRARY!

    /**
     * Initially constructs the UI (prior to any model changes).
     */
    public View() {

        // We use a VBox as a container to put the elements in a vertical structure
        VBox root = new VBox();

        // These set how the elements are aligned and spaced in the vertical container.
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        // Make the UI components
        addGoalButton = new Button("Add Goal");
        removeGoalButton = new Button("Clear Goals");
        l = new Label("My Goals: ");

        // Add the UI components to the VBox, and add the VBox to this view.
        root.getChildren().addAll(l, addGoalButton, removeGoalButton);
        this.getChildren().add(root);
    }

    public void setModel(Model m) {
        this.m = m;
    }

    /**
     * Set up the "passing of the events" from the UI elements to the controller.
     * @param c the controller.
     */
    public void setupEvents(Controller c) {
        // For each of these, there is a method in the controller that is executed for each button.
        // i.e. we pass the action event of the button to a method in the controller to be dealt with there.
        addGoalButton.setOnAction(c::handleButtonPress);
        removeGoalButton.setOnAction(c::removeButtonPress);
    }

    /**
     * What happens when the model is updated? This view updates itself.
     */
    @Override
    public void modelUpdated() {
        update();
    }

    /**
     * Gets called when the subscribers are notified (i.e. when the model changes).
     * How the view is to be updated/redrawn on any model change.
     */
    public void update() {
        // Update the label to show the new goal list
        l.setText("My Goals: " + m.getGoals());
    }
}
