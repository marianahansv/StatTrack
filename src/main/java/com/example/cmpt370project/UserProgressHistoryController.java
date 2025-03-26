package com.example.cmpt370project;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The Controller class for managing the user's historical data. The controller class is responsible for storing the
 * setters - such as setting the model and the view of the Model-View-Controller implementation of our application. It
 * also focuses on updating the views for all the line graph, scatter charts, pie charts and the descriptive statistics.
 * It also helps to ensure that the model and the view are not communicating directly.
 */
public class UserProgressHistoryController {
    /**
     * The main view box creation page for the historical goal data for the user.
     */
    private UserProgressHistoryVisView historicalChartView;

    /**
     * The view page for the historical goal data for the user.
     */
    private UserHIstoryProgressVisuals historicalChartProgress;

    /**
     * The model for the historical goal data for the user.
     */
    private UserProgressHIstoryVisModel historicalChartModel;

    /**
     * The constructor for the controller which is aware of both the model and the view page.
     * @param historicalChartModel: The model page for the "Goal History" page.
     */
    public UserProgressHistoryController(UserProgressHIstoryVisModel historicalChartModel) {
        this.historicalChartModel = historicalChartModel;
    }

    /**
     * Allows to set the view class that is needed to go with this particular controller.
     * @param historicalChartProgress: The view page for the "Goal History" page.
     */
    public void setupViewClass (UserHIstoryProgressVisuals historicalChartProgress){
        this.historicalChartProgress = historicalChartProgress;
    }

    /**
     * Allows to set the model class that is needed to go with this particular controller.
     * @param historicalChartModel: The model for the "Goal History" page.
     */
    public void setGoalPlanModel(UserProgressHIstoryVisModel historicalChartModel){
        this.historicalChartModel = historicalChartModel;
    }

    /**
     * Gets the start date based on the user's start dates input.
     * @return: A string that will return the date or 0 if the date selected was empty.
     */
    public String getStartDate(){
        return (historicalChartProgress.leftgrid_dates.get()!= null) ?
                                                        historicalChartProgress.leftgrid_dates.get().getText() : "0";
    }

    /**
     * Gets the start month based on the user's start month input.
     * @return: A string that will return the month selected from the dropdown menu.
     */
    public String getStartMonth(){
        return (historicalChartProgress.leftmonth_grid_selector.getValue());
    }

    /**
     * Gets the start year based on the user's start year input.
     * @return: A string that will return the year selected from the dropdown menu.
     */
    public String getStartYear(){
        return (historicalChartProgress.yearSelector_left.getValue());
    }

    /**
     * Gets the start date based on the user's start dates input.
     * @return: A string that will return the date or 0 if the date selected was empty.
     */
    public String getEndDate(){
        return (historicalChartProgress.rightgrid_dates.get() != null) ?
                                                        historicalChartProgress.rightgrid_dates.get().getText() : "0";
    }

    /**
     * Gets the end month based on the user's start month input.
     * @return: A string that will return the month selected from the dropdown menu.
     */
    public String getEndMonth(){
        return (historicalChartProgress.rightmonth_grid_selector.getValue());
    }

    /**
     * Gets the end year based on the user's start month input.
     * @return: A string that will return the month selected from the dropdown menu.
     */
    public String getEndYear(){
        return (historicalChartProgress.yearSelector_right.getValue());
    }

