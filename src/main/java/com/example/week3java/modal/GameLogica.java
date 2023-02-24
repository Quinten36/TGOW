package com.example.week3java.modal;

import com.example.week3java.controller.GameController;
import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.concurrent.*;

public class GameLogica {
    public ArrayList<CustomPane> velden = new ArrayList<>();
    public int GROOTTE = 7;

    public Stapel serviesKast = new Stapel();

    public Booord speelBord;
    public HulpFunc hulp;
    public static char[] maximaleSpelers = {'B', 'H', 'T', 'P'};
    public int[] spelersInSpel;
    public int[] robots;
    public int spelerAanZet;
    public Coordinaat startPositie = null;

    public boolean gameIsGaande = false;
    private final int milliesSleep = 50;

    private Label feedbackText;

    public GameLogica() {
        this.speelBord = new Booord(GROOTTE);
        this.hulp = new HulpFunc(this);
    }

    public GameLogica(int[] robots) {
        this.speelBord = new Booord(GROOTTE);
        this.hulp = new HulpFunc(this);
        this.robots = robots;
    }

    /**
     * zet de begin vakjes van elke speler in een hoek tegenovergesteld van de tegenstander
     * @param spelers het aantal spelers wat meespeeld
     */
    public void setOriginVelden(int spelers) {
        if (spelers == 2) {
            spelersInSpel = new int[2];
            for (int t = 0; t < spelers; t++) //rij
                spelersInSpel[t] = (int) maximaleSpelers[t];
            for (int j = 0; j < spelers; j++) {//rij
                for (int i = GROOTTE-1; i > (GROOTTE-1-spelers); i--) { //kolom
                    System.out.println(i);
                    //TODO: dit nog dynamisch maken
                    this.speelBord.setWaarde(new Coordinaat(j, i), spelersInSpel[0]);
                    this.speelBord.setWaarde(new Coordinaat(i, j), spelersInSpel[1]);
                }
            }
        }
    }

