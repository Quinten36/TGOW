package com.example.week3java;

import com.example.week3java.controller.GameController;
import com.example.week3java.modal.Coordinaat;
import com.example.week3java.modal.CustomPane;
import com.example.week3java.modal.GameLogica;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testCustomPane {
    @Test
    void demoTestMethod() {
        assertTrue(true);
    }

    @Test
    public void checkOfKleurJuistWordtBepaald() {
        GameLogica loc = new GameLogica();
        CustomPane pane = new CustomPane(40,40,66, new Coordinaat(0,0));
        loc.speelBord.setWaarde(new Coordinaat(0,0),66);
        CustomPane pane2 = new CustomPane(40,40,72, new Coordinaat(1,1));
        loc.speelBord.setWaarde(new Coordinaat(1,1),72);

        Color kleur = pane.bepaalKleur();
        Color kleur2 = pane2.bepaalKleur();

        assertEquals(Color.LIGHTGREEN, kleur);
        assertEquals(Color.RED, kleur2);
    }

    @Test
    public void checkOfDeTekstWordtAangepast() {
        GameLogica loc = new GameLogica();
        GameController.gameRules = loc;
        CustomPane pane = new CustomPane(40,40,66, new Coordinaat(0,0));
        loc.speelBord.setWaarde(new Coordinaat(0,0),66);

        pane.updateDeTekst();
        String c = pane.waardeText.getText();

        loc.speelBord.setWaarde(new Coordinaat(0,0),72);
        pane.updateDeTekst();
        String c2 = pane.waardeText.getText();

        assertEquals("B", c);
        assertEquals("H", c2);
    }
}
