package it.walle.pokemongoosegame.boardfactory.procedurallygenerated;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WhatYellowCellStartingIndexDAO {
    @Query("SELECT * from whatyellowcellstartingindex where boardSettingsName = :boardSettingsName")
    LiveData<List<WhatYellowCellStartingIndex>> getAllYellowCellStartingIndexesByBoardSettingsName(String boardSettingsName);

    @Insert
    public void storeYellowCellStartingIndexToBoardSettings(WhatYellowCellStartingIndex... yellowCellStartingIndex);
}
