package com.example.week3java.modal;

import com.example.week3java.controller.GameController;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Arrays;

public class GameLogica {
    public ArrayList<CustomPane> velden = new ArrayList<>();

    public Stapel serviesKast = new Stapel();

    public Booord speelBord;
    public HulpFunc hulp;
    public static char[] maximaleSpelers = {'B', 'H', 'T', 'P'};
    public int[] spelersInSpel;
    public int spelerAanZet;
    public Coordinaat startPositie = null;

    public boolean gameIsGaande = false;

    private Label feedbackText;

    //TODO: overal waar parameter coordianten zijn + waarde misschien een class maken of in de coordinaat gooien

    public GameLogica() {
        this.speelBord = new Booord();
        this.hulp = new HulpFunc(this);
    }

    /**
     * zet de begin vakjes van elke speler in een hoek tegenovergesteld van de tegenstander
     * @param spelers het aantal spelers wat meespeeld
     */
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

    /**
     * kijkt eerst of het spel gaande is, en handeld daarna de input/zetten af die de speler kan doen
     * @param x de x coordinaat van het beginvakje waarvan hij een zet gaat doen
     * @param y de y coordinaat van het beginvakje waarvan hij een zet gaat doen
     * @param hoeveelste het hoeveelste vakje van de vakjes array het is
     * @throws Exception als hij wonky doet
     */
    public void doeZet(int x, int y, int hoeveelste) throws Exception {
        if (gameIsGaande) {
            boolean isZetValidVoorBegin = speelBord.checkWaarde(new Coordinaat(x,y), spelersInSpel[spelerAanZet]);

            if (isZetValidVoorBegin) {
                startPositie = new Coordinaat(x, y);
                regelDeHighlight(hoeveelste);
                feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" kan nu zijn tweede move doen");
            } else {
                boolean isZetValidVoorMove = speelBord.checkWaarde(new Coordinaat(x,y), 0);
                boolean o = false;
                if (isZetValidVoorMove && startPositie != null) {
                    o |= verdubbelen(x, y);
                    o |= verplaats(x, y);
                } else {
                    feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" moet een eerst een zet doen op je eigen vakje");
                }
                verwijderAlleHighlights();
                if (o)
                    geefDeBeurtDoor();
            }
            updateAlleVelden();
        }
    }

    /**
     * kijkt of je mag springen naar het vakje wat je meegeef. Infecteert daarna en update dan alle velden voor de nieuwe text en kleur
     * @param x de x coordinaat van het bestemmings vakje
     * @param y de y coordinaat van het bestemmings vakje
     * @return geeft terug of het wel op niet is gelukt
     */
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

    /**
     * kijkt of je mag verdubbelen naar het vakje wat je meegeef. Infecteert daarna en update dan alle velden voor de nieuwe text en kleur
     * @param x de x coordinaat van het bestemmings vakje
     * @param y de y coordinaat van het bestemmings vakje
     * @return geeft terug of het wel op niet is gelukt
     */
    public boolean verdubbelen(int x, int y){
        boolean output = false;
        if(hulp.magDeDuplicatieVoorkomen(startPositie.x, startPositie.y, x, y)){
            speelBord.setWaarde(new Coordinaat(x,y), (int) spelersInSpel[spelerAanZet]);
            infecteer(x,y);
            output = true;
        }
        updateAlleVelden();
        return output;
    }

    /**
     * infecteerd de tegenstander die rondom het vakjes lig (straal van 1) met het virus van deze speler
     * @param x de x coordinaat van de oorsprong vakje
     * @param y de y coordinaat van de oorsprong vakje
     */
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

    /**
     * kijkt of het spel voorbij is, ligt het huldige bord in de servies kast stapel en zorgt dat de volgende speler aan zet is. en weergeeft dat op het scherm
     */
    private void geefDeBeurtDoor() {
        int[][] copy = HulpFunc.diepeCopy(speelBord.getSpeelBooordt());
        serviesKast.legNeer(copy);

        //ga naar de volgende speler om te kijken of hij nog zetten kan doen
        gaNaarVolgendeSpeler();

        if (hulp.kijkenOfHetSpelOverIs(spelersInSpel[spelerAanZet])) {
            gameIsGaande = false;
            //ga een speler terug omdat die heeft gewonnen
            gaEenSpelerTerug();
            feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" heeft gewonnen. Het spel is afgelopen");
        } else
            feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" kan nu een move zetten");
    }

    /**
     * Hierin gaat hij naar de volgende speler
     */
    public void gaNaarVolgendeSpeler() {
        if (spelerAanZet == (spelersInSpel.length-1))
            spelerAanZet = 0;
        else
            spelerAanZet++;
    }

    /**
     * Hiermee gaat hij een speler terug
     */
    public void gaEenSpelerTerug() {
        spelerAanZet--;
        if (spelerAanZet < 0) spelerAanZet = spelersInSpel.length-1;
    }

    /**
     * dit start het spel, zorgt dat een speler kan beginnen met een zet doen en krijgt een referentie van de feedback label uit de controller zodat ik vanaf hier feedback op het scherm kan zetten
     * @param feedbackText de referentie naar het feedback label
     */
    public void spelStart(Label feedbackText) {
        spelerAanZet = 0;
        this.feedbackText = feedbackText;
        feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" kan nu een move zetten");
        gameIsGaande = true;
        int[][] copy = HulpFunc.diepeCopy(speelBord.getSpeelBooordt());
        serviesKast.legNeer(copy);
    }

    /**
     * Vraag om het speelbord
     * @return krijg een copy van het speelbord
     */
    public int[][] getSpeelbord() {
        return speelBord.getSpeelBooordt();
    }

    /**
     * zorgt ervoor dat je kan abboneren om updates te krijgen van de observers
     * @param veld het veld wat als abbonee wordt toegevoegd
     */
    public void subVeld(CustomPane veld) {velden.add(veld);}

    /**
     * update alle velden door een update call te sturen naar alle subscribers
     */
    public void updateAlleVelden() {
        for (CustomPane p : velden) {
            p.update(p);
        }
    }

    /**
     * zorg ervoor dat alle panes hun normale kleur krijgen, dat 1 pane een kleur krijg en dat daarna alles wordt geupdated visueel
     * @param hoeveelste
     */
    private void regelDeHighlight(int hoeveelste) {
        verwijderAlleHighlights();
        maakVeldHighlight(velden.get(hoeveelste));
        updateAlleVelden();
    }

    /**
     * zorgt ervoor dat alle panes weer hun oorspronkelijke kleur krijgen
     */
    private void verwijderAlleHighlights() {
        for (CustomPane p : velden)
            p.geselecteerd = false;
        updateAlleVelden();
    }

    /**
     * zorgt dat de geselecteerde pane een ander kleurtje krijg
     * @param p het paneel wat een ander kleurtje moet krijgen
     */
    private void maakVeldHighlight(CustomPane p) {
        p.geselecteerd = true;
    }
}
