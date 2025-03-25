package com.example.cmpt370project;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Objects;

/**
 * Parent class for all integration tests; sets up the application for running UI tests with TestFX.
 */
public class AppIntegrationTest extends ApplicationTest {

    protected DashboardView root;

    @Override
    public void start(Stage stage) throws Exception {
        //Icon
        Image appIcon = new Image((Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
        stage.getIcons().add(appIcon);

        // Instantiate the main UI (DashboardView class, which sets up MVC and basic UI)
        root = new DashboardView();

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
}
