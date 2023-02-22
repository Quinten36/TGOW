package com.example.week3java;

import com.example.week3java.controller.SceneController;
import com.example.week3java.modal.Stapel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneController.mainStage = stage;
        SceneController.switchTo("hello-view", "Main screen");
    }

    public static void main(String[] args) {
        launch();
    }
}