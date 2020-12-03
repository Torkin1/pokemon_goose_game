package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

public class Stats {
    private int base_stat; //valore base della stat
    private StatInformations stat;

    public Stats(){}

    public int getBase_stat() {
        return base_stat;
    }

    public void setBase_stat(int base_stat) {
        this.base_stat = base_stat;
    }

    public StatInformations getStat() {
        return stat;
    }

    public void setStat(StatInformations stat) {
        this.stat = stat;
    }
}
