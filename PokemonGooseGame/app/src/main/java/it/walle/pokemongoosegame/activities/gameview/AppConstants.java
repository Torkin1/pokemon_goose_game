package it.walle.pokemongoosegame.activities.gameview;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import it.walle.pokemongoosegame.Bootstrap;

public class AppConstants {

    private static AppConstants ref = null;

    public synchronized static AppConstants getInstance(Context context){
        if (ref == null){
            ref = new AppConstants(context.getApplicationContext());
        }
        return ref;
    }

    //set the screen dimension
    public final int
            SCREEN_WIDTH,
            SCREEN_HEIGHT,
            CELL_MARGIN = 15,
            LEFT_GAME_MENU_WIDTH;

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public AppConstants(Context context) {

        //setting screen measure and the height and width variable, for any device
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        float menu_dp_width = 90f;
        this.LEFT_GAME_MENU_WIDTH = (int) convertDpToPixel(menu_dp_width, context);

    }



}
