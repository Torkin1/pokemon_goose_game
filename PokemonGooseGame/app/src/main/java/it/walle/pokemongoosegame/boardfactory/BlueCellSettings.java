package it.walle.pokemongoosegame.boardfactory;

import androidx.annotation.NonNull;

public class BlueCellSettings {
    private String blueCellClassName;
    private int boardIndex;

    public String getBlueCellClassName() {
        return blueCellClassName;
    }

    public void setBlueCellClassName(String blueCellClassName) {
        this.blueCellClassName = blueCellClassName;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getBlueCellClassName();
    }
}
