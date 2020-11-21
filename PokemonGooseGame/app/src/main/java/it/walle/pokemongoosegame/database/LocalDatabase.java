package it.walle.pokemongoosegame.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import it.walle.pokemongoosegame.boardfactory.BlueCellSettings;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGParams;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowCellStartingIndex;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowEffectName;
import it.walle.pokemongoosegame.database.boardpgdao.BlueCellSettingsDAO;
import it.walle.pokemongoosegame.database.boardpgdao.BoardPGParamsDAO;
import it.walle.pokemongoosegame.database.boardpgdao.WhatYellowCellStartingIndexDAO;
import it.walle.pokemongoosegame.database.boardpgdao.WhatYellowEffectNameDAO;

@Database(entities = {
        // Entities modelling the settings tree for BoardFactory
        BoardPGParams.class,
        BlueCellSettings.class,
        WhatYellowCellStartingIndex.class,
        WhatYellowEffectName.class,
}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    private static RoomDatabase ref = null;
    public static RoomDatabase getReference(Context context){
        if (ref == null){
            ref = Room
                    .databaseBuilder(
                        context.getApplicationContext(),
                            LocalDatabase.class,
                            LocalDatabase.class.getName()
                    ).build();
        }
        return ref;
    }
    // DAO getters here
    public abstract BlueCellSettingsDAO BlueCellSettingsDAO();
    public abstract BoardPGParamsDAO BoardPGParamsDAO();
    public abstract WhatYellowCellStartingIndexDAO WhatYellowCellStartingIndexDAO();
    public abstract WhatYellowEffectNameDAO WhatYellowEffectNameDAO();
}
