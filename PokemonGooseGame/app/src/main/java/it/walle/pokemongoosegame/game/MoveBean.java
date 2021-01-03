package it.walle.pokemongoosegame.game;

import android.content.Context;

public class MoveBean {
    private String playerUsername;  // The username of the player who has to be moved
    private int boardIndex;         // The index of the Cell in the board
    private Context context;        // The context of the current view

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int boardIndex) {
        this.boardIndex = boardIndex;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

//setter and getter of the contxt
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
