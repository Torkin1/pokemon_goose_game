package it.walle.pokemongoosegame.game;


public class NoGameSetupException extends Throwable {
    public NoGameSetupException() {
        super("No game setup. Call GameController.setupGame to setup a new Game instance");
    }
}
