package it.walle.pokemongoosegame.createboardsettings;

import java.util.List;

public class CreateBoardSettingsBean {
    private int numCells;                               //Number of cells in the board
    private int yellowCellDelta;                        //Distance between yellow cells
    private String boardPGParamsName;                   //Board name
    private String boardSettingsName;
    private List<Integer> yellowCellStartingIndex;      //Starting index of the yellow cells
    private List<String> blueCellName;
    private List<Integer> blueCellIndex;
    private String[] yellowEffectClassName;         //The name of the class of the corresponding effect

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

    public String getBoardPGParamsName() {
        return boardPGParamsName;
    }

    public void setBoardPGParamsName(String boardPGParamsName) {
        this.boardPGParamsName = boardPGParamsName;
    }

    public List<Integer> getYellowCellStartingIndex() {
        return yellowCellStartingIndex;
    }

    public void setYellowCellStartingIndex(List<Integer> yellowCellStartingIndex) {
        this.yellowCellStartingIndex = yellowCellStartingIndex;
    }

    public String getBoardSettingsName() {
        return boardSettingsName;
    }

    public void setBoardSettingsName(String boardSettingsName) {
        this.boardSettingsName = boardSettingsName;
    }

    public List<String> getBlueCellName() {
        return blueCellName;
    }

    public void setBlueCellName(List<String> blueCellName) {
        this.blueCellName = blueCellName;
    }

    public List<Integer> getBlueCellIndex() {
        return blueCellIndex;
    }

    public void setBlueCellIndex(List<Integer> blueCellIndex) {
        this.blueCellIndex = blueCellIndex;
    }

    public String[] getYellowEffectClassName() {
        return yellowEffectClassName;
    }

    public void setYellowEffectClassName(String[] yellowEffectClassName) {
        this.yellowEffectClassName = yellowEffectClassName;
    }
}