    /**
     * Helper function to put the desired date in a specific format yyyy-MM-dd to perform checks and fed it as an input.
     * @param year: The desired year.
     * @param month: The desired month.
     * @param day: The desired day.
     * @return: A specifically formatted yyyy-MM-dd for the specific date that was provided by the user.
     */
    public LocalDate dateFormatting(String year, String month, String day) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (Integer.parseInt(day) > 0 && Integer.parseInt(day) <= 9) {
            return LocalDate.parse(year + "-" + historicalChartModel.numericalMonth(month) + "-" +
                    historicalChartModel.numericalDay(day), format);
        }
        return LocalDate.parse(year + "-" + historicalChartModel.numericalMonth(month) + "-" + day, format);
    }

    /**
     * Getter function to obtain the formatted start date that can be used in the view class.
     * @return: LocalDate type with a specific format of yyyy-MM-dd.
     */
    public LocalDate getstartFormatDate(){
        return dateFormatting(getStartYear(), getStartMonth(), getStartDate());
    }

    /**
     * Getter function to obtain the formatted end date that can be used in the view class.
     * @return: LocalDate type with a specific format of yyyy-MM-dd.
     */
    public LocalDate getendFormatDate(){
        return dateFormatting(getEndYear(), getEndMonth(), getEndDate());
    }

    /**
     * Getter function to filter out all the easy goals from the given start date to the given end date.
     * @return: A list of easy goals.
     */
    public List<Goal> getEasyGoals(){
        return historicalChartModel.filteredGoalList(historicalChartModel.easyGoals(), getstartFormatDate(),
                                                                                                    getendFormatDate());
    }

    /**
     * Getter function to filter out all the medium goals from the given start date to the given end date.
     * @return: A list of medium goals.
     */
    public List<Goal> getMediumGoals(){
        return historicalChartModel.filteredGoalList(historicalChartModel.mediumGoals(), getstartFormatDate(),
                                                                                                    getendFormatDate());
    }

    /**
     * Getter function to filter out all the hard goals from the given start date to the given end date.
     * @return: A list of hard goals.
     */
    public List<Goal> getHardGoals(){
        return historicalChartModel.filteredGoalList(historicalChartModel.hardGoals(), getstartFormatDate(),
                                                                                                    getendFormatDate());
    }

    /**
     * Getter function to filter out the goals from the given start date to the given end date.
     * @return: A list of goals.
     */
    public List<Goal> getfilteredGoals(){
        return historicalChartModel.filteredGoalList(historicalChartModel.getGoals(), getstartFormatDate(),
                                                                                                    getendFormatDate());
    }

    /**
     * Getter function to filter out all the personal goals from the given start date to the given end date.
     * @return: A list of personal goals.
     */
    public List<Goal> getPersonalGoals(){
        return historicalChartModel.filteredGoalList(historicalChartModel.personalGoals(), getstartFormatDate(),
                                                                                                    getendFormatDate());
    }

    /**
     * Getter function to filter out all the fitness goals from the given start date to the given end date.
     * @return: A list of fitness goals.
     */
    public List<Goal> getFitnessGoals(){
        return historicalChartModel.filteredGoalList(historicalChartModel.fitnessGoals(), getstartFormatDate(),
                getendFormatDate());
    }

    /**
     * Getter function to filter out all the general goals from the given start date to the given end date.
     * @return: A list of general goals.
     */
    public List<Goal> getGeneralGoals(){
        return historicalChartModel.filteredGoalList(historicalChartModel.generalGoals(), getstartFormatDate(),
                getendFormatDate());
    }

    /**
     * Getter function to obtain the total number of goals that the user currently has.
     * @return: A number representing the goals.
     */
    public int totalGoalCount(){
        return historicalChartModel.getGoalCount();
    }

    /**
     * Getter function to obtain all the goals without any filters.
     * @return: A list of all unfiltered goals.
     */
    public List<Goal> getUserGoals(){
        List<Goal> allGoals = new ArrayList<Goal>();
        List<Goal> easyGoal = getEasyGoals();
        List<Goal> mediumGoal = getMediumGoals();
        List<Goal> hardGoal = getHardGoals();

        allGoals.addAll(easyGoal);
        allGoals.addAll(mediumGoal);
        allGoals.addAll(hardGoal);

        System.out.println(allGoals);
        System.out.println(allGoals.isEmpty());
        return allGoals;
    }

    /**
     * Update function for the pie chart to ensure that it is constantly being updated whenever the data updates.
     */
    void updatePieCharts(){}

    /**
     * Update function for the line chart to ensure that it is constantly being updated whenever the data updates.
     */
    void updateLineCharts(){}

    /**
     * Update function for the scatter chart to ensure that it is constantly being updated whenever the data updates.
     */
    void updateScatterCharts(){}

    /**
     * Update function for the descriptive statistics to ensure that it is constantly being updated whenever the
     * data updates.
     */
    void updateDescriptiveStatistics(){}
}
