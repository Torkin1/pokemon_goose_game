package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

import it.walle.pokemongoosegame.entity.pokeapi.EntityPointer;

public class Stats {
    private int base_stat; //valore base della stat
    private EntityPointer stat;

    public Stats(){}

    public int getBase_stat() {
        return base_stat;
    }

    public void setBase_stat(int base_stat) {
        this.base_stat = base_stat;
    }

    public EntityPointer getStat() {
        return stat;
    }

    public void setStat(EntityPointer stat) {
        this.stat = stat;
    }
}
