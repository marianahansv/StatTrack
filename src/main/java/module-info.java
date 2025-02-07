module com.example.cmpt370project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cmpt370project to javafx.fxml;
    exports com.example.cmpt370project;
}