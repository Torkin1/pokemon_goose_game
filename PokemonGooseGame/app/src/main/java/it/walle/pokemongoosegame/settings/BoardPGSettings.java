package it.walle.pokemongoosegame.settings;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import it.walle.pokemongoosegame.boardfactory.BlueCellSettings;
import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.BoardPGParams;

public class BoardPGSettings {
    @Embedded
    private BoardPGParams boardPGParams;                                            // Additional params needed by BoardFactoryPG
    @Relation(parentColumn = "name", entityColumn = "boardSettingsName")
    private List<WhatYellowCellStartingIndex> yellowCellStartingIndexes;            // Starting from these indexes, there will be one yellow cell every yellowCellDelta cells .
    @Relation(parentColumn = "name", entityColumn = "boardSettingsName" )
    private List<BlueCellSettings> blueCellSettings;                                // Parameters needed by board factory to inject blue cells in the board, one for each blue cell
    @Relation(parentColumn = "name", entityColumn = "boardSettingsName" )
    private List<WhatYellowEffectName> yellowEffectName;                           // Yellow effect names which can be injected in the board

    public BoardPGParams getBoardPGParams() {
        return boardPGParams;
    }

    public void setBoardPGParams(BoardPGParams boardPGParams) {
        this.boardPGParams = boardPGParams;
    }

    public List<WhatYellowCellStartingIndex> getYellowCellStartingIndexes() {
        return yellowCellStartingIndexes;
    }

    public void setYellowCellStartingIndexes(List<WhatYellowCellStartingIndex> yellowCellStartingIndexes) {
        this.yellowCellStartingIndexes = yellowCellStartingIndexes;
    }

    public List<BlueCellSettings> getBlueCellSettings() {
        return blueCellSettings;
    }

    public void setBlueCellSettings(List<BlueCellSettings> blueCellSettings) {
        this.blueCellSettings = blueCellSettings;
    }

    public List<WhatYellowEffectName> getYellowEffectName() {
        return yellowEffectName;
    }

    public void setYellowEffectName(List<WhatYellowEffectName> yellowEffectName) {
        this.yellowEffectName = yellowEffectName;
    }
}
