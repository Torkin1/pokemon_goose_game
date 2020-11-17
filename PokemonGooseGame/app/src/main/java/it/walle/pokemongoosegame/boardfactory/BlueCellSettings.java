package it.walle.pokemongoosegame.boardfactory;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"boardSettingsName", "blueCellName"})
public class BlueCellSettings {
    @NonNull
    private String boardSettingsName;       // The name of the corresponding board configuration
    @NonNull
    private String blueCellName;            // The name of the corresponding blue cell
    private int boardIndex;                 // The index where the corresponding blue cell will be placed

    public String getBlueCellName() {
        return blueCellName;
    }

    public void setBlueCellName(String blueCellName) {
        this.blueCellName = blueCellName;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
    }

    public String getBoardSettingsName() {
        return boardSettingsName;
    }

    public void setBoardSettingsName(String boardSettingsName) {
        this.boardSettingsName = boardSettingsName;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getBlueCellName();
    }
}
