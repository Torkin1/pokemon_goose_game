package it.walle.pokemongoosegame.selectPokemon;

import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.pokeapi.Pokemon;

public class SelectPokemonBean {
    private Player player;
    private String pokemonChoose;
    private Pokemon pokemon;

    public void setPlayer(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void setPokemonChoose(String pokemonChoose){
        this.pokemonChoose = pokemonChoose;
    }

    public String getPokemonChoose(){
        return this.pokemonChoose;
    }

    public void setPokemon(Pokemon pokemon){
        this.pokemon = pokemon;
    }

    public Pokemon getPokemon(){
        return this.pokemon;
    }
}
