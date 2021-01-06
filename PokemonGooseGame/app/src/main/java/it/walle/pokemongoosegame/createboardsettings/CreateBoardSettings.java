package it.walle.pokemongoosegame.createboardsettings;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.boardfactory.BlueCellSettings;
import it.walle.pokemongoosegame.database.local.boardpgdao.BoardSettingsDao;
import it.walle.pokemongoosegame.entity.board.pgsettings.BoardPGParams;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowCellStartingIndex;
import it.walle.pokemongoosegame.entity.board.pgsettings.WhatYellowEffectName;

public class CreateBoardSettings {
    private BoardPGParams boardPGParams;
    private List<BlueCellSettings> blueCellSettingsList;
    private List<WhatYellowCellStartingIndex> whatYellowCellStartingIndexList;
    private List<WhatYellowEffectName> whatYellowEffectNameList;
    private CreateBoardSettingsBean bean;
    private static CreateBoardSettings ref = null;

    public static CreateBoardSettings getReference(CreateBoardSettingsBean bean) {
        if (ref == null){
            ref = new CreateBoardSettings(bean);
        }
        return ref;
    }

    private CreateBoardSettings(CreateBoardSettingsBean bean) {
        this.boardPGParams = new BoardPGParams();
        this.blueCellSettingsList = new ArrayList<>();
        this.whatYellowCellStartingIndexList = new ArrayList<>();
        this.whatYellowEffectNameList = new ArrayList<>();
        this.bean = bean;
    }

    public void storeBoardSettings(Context context){
        BoardSettingsDao
                .getReference()
                .storeBoard(context,
                        boardPGParams,
                        blueCellSettingsList,
                        whatYellowCellStartingIndexList,
                        whatYellowEffectNameList);
    }

    public void setBoardPGParams(){
        boardPGParams.setNumCells(bean.getNumCells());
        boardPGParams.setYellowCellDelta(bean.getYellowCellDelta());
        boardPGParams.setName(bean.getBoardPGParamsName());
    }

    public void setBlueCellSettings(){
        for(int i = 0; i < bean.getBlueCellIndex().size(); i++){
            BlueCellSettings blueCellSettings = new BlueCellSettings();

            blueCellSettings.setBlueCellName(bean.getBlueCellName().get(i));
            blueCellSettings.setBoardIndex(bean.getBlueCellIndex().get(i));
            blueCellSettings.setBoardSettingsName(bean.getBoardSettingsName());

            blueCellSettingsList.add(blueCellSettings);
        }
    }

    public void setWhatYellowCellStartingIndex(){
        for(int i = 0; i < bean.getYellowCellStartingIndex().size(); i++){
            WhatYellowCellStartingIndex whatYellowCellStartingIndex = new WhatYellowCellStartingIndex();

            whatYellowCellStartingIndex.setIndex(bean.getYellowCellStartingIndex().get(i));
            whatYellowCellStartingIndex.setBoardSettingsName(bean.getBoardSettingsName());

            whatYellowCellStartingIndexList.add(whatYellowCellStartingIndex);
        }
    }

    public void setWhatYellowEffectName(){
        for (int i = 0; i < bean.getYellowEffectClassName().length; i++){
            WhatYellowEffectName whatYellowEffectName = new WhatYellowEffectName();

            whatYellowEffectName.setYellowEffectClassName(bean.getYellowEffectClassName()[i]);
            whatYellowEffectName.setBoardSettingsName(bean.getBoardSettingsName());

            whatYellowEffectNameList.add(whatYellowEffectName);
        }
    }
}
