package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

import it.walle.pokemongoosegame.entity.pokeapi.type.TypePointer;

public class TypePointerPokemon {
    private int slot;
    private TypePointer type;

    public TypePointer getType() {
        return type;
    }

    public void setType(TypePointer type) {
        this.type = type;
    }
}
