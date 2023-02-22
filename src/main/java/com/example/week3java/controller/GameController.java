package com.example.week3java.controller;

import com.example.week3java.modal.BordOberserver;
import com.example.week3java.modal.Coordinaat;
import com.example.week3java.modal.CustomPane;
import com.example.week3java.modal.GameLogica;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable, BordOberserver {
    @FXML
    private Pane gamePane;
    @FXML
    private Label feedback;

    public static GameLogica gameRules;
    public GridPane gameBord;

    public CustomPane maakVakje(int waarde, Coordinaat coor) {
        CustomPane paneel = new CustomPane( 40, 40, waarde, coor);
        return paneel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(2);
        gridPane.setVgap(-12);

        gameRules = new GameLogica();
        gameRules.sub(this);
        gameRules.setOriginVelden(2);

        int[][] copyBoard = gameRules.getSpeelbord();
        // Maak een grid
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                // maak custom blokjes op basis van de waarde
                final Coordinaat c = new Coordinaat(j,i);
                CustomPane vakje = maakVakje(copyBoard[j][i], c);

                final int rij = i;
                final int kolom = j;
                final int hoeveelste = gridPane.getChildren().size();
                vakje.getPane().setOnMouseClicked((MouseEvent event) -> {
                    try {
                        gameRules.doeZet(kolom, rij, hoeveelste);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    System.out.println("Het mooi vakje op [" + rij + ", " + kolom + "] is bekoekeloerd. Dit is de ["+hoeveelste+"] van de gridpane");
                });
                gridPane.add(vakje.getPane(), i, j); // voeg het vakje toe aan de grid, de grid doet zelf de positionering
                //subscribe aan het velden array
                gameRules.subVeld(vakje);
            }
        }

        gamePane.getChildren().add(gridPane);
        gameBord = (GridPane) gamePane.getChildren().get(0);

        gameRules.spelStart(feedback);
    }

    @Override
    public void update(int x, int y, int waarde) {
//        int field = (x+1)*(y+1);
//        gameBord.getChildren().get(field);
    }

    @Override
    public void geefFeedback(String feedbackw) {
        feedback.setText(feedbackw);
    }
}
