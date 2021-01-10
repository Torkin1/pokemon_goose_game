package it.walle.pokemongoosegame.activities.gameview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.board.cell.Cell;
import it.walle.pokemongoosegame.utils.DrawableGetter;
import it.walle.pokemongoosegame.utils.DrawableNotFoundException;

public class BitmapBank {

    //The class is incomplete, has to be a bigger that can mange all the images on the project
    //and convert/size/move etcc

    private static final String TAG = BitmapBank.class.getSimpleName();
    //bg reff
    Bitmap background, pawn, cell;
    Resources res;//a ress variable

    public BitmapBank(Resources res, Context context) {
        //passing the res, adn setting it, after we create the bitmaps
        this.res = res;
        background = BitmapFactory.decodeResource(res, R.drawable.bg_large);

        cell = BitmapFactory.decodeResource(res, R.drawable.cell_bg_cell);

        pawn = BitmapFactory.decodeResource(res, R.drawable.crab_with_a_knife);


        //directly scale some of the images
        //in the future all will be done only by calculating the dimension in a percentage of the screen
        cell = scaleCell(cell);
        //scale the board

        //to scale the pawn image
        pawn = scalePawn(pawn, getCellWidth(), getCellHeight());

        //to scale the bg  image
        background = scaleBg(background, context);//care, if is a moving bg, check the smoothness, can tell a lot

    }




    public Bitmap getPawn() {
        return pawn;
    }

    public int getPawnWidth() {
        return pawn.getWidth();
    }

    public int getPawnHeight() {
        return pawn.getHeight();
    }

    //return background bitmap
    public Bitmap getBackground() {
        return background;
    }

    //Return bg width
    public int getBackgroundWidth() {
        return background.getWidth();
    }

    //Return bg height
    public int getBackgroundHeight() {
        return background.getHeight();
    }


    public Bitmap getCell() {
        return cell;
    }

    public int getCellWidth() {
        return cell.getWidth();
    }

    public int getCellHeight() {
        return cell.getHeight();
    }


    public void setCellResBlue() {
        cell = BitmapFactory.decodeResource(res, R.drawable.cell_bg_bluecell);
        cell = scaleCell(cell);

    }

    public void setCellResYellow() {
        cell = BitmapFactory.decodeResource(res, R.drawable.cell_bg_yellowcell);
        cell = scaleCell(cell);

    }

    public void setCellResNormal() {
        cell = BitmapFactory.decodeResource(res, R.drawable.cell_bg_cell);
        cell = scaleCell(cell);

    }



    public void setCellRes(Class<? extends Cell> cellClass){
        try {
            cell = BitmapFactory.decodeResource(res, DrawableGetter.getReference().getCellBgDrawableId(cellClass));
            cell = scaleCell(cell);
        } catch (DrawableNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
            setCellResNormal();
        }
    }


    //scale the image checking the devices sizes
    public Bitmap scaleBg(Bitmap bitmap, Context context) {

        /*
         * We'll multiply widthHeightRatio with screenHeight to get scaled width of the bitmap
         * then call createScaledBitmap() to create new bitmap, scaled from an existing bitmap,
         * when possibile
         * */

        return Bitmap.createScaledBitmap(bitmap, AppConstants.getInstance(context).SCREEN_WIDTH, AppConstants.getInstance(context).SCREEN_HEIGHT, false);//chiamo il metodo nel costruttore
    }



    public Bitmap scalePawn(Bitmap pawn, int width, int height) {
        return Bitmap.createScaledBitmap(pawn, width, height, false);
    }

    private Bitmap scaleCell(Bitmap cell) {
        return Bitmap.createScaledBitmap(cell, cell.getWidth() * 2, cell.getWidth() * 2, false);
    }

    public Bitmap scaleTypeIcon(Bitmap typeIcon){
        return Bitmap.createScaledBitmap(typeIcon, cell.getWidth()/5, cell.getHeight()/5, false);
    }

    public Bitmap scaleConfetti(Bitmap confetti_img){
        return Bitmap.createScaledBitmap(confetti_img, 30,30,false);
    }
}
