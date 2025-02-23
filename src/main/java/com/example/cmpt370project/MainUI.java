package com.example.cmpt370project;

import javafx.scene.layout.StackPane;

/**
 * This class sets up the Model-View-Controller structure.
 */
public class MainUI extends StackPane {

    public MainUI() {

        // Create MVC components
        GoalModel m = new GoalModel();
        Controller c = new Controller();
        DashboardView v = new DashboardView();

        // Connect MVC Components

        // MODEL SETUP
        // Add the view as a subscriber to the model (when project gets bigger, can add multiple views as subscribers)
        m.addSubscriber(v);

        // CONTROLLER SETUP
        // Give the controller access to the model
        c.setModel(m);

        // VIEW SETUP
        // Give the view access to the model, and temporary access to the controller so it can set up the event handling
        // (i.e. for event handling, the view essentially hands over the events when they happen to the controller)
        v.setModel(m);
        v.setupEvents(c);
        // Add view to this MainUI StackPane so it can be shown in the window (it is the UI after all...)
        this.getChildren().addAll(v);
    }
}
