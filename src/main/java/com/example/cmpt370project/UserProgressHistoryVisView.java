package com.example.cmpt370project;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The class focuses on creating a historical view of the goals for the users. It allows them to have a visualization
 * view with a given start date and end date.
 */
public class UserProgressHistoryVisView extends StackPane implements Subscriber{
    /**
     * The model where the data is being retrieved from.
     */
    private GoalModel goalModel;

    /**
     * The view where the pie charts, line charts, scatter plots visualization.
     */
    private UserHIstoryProgressVisuals historicalChartProgress;

    /**
     * Create a historical visualization representation page
     */
    public UserProgressHistoryVisView(GoalModel goalModel){

    }

    /**
     * This will update the view everytime the model is updated.
     */
    @Override
    public void modelUpdated() {}

}
