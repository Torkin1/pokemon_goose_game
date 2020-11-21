package it.walle.pokemongoosegame.entity.board.pgsettings;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (primaryKeys = {"boardSettingsName", "yellowEffectName"})
public class WhatYellowEffectName {
    @NonNull
    private String boardSettingsName;
    @NonNull
    private String yellowEffectName;

    public String getBoardSettingsName() {
        return boardSettingsName;
    }

    public void setBoardSettingsName(String boardSettingsName) {
        this.boardSettingsName = boardSettingsName;
    }

    public String getYellowEffectName() {
        return yellowEffectName;
    }

    public void setYellowEffectName(String yellowEffectName) {
        this.yellowEffectName = yellowEffectName;
    }
}
