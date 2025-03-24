package com.example.cmpt370project;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoalMotivationIntegrationTests extends ApplicationTest {

    private DashboardView root;

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

    /**
     * Test that setting the name to empty string returns the default username
     */
    @Test
    public void testNameClear() {
        // Trigger the dialog by clicking the button
        clickOn(root.homePage.changeNameButton);

        // Find the text field inside the dialog and type in a name
        write("");

        // Click the "OK" button to confirm the input
        clickOn(".dialog-pane .button:label('OK')"); // Clicking the "OK" button by its label

        // Verify that the userHistoryDataModel has the updated user name
        assertEquals("User", root.userHistoryDataModel.getUserName(), "User name should be updated to 'User'");
    }


}
