package it.walle.pokemongoosegame.utils;

public class TypeDrawableNotFoundException extends Throwable {
    public TypeDrawableNotFoundException(String name) {
        super("Type drawable not found with name " + name);
    }
}
