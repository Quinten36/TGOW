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
        if (onderste == null) {
            onderste = ding;
            bovenste = ding;
        } else {
            var ouderBovenste = bovenste;
            ding.vorige = ouderBovenste;
            bovenste = ding;
            ouderBovenste.volgende = ding;
        }
    }

    public stapelItem pakBovensteDing() {
        if (bovenste == null) return null;
        var oudeBovenste = bovenste;
        bovenste = oudeBovenste.vorige;
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
