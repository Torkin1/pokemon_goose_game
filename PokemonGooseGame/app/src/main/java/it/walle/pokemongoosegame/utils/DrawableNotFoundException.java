package it.walle.pokemongoosegame.utils;

public class DrawableNotFoundException extends Throwable {
    public DrawableNotFoundException(String name) {
        super("drawable not found with name " + name);
    }
}
