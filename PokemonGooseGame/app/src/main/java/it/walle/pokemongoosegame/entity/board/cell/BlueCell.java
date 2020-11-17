package it.walle.pokemongoosegame.entity.board.cell;


public class BlueCell extends Cell {

    private String name;        // BlueCells are distinctive cells, so they must be distinguished from one another by their name
    private int boardIndex;     // The index of the board where this blue cell will be placed

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
