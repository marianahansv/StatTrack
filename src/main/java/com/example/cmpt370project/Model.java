package com.example.cmpt370project;
import java.util.ArrayList;
import java.util.List;

/**
 * Model in the MVC Structure, holds all the DATA OF THE APPLICATION and PROVIDES METHODS TO OPERATE ON THIS DATA.
 * We may consider having multiple models to separate areas of data in our application (i.e. Goal Model, Plan Model, etc.)
 *
 */

// TODO: At some point in our application, we need to look at how we will store this data to JSON before app close or
//  perhaps when user makes changes. Likely coordinating this between Model and Controller classes.

public class Model {

    /**
     * The subscriber list (i.e. the view), which will update when the view changes.
     */
    private List<Subscriber> subscribers;

    /**
     * For this example, a list of goals that the user has.
     */
    private List<String> goals;

    /**
     * Initialize attributes of the model.
     */
    public Model() {
        subscribers = new ArrayList<Subscriber>();
        goals = new ArrayList<>();
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Adds a goal to the list of goals.
     * @param goal goal to be added.
     */
    public void addGoal(String goal) {
        goals.add(goal);
        notifySubscribers(); // WHENEVER CHANGE THE DATA IN THE MODEL, NOTIFY THE SUBSCRIBERS!!
    }

    /**
     * Empties the goal list.
     */
    public void clearGoals() {
        goals.clear();
        notifySubscribers(); // WHENEVER CHANGE THE DATA IN THE MODEL, NOTIFY THE SUBSCRIBERS!!
    }

    public List<String> getGoals() {
        return goals;
    }

    /**
     * Return the number of goals in this list.
     * @return number of goals in the list.
     */
    public int getGoalCount() {
        return goals.size();
    }

    /**
     * Notify th subscribers of this model that the data has changed.
     */
    public void notifySubscribers() {
        subscribers.forEach(subscriber -> subscriber.modelUpdated());
    }
}
