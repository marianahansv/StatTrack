package com.example.cmpt370project;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The Main Application class that runs the app. (i.e. run the code from this class)
 */
public class GoalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //Icon
        Image appIcon = new Image((Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
        stage.getIcons().add(appIcon);

        // Instantiate the main UI (DashboardView class, which sets up MVC and basic UI)
        DashboardView root = new DashboardView();

        // Get the screen bounds of the primary screen using JavaFX Screen class
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // Pass to the scene of the application
        Scene scene = new Scene(root, screenBounds.getWidth() - 100, screenBounds.getHeight() - 100);

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