package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

public class StatPointer {
    private String name; //nome della stat
    private String url; //url completo della stat

    public StatPointer(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
