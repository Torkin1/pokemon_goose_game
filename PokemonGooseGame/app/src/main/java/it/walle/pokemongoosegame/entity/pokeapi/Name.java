package it.walle.pokemongoosegame.entity.pokeapi;

public class Name {
    private String name;                    // Name of type
    private LanguagePointer language;       // Language of the name

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LanguagePointer getLanguage() {
        return language;
    }

    public void setLanguage(LanguagePointer language) {
        this.language = language;
    }
}
