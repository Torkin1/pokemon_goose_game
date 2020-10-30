package it.walle.pokemongoosegame.boardfactory;

import it.walle.pokemongoosegame.entity.Board;

public abstract class CreateBoardBean {
    private Board board;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
