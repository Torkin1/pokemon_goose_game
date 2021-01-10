package it.walle.pokemongoosegame.boardfactory;

import android.content.Context;
import android.os.Handler;

import it.walle.pokemongoosegame.entity.board.Board;

public abstract class CreateBoardBean {
    private Board board;                    // The requested board
    private Handler.Callback callback;      // Just in case the board is generated asynchronously
    private Handler handler;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Handler.Callback getCallback() {
        return callback;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setCallback(Handler.Callback callback) {
        this.callback = callback;
    }
}
