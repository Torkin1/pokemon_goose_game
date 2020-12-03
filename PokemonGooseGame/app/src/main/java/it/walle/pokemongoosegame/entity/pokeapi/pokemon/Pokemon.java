package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

import it.walle.pokemongoosegame.entity.pokeapi.type.TypePointer;

public class Pokemon{

    private int id;
    private String name;
    private Stats[] stats;
    private TypePointerPokemon[] types;

    public Pokemon(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stats[] getStats() {
        return stats;
    }

    public void setStats(Stats[] stats) {
        this.stats = stats;
    }

    public TypePointerPokemon[] getTypes() {
        return types;
    }

    public void setTypes(TypePointerPokemon[] types) {
        this.types = types;
    }
}
