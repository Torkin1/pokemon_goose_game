package it.walle.pokemongoosegame.game;

public class MoveBean {
    private String playerUsername;  // The username of the player who has to be moved
    private int boardIndex;         // The index of the Cell in the board

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
    }
}