    /**
     * kijkt eerst of het spel gaande is, en handeld daarna de input/zetten af die de speler kan doen
     * @param coor de x en y coordinaat van het beginvakje waarvan hij een zet gaat doen
     * @param hoeveelste het hoeveelste vakje van de vakjes array het is
     * @throws Exception als hij wonky doet
     */
    public void doeZet(Coordinaat coor, int hoeveelste) throws Exception {
        boolean isZetValidVoorBegin = speelBord.checkWaarde(coor, spelersInSpel[spelerAanZet]);

        if(gameIsGaande) {
            if (isZetValidVoorBegin) {
                startPositie = coor;
                regelDeHighlight(hoeveelste);
                feedbackText.setText("Speler " + (char) spelersInSpel[spelerAanZet] + " kan nu zijn tweede move doen");
            } else {
                boolean isZetValidVoorMove = speelBord.checkWaarde(coor, 0);
                boolean o = false;
                if (isZetValidVoorMove && startPositie != null) {
                    o = verdubbelen(coor);
                    o |= verplaats(coor);
                } else {
                    feedbackText.setText("Speler " + (char) spelersInSpel[spelerAanZet] + " moet een eerst een zet doen op je eigen vakje");
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
     * @param coor de x en y coordinaat van het bestemmings vakje
     * @return geeft terug of het wel op niet is gelukt
     */
    private boolean verplaats(Coordinaat coor) {
        boolean output = false;
        if(hulp.magDeVerplaatsingVoorkomen(startPositie, coor)){
            speelBord.setWaarde(coor, (int) spelersInSpel[spelerAanZet]);
            speelBord.setWaarde(startPositie, 0);

            infecteer(coor);
            output = true;
        }
        updateAlleVelden();
        return output;
    }

    /**
     * kijkt of je mag verdubbelen naar het vakje wat je meegeef. Infecteert daarna en update dan alle velden voor de nieuwe text en kleur
     * @param coor de x en y coordinaat van het bestemmings vakje
     * @return geeft terug of het wel op niet is gelukt
     */
    public boolean verdubbelen(Coordinaat coor){
        boolean output = false;
        if(hulp.magDeDuplicatieVoorkomen(startPositie, coor)){
            speelBord.setWaarde(coor, (int) spelersInSpel[spelerAanZet]);
            infecteer(coor);
            output = true;
        }
        updateAlleVelden();
        return output;
    }

    /**
     * infecteerd de tegenstander die rondom het vakjes lig (straal van 1) met het virus van deze speler
     * @param coor de x en y coordinaat van de oorsprong vakje
     */
    private void infecteer(Coordinaat coor){
        int kleurVanVak = GameController.gameRules.speelBord.getWaarde(coor);
        for (int xj = -1; xj <= 1; xj++) {
            //x is rij
            //y is kolom
            for(int yi = -1; yi<=1;yi++){
                try {
                    Coordinaat tijdelijk = new Coordinaat(coor.x+xj, coor.y+yi);
                    if (speelBord.getWaarde(tijdelijk) != 0 && speelBord.getWaarde(tijdelijk) != kleurVanVak)
                        speelBord.setWaarde(tijdelijk, kleurVanVak);
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

        if (gameIsGaande) {
            if (hulp.kijkenOfHetSpelOverIs(spelersInSpel[spelerAanZet])) {
                gameIsGaande = false;
                System.out.println("DONE");
                spelIsOver();
            } else {
                for (int i : robots)
                    if (spelersInSpel[spelerAanZet] == i) {
                        PauseTransition pause = new PauseTransition(Duration.millis(milliesSleep));
                        pause.setOnFinished(event ->
                        {
                            try {
                                doeDeAIMove();
                            } catch (Exception ignored) {
                            }
                        }
                        );
                        pause.play();
                    }
                feedbackText.setText("Speler " + (char) spelersInSpel[spelerAanZet] + " kan nu een move zetten");
            }
        }
    }

    /**
     * Als het spel over is dan weergeef wie heeft gewonnen
     */
    private void spelIsOver() {
        if (gameIsGaande)
            gameIsGaande = false;
        int[][] copy = speelBord.getSpeelBooordt();
        int countP1 = 0;
        int countP2 = 0;
        int index = 0;
        for (int i = 0; i< GROOTTE;i++) {
            for (int j = 0; j < GROOTTE;j++) {
                if (copy[i][j] == spelersInSpel[index])
                    countP1++;
                if (copy[i][j] == spelersInSpel[index+1])
                    countP2++;
            }
        }
        System.out.println("Count speler 1: "+countP1);
        System.out.println("Count speler 1: "+countP2);
        if (countP1 > countP2)
            feedbackText.setText("Speler "+(char) spelersInSpel[index]+" heeft gewonnen. Het spel is afgelopen");
        if (countP2 > countP1)
            feedbackText.setText("Speler "+(char) spelersInSpel[index+1]+" heeft gewonnen. Het spel is afgelopen");
        if (countP1 == countP2)
            feedbackText.setText("Gelijk spel. Niemand heeft gewonnen");
        //ga een speler terug omdat die heeft gewonnen
        gaEenSpelerTerug();
    }

    /**
     * Bedenk en doe de move die de AI kan doen
     * @throws Exception Als hij wonky doet
     */
    private void doeDeAIMove() throws Exception {
        //krijg al jouw vakjes
        ArrayList<Coordinaat> alleVakjesVanJou = getAlJouwVakjes(spelersInSpel[spelerAanZet]);//KLOPT

        //krijg alle zetten van jezelf
        ArrayList<ResultForm> allMogelijkeZetten = getAlleZetten(alleVakjesVanJou);//KLOPT OOK

        //Bereken score en krijg lijst van beste zetten
        ArrayList<ResultForm> results = berekenBesteMove(allMogelijkeZetten);

        if (results.size() == 0) {
            System.out.println("FINISH");
        } else {
            ResultForm zet = results.get((int) Math.floor(Math.random() * results.size()));
            doeZet(zet.beginPunt, 0);
            doeZet(zet.eindPunt, 0);
//           System.out.println("Deze zet doet hij. Van: ["+zet.beginPunt.x+","+zet.beginPunt.y+"] Naar: ["+zet.eindPunt.x+","+zet.eindPunt.y+"]");
        }
    }

    /**
     * Bereken de beste moves die een AI kan doen, met bij elke move de score
     * @param allMogelijkeZetten alle mogelijke zetten die de AI kan doen
     * @return een array met alle beste moves met score
     */
    private ArrayList<ResultForm> berekenBesteMove(ArrayList<ResultForm> allMogelijkeZetten) {
        int besteScore = 0;
        ArrayList<ResultForm> besteMoves = new ArrayList<>();

        for (ResultForm coordsSet : allMogelijkeZetten) {
            int score = berekenScoreMove(coordsSet.beginPunt, coordsSet.eindPunt);
            if (score == besteScore) {
                coordsSet.score = score;
                besteMoves.add(coordsSet);
                // voeg toe aan beste moves
            }
            if (score > besteScore) {
                besteScore = score;
                coordsSet.score = score;
                besteMoves.clear();
                besteMoves.add(coordsSet);
                //set new best score
                //empty beste moves and add new one
            }
        }

//TODO: counter maken hoeveel vakjes per persoon heeft

        ArrayList<ResultForm> output = new ArrayList<>(besteMoves);
        return output;
    }

    /**
     * bereken de totaal score van een bepaalde move. inclusief plus en min punten
     * @param o startPositie
     * @param o1 eindPositie
     * @return geeft de totale score terug
     */
    private int berekenScoreMove(Coordinaat o, Coordinaat o1) {
        int score = 0;
        if (hulp.magDeDuplicatieVoorkomen(o, o1))
            score++;
        score += hulp.kijkHoeveelGeinfecteerdKunnenWorden(o1, spelersInSpel[spelerAanZet]);
        score *= 10;

        int minusPoint = calculateMinusPoints(o1);
        score -= minusPoint;

        return score;
    }

    /**
     * Zorgt dat alles wordt gedaan om de minpunten te berekenen
     * @param moveToCheck de moves die hij moet checken
     * @return het aantal minpunten
     */
    private int calculateMinusPoints(Coordinaat moveToCheck) {
        int tegenstander = spelerAanZet;
        tegenstander++;
        int score = 0;
        if (tegenstander == (spelersInSpel.length))
            tegenstander = 0;
        ArrayList<Coordinaat> tegenstandersVakjes = getAlJouwVakjes(spelersInSpel[tegenstander]);
//
        ArrayList<ResultForm> tegenstandersMoves = getAlleZetten(tegenstandersVakjes);

        score = CheckMatchingPlaces(tegenstandersMoves, moveToCheck);

        return score;
    }

    /**
     * berekent een score op basis van op hoeveel manieren de tegenstander het vakje wat je wilt innemen weer terug kan innemen
     * @param tegenstandersMoves alle moves die de tegenstander kan doen
     * @param moveToCheck de move die je wilt doen en wilt checken
     * @return de score van op hoeveel manieren de tegenstander het vakje kan terug innemen
     */
    private int CheckMatchingPlaces(ArrayList<ResultForm> tegenstandersMoves, Coordinaat moveToCheck) {
        int score = 0;

            for (ResultForm teg : tegenstandersMoves) {
                if (moveToCheck.x == teg.eindPunt.x && moveToCheck.y == teg.eindPunt.y) {
                    score += 1;
                }
            }

        return score;
    }

    /**
     * verkrijg alle vakjes van een bepaalde speler
     * @param spelerTeCheck de speler waarvan hij de vakjes moet krijgen
     * @return een lijst met alle vakjes van een speler
     */
    private ArrayList<Coordinaat> getAlJouwVakjes(int spelerTeCheck) {
        ArrayList<Coordinaat> alleVakjesVanJou = new ArrayList<>();
        int[][] copy = speelBord.getSpeelBooordt();
        for (int i = 0; i < GROOTTE; i++)
            for (int j = 0; j < GROOTTE; j++)
                if (copy[i][j] == spelerTeCheck)
                    alleVakjesVanJou.add(new Coordinaat(i,j));
        return alleVakjesVanJou;
    }

    /**
     * verkrijg alle alle zetten die de je kan doen op basis van de lijst van de vakjes van jou
     * @param alleVakjesVanJou de lijst met vakjes van jouw
     * @return een lijst met alle mogelijke zetten (From, to) format
     */
    private ArrayList<ResultForm> getAlleZetten(ArrayList<Coordinaat> alleVakjesVanJou) {
        ArrayList<ResultForm> allMogelijkeZetten = new ArrayList<>();
        for (Coordinaat c : alleVakjesVanJou) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (hulp.magDeDuplicatieVoorkomen(c, c.voegToeXY(i, j))) {
                        allMogelijkeZetten.add(new ResultForm(c, c.voegToeXY(i, j), -20));
                    }
                }
            }

            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    if (hulp.magDeVerplaatsingVoorkomen(c, c.voegToeXY(i, j))) {
                        allMogelijkeZetten.add(new ResultForm(c, c.voegToeXY(i, j), -20));
                    }
                }
            }
        }
        return allMogelijkeZetten;
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
    public void spelStart(Label feedbackText) throws Exception {
        spelerAanZet = 0;
        this.feedbackText = feedbackText;
        feedbackText.setText("Speler "+(char) spelersInSpel[spelerAanZet]+" kan nu een move zetten");
        gameIsGaande = true;
        int[][] copy = HulpFunc.diepeCopy(speelBord.getSpeelBooordt());
        serviesKast.legNeer(copy);
        if (spelersInSpel[spelerAanZet] == maximaleSpelers[0])
            doeDeAIMove();
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
