package it.walle.pokemongoosegame.boardfactory.procedurallygenerated;

import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;

public class CreateBoardPGBean extends CreateBoardBean {
    private BoardPGSettings boardSettings;

    public BoardPGSettings getBoardSettings() {
        return boardSettings;
    }

    public void setBoardSettings(BoardPGSettings boardSettings) {
        this.boardSettings = boardSettings;
    }
}
