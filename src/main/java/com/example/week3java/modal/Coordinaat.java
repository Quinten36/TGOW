package com.example.week3java.modal;

public class Coordinaat {
    public int x;
    public int y;

    public Coordinaat(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinaat voegToeX(int c) {
        return new Coordinaat(this.x+c, this.y);
    }

    public Coordinaat voegToeY(int c) {
        return new Coordinaat(this.x, this.y+c);
    }

    public Coordinaat voegToeXY(int x, int y) {
        return new Coordinaat(this.x+x, this.y+y);
    }
}
