StatTrack Application - Installation Instructions

Introduction:
This document provides instructions on how to run the StatTracker application.

Prerequisites:
To run StatTrack, you need:

1.  Java Runtime Environment (JRE) 17 or later:
    * StatTracker requires a compatible JRE to execute.
    * Download and install a JRE from a trusted source:
        * Adoptium (Recommended): [https://adoptium.net/temurin/releases/](https://adoptium.net/temurin/releases/)
        * Oracle: [https://www.oracle.com/java/technologies/javase-downloads.html](https://www.oracle.com/java/technologies/javase-downloads.html)
2.  JavaFX SDK 17 or later:
    * StatTracker relies on JavaFX for its graphical user interface.
    * Download the JavaFX SDK from the official OpenJFX website:
        * OpenJFX: [https://openjfx.io/downloads/](https://openjfx.io/downloads/)
    * Extract the SDK to a location on your computer (e.g., C:\javafx-sdk-17.0.14).

Running the Application using the Batch File (Recommended):

1.  Place the provided `runStatTrack.bat` file in the same directory as the StatTrack JAR file (StatTrack.jar).
2.  Double-click the `runStatTrack.bat` file.
3.  The application will start.

Running the Application Manually:

1.  Open a command prompt or PowerShell window.
2.  Navigate to the directory containing the StatTrack JAR file (StatTrack.jar).
    * Use the `cd` command to change directories.
    * Example: `cd C:\Users\YourUsername\StatTrack`
3.  Run the application using the following command:

    ```bash
    java --module-path "C:\Program Files\javafx-sdk-17.0.14\lib" --add-modules javafx.controls,javafx.fxml -jar StatTrack.jar
    ```

    * Replace `"C:\Program Files\javafx-sdk-17.0.14\lib"` with the actual path to your JavaFX SDK's `lib` directory.

Troubleshooting:

* "Error: JavaFX runtime components are missing":
    * This error indicates that the JavaFX modules are not found.
    * Double-check that the `--module-path` is correct and that the JavaFX SDK is installed.
    * Ensure that the provided bat file, has the correct path to the javafx library.
* "Error: Could not find or load main class":
    * This error means the JAR file is corrupted or the main class is not specified correctly.
    * Ensure that the jar file is in the current directory.
* "runStatTrack is not recognized as an internal or external command":
    * Ensure that the runStatTrack.bat file is located in the same directory as the jar file.
    * If you are trying to run it from another directory, you will have to include the full path to the bat file.

Notes:

* This application requires JavaFX version 17 or later. Older versions may not be compatible.
* The path to the JavaFX SDK may vary depending on your installation.
* If you are having issues, ensure that your environment variables are correctly set.

Contact:
For further assistance, please contact the team for solutions.