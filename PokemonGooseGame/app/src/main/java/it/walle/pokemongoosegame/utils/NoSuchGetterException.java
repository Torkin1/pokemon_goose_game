package it.walle.pokemongoosegame.utils;

public class NoSuchGetterException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String attrName;

    public NoSuchGetterException(String attrName) {
        super("no such getter for attribute " + attrName);//should be stored in strings
        this.attrName = attrName;
    }

    public String getAttrName() {
        return this.attrName;
    }
}
