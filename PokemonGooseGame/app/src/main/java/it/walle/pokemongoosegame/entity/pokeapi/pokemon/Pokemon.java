package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

public class Pokemon{

    private int id;
    private int currentHp;                  // Remaining pokemon health
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

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }
}
