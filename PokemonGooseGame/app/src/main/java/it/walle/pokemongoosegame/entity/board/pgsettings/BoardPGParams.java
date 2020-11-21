package it.walle.pokemongoosegame.entity.board.pgsettings;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import it.walle.pokemongoosegame.boardfactory.BoardParams;

@Entity(inheritSuperIndices = true)
public class BoardPGParams extends BoardParams {

    private int numCells;                 // Number of cells on the board
    private int yellowCellDelta;          // The distance from two yellow cells

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
}
