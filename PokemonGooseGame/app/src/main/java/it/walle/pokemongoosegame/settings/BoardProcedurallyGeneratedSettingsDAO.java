package it.walle.pokemongoosegame.settings;

import it.walle.pokemongoosegame.boardfactory.BoardProcedurallyGeneratedSettings;

public interface BoardProcedurallyGeneratedSettingsDAO {
    public void storeBoardSettings(BoardProcedurallyGeneratedSettings settings);
    public BoardProcedurallyGeneratedSettings loadBoardSettings() throws UnableToLoadSettingsException;
}
