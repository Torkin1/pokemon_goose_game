package it.walle.pokemongoosegame.entity.board.pgsettings;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity (primaryKeys = {"boardSettingsName", "yellowEffectClassName"})
public class WhatYellowEffectName {
    @NonNull
    private String boardSettingsName;       // BoardSettings name which this entity refers
    @NonNull
    private String yellowEffectClassName;   // The name of the class of the corresponding effect

    public String getBoardSettingsName() {
        return boardSettingsName;
    }

    public void setBoardSettingsName(String boardSettingsName) {
        this.boardSettingsName = boardSettingsName;
    }

    @NonNull
    public String getYellowEffectClassName() {
        return yellowEffectClassName;
    }

    public void setYellowEffectClassName(@NonNull String yellowEffectClassName) {
        this.yellowEffectClassName = yellowEffectClassName;
    }
}
