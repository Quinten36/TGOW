package com.example.week3java.modal;

import com.example.week3java.controller.GameController;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class GameLogica {
    public ArrayList<GameController> subscribers = new ArrayList<>();
    public ArrayList<CustomPane> velden = new ArrayList<>();

    public Booord speelBord;
    public HulpFunc hulp;
    public static char[] maximaleSpelers = {'B', 'H', 'T', 'P'};
    public int[] spelersInSpel;
    public int spelerAanZet;
    public Coordinaat startPositie = null;
    public Coordinaat endPositie = null;

    private Label feedbackText;

    //TODO: overal waar parameter coordianten zijn + waarde misschien een class maken of in de coordinaat gooien

    public GameLogica() {
        this.speelBord = new Booord();
        this.hulp = new HulpFunc(this);
    }

    public void setOriginVelden(int spelers) {
        if (spelers == 2) {
            spelersInSpel = new int[2];
            for (int j = 0; j < spelers; j++) {//rij
                spelersInSpel[j] = (int) maximaleSpelers[j];
                for (int i = 6; i > (6-spelers); i--) { //kolom
                    //TODO: dit nog dynamisch maken
                    //TODO: fix deze bug
                    this.speelBord.setWaarde(new Coordinaat(j, i), spelersInSpel[0]);//spelersInSpel[1]
                    this.speelBord.setWaarde(new Coordinaat(i, j), 'H');
                }

            }
        }
    }

    public void doeZet(int x, int y, int hoeveelste) throws Exception {//
        boolean isZetValidVoorBegin = checkMove(x, y, spelersInSpel[spelerAanZet]);

        if (isZetValidVoorBegin) {
            startPositie = new Coordinaat(x, y);
            regelDeHighlight(hoeveelste);
            feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" kan nu zijn tweede move doen");
            //TODO: maak een check dat hij hieronder alleen uitvoer als startpositie iets is
        } else {
            boolean isZetValidVoorMove = checkMove(x, y, 0);
            boolean o = false;
            if (isZetValidVoorMove && startPositie != null) {
                o |= verdubbelen(x, y);
                o |= verplaats(x, y);
                //check if can copy
                //check if can jump
            } else {
                verwijderAlleHighlights();
                feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" moet een eerst een zet doen op je eigen vakje");
            }
            if (o)
                geefDeBeurtDoor();
        }
    }

    private boolean verplaats(int x, int y) throws Exception {
        boolean output = false;
        if(hulp.magDeVerplaatsingVoorkomen(startPositie.x, startPositie.y, x, y)){
            speelBord.setWaarde(new Coordinaat(x,y), (int) spelersInSpel[spelerAanZet]);
            speelBord.setWaarde(startPositie, 0);

            infecteer(x,y);
            output = true;
        }
        updateAlleVelden();
        return output;
    }

    public boolean verdubbelen(int x, int y){
        boolean output = false;
        if(hulp.magDeDuplicatieVoorkomen(startPositie.x, startPositie.y, x, y)){
            System.out.println("doe zet");
            speelBord.setWaarde(new Coordinaat(x,y), (int) spelersInSpel[spelerAanZet]);
            output = true;
            infecteer(x,y);
        }

        updateAlleVelden();
        return output;
    }

    private void infecteer(int x, int y){
        int kleurVanVak = GameController.gameRules.speelBord.getWaarde(new Coordinaat(x,y));
        for (int xj = -1; xj <= 1; xj++) {
            //x is rij
            //y is kolom
            for(int yi = -1; yi<=1;yi++){
                try {
                    if (speelBord.getWaarde(new Coordinaat(x+xj, y+yi)) != 0 && speelBord.getWaarde(new Coordinaat(x+xj, y+yi)) != kleurVanVak)
                        speelBord.setWaarde(new Coordinaat(x+xj, y+yi), kleurVanVak);
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void geefDeBeurtDoor() {
        if (spelerAanZet == (spelersInSpel.length-1))
            spelerAanZet = 0;
        else
            spelerAanZet++;
        //TODO: feedback algemene fucntie maken?
        feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" kan nu een move zetten");
    }

    private boolean checkMove(int x, int y, int waarde) {
        return speelBord.checkWaarde(new Coordinaat(x,y), waarde);
    }

    public void spelStart(Label feedbackText) {
        spelerAanZet = 0;
        this.feedbackText = feedbackText;
        feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" kan nu een move zetten");
    }

    //validate move

    //spread

    //update

    //get speelbord
    public int[][] getSpeelbord() {
        return speelBord.getSpeelBooordt();
    }

    //TODO: Naam veranderen?
    public void sub(GameController sub) {
        subscribers.add(sub);
    }

    public void subVeld(CustomPane veld) {velden.add(veld);}

    public void updateAlleVelden() {
        for (CustomPane p : velden) {
            p.update(p);
        }
    }

    private void regelDeHighlight(int hoeveelste) {
        verwijderAlleHighlights();
        maakVeldHighlight(velden.get(hoeveelste));
        updateAlleVelden();
    }

    private void verwijderAlleHighlights() {
        for (CustomPane p : velden)
            p.geselecteerd = false;
        updateAlleVelden();
    }

    private void maakVeldHighlight(CustomPane p) {
        p.geselecteerd = true;
    }
}
