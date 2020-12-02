package it.walle.pokemongoosegame.volley;

public enum PokeAPIVersion {

    V2 (2),
    ;

    private int version;

    PokeAPIVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
