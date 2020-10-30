package it.walle.pokemongoosegame.boardfactory;

import androidx.annotation.NonNull;

public enum BoardType {
    PROCEDURALLY_GENERATED ("ProcedurallyGenerated");

    private String type;

    private BoardType (String type){
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return this.type;
    }
}
