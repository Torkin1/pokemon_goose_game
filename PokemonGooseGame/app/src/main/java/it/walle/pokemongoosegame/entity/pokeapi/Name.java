package it.walle.pokemongoosegame.entity.pokeapi;

public class Name {
    private String name;                  // Name of type
    private EntityPointer language;       // Language of the name

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityPointer getLanguage() {
        return language;
    }

    public void setLanguage(EntityPointer language) {
        this.language = language;
    }
}
