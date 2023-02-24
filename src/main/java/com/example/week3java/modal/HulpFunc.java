package com.example.week3java.modal;

import com.example.week3java.controller.GameController;

import java.util.ArrayList;
import java.util.Arrays;

public class HulpFunc {
    private GameLogica rules;

    public HulpFunc(GameLogica rules) {
        this.rules = rules;
    }

    /**
     * Doet alle checks of de speler mag dupliceren naar het gewenste vakje
     * @param origin de coordinaten van waar hij de zet wilt beginnen
     * @param bestemming de coordinaten van waar hij de zet wilt eindigen
     * @return geeft true/false weer als hij de zet mag doen of niet
     */
    public boolean magDeDuplicatieVoorkomen(Coordinaat origin, Coordinaat bestemming){
        return
            !isLeeg(origin)
            &&
            isLeeg(bestemming)
            &&
            isSchuin(origin,bestemming);
    }

    /**
     * checkt of hij geselecteerde vakje leeg is
     * @param coor de coordinaten van het vakje wat hij moet controleren
     * @return true of false of hij wel of niet leeg is
     */
    private boolean isLeeg(Coordinaat coor) {
        try {
            int[][] veld = rules.speelBord.getSpeelBooordt();
            return veld[coor.x][coor.y]==0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * hij kijkt of hij naar een vakje mag verplaatsen die aanliggend is
     * @param origin de coordianten van de origin vakje
     * @param bestemming de coordinaten van het bestemmings vakje
     * @return hij geeft terug of hij de zet mag doen
     */
    private boolean isSchuin(Coordinaat origin, Coordinaat bestemming){
        int dx = Math.abs(bestemming.x - origin.x);
        int dy = Math.abs(bestemming.y - origin.y);
        return dx + dy >= 1;
//        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1);
    }

    /**
     * Doet alle checks of de speler mag verplaatsen naar het gewenste vakje
     * @param origin de coordinaten van waar hij de zet wilt beginnen
     * @param bestemming de coordinaten van waar hij de zet wilt eindigen
     * @return geeft true/false weer als hij de zet mag doen of niet
     */
    public boolean magDeVerplaatsingVoorkomen(Coordinaat origin, Coordinaat bestemming){
        return
            !isLeeg(origin)
                &&
                isLeeg(bestemming)
                &&
                isOp2BlokkenAfstand(origin,bestemming);
    }

    /**
     * kijk of het blokje op 2 blokken afstand is
     * @param origin de coordinaten van de origin vakje
     * @param bestemming de coordianten van het bestemmings vakje
     * @return een true/false of de zet valid is
     */
    private boolean isOp2BlokkenAfstand(Coordinaat origin, Coordinaat bestemming){
        // Bereken het verschil tussen de 2 coordinaten
        int xDiff = Math.abs(origin.x -  bestemming.x);
        int yDiff = Math.abs(origin.y - bestemming.y);

        return (xDiff + yDiff) >= 2 && (xDiff != 1 && yDiff != 1);
//        if (xDiff == 2 && yDiff == 0 || xDiff == 2 && yDiff == 2|| xDiff == 1 && yDiff == 2|| xDiff == 2 && yDiff == 1) {
//            return true;
//        } else if (xDiff == 0 && yDiff == 2) {
//            return true;
//        } else {
//            return false;
//        }
    }

    /**
     * checkt of een speler die hij meektijg nog een zet kan doen in het veld
     * @param spelerOmTeChecken de speler waarvan hij het moet checken
     * @return true als hij spel over is, false als er nog een zet is
     */
    public boolean kijkenOfHetSpelOverIs(int spelerOmTeChecken) {
        ArrayList<Coordinaat> alleVakjeVanSpeler = new ArrayList<>();
        for (int i = 0; i < GameController.gameRules.GROOTTE; i++) {
            for (int j = 0; j < GameController.gameRules.GROOTTE; j++) {
                if (GameController.gameRules.speelBord.getWaarde(new Coordinaat(i,j)) == spelerOmTeChecken)
                    alleVakjeVanSpeler.add(new Coordinaat(i,j));
            }
        }
        for (Coordinaat c : alleVakjeVanSpeler) {
            //verdubbelen N,E,Z,W FIXED
            if (verdubbelNESW(c)) {
                return false;
            }
            //verdubbel NE,ZE,ZW,NW FIXED
            if (verdubbelNEZEZWNW(c)) {
                return false;
            }
            //springen N,E,Z,W FIXED
            if (springenNESW(c)) {
                return false;
            }
            //springen NE,ZE,ZW,NW FIXED
            if (springenNESESWNW(c)) {
                return false;
            }
            //springen NNE,ENE,EZE,ZZE,ZZW,WZW,WNW,NNW
            if (springenAlsPaard(c)) {
                return false;
            }
        }
        System.out.println("het is true");
        return true;
    }

    /**
     * checkt of er nog een move als hij horizontaal of verticaal verdubbeld
     * @param c coordinaat van waar hij begint
     * @return true/false of er een zet mogelijk is
     */
    public boolean verdubbelNEZEZWNW(Coordinaat c) {
        return magDeDuplicatieVoorkomen(c, c.voegToeXY(1,-1))||magDeDuplicatieVoorkomen(c, c.voegToeXY(1,1))||
                magDeDuplicatieVoorkomen(c, c.voegToeXY(-1,1))||magDeDuplicatieVoorkomen(c, c.voegToeXY(-1,-1));
    }

    /**
     * checkt of er nog een move als hij diagonaal verdubbeld
     * @param c coordinaat van waar hij begint
     * @return true/false of er een zet mogelijk is
     */
    public boolean verdubbelNESW(Coordinaat c) {
        return magDeDuplicatieVoorkomen(c, c.voegToeY(-1)) || magDeDuplicatieVoorkomen(c, c.voegToeX(1)) ||
                magDeDuplicatieVoorkomen(c, c.voegToeY(1)) || magDeDuplicatieVoorkomen(c, c.voegToeX(-1));
    }

    /**
     * checkt of er nog een move als hij horizontaal of verticaal springt
     * @param c coordinaat van waar hij begint
     * @return true/false of er een zet mogelijk is
     */
    public boolean springenNESW(Coordinaat c) {
        return magDeVerplaatsingVoorkomen(c, c.voegToeY(-2))||magDeVerplaatsingVoorkomen(c, c.voegToeX(2))||
                magDeVerplaatsingVoorkomen(c, c.voegToeY(2))||magDeVerplaatsingVoorkomen(c, c.voegToeX(-2));
    }

    /**
     * checkt of er nog een move als hij springt diagonaal
     * @param c coordinaat van waar hij begint
     * @return true/false of er een zet mogelijk is
     */
    public boolean springenNESESWNW(Coordinaat c) {
        return magDeVerplaatsingVoorkomen(c, c.voegToeXY(2,-2))||magDeVerplaatsingVoorkomen(c, c.voegToeXY(2,2))||
                magDeVerplaatsingVoorkomen(c, c.voegToeXY(-2, 2))||magDeVerplaatsingVoorkomen(c, c.voegToeXY(-2,-2));
    }

    /**
     * checkt of er nog een move als hij springt als een paard
     * @param c coordinaat van waar hij begint
     * @return true/false of er een zet mogelijk is
     */
    public boolean springenAlsPaard(Coordinaat c) {
        return magDeVerplaatsingVoorkomen(c, c.voegToeXY(1,-2))||magDeVerplaatsingVoorkomen(c, c.voegToeXY(2,-1))||
                magDeVerplaatsingVoorkomen(c, c.voegToeXY(2,1))||magDeVerplaatsingVoorkomen(c, c.voegToeXY(1,2))||
                magDeVerplaatsingVoorkomen(c, c.voegToeXY(-1,2))||magDeVerplaatsingVoorkomen(c, c.voegToeXY(-2,1))||
                magDeVerplaatsingVoorkomen(c, c.voegToeXY(-2,-1))||magDeVerplaatsingVoorkomen(c, c.voegToeXY(-1,-2));
    }

    /**
     * dit is een hulpmethode om een diepe clone te maken van een array
     * @param original de orginele array
     * @return de gekopierde array
     */
    public static int[][] diepeCopy(int[][] original) {
        int[][] clone = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            clone[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return clone;
    }

    public ArrayList<Coordinaat> vindDeMovesDubNESW(Coordinaat c) {
        ArrayList<Coordinaat> out = new ArrayList<>();
        if (magDeDuplicatieVoorkomen(c, c.voegToeY(-1)))
            out.add(c.voegToeY(-1));
        if (magDeDuplicatieVoorkomen(c, c.voegToeX(1)))
            out.add(c.voegToeX(1));
        if (magDeDuplicatieVoorkomen(c, c.voegToeY(1)))
            out.add(c.voegToeY(1));
        if (magDeDuplicatieVoorkomen(c, c.voegToeX(-1)))
            out.add(c.voegToeX(-1));
        return out;
    }

    public int kijkHoeveelGeinfecteerdKunnenWorden(Coordinaat coor, int spelerDieErZouStaan) {
        int hoeveel = 0;
        for (int xj = -1; xj <= 1; xj++) {
            for(int yi = -1; yi<=1;yi++){
                int waarde = GameController.gameRules.speelBord.getWaarde(new Coordinaat(coor.x+xj, coor.y+yi));
                if (waarde == -1) continue;
                if (waarde != 0 && waarde != spelerDieErZouStaan)
                    hoeveel++;
            }
        }
        return hoeveel;
    }
}