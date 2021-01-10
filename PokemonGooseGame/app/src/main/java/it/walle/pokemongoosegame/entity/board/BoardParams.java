package it.walle.pokemongoosegame.entity.board;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity()
public abstract class BoardParams {

    @PrimaryKey
    @NonNull
    private String name;    // Board setups can be stored with different names, i.e. defaultSettings, allRandomSettings, mySettings, ...

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
