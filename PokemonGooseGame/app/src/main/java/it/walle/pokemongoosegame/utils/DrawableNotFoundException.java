package it.walle.pokemongoosegame.utils;

public class DrawableNotFoundException extends Throwable {
    public DrawableNotFoundException(String name) {
        super("Type drawable not found with name " + name);//should be stored in string for now it's ok
    }
}
