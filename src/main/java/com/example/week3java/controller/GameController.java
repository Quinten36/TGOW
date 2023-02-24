package com.example.week3java.controller;

import com.example.week3java.modal.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
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

        //FIXME: Hier kan je aangeven wie ai is. Default is H ai en b speler
//        int[] r = {'B','H'};
        int[] r = {'H'};
        gameRules = new GameLogica(r);
        gameRules.setOriginVelden(2);

        int[][] copyBoard = gameRules.getSpeelbord();
        // Maak een grid
        for (int i = 0; i < gameRules.GROOTTE; i++) {
            for (int j = 0; j < gameRules.GROOTTE; j++) {
                // maak custom blokjes op basis van de waarde
                final Coordinaat c = new Coordinaat(j,i);
                CustomPane vakje = maakVakje(copyBoard[j][i], c);

                final int rij = i;
                final int kolom = j;
                final int hoeveelste = gridPane.getChildren().size();
                vakje.getPane().setOnMouseClicked((MouseEvent event) -> {
                    try {
                        gameRules.doeZet(new Coordinaat(kolom, rij), hoeveelste);
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

        try {
            gameRules.spelStart(feedback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void geefFeedback(String feedbackw) {
        feedback.setText(feedbackw);
    }

    //gefixed met de debug ogen van de god Sander
    @FXML
    void GaBoordenTerug(ActionEvent event) {
        if (gameRules.spelerAanZet == 0) { //alleen als de speler aan de beurt is mag hij cheaten
            gameRules.serviesKast.pakBovensteDing(); //pakt huidige stand
            gameRules.serviesKast.pakBovensteDing(); //pakt de stand zoals hij bij de tegenstand stond
            int[][] oud3 = (int[][]) gameRules.serviesKast.krijgWaardeBovenste(); //pakt de stand hoe hij stond toen je aan de beurt was
            for (int i = 0; i< gameRules.GROOTTE;i++)
                for (int j = 0; j < gameRules.GROOTTE;j++)
                    gameRules.speelBord.setWaarde(new Coordinaat(j,i), oud3[j][i]);

            gameRules.gameIsGaande = true;

            gameRules.updateAlleVelden();
        }
    }

    @FXML
    void goResetThingsTheEasyWay(ActionEvent event) throws IOException {
        SceneController.switchTo("game", "Game screen");
    }
}
