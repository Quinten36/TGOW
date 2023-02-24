package com.example.week3java.modal;

public class Booord {
    private int[][] speelBooordt; // maak een speelboard van 7 bij 7
    private int GROOTTE;

    /**
     * Maak een leeg bord aan
     */
    public Booord(int size) {
        speelBooordt = new int[size][size];
        this.GROOTTE = size;
        // vul het speelboord met lege data
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
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
     * geeft de waarde terug van het vakje mits het binnen het bord ligt
     * @param coor de x en y coordinaat van het vakje
     * @return de waarde die het vakje heeft. -1 als de coordinaten niet kloppen
     */
    public int getWaarde(Coordinaat coor) {
        if (coor.x >= 0 && coor.y >= 0 && coor.x < GROOTTE && coor.y < GROOTTE)
            return speelBooordt[coor.x][coor.y];
        else
            return -1;
    }

    /**
     * update de waarde van een specifiek vakje mits de coordinatie erbinnen vallen
     * @param coor de x and y coordinaat van het vakje
     * @param waarde de waarde die in het vakje moet komen te staan
     */
    public void setWaarde(Coordinaat coor, int waarde) {
        if (coor.x >= 0 && coor.y >= 0 && coor.x < GROOTTE && coor.y < GROOTTE)
            speelBooordt[coor.x][coor.y] = waarde;
    }

    /**
     * Kijkt of de waarde die hij meekrijg overeen komt met de waarde op een specifiek vakje. Als de coordinaten er buiten vallen is het altijd false
     * @param coor de x en y coordinaat van het vakje
     * @param waarde de waarde waarmee hij het moet vergelijken
     * @return retourneert de waar of niet waar als de waarde overeenkomt met de waarde van het vakje. Als de coordinaten te groot zijn is het altijd false
     */
    public boolean checkWaarde(Coordinaat coor, int waarde) {
        if (coor.x >= 0 && coor.y >= 0 && coor.x < GROOTTE && coor.y < GROOTTE)
            return speelBooordt[coor.x][coor.y] == waarde;
        else
            return false;
    }
}
