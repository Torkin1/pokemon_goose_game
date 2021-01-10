package it.walle.pokemongoosegame.activities.addplayer;

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
