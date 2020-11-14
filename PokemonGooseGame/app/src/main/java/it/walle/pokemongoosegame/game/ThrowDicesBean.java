package it.walle.pokemongoosegame.game;

public class ThrowDicesBean {
    private int numOfDices; // How many dices need to be thrown
    private int numOfFaces; // How many faces each dice must have

    public int getNumOfDices() {
        return numOfDices;
    }

    public void setNumOfDices(int numOfDices) {
        this.numOfDices = numOfDices;
    }

    public int getNumOfFaces() {
        return numOfFaces;
    }

    public void setNumOfFaces(int numOfFaces) {
        this.numOfFaces = numOfFaces;
    }
}
