package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

import it.walle.pokemongoosegame.entity.pokeapi.EntityPointer;

public class TypePointerPokemon {
    private int slot;
    private EntityPointer type;

    public EntityPointer getType() {
        return type;
    }

    public void setType(EntityPointer type) {
        this.type = type;
    }
}
