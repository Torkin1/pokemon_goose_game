package it.walle.pokemongoosegame.game;

public abstract class ChangeEntityValueBean {

    // Extends this class to add key values, which shall be used to retrieve what Object has to be updated

    private int value;          // Amount to add

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

