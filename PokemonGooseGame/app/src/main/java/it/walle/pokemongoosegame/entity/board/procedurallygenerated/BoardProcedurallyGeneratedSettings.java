package it.walle.pokemongoosegame.entity.board.procedurallygenerated;

import androidx.annotation.NonNull;

import it.walle.pokemongoosegame.entity.board.BoardSettings;
import it.walle.pokemongoosegame.entity.board.cell.BlueCell;
import it.walle.pokemongoosegame.entity.effect.YellowEffect;

public class BoardProcedurallyGeneratedSettings extends BoardSettings {

    private int numCells;                       // Number of cells on the board
    private int yellowCellDelta;                // The distance from two yellow cells
    private int[] yellowCellStartingIndexes;    // Starting from these indexes, there will be one yellow cell every yellowCellDelta cells .
    private BlueCell[] blueCells;               // Blue cells which are to be injected in the board.
    private YellowEffect[] yellowEffect;        // Yellow effects which can be injected in the board

    public BoardProcedurallyGeneratedSettings(@NonNull String name) {
        super(name);
    }


    public BlueCell[] getBlueCells() {
        return blueCells;
    }

    public void setBlueCells(BlueCell[] blueCells) {
        this.blueCells = blueCells;
    }

    public YellowEffect[] getYellowEffect() {
        return yellowEffect;
    }

    public void setYellowEffect(YellowEffect[] yellowEffect) {
        this.yellowEffect = yellowEffect;
    }

    public int getNumCells() {
        return numCells;
    }

    public void setNumCells(int numCells) {
        this.numCells = numCells;
    }

    public int getYellowCellDelta() {
        return yellowCellDelta;
    }

    public void setYellowCellDelta(int yellowCellDelta) {
        this.yellowCellDelta = yellowCellDelta;
    }

    public int[] getYellowCellStartingIndexes() {
        return yellowCellStartingIndexes;
    }

    public void setYellowCellStartingIndexes(int[] yellowCellStartingIndexes) {
        this.yellowCellStartingIndexes = yellowCellStartingIndexes;
    }
}
