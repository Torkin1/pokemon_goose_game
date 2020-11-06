package it.walle.pokemongoosegame.boardfactory;

import android.content.Context;

import it.walle.pokemongoosegame.entity.board.Board;

public abstract class CreateBoardBean {
    private Context context;
    private Board board;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
