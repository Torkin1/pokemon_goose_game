package it.walle.pokemongoosegame.game;

import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;

public class AddNewPlayerBean {
    private String playerUsername;  // The player username
    private Pokemon pokemon;          // The pokemon id

    //setters and getters
    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }
}
