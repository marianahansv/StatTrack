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
    private UserProgressHIstoryVisModel historicalChartModel;

    /**
     * The view where the selection buttons for the charts and the descriptive statistics are placed.
     */
    private UserHIstoryProgressVisuals historicalChartProgress;

    /**
     * The controller that stores some functions that both the view and the model will require.
     */
    private UserProgressHistoryController historicalChartController;

    /**
     * Create a historical visualization representation page
     */
    public UserProgressHistoryVisView(UserProgressHIstoryVisModel historicalChartModel, UserProgressHistoryController historicalChartController) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        Label enterlabel = new Label("Visualizing YOUR historical footprint!");
        enterlabel.setStyle("-fx-font-weight: bold; -fx-font-size: 24px");

        root.getChildren().add(enterlabel);
        root.setAlignment(Pos.TOP_LEFT);
        historicalChartProgress = new UserHIstoryProgressVisuals(historicalChartModel, historicalChartController);
        root.getChildren().add(historicalChartProgress);
        this.getChildren().add(root);
    }

    /**
     * Helps to set the accurate model that goes with this view. In this case, it would be
     * UserProgressHistoryVisModel
     * @param historicalChartModel: The model that contains the data for this view
     */
    public void setGoalPlanModel(UserProgressHIstoryVisModel historicalChartModel){
        this.historicalChartModel = historicalChartModel;
    }

    public void setHistoricalChartController(UserProgressHistoryController historicalChartController){
        this.historicalChartController = historicalChartController;
    }
    /**
     * Helps to update the view whenever there are any changes made to the model
     */
    private void drawView(){
        historicalChartProgress.updatePieCharts();
        historicalChartProgress.updateScatterCharts();
        historicalChartProgress.updateLineCharts();
    }

    /**
     * This will update the view everytime the model is updated.
     */
    @Override
    public void modelUpdated() {}

}
