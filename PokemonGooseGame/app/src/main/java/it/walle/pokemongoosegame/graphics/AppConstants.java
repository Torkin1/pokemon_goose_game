package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class AppConstants {

    private static AppConstants ref = null;

    public static AppConstants getInstance(Context context){
        if (ref == null){
            ref = new AppConstants(context.getApplicationContext());
        }
        return ref;
    }

    static BitmapBank bitmapBank;//reff to the obj bitmapBank


    //to now the screen dimension
    public final int
            SCREEN_WIDTH,
            SCREEN_HEIGHT,
            CELL_MARGIN = 15,
            LEFT_GAME_MENU_WIDTH,
            CELLS_IN_A_SCREEN = 0,      // TODO: set value in constructor
            VELOCITY_WHILE_MOVING;


    public static boolean isDrawable = true;

    public int
            DISPLAYED_SCREEN = 1,       // TODO: DISPLAYED_SCREEN deve essere messo come attributo di game engine
            PAWNS_SCREEN = 1;           // TODO: move this in a Pawn object

    //for gravity when the pawn has to move up
    static int gravity;                 // TODO: move this in a Pawn object

    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public AppConstants(Context context) {
        // TODO: move tthis in constructor method

        //setting screen measure and the height and width variable, for any device
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        float dip = 90f;
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
        float menu_dp_width = 90f;
        this.LEFT_GAME_MENU_WIDTH = (int) convertDpToPixel(menu_dp_width, context);

        bitmapBank = new BitmapBank(context.getResources(), context);//nella bitmalbank nel costruttore ricevo delle res

        //Want to get the realitve layout so i could get its dimension


        AppConstants.gravity = 0; //se voglio la gravità va messa la velocità della pawn nella sua classe e inizializzata nel costruttore della sua classe

        this.VELOCITY_WHILE_MOVING = -10;


    }

    //return bitmapbank istance
    public static BitmapBank getBitmapBank() {
        return bitmapBank;
    }
}
