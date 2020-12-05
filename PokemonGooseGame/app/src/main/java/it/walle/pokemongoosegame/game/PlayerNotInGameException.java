package it.walle.pokemongoosegame.game;

public class PlayerNotInGameException extends RuntimeException {
    public PlayerNotInGameException(String username) {
        super("Player not in game: " + username);
    }
}
