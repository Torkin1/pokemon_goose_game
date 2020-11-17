package it.walle.pokemongoosegame.settings;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WhatYellowEffectNameDAO {
    @Query("SELECT * FROM WhatYellowEffectName where boardSettingsName = :boardSettingsName")
    public LiveData<List<WhatYellowEffectName>> getYellowEffectNamesByBoardSettingsName(String boardSettingsName);

    @Insert
    public void storeYellowEffectNameToBoardSettings(WhatYellowEffectName... yellowEffectName);
}
