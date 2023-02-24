package com.example.week3java;

import com.example.week3java.modal.Booord;
import com.example.week3java.modal.Coordinaat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class testBooord {
    @Test
    void demoTestMethod() {
        assertTrue(true);
    }

    @Test
    public void checkOfHetBordLeegIs() {
        Booord bord = new Booord(7);
        assertEquals(0, bord.getWaarde(new Coordinaat(0,0)));
        assertEquals(0, bord.getWaarde(new Coordinaat(5,1)));
        assertEquals(0, bord.getWaarde(new Coordinaat(4,3)));
    }

    @Test
    public void checkOfJeWaardesKanInvoegen() {
        Booord bord = new Booord(7);
        bord.setWaarde(new Coordinaat(3,3), 66);
        assertEquals(66, bord.getWaarde(new Coordinaat(3,3)));
    }

    @Test
    public void checkOfDeWaardeCheckHetDoet() {
        Booord bord = new Booord(7);
        bord.setWaarde(new Coordinaat(2,2), 66);
        assertTrue(bord.checkWaarde(new Coordinaat(2,2),66));
        assertFalse(bord.checkWaarde(new Coordinaat(0,0),66));
    }

    @Test
    public void checkOfJeWaardesKanInvoegenBuitenHetBord() {
        Booord bord = new Booord(7);
        bord.setWaarde(new Coordinaat(9,2), 66);
        assertEquals(-1, bord.getWaarde(new Coordinaat(9,2)));
    }
}
