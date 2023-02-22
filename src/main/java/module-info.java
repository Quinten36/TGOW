module com.example.week3java {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.week3java to javafx.fxml;
    exports com.example.week3java;
    exports com.example.week3java.controller;
    opens com.example.week3java.controller to javafx.fxml;
}