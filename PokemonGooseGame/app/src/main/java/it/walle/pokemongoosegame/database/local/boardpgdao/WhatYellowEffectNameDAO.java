package it.walle.pokemongoosegame.database.local.boardpgdao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowEffectName;

@Dao
public interface WhatYellowEffectNameDAO {
    @Query("SELECT * FROM WhatYellowEffectName where boardSettingsName = :boardSettingsName")
    public LiveData<List<WhatYellowEffectName>> getYellowEffectNamesByBoardSettingsName(String boardSettingsName);

    @Insert
    public void storeYellowEffectNameToBoardSettings(WhatYellowEffectName... yellowEffectName);
}
