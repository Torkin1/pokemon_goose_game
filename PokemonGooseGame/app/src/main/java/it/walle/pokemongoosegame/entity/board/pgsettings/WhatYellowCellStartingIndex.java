package it.walle.pokemongoosegame.entity.board.pgsettings;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"boardSettingsName", "index"})
public class WhatYellowCellStartingIndex {
    @NonNull
    private String boardSettingsName;           // Name of the boardSettings associated object
    @NonNull
    private int index;                          // The starting index

    //the method should be anothed with nonNull, checkinf for bug from last update
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
