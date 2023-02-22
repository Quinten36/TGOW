package com.example.week3java.modal;

public class Stapel {
    public stapelItem onderste;
    public stapelItem bovenste;

    public Stapel() {
        onderste = null;
        bovenste = null;
    }

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

    public stapelItem pakBovensteDing() {
        if (onderste == null)
            return null;
        var oudeBovenste = bovenste;
        bovenste = oudeBovenste.vorige;
        //TODO: kijken of dit goed is en testen
        if (bovenste == null) {
            this.bovenste = onderste;
        }
        return oudeBovenste;
    }

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
