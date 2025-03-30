package com.example.cmpt370project;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The class focuses on creating a historical view of the goals for the users. It allows them to have a visualization
 * view with a given start date and end date. Main code for the view for the historical data is inside the
 * UserProgressHistoryController.java
 */
public class UserProgressHistoryVisView extends StackPane implements Subscriber {
    /**
     * The view where the selection buttons for the charts and the descriptive statistics are placed.
     */
    private UserHIstoryProgressVisuals historicalChartProgress;

    /**
     * Create a historical visualization representation page where all the elements are then added by the
     * UserHistoryProgressVisuals.java
     */
    public UserProgressHistoryVisView(UserProgressHistoryController historicalChartController) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(25);
        root.setPadding(new Insets(20));
        Label enterlabel = new Label("Let's Visualize Your Goal History: ");
        enterlabel.setStyle("-fx-font-weight: bold; -fx-font-size: 28px");

        // Space down the calendar a little more
        Region spacer = new Region();

        root.getChildren().add(enterlabel);
        root.setAlignment(Pos.TOP_LEFT);
        historicalChartProgress = new UserHIstoryProgressVisuals(historicalChartController);
        root.getChildren().addAll(spacer, historicalChartProgress);
        this.getChildren().add(root);
    }

    @Override
    public void modelUpdated() {
    }
}