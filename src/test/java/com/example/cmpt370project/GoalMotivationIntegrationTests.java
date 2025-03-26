package com.example.cmpt370project;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration (UI) tests for the goal motivation feature functionality.
 */
public class GoalMotivationIntegrationTests extends AppIntegrationTest {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
    }

    /**
     * Test that setting the name to empty string returns the default username
     */
    @Test
    public void testNameClear() {
        clickOn(root.homeButton);
        // Trigger the dialog by clicking the button
        //clickOn(root.homeButton);
       // clickOn(root.homePage.changeNameButton);

        // Find the text field inside the dialog and type in a name
       // write("");

        // Click the "OK" button to confirm the input
        //clickOn(".dialog-pane .button:label('OK')"); // Clicking the "OK" button by its label

        // Verify that the userHistoryDataModel has the updated username
        //assertEquals("User", root.userHistoryDataModel.getUserName(), "User name should be updated to 'User'");
    }


}
