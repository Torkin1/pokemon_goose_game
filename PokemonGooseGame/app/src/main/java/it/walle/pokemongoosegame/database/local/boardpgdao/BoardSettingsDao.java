package it.walle.pokemongoosegame.database.local.boardpgdao;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Observer;

import it.walle.pokemongoosegame.boardfactory.BlueCellSettings;
import it.walle.pokemongoosegame.database.local.LocalDatabase;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGParams;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowCellStartingIndex;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowEffectName;

public class BoardSettingsDao {

    private static BoardSettingsDao ref = null;

    public static BoardSettingsDao getReference(){
        if (ref == null){
            ref = new BoardSettingsDao();
        }
        return ref;
    }

    public static final int MSG_OK = 0;     // The operation to the db was a success

    private BoardSettingsDao(){}

    public void storeBoard(Context context,
                           @NonNull BoardPGParams boardPGParams,
                           List<BlueCellSettings> blueCellSettingsList,
                           List<WhatYellowCellStartingIndex> whatYellowCellStartingIndexList,
                           List<WhatYellowEffectName> whatYellowEffectNameList
    ){
        this.storeBoard(context,
                boardPGParams,
                blueCellSettingsList,
                whatYellowCellStartingIndexList,
                whatYellowEffectNameList,
                null);
    }

    public void storeBoard(Context context,
                           @NonNull BoardPGParams boardPGParams,
                           List<BlueCellSettings> blueCellSettingsList,
                           List<WhatYellowCellStartingIndex> whatYellowCellStartingIndexList,
                           List<WhatYellowEffectName> whatYellowEffectNameList,
                           Handler handler  // Set this if you want to wait for the store to finish
    )
    {

        //a thread will always kepp an eye on the changin
        (new Thread(new Runnable() {
            @Override
            public void run() {
                ((LocalDatabase) LocalDatabase.getReference(context))
                        .BoardPGParamsDAO()
                        .storeBoardPGParams(boardPGParams);

                for (int i = 0; i < blueCellSettingsList.size(); i++){
                    ((LocalDatabase) LocalDatabase.getReference(context))
                            .BlueCellSettingsDAO()
                            .storeBlueCellSettingsToBoardCellSettings(blueCellSettingsList.get(i));
                }

                //calculate where the yellow cell has to be added
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

                // Notices the handler that the job is done
                if (handler != null){
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    msg.what = MSG_OK;
                    msg.sendToTarget();
                }

            }
        })).start();

    }
}
