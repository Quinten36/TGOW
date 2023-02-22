package com.example.week3java.controller;

import com.example.week3java.controller.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class HelloController {
    @FXML
    private Button PlayGame;

    @FXML
    private Button Quit;

    @FXML
    protected void playGame() throws IOException {
        SceneController.switchTo("game", "Game screen");
    }
}