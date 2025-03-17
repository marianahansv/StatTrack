package com.example.cmpt370project;

import java.time.LocalDate;

public class UserProgressHistoryController {
    private UserProgressHistoryVisView historicalChartView;

    private UserHIstoryProgressVisuals historicalChartProgress;

    private UserProgressHIstoryVisModel historicalChartModel;

    public UserProgressHistoryController(UserHIstoryProgressVisuals historicalChartProgress, UserProgressHIstoryVisModel historicalChartModel) {
        this.historicalChartProgress = historicalChartProgress;
        this.historicalChartModel = historicalChartModel;
    }

    public void setupViewClass (UserHIstoryProgressVisuals historicalChartProgress){
        this.historicalChartProgress = historicalChartProgress;
    }

    public String getStartDate(){
        return (historicalChartProgress.leftgrid_dates.get()!= null) ? historicalChartProgress.leftgrid_dates.get().getText() : "0";
    }

    public String getStartMonth(){
        return (historicalChartProgress.leftmonth_grid_selector.getValue());
    }

    public String getStartYear(){
        return (historicalChartProgress.yearSelector_left.getValue());
    }

    public String getEndDate(){
        return (historicalChartProgress.rightgrid_dates.get() != null) ? historicalChartProgress.rightgrid_dates.get().getText() : "0";
    }

    public String getEndMonth(){
        return (historicalChartProgress.rightmonth_grid_selector.getValue());
    }

    public String getEndYear(){
        return (historicalChartProgress.yearSelector_right.getValue());
    }

}
