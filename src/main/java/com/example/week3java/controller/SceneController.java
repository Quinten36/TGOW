package com.example.week3java.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneController {
    public static Stage mainStage;

    /**
     * A function to switch from scene to another scene
     * @param location = The location to where de scene need to be switched too
     * @param title = The title of the view
     * @throws IOException = throws error when it goes wrong
     */
    public static void switchTo(String location, String title) throws IOException {
        FXMLLoader FXMLloader = new FXMLLoader(SceneController.class.getResource("/com/example/week3java/"+location+".fxml"));

        BorderPane rootpane = new BorderPane();
        rootpane.setCenter(FXMLloader.load());

        Scene scene = new Scene(rootpane, 600, 400);
        mainStage.setScene(scene);
        mainStage.setTitle(title);
        mainStage.centerOnScreen();
        mainStage.show();

    }
}


