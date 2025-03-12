package com.example.cmpt370project;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserProgressHIstoryVisModel {
    private HashMap<String,Goal> goals;
    private List<Subscriber> subscribers;
    private String userName;
    private Integer goalsCompletedforDay;

    public UserProgressHIstoryVisModel(){
        goals = new HashMap<>();
        subscribers = new ArrayList<>();
        userName = null; //default
        goalsCompletedforDay = 0; //default
    }

}
