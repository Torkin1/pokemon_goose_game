package it.walle.pokemongoosegame.settings;

import it.walle.pokemongoosegame.entity.board.BoardSettings;

public interface BoardSettingsDAO {
    public void storeBoardSettings(BoardSettings settings);

    // In order to load the BoardSetting object, the caller must specify which child of BoardSettings wants.
    public BoardSettings loadBoardSettings(Class<? extends BoardSettings> boardSettingsClass, String name) throws UnableToLoadSettingsException;
}