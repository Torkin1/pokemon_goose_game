package it.walle.pokemongoosegame.entity;

public class Player {

    private String username;
    private Pokemon pokemon;

    public Player(){
        this.username = null;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }
}
