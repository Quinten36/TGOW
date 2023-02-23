package com.example.week3java.modal;

import com.example.week3java.controller.GameController;

import java.util.ArrayList;
import java.util.Arrays;

public class HulpFunc {
    private GameLogica rules;

    public HulpFunc(GameLogica rules) {
        this.rules = rules;
    }

    public boolean magDeDuplicatieVoorkomen(int xNu, int yNu, int xMove, int yMove){
        return
            !isLeeg(xNu, yNu)
            &&
            isLeeg(xMove,yMove)
            &&
            isSchuin(xNu,yNu,xMove,yMove);
    }

    private boolean isLeeg(int x, int y) {
        try {
            int[][] veld = rules.speelBord.getSpeelBooordt();
            return veld[x][y]==0;
        } catch (Exception e) {
            return false;
        }
    }

    //check aanliggend (ook schuin)
    private boolean isSchuin(int x1, int y1, int x2, int y2){
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        //TODO: optimze de code
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1) || (dx == 1 && dy == 1);
    }

    public boolean magDeVerplaatsingVoorkomen(int xNu, int yNu, int xMove, int yMove){
        return
            !isLeeg(xNu, yNu)
                &&
                isLeeg(xMove,yMove)
                &&
                isOp2BlokkenAfstand(xNu,yNu,xMove,yMove);
    }

    private boolean isOp2BlokkenAfstand(int x1, int y1, int x2, int y2){
        // Bereken het verschil tussen de 2 coordinaten
        int xDiff = Math.abs(x1 - x2);
        int yDiff = Math.abs(y1 - y2);

        // check of het verschil wel echt 2 is. en niet stiekem 1.
        if (xDiff == 2 && yDiff == 0 || xDiff == 2 && yDiff == 2|| xDiff == 1 && yDiff == 2|| xDiff == 2 && yDiff == 1) {
            return true;
        } else if (xDiff == 0 && yDiff == 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean kijkenOfHetSpelOverIs(int spelerOmTeChecken) {
        ArrayList<Coordinaat> alleVakjeVanSpeler = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (GameController.gameRules.speelBord.getWaarde(new Coordinaat(i,j)) == spelerOmTeChecken)
                    alleVakjeVanSpeler.add(new Coordinaat(i,j));
            }
        }
        for (Coordinaat c : alleVakjeVanSpeler) {
            //verdubbelen N,E,Z,W FIXED
            if (magDeDuplicatieVoorkomen(c.x, c.y, c.x, c.y-1)||magDeDuplicatieVoorkomen(c.x, c.y, c.x+1, c.y)||
                    magDeDuplicatieVoorkomen(c.x, c.y, c.x, c.y+1)||magDeDuplicatieVoorkomen(c.x, c.y, c.x-1, c.y))
                return false;
            //verdubbel NE,ZE,ZW,NW FIXED
            if (magDeDuplicatieVoorkomen(c.x, c.y, c.x+1, c.y-1)||magDeDuplicatieVoorkomen(c.x, c.y, c.x+1, c.y+1)||
                    magDeDuplicatieVoorkomen(c.x, c.y, c.x-1, c.y+1)||magDeDuplicatieVoorkomen(c.x, c.y, c.x-1, c.y-1))
                return false;
            //springen N,E,Z,W FIXED
            if (magDeVerplaatsingVoorkomen(c.x, c.y, c.x, c.y-2)||magDeVerplaatsingVoorkomen(c.x, c.y, c.x+2, c.y)||
                    magDeVerplaatsingVoorkomen(c.x, c.y, c.x, c.y+2)||magDeVerplaatsingVoorkomen(c.x, c.y, c.x-2, c.y))
                return false;
            //springen NE,ZE,ZW,NW FIXED
            if (magDeVerplaatsingVoorkomen(c.x, c.y, c.x+2, c.y-2)||magDeVerplaatsingVoorkomen(c.x, c.y, c.x+2, c.y+2)||
                    magDeVerplaatsingVoorkomen(c.x, c.y, c.x-2, c.y-2)||magDeVerplaatsingVoorkomen(c.x, c.y, c.x-2, c.y-2))
                return false;
            //springen NNE,ENE,EZE,ZZE,ZZW,WZW,WNW,NNW
            if (magDeVerplaatsingVoorkomen(c.x, c.y, c.x+1, c.y-2)||magDeVerplaatsingVoorkomen(c.x, c.y, c.x+2, c.y-1)||
                    magDeVerplaatsingVoorkomen(c.x, c.y, c.x+2, c.y+1)||magDeVerplaatsingVoorkomen(c.x, c.y, c.x+1, c.y+2)||
                    magDeVerplaatsingVoorkomen(c.x, c.y, c.x-1, c.y+2)||magDeVerplaatsingVoorkomen(c.x, c.y, c.x-2, c.y+1)||
                    magDeVerplaatsingVoorkomen(c.x, c.y, c.x-2, c.y-1)||magDeVerplaatsingVoorkomen(c.x, c.y, c.x-1, c.y-2))
                return false;
        }
        return true;
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
}