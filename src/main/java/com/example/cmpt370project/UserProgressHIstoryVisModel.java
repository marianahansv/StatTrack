package com.example.cmpt370project;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The model class for the "Goal History" page.
 */
public class UserProgressHIstoryVisModel extends GoalModel{
    /**
     * Instance variable to store all the subscribers.
     */
    private List<Subscriber> subscribers;

    /**
     * Instance variable to store the model where the json file of the user's historical data is located.
     */
    public UserHistoryDataModel dataHistoricalModel;

    /**
     * The model for the user progress historical data that stores the logic required for the charts to utilize.
     * @param UH: User history data model is used to access its
     */
    public UserProgressHIstoryVisModel(UserHistoryDataModel UH){
        super(UH);
        subscribers = new ArrayList<>();
    }

    /**
     * Helps to add a list of subscribers to our list
     * @param subscriber: The subscriber
     */
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
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

    /**
     * The method to obtain all the easy goals inside a list.
     * @return: A list of easy goals.
     */
    public List<Goal> easyGoals(){
        return getGoalsByDifficulty("Easy");
    }

    /**
     * The method to obtain all the medium goals inside a list.
     * @return: A list of medium goals.
     */
    public List<Goal> mediumGoals(){
        return getGoalsByDifficulty("Medium");
    }

    /**
     * The method to obtain all the hard goals inside a list.
     * @return: A list of hard goals.
     */
    public List<Goal> hardGoals(){
        return getGoalsByDifficulty("Hard");
    }

    /**
     * The method to obtain all the personal goals inside a list.
     * @return: A list of personal goals.
     */
    public List<Goal> personalGoals(){return getGoalsForSection("Personal");}

    /**
     * The method to obtain all the fitness goals inside a list.
     * @return: A list of fitness goals.
     */
    public List<Goal> fitnessGoals(){return getGoalsForSection("Fitness");}

    /**
     * The method to obtain all the general goals inside a list.
     * @return: A list of general goals.
     */
    public List<Goal> generalGoals(){return getGoalsForSection("General");}

    /**
     * A helper method to associate every month with two-digit numbers.
     * @return: A String of two-digit numbers to represent the month.
     */
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

    /**
     * A helper method to associate every day with two-digit numbers.
     * @return: A String of two-digit numbers to represent the day.
     */
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

}
