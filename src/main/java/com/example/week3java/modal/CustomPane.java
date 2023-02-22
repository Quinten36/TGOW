package com.example.week3java.modal;

import com.example.week3java.controller.GameController;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CustomPane extends Pane implements VeldUpdater {
    public Pane vak;
    public Rectangle kleurAchtergrond;
    private Coordinaat coor;
    public int waarde;
    public Text waardeText;
    public boolean geselecteerd = false;

    //TODO: waarde weghalen?
    /**
     * Maak een custom pane aan met een reactangle als achtergrond kleur en een text met de speler wie het vakje bezit
     * @param width hoe breed het vakje moet zijn
     * @param height hoe hoog het vakje moet zijn
     * @param waarde welke waarde/kleur het vakje moet zijn
     */
    public CustomPane(int width, int height, int waarde, Coordinaat coor) {
        this.vak = new Pane();
        this.kleurAchtergrond = new Rectangle(width, height);
        this.coor = coor;
        this.waarde = waarde;
        this.kleurAchtergrond.setFill(bepaalKleur());
        this.waardeText = new Text(""+(char) this.waarde);
        //positioneer het vakje in het midden
        this.waardeText.setTranslateX(15.0);
        this.waardeText.setTranslateY(25.0);
        this.vak.getChildren().addAll(this.kleurAchtergrond, this.waardeText);
    }

    /**
     * Bepaal de kleur en geef die terug
     * @return geeft de kleur terug van de speler
     */
    private Color bepaalKleur() {
        Color kleur = Color.WHITE;// zet de kleur op wit voor een leeg vult
        if (this.waarde == (int) GameLogica.maximaleSpelers[0])
            kleur = Color.LIGHTGREEN; // et de kleur op groen als het de waarde 102 (B) heeft
        if (this.waarde == (int) GameLogica.maximaleSpelers[1])
            kleur = Color.RED; // zet de kleur op rood als het de waarde 110 (H) heeft
        if (this.waarde == (int) GameLogica.maximaleSpelers[2])
            kleur = Color.LIGHTGREEN; // et de kleur op groen als het de waarde 102 (B) heeft
        if (this.waarde == (int) GameLogica.maximaleSpelers[3])
            kleur = Color.LIGHTBLUE; // zet de kleur op rood als het de waarde 110 (H) heeft
        return kleur;
    }

    /**
     * Update de tekst in een vakje
     */
    private void updateDeTekst() {
        this.waarde = GameController.gameRules.speelBord.getWaarde(coor);
        this.waardeText.setText(this.waarde == 0 ? "" : ""+(char) this.waarde);
    }

    /**
     * update de kleur van een vakje
     */
    private void updateDeKleur() {
        this.kleurAchtergrond.setFill(bepaalKleur());
    }

    /**
     * Geeft het vakje terug
     * @return geef het vakje terug
     */
    public Pane getPane() {
        return this.vak;
    }

    /**
     * De methode die wordt aangeroepen om alle observers een update te geven
     * @param pane de pane die geupdate moet worden qua highlight
     */
    @Override
    public void update(CustomPane pane) {
        if (pane.geselecteerd) {
            kleurAchtergrond.setFill(Color.LIGHTGREY);
        } else {
            updateDeKleur();
            updateDeTekst();
        }
    }
}
