package com.cokeappingo.class_utile;

public class generateur {
    int pack_number;
    int current_place_available;

    public generateur(){}

    public generateur(int pack_number, int current_place_available) {
        this.pack_number = pack_number;
        this.current_place_available = current_place_available;
    }

    public int getPack_number() {
        return pack_number;
    }

    public void setPack_number(int pack_number) {
        this.pack_number = pack_number;
    }

    public int getCurrent_place_available() {
        return current_place_available;
    }

    public void setCurrent_place_available(int current_place_available) {
        this.current_place_available = current_place_available;
    }
}
