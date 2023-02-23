package com.example.week3java.modal;

public class Stapel {
    public stapelItem onderste;
    public stapelItem bovenste;

    public Stapel() {
        onderste = null;
        bovenste = null;
    }

    /**
     * Leg een item neer op de item. met de check of de stapel leeg is of niet
     * @param item het item wat hij moet neerleggen
     */
    public void legNeer(Object item) {
        stapelItem ding = new stapelItem(item);
        if (this.onderste == null) {
            this.onderste = ding;
            this.bovenste = ding;
        } else {
            var onderBovenste = bovenste;
            ding.vorige = onderBovenste;
            bovenste = ding;
            onderBovenste.volgende = ding;
        }
    }

    /**
     * Deze pakt het bovenste item/bord en geef die aan degene die erom vraag
     * @return geeft het bovenste item terug
     */
    public stapelItem pakBovensteDing() {
        if (onderste == null)
            return null;
        var oudeBovenste = bovenste;
        bovenste = oudeBovenste.vorige;
        if (bovenste == null) {
            this.bovenste = onderste;
        }
        return oudeBovenste;
    }

    /**
     * verkrijg de bovenste waarde zonder hem te pakken
     * @return kijk de waarde van bovenste bord
     */
    public Object krijgWaardeBovenste() {
        return bovenste.krijgWaarde();
    }

    public class stapelItem {
        private final Object waarde;
        public stapelItem volgende;
        public stapelItem vorige;

        public stapelItem(Object waarde) {
            this.waarde = waarde;
        }

        public Object krijgWaarde() {
            return waarde;
        }
    }
}
