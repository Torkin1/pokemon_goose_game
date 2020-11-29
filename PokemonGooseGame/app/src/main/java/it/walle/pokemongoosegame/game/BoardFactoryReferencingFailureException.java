package it.walle.pokemongoosegame.game;

public class BoardFactoryReferencingFailureException extends Throwable {
    public BoardFactoryReferencingFailureException(Exception e) {
        super("Failed to add board: " + e.getMessage(), e);
    }
}
