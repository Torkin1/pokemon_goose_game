package it.walle.pokemongoosegame.settings;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public class WhatYellowCellStartingIndex {
    @PrimaryKey @NonNull
    private String boardSettingsName;           // Name of the boardSettings associated object
    private int index;                          // The starting index

    public String getBoardSettingsName() {
        return boardSettingsName;
    }

    public void setBoardSettingsName(String boardSettingsName) {
        this.boardSettingsName = boardSettingsName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
