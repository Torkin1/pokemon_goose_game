package it.walle.pokemongoosegame.boardfactory;

public class CreateBoardProcedurallyGeneratedBean extends CreateBoardBean{
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
