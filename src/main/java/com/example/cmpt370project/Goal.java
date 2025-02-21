package com.example.cmpt370project;

import java.time.LocalDate;

/**
 * The Goal class represents an individual goal with various attributes to track its details and progress.
 * This class provides the structure for creating, reading, and updating goals.
 */
public class Goal {
    private String title;

    private String section = "general"; //default section
    private String difficulty = "medium"; //default difficulty
    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();

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

    /**
     * Returns a string representation of the Goal
     */
    @Override
    public String toString() {
        return "Goal{" +
                "title='" + title + '\'' +
                ", section='" + section + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
