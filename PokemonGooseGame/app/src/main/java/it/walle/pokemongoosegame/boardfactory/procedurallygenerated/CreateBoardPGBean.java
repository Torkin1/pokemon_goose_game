package it.walle.pokemongoosegame.boardfactory.procedurallygenerated;

import android.os.Handler;

import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGSettings;

public class CreateBoardPGBean extends CreateBoardBean {
    private BoardPGSettings boardSettings;                                  // Settings used by the board factory to pocedurally generate a new board
    private CreateBoardPGHandlerCallback CreateBoardPGHandlerCallback;           // Creation is ran on another thread, a listener is needed to use the newly created board
    private Handler handler;

    public BoardPGSettings getBoardSettings() {
        return boardSettings;
    }

    public void setBoardSettings(BoardPGSettings boardSettings) {
        this.boardSettings = boardSettings;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setCreateBoardPGHandlerCallback(CreateBoardPGHandlerCallback createBoardPGHandlerCallback) {
        this.CreateBoardPGHandlerCallback = createBoardPGHandlerCallback;
        this.handler = new Handler(this.CreateBoardPGHandlerCallback);
    }
}
