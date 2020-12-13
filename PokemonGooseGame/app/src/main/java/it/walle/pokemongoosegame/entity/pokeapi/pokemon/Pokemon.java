package it.walle.pokemongoosegame.entity.pokeapi.pokemon;
public class Pokemon{

    // PokeAPI read-only fields
    private int id;
    private String name;
    private Stats[] stats;
    private TypePointerPokemon[] types;
    private Sprites sprites;

    // Dynamic fields
    private int currentHp;                  // Remaining pokemon health

    public Pokemon(){}

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

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
