package it.walle.pokemongoosegame.boardfactory.procedurallygenerated;

import it.walle.pokemongoosegame.boardfactory.BlueCellSettings;
import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;
import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.BoardProcedurallyGeneratedSettings;

public class CreateBoardProcedurallyGeneratedBean extends CreateBoardBean {
    private BoardProcedurallyGeneratedSettings boardSettings;
    private BlueCellSettings[] blueCellSettings;

    public BlueCellSettings[] getBlueCellSettings() {
        return blueCellSettings;
    }

    public void setBlueCellSettings(BlueCellSettings[] blueCellSettings) {
        this.blueCellSettings = blueCellSettings;
    }

    public BoardProcedurallyGeneratedSettings getBoardSettings() {
        return boardSettings;
    }

    public void setBoardSettings(BoardProcedurallyGeneratedSettings boardSettings) {
        this.boardSettings = boardSettings;
    }
}
