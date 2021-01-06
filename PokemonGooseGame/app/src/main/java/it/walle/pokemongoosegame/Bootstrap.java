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
import it.walle.pokemongoosegame.entity.effect.ItsATrap;
import it.walle.pokemongoosegame.entity.effect.LittleHiddenTreasure;
import it.walle.pokemongoosegame.entity.effect.NoPlaceLikeHome;
import it.walle.pokemongoosegame.entity.effect.TheTributeOfThePious;
import it.walle.pokemongoosegame.graphics.AppConstants;
import it.walle.pokemongoosegame.graphics.BitmapBank;

import static it.walle.pokemongoosegame.graphics.AppConstants.convertDpToPixel;

public class Bootstrap {

    // Shared preferences names
    public final static String
            SHARED_PREF_NAME = Bootstrap.class.getName(),
            IS_FIRST_BOOT_SHARED_PREF_NAME = "isFirstBoot";

    // default board params
    private final static int NUM_CELLS = 63;
    private final static int YELLOW_CELLS_DELTA = 9;
    private final static int[] YELLOW_CELLS_STARTING_INDEX = {5, 9};
    private final static String BOARD_NAME = "Default";
    private final static String[] YELLOW_EFFECT_CLASS_NAME = {
            LittleHiddenTreasure.class.getName(),
            NoPlaceLikeHome.class.getName(),
            ItsATrap.class.getName(),
            TheTributeOfThePious.class.getName()
            };

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
        bean.setYellowEffectClassName(YELLOW_EFFECT_CLASS_NAME);
        bean.setBoardPGParamsName(BOARD_NAME);
        bean.setBoardSettingsName(BOARD_NAME);

        CreateBoardSettings controller = CreateBoardSettings.getReference(bean);

        controller.setBoardPGParams();
        controller.setWhatYellowCellStartingIndex();
        controller.setWhatYellowEffectName();
        controller.storeBoardSettings(context);
    }
}
