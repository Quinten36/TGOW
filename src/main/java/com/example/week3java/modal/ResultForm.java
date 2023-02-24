package com.example.week3java.modal;

public class ResultForm {
    public Coordinaat beginPunt;
    public Coordinaat eindPunt;
    public int score = -5;

    public ResultForm(Coordinaat beginPunt, Coordinaat beindPunt, int score) {
        this.beginPunt = beginPunt;
        this.eindPunt = beindPunt;
        this.score = score;
    }
}
