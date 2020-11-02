package it.walle.pokemongoosegame.boardfactory;

public class IndexUnavailableException extends Exception{

    public IndexUnavailableException(){}

    public IndexUnavailableException(Exception e) {
        super(e.getMessage(), e);
    }
}
