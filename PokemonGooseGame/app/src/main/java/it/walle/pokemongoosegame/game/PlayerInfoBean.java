package it.walle.pokemongoosegame.game;

import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.Pokemon;

public class PlayerInfoBean {
    private String username;    // The username of the player which we want to collect infos
    private Player player;      // The real holder of the player infos

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getMoney() {
        return player.getMoney();
    }

    public int getPokemonId(){
        return this.player.getPokemon().getId();
    }

    public int getNumOfIdleTurns() {
        return player.getNumOfIdleTurns();
    }

    public void setIdleTurns(int turns) {
        player.setIdleTurns(turns);
    }

    public Pokemon getPokemon() {
        return player.getPokemon();
    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }
}
