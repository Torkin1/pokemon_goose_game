package it.walle.pokemongoosegame.entity;

public class PlayerNotInGameException extends RuntimeException {
    public PlayerNotInGameException(String username) {
        super("Player not in game: " + username);
    }
}
