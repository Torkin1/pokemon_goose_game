package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

public enum StatsEnumeration {
    HP("hp"),
    ;

    private String stat;

    StatsEnumeration(String stat) {
        this.stat = stat;
    }

    public String getStat(){
        return stat;
    }

}
