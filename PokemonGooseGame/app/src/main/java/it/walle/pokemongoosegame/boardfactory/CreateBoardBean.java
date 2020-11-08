package it.walle.pokemongoosegame.boardfactory;

import android.content.Context;

import it.walle.pokemongoosegame.entity.board.Board;

public abstract class CreateBoardBean {
    private Board board;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }


}
