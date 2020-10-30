package it.walle.pokemongoosegame.gamefactory;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.entity.Game;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.Pokemon;

public class GameFactoryBean {

    private final List<Player> players = new ArrayList<>();
    private Game game;

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(String playerName, String pokemonName){

        // Creates a Player object with player name
        Player player = new Player();
        player.setName(playerName);

        // Creates a Pokemon object with pokemon name and saves its reference into the Player object
        Pokemon pokemon = new Pokemon();
        pokemon.setName(pokemonName);
        player.setPokemon(pokemon);

        // Adds the player to the players list
        this.players.add(player);
    }

    public Game getGame(){
        return this.game;
    }
}
