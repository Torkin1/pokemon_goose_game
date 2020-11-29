package it.walle.pokemongoosegame.entity.board.cell;


public class BlueCell extends Cell {

    private String title;       // Readable name of the cell
    private int boardIndex;     // The index of the board where this blue cell will be placed

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
