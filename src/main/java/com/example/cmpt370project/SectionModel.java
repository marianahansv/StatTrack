package com.example.cmpt370project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Sections are stored in a JSON file and loaded
 * on application startup. If no sections are found, default sections are added.
 */
public class SectionModel {
    private static final String FILE_NAME = System.getProperty("user.home") + "/GoalApplication/sections.json";
    private List<String> sections;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Loads sections from the JSON file.
     * If no sections are found, default sections ("General", "Personal", "Fitness") are added.
     */
    public SectionModel() {
        sections = new ArrayList<>();
        loadSectionsFromFile();
        // Set default sections if none are loaded.
        if (sections.isEmpty()) {
            sections.add("General");
            sections.add("Personal");
            sections.add("Fitness");
            saveSectionsToFile();
        }
    }

    /**
     * Adds a new section to the model if it does not already exist, then saves the updated list to file.
     *
     * @param section the name of the section to add
     */
    public boolean addSection(String section) {
        if (!sections.contains(section)) {
            sections.add(section);
            saveSectionsToFile();
            return true;
        } else {
            return false; // Section already exists
        }
    }

    /**
     * Deletes the specified section from the model if it exists, then saves the updated list.
     *
     * @param section the name of the section to delete
     * @return true if the section was deleted, false otherwise
     */
    public boolean deleteSection(String section, UserHistoryDataModel userHistoryDataModel) {
        if (!sections.contains(section)) {
            return false;
        }
        GoalModel goalModel = new GoalModel(userHistoryDataModel);
        goalModel.deleteGoalsInSection(section);
        sections.remove(section);
        saveSectionsToFile();

        return true;
    }

    /**
     * Returns the list of sections.
     *
     * @return a {@code List<String>} containing all sections
     */
    public List<String> getSections() {
        return sections;
    }

    /**
     * Checks if the sections file exists, and creates the necessary directories and file if not.
     */
    private void checkIfFileExists() {
        try {
            Path pathToFile = Paths.get(FILE_NAME);
            if (Files.notExists(pathToFile.getParent())) {
                Files.createDirectories(pathToFile.getParent());
            }
            if (Files.notExists(pathToFile)) {
                Files.createFile(pathToFile);
            }
        } catch (IOException e) {
            System.err.println("Error checking file: " + e.getMessage());
        }
    }

    /**
     * Saves the current list of sections to a JSON file.
     */
    public void saveSectionsToFile() {
        checkIfFileExists();
        try (Writer writer = new FileWriter(FILE_NAME)) {
            gson.toJson(sections, writer);
        } catch (IOException e) {
            System.err.println("Error saving sections to file: " + e.getMessage());
        }
    }

    /**
     * Loads sections from the JSON file into the model.
     * If the file is empty, no sections are loaded.
     */
    public void loadSectionsFromFile() {
        checkIfFileExists();
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() != 0) {
            try (Reader reader = new FileReader(FILE_NAME)) {
                String[] loadedSections = gson.fromJson(reader, String[].class);
                if (loadedSections != null) {
                    for (String sec : loadedSections) {
                        sections.add(sec);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading sections from file: " + e.getMessage());
            }
        }
    }
}
