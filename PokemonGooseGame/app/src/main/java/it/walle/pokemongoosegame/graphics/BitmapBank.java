package it.walle.pokemongoosegame.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import it.walle.pokemongoosegame.R;

public class BitmapBank {

    //bg reff
    Bitmap background;

    public BitmapBank(Resources res){
        //Now I take normal iamges and covert them to bitmaps
        background = BitmapFactory.decodeResource(res, R.drawable.bg_large);

        //to scale the image
        background = scaleImage(background);//care, if is a moving bg, check the smoothness, can tell a lot

    }

    //return background bitmap
    public Bitmap getBackground(){
        return background;
    }

    //Return bg width
    public  int getBackgroundWidth(){
        return background.getWidth();
    }

    //Return bg height
    public int getBackgroundHeight(){
        return background.getHeight();
    }

    //scale the image checking the devices sizes
    public Bitmap scaleImage(Bitmap bitmap){
        float widthHeightRatio = getBackgroundWidth() / getBackgroundHeight();
        /*
         * We'll multiply widthHeightRatio with screenHeight to get scaled width of the bitmap
         * then call createScaledBitmap() to create new bitmap, scaled from an existing bitmap,
         * when possibile
         * */

        int backgroundScaleWidth = (int) widthHeightRatio * AppConstants.SCREEN_HEIGHT;
        return Bitmap.createScaledBitmap(bitmap, AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT, false);//chiamo il metodo nel costruttore
    }
}
