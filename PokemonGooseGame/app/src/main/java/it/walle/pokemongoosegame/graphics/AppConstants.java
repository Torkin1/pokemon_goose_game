package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class AppConstants {

    static BitmapBank bitmapBank;//reff to the obj bitmapBank
    static  GameEngine gameEngine; //reff to the obj GameEngine

    //to now the screen dimension
    static int SCREEN_WIDTH, SCREEN_HEIGHT;

    //initiliazing theref
    public static void initialization(Context context){
        //setting screen measure
        setScreenSize(context);//I'll use it to scale the images for the best result
        bitmapBank = new BitmapBank(context.getResources());//nella bitmalbank nel costruttore ricevo delle res
        gameEngine = new GameEngine();
    }

    //return bitmapbank istance
    public static BitmapBank getBitmapBank(){
        return bitmapBank;
    }

    //return gameEngine istance
    public static GameEngine getGameEngine(){
        return gameEngine;
    }//using the same logic we have the methods (isRunning(),setIsRunning()) in GameThread

    //seting the height and width variable, for any device
    private static void setScreenSize(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        AppConstants.SCREEN_WIDTH = width;
        AppConstants.SCREEN_HEIGHT = height;
    }
}
