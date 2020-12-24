package it.walle.pokemongoosegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.createboardsettings.CreateBoardSettings;
import it.walle.pokemongoosegame.createboardsettings.CreateBoardSettingsBean;
import it.walle.pokemongoosegame.graphics.AppConstants;
import it.walle.pokemongoosegame.graphics.BitmapBank;

import static it.walle.pokemongoosegame.graphics.AppConstants.convertDpToPixel;

public class Bootstrap {

    // Shared preferences names
    public final static String
            SHARED_PREF_NAME = Bootstrap.class.getName(),
            IS_FIRST_BOOT_SHARED_PREF_NAME = "isFirstBoot",
            CELLS_IN_A_SCREEN_SHARED_PREF_NAME = "cellsInAScreen";

    // default board params
    private final static int NUM_CELLS = 63;
    private final static int YELLOW_CELLS_DELTA = 9;
    private final static int[] YELLOW_CELLS_STARTING_INDEX = {5, 9};
    private final static String BOARD_NAME = "Default";

    private static Bootstrap ref = null;

    public static Bootstrap getReference(){
        if (ref == null){
            ref = new Bootstrap();
        }
        return ref;
    }

    private Bootstrap(){}

    public void doOnBoot(Context context){

        // If it's first boot do some additional setup
        if (context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getBoolean(IS_FIRST_BOOT_SHARED_PREF_NAME, true)){
            doOnFirstBoot(context);
        }

    }

    private void doOnFirstBoot(Context context){

        SharedPreferences.Editor settingsEditor =  context
                .getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit();

        // Stores if it's first boot
        settingsEditor.putBoolean(IS_FIRST_BOOT_SHARED_PREF_NAME, false);

        // Stores a default board configuration in the db
        Bootstrap.getReference().createDefaultBoard(context);

        // stores how many cells fit in the screen
        settingsEditor.putInt(CELLS_IN_A_SCREEN_SHARED_PREF_NAME, calculateCellsInAScreen(context));

        // saves settings
        settingsEditor.apply();
    }

    private void createDefaultBoard(Context context){
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

        controller.setBoardPGParams();
        controller.setWhatYellowCellStartingIndex();
        controller.storeBoardSettings(context);
    }

    private int calculateCellsInAScreen(Context context){

        int counter;

        BitmapBank bitmapBank = new BitmapBank(context.getResources(), context);

        int cols = (AppConstants.getInstance(context).SCREEN_WIDTH - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH -
                AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellWidth();

        int rows = (AppConstants.getInstance(context).SCREEN_HEIGHT - AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellHeight();

        counter = cols*rows;

        return counter;


    }
}
