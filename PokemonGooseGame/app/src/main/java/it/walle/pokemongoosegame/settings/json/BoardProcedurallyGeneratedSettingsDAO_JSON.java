package it.walle.pokemongoosegame.settings.json;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import it.walle.pokemongoosegame.boardfactory.BoardProcedurallyGeneratedSettings;
import it.walle.pokemongoosegame.settings.BoardProcedurallyGeneratedSettingsDAO;
import it.walle.pokemongoosegame.settings.UnableToLoadSettingsException;

public class BoardProcedurallyGeneratedSettingsDAO_JSON extends SettingsDAO_JSON<BoardProcedurallyGeneratedSettings> implements BoardProcedurallyGeneratedSettingsDAO {

    private static final String SETTINGS_FILE_NAME = BoardProcedurallyGeneratedSettings.class.getSimpleName();
    private static final String LOG_TAG = BoardProcedurallyGeneratedSettingsDAO_JSON.class.getName();

    public BoardProcedurallyGeneratedSettingsDAO_JSON(Context context) {
        super(context);
    }

    @Override
    public void storeBoardSettings(BoardProcedurallyGeneratedSettings settings){
        this.storeSettings(settings, SETTINGS_FILE_NAME);
    }
    @Override
    public BoardProcedurallyGeneratedSettings loadBoardSettings() throws UnableToLoadSettingsException {
        try {
            return this.loadSettings(SETTINGS_FILE_NAME);
        } catch (SettingsFileNotFoundException | IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw new UnableToLoadSettingsException();
        }
    }

}
