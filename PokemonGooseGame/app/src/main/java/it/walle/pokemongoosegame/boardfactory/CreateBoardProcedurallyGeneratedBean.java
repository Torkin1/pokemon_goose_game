package it.walle.pokemongoosegame.boardfactory;

import it.walle.pokemongoosegame.entity.Board;

public class CreateBoardProcedurallyGeneratedBean extends CreateBoardBean{
    private int numCells;
    private int yellowCellDelta;
    private int[] yellowCellStartingIndexes;

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
