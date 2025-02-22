package com.example.cmpt370project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The Main Application class that runs the app. (i.e. run the code from this class)
 */
public class GoalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // Instantiate the MainUI (see MainUI class, which sets up MVC)
        MainUI root = new MainUI();

        // Pass to the scene of the application
        // (JavaFX application is composed of a Stage with a Scene, which holds all your UI elements)
        // 400 x 400 is the default size of the window.
        Scene scene = new Scene(root, 1000, 600);

        // Set the title of the window when it comes up
        stage.setTitle("Goal Tracker");

        // Set the scene to the stage and show it!
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}