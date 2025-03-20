package com.example.cmpt370project;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UserProgressHIstoryVisModel extends GoalModel{
    private List<Subscriber> subscribers;
    public UserHistoryDataModel dataHistoricalModel;

    /**
     * The model for the user progress historical data that stores the logic required
     * for the charts to utilize.
     * @param UH: User history data model is used to access its
     */
    public UserProgressHIstoryVisModel(UserHistoryDataModel UH){
        super(UH);
        subscribers = new ArrayList<>();
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Helps to take the goals from the json file directly with the function from GoalModel.
     * @return: A list of goals that the user has inputted into the system.
     */
    public List<Goal> getGoals(){
        return new ArrayList<>(load_goals_from_file_hashmap().values());
    }

    public int goalCount(){
        return getGoalCount();
    }

    /**
     * The method is used to filter out all the goals based on the starting and ending dates that the user picks. It can
     * be used by all the graphs - Pie Chart, Line Graphs and Scatter Charts.
     * @param goals: The list of goals in the user's computer
     * @param startDate: The starting date of the goals we want to assess
     * @param endDate: The ending date of the goals we want to assess
     * @return: A list of goals that belong to a specific timeframe given the start date and the end date by the user.
     */
    public List<Goal> filteredGoalList(List<Goal> goals, LocalDate startDate, LocalDate endDate){
        return goals.stream().filter(goal -> !goal.getStartDate().isBefore(startDate) &&
                !goal.getEndDate().isAfter(endDate)).collect(Collectors.toList());
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

    public List<Goal> personalGoals(){return getGoalsForSection("Personal");}

    public List<Goal> fitnessGoals(){return getGoalsForSection("Fitness");}

    public List<Goal> generalGoals(){return getGoalsForSection("General");}

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
