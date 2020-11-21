package it.walle.pokemongoosegame.database.boardpgdao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.walle.pokemongoosegame.boardfactory.BlueCellSettings;

@Dao
public interface BlueCellSettingsDAO {
    @Query("SELECT * from bluecellsettings where boardSettingsName = :boardSettingsName")
    LiveData<List<BlueCellSettings>> getBlueCellSettingsByBoardSettingsName(String boardSettingsName);

    @Insert
    void storeBlueCellSettingsToBoardCellSettings(BlueCellSettings... blueCellSettings);
}
