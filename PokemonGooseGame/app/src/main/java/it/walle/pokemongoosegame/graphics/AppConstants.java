package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class AppConstants {

    static BitmapBank bitmapBank;//reff to the obj bitmapBank
    static GameEngine gameEngine; //reff to the obj GameEngine


    //to now the screen dimension
    static int SCREEN_WIDTH, SCREEN_HEIGHT, CELL_MARGIN = 15;
    public static int LEFT_GAME_MENU_WIDTH, LEFT_GAME_MENU_HEIGHT, CELL_WH,
            TOTAL_CELLS = 69, DONE_CELLS = 0,
            DISPLAYED_SCREEN = 1, PAWNS_SCREEN = 1, TOTAL_SCREENS = 1,
            CELLS_IN_A_SCREEN = 0;
    public static boolean DRAWABLE = true;

    //for gravity when the pawn has to move up
    static int gravity;

    //to check the movement speed
    static int VELOCITY_WHILE_MOVING;

    //initiliazing theref
    public static void initialization(Context context) {
        //setting screen measure
        setScreenSize(context);//I'll use it to scale the images for the best result
        float menu_dp_width = 90f;
        AppConstants.LEFT_GAME_MENU_WIDTH = (int) convertDpToPixel(menu_dp_width, context);

        bitmapBank = new BitmapBank(context.getResources());//nella bitmalbank nel costruttore ricevo delle res
        gameEngine = new GameEngine();

        //Want to get the realitve layout so i could get its dimension


        AppConstants.gravity = 0; //se voglio la gravità va messa la velocità della pawn nella sua classe e inizializzata nel costruttore della sua classe

        AppConstants.VELOCITY_WHILE_MOVING = -10;
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    //return bitmapbank istance
    public static BitmapBank getBitmapBank() {
        return bitmapBank;
    }

    //return gameEngine istance
    public static GameEngine getGameEngine() {
        return gameEngine;
    }//using the same logic we have the methods (isRunning(),setIsRunning()) in GameThread

    //seting the height and width variable, for any device
    private static void setScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        AppConstants.SCREEN_WIDTH = width;
        AppConstants.SCREEN_HEIGHT = height;
        float dip = 90f;
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
        AppConstants.LEFT_GAME_MENU_WIDTH = (int) px;
        AppConstants.CELL_WH = (int) px;

    }

    public void setRemainingCells(int remainingCells) {
        AppConstants.DONE_CELLS = remainingCells;
    }

    public void setLeftMenuWH(int width, int height) {
        AppConstants.LEFT_GAME_MENU_HEIGHT = height;
        AppConstants.LEFT_GAME_MENU_WIDTH = width;
    }
}
