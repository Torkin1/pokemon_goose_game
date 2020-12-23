package it.walle.pokemongoosegame.database.local.boardpgdao;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

import it.walle.pokemongoosegame.boardfactory.BlueCellSettings;
import it.walle.pokemongoosegame.database.local.LocalDatabase;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGParams;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowCellStartingIndex;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowEffectName;

public class BoardSettingsDao {

    private static BoardSettingsDao ref = null;
    private Context context;

    public static BoardSettingsDao getReference(){
        if (ref == null){
            ref = new BoardSettingsDao();
        }
        return ref;
    }

    private BoardSettingsDao(){}

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void storeBoard(@NonNull BoardPGParams boardPGParams,
                           List<BlueCellSettings> blueCellSettingsList,
                           List<WhatYellowCellStartingIndex> whatYellowCellStartingIndexList,
                           List<WhatYellowEffectName> whatYellowEffectNameList)
    {

        ((LocalDatabase) LocalDatabase.getReference(context))
                .BoardPGParamsDAO()
                .storeBoardPGParams(boardPGParams);

        for (int i = 0; i < blueCellSettingsList.size(); i++){
            ((LocalDatabase) LocalDatabase.getReference(context))
                    .BlueCellSettingsDAO()
                    .storeBlueCellSettingsToBoardCellSettings(blueCellSettingsList.get(i));
        }

        for (int i = 0; i < whatYellowCellStartingIndexList.size(); i++) {
            ((LocalDatabase) LocalDatabase.getReference(context))
                    .WhatYellowCellStartingIndexDAO()
                    .storeYellowCellStartingIndexToBoardSettings(whatYellowCellStartingIndexList.get(i));
        }

        for (int i = 0; i < whatYellowEffectNameList.size(); i++) {
            ((LocalDatabase) LocalDatabase.getReference(context))
                    .WhatYellowEffectNameDAO()
                    .storeYellowEffectNameToBoardSettings(whatYellowEffectNameList.get(i));
        }
    }
}
