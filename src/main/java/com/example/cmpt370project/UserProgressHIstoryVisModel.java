package com.example.cmpt370project;
import java.time.LocalDate;
import java.util.*;

public class UserProgressHIstoryVisModel extends GoalModel{
    private HashMap<String,Goal> goals;
    private List<Subscriber> subscribers;
    private String userName;
    private Integer goalsCompletedforDay;
    public UserHistoryDataModel dataHistoricalModel;

    public UserProgressHIstoryVisModel(){
        goals = new HashMap<>();
        subscribers = new ArrayList<>();
        userName = null; //default
        goalsCompletedforDay = 0; //default
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public List<Goal> getUserGoals(){
        return getGoals();
    }

    public List<Goal> easyGoals(){
        return getGoalsByDifficulty("Easy");
    }

    public List<Goal> mediumGoals(){
        return getGoalsByDifficulty("Medium");
    }

    public List<Goal> hardGoals(){
        return getGoalsByDifficulty("Hard");
    }

    public String numericalMonth(String monthName){
        switch(monthName){
            case "January": return "01";
            case "February": return "02";
            case "March": return "03";
            case "April": return "04";
            case "May": return "05";
            case "June": return "06";
            case "July": return "07";
            case "August": return "08";
            case "September": return "09";
            case "October": return "10";
            case "November": return "11";
            case "December": return "12";
            default: return null;
        }
    }
    public String numericalDay(String dayName){
        switch(dayName){
            case "1": return "01";
            case "2": return "02";
            case "3": return "03";
            case "4": return "04";
            case "5": return "05";
            case "6": return "06";
            case "7": return "07";
            case "8": return "08";
            case "9": return "09";
            default: return null;
        }
    }

    /* Returns the number of goals completed for that day specifically */
    public int completedGoals(){
        return dataHistoricalModel.getDailyCompletedGoals();
    }

}
