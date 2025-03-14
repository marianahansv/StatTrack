package com.example.cmpt370project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The Goal class represents an individual goal with various attributes to track its details and progress.
 * This class provides methods for creating, reading, and updating goals.
 */
public class Goal {
    private String title;
    private String section = "General"; //default section
    private String difficulty = "Medium"; //default difficulty
    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();
    private boolean completed = false; // defaults to false as why would you make a goal if it is completed already

    // Constructors to initialize a new goal
    /**
     * Constructs a new Goal object with the specified attributes.
     * @param title The title of the goal
     * @param startDate The date when the goal starts
     * @param endDate The target date to complete the goal
     */
    public Goal(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    /**
     * Constructs a new Goal object with the specified attributes.
     * @param title The title of the goal
     * @param section The section this goal belongs to (e.g., "work", "personal")
     * @param difficulty The difficulty of the goal (e.g., "easy", "hard")
     * @param startDate The date when the goal starts
     * @param endDate The target date to complete the goal
     */
    public Goal(String title, String section, String difficulty, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.section = section;
        this.difficulty = difficulty;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and setters :)

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

//    /**
//     * Returns a string representation of the Goal
//     */
//    @Override
//    public String toString() {
//        return "Goal{" +
//                "title='" + title + '\'' +
//                ", section='" + section + '\'' +
//                ", difficulty='" + difficulty + '\'' +
//                ", startDate=" + startDate +
//                ", endDate=" + endDate +
//                '}';
//    }

    /**
     * Returns a string representation of the Goal
     */
    @Override
    public String toString() {
        return "Title: " + title + " | " +
                "Section: " + section + " | " +
                "Difficulty: " + difficulty + " | " +
                "Start Date: " + startDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")) + " | " +
                "End Date: " + endDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"))+ " | " +
                "Completed: " + isCompleted();
    }

    // Would love to use this string representation, but means we need to fix the sections UI.
//    /**
//     * Returns a string representation of the Goal
//     */
//    @Override
//    public String toString() {
//        return "Title: " + title + "\n" +
//                "Section: " + section + "\n" +
//                "Difficulty: " + difficulty + "\n" +
//                "Start Date: " + startDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")) + "\n" +
//                "End Date: " + endDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"));
//    }

    /**
     * Unit Testing (basically for understanding how this works but it's pretty simple tbt to 270 :) )
     */
    public static void main(String[] args) {
        // Test Goal creation and attributes
        Goal goal1 = new Goal(
                "Read 3 books: X, Y, and Z.",
                "Personal",
                "Medium",
                LocalDate.of(2025, 2, 1),
                LocalDate.of(2025, 3, 1)
        );

        // Display goal information
        System.out.println("Goal 1 Details:");
        System.out.println("Title: " + goal1.getTitle());
        System.out.println("Section: " + goal1.getSection());
        System.out.println("Difficulty: " + goal1.getDifficulty());
        System.out.println("Start Date: " + goal1.getStartDate());
        System.out.println("End Date: " + goal1.getEndDate());

        // Test updating section
        goal1.setSection("Learning");
        System.out.println("\nAfter Section Update:");
        System.out.println("Section: " + goal1.getSection());

        // Test goal's toString method
        System.out.println("\nGoal 1 toString():");
        System.out.println(goal1.toString());

        // Test Goal with different attributes (different constructor)
        Goal goal2 = new Goal(
                "Run a Marathon",
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 11, 1)
        );

        // Display goal information (with default values)
        System.out.println("\nGoal 2 Details:");
        System.out.println("Title: " + goal2.getTitle());
        System.out.println("Section: " + goal2.getSection());
        System.out.println("Difficulty: " + goal2.getDifficulty());
        System.out.println("Start Date: " + goal2.getStartDate());
        System.out.println("End Date: " + goal2.getEndDate());


        // Test toString() method
        System.out.println("\nGoal 2 toString():");
        System.out.println(goal2.toString());
    }
}
