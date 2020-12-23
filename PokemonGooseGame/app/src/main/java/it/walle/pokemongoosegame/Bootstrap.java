package it.walle.pokemongoosegame;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.createboardsettings.CreateBoardSettings;
import it.walle.pokemongoosegame.createboardsettings.CreateBoardSettingsBean;

public class Bootstrap {
    private final static int NUM_CELLS = 64;
    private final static int YELLOW_CELLS_DELTA = 9;
    private final static int[] YELLOW_CELLS_STARTING_INDEX = {5, 9};
    private final static String BOARD_NAME = "Default";

    private static Bootstrap ref = null;
    private Context context;

    public static Bootstrap getReference(){
        if (ref == null){
            ref = new Bootstrap();
        }
        return ref;
    }

    private Bootstrap(){}

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void createDefaultBoard(){
        CreateBoardSettingsBean bean = new CreateBoardSettingsBean();

        List<Integer> yellowCellStartingIndex = new ArrayList<>();
        yellowCellStartingIndex.add(YELLOW_CELLS_STARTING_INDEX[0]);
        yellowCellStartingIndex.add(YELLOW_CELLS_STARTING_INDEX[1]);

        bean.setNumCells(NUM_CELLS);
        bean.setYellowCellDelta(YELLOW_CELLS_DELTA);
        bean.setYellowCellStartingIndex(yellowCellStartingIndex);
        bean.setBoardPGParamsName(BOARD_NAME);
        bean.setBoardSettingsName(BOARD_NAME);

        CreateBoardSettings controller = CreateBoardSettings.getReference(bean);
        controller.setContext(context);

        controller.setBoardPGParams();
        controller.setWhatYellowCellStartingIndex();
        controller.storeBoardSettings();
    }
}
