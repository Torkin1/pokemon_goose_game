package it.walle.pokemongoosegame.entity.board;

import androidx.annotation.NonNull;

public abstract class BoardSettings {
    @NonNull
    private String name;    // Board setups can be stored with different names, i.e. defaultSettings, allRandomSettings, mySettings, ...

    public BoardSettings (@NonNull String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
