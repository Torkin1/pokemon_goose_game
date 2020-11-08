package it.walle.pokemongoosegame.game;

public class BoardFactoryCreationFailureException extends Throwable {
    public BoardFactoryCreationFailureException(Exception e) {
        super("Failed to add board: " + e.getMessage(), e);
    }
}
