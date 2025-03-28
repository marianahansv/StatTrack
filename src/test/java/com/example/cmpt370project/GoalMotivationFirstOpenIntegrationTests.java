package com.example.cmpt370project;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration (UI) tests for the goal motivation feature functionality, where the app is opened for the first time.
 * (i.e. tests for Persona 5, User Story 2)
 */
public class GoalMotivationFirstOpenIntegrationTests extends ApplicationTest {

    protected DashboardView root;
    static Path originalJsonPath = Paths.get("C:\\Users\\katew\\GoalApplication\\userHistoryData.json");
    static Path backupJsonPath = Paths.get("C:\\Users\\katew\\GoalApplication\\userHistoryDataB.json");

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Make a copy of the userHistoryData, because want to reset it to run these tests

        // Create a backup of the original file if it exists
        if (Files.exists(originalJsonPath)) {
            Files.copy(originalJsonPath, backupJsonPath, StandardCopyOption.REPLACE_EXISTING);
        }

        Files.write(originalJsonPath, new byte[] {});

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
     * Test 1: Set New Username to Receive Motivational Greetings (valid data)
     */
    @Test
    public void P5_US2_TC01() {
        clickOn(".text-input");

        // Find the text field inside the dialog and type in a name
        write("HasAPlanFran");

        // Click the "OK" button to confirm the input
        clickOn(".dialog-pane .button:label('OK')");

        // Verify that the userHistoryDataModel has the updated username
        assertEquals("HasAPlanFran", root.userHistoryDataModel.getUserName(), "User name should be set to 'HasAPlanFran'");

    }

    /**
     * Test 2: Set Username to Receive Motivational Greetings (choosing not to set username)
     */
    @Test
    public void P5_US2_TC02() {
        clickOn(".text-input");

        // Find the text field inside the dialog and type in a name
        write("");

        // Click the "OK" button to confirm the input
        clickOn(".dialog-pane .button:label('OK')");

        // Verify that the userHistoryDataModel has the updated username
        assertEquals("User", root.userHistoryDataModel.getUserName(), "User name should be set to 'User'");

    }

    @AfterAll
    public static void cleanup() throws IOException {
        // After all the tests, restore the original JSON data from the backup.
        if (Files.exists(backupJsonPath)) {
            Files.copy(backupJsonPath, originalJsonPath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Clean up the backup file if necessary
        Files.deleteIfExists(backupJsonPath);
    }
}
