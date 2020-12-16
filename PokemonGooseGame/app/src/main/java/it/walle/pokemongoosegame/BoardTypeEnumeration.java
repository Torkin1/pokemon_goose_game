package it.walle.pokemongoosegame;

import it.walle.pokemongoosegame.game.ThrowDicesBean;

public enum BoardTypeEnumeration {
    PG("Procedurally generated"),
    ;

    private String type;

    BoardTypeEnumeration(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
