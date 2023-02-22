package com.example.week3java.modal;

public class Booord {
    private int[][] speelBooordt = new int[7][7]; // maak een speelboard van 7 bij 7

    /**
     * Maak een leeg bord aan
     */
    public Booord() {
        // vul het speelboord met lege data
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
                speelBooordt[i][j] = 0;
    }

    /**
     * geeft een copy terug van het speelboard. Zonder dat je van waar je het aanroep de velden kan aanpassen
     * @return een copy van het speelboard
     */
    public int[][] getSpeelBooordt() {
        int [][] copy = speelBooordt.clone();
        return copy;
    }

    /**
     * geeft de waarde terug van het vakje
     * @param x de x coordinaat van het vakje
     * @param y de y coordinaat van het vakje
     * @return de waarde die het vakje heeft
     */
    public int getWaarde(Coordinaat coor) {
        return speelBooordt[coor.x][coor.y];
    }

    /**
     * update de waarde van een specifiek vakje
     * @param x de x coordinaat van het vakje
     * @param y de y coordinaat van het vakje
     * @param waarde de waarde die in het vakje moet komen te staan
     */
    public void setWaarde(Coordinaat coor, int waarde) {
        speelBooordt[coor.x][coor.y] = waarde;
    }

    /**
     * Kijkt of de waarde die hij meekrijg overeen komt met de waarde op een specifiek vakje
     * @param x de x coordinaat van het vakje
     * @param y de y coordinaat van het vakje
     * @param waarde de waarde waarmee hij het moet vergelijken
     * @return retourneert de waar of niet waar als de waarde overeenkomt met de waarde van het vakje
     */
    public boolean checkWaarde(Coordinaat coor, int waarde) {
        return speelBooordt[coor.x][coor.y] == waarde;
    }
}
