package it.walle.pokemongoosegame.settings.json;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import it.walle.pokemongoosegame.entity.board.BoardSettings;
import it.walle.pokemongoosegame.settings.BoardSettingsDAO;
import it.walle.pokemongoosegame.settings.UnableToLoadSettingsException;

public class BoardSettingsDAO_JSON extends DAO_JSON<BoardSettings> implements BoardSettingsDAO {

    private static final String LOG_TAG = BoardSettingsDAO_JSON.class.getName();

    public BoardSettingsDAO_JSON(Context context) {
        super(context);
    }

    @Override
    public void storeBoardSettings(BoardSettings settings){
        this.storeSettings(settings, settings.getName());
    }
    @Override
    public BoardSettings loadBoardSettings(Class<? extends BoardSettings> boardSettingsClass, String name) throws UnableToLoadSettingsException {
        try {
            return this.loadSettings(boardSettingsClass, name);
        } catch (SettingsFileNotFoundException | IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw new UnableToLoadSettingsException();
        }
    }

}
