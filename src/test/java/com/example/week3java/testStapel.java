package com.example.week3java;

import com.example.week3java.modal.Stapel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testStapel {
    @Test
    void demoTestMethod() {
        assertTrue(true);
    }

    @Test
    void checkIfStapelIsLeeg() {
        Stapel s = new Stapel();
        assertNull(s.onderste);
        assertNull(s.bovenste);
    }

    @Test
    void IfEenItemDanBovensteEnOndersteGelijk() {
        Stapel s = new Stapel();
        s.legNeer(15);
        assertEquals(s.bovenste.krijgWaarde(), 15);
        assertEquals(s.onderste.krijgWaarde(), 15);
    }

    @Test
    void PakAlsErMaar1ItemOpDeStackIs() {
        Stapel s = new Stapel();
        s.legNeer(15);
        var t = s.pakBovensteDing();
        assertEquals(t.krijgWaarde(),15);
        assertNull(s.bovenste);
    }

    @Test
    void meerdereVerschillendeItemsInDeStack() {
        Stapel s = new Stapel();
        s.legNeer(15);
        s.legNeer("Boe");
        var t = s.pakBovensteDing();
        assertEquals(t.krijgWaarde(),"Boe");
        t = s.pakBovensteDing();
        assertEquals(t.krijgWaarde(),15);
        s.legNeer("Shrik");
        t = s.pakBovensteDing();
        assertEquals(t.krijgWaarde(),"Schrik");
    }

    @Test
    void PakMeerdereItemsVanDeStapel() {
        Stapel s = new Stapel();
        s.legNeer(15);
        s.legNeer(16);
        s.legNeer(17);
        s.legNeer(18);
        s.pakBovensteDing();
        s.pakBovensteDing();
        assertEquals(s.krijgWaardeBovenste(), 16);
    }

    @Test
    public void CheckOfJeDeStapelKanVullenLegenEnWeerVullen() {
        Stapel s = new Stapel();
        s.legNeer(15);
        s.pakBovensteDing();
        s.legNeer(18);
        assertEquals(s.krijgWaardeBovenste(), 18);
    }
}