package it.walle.pokemongoosegame.graphics;

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

    private static final String TAG = BitmapBank.class.getSimpleName();
    //bg reff
    Bitmap background, pawn, cell;
    Resources res;

    public BitmapBank(Resources res, Context context) {
        //Now I take normal iamges and covert them to bitmaps
        this.res = res;
        background = BitmapFactory.decodeResource(res, R.drawable.bg_large);

        cell = BitmapFactory.decodeResource(res, R.drawable.cell_bg_normal);

        pawn = BitmapFactory.decodeResource(res, R.drawable.crab_with_a_knife);


        cell = scaleCell(cell);
        //scale the board

        //to scale the pawn  image
        pawn = scalePawn(pawn);

        //to scale the bg  image
        background = scaleImage(background, context);//care, if is a moving bg, check the smoothness, can tell a lot

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
        cell = BitmapFactory.decodeResource(res, R.drawable.cell_bg_blue);
        cell = scaleCell(cell);

    }

    public void setCellResYellow() {
        cell = BitmapFactory.decodeResource(res, R.drawable.cell_bg_yellow);
        cell = scaleCell(cell);

    }

    public void setCellResNormal() {
        cell = BitmapFactory.decodeResource(res, R.drawable.cell_bg_normal);
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
    public Bitmap scaleImage(Bitmap bitmap, Context context) {
        float widthHeightRatio = getBackgroundWidth() / getBackgroundHeight();
        /*
         * We'll multiply widthHeightRatio with screenHeight to get scaled width of the bitmap
         * then call createScaledBitmap() to create new bitmap, scaled from an existing bitmap,
         * when possibile
         * */

        int backgroundScaleWidth = (int) widthHeightRatio * AppConstants.getInstance(context).SCREEN_HEIGHT;
        return Bitmap.createScaledBitmap(bitmap, AppConstants.getInstance(context).SCREEN_WIDTH, AppConstants.getInstance(context).SCREEN_HEIGHT, false);//chiamo il metodo nel costruttore
    }

    private Bitmap scaleBoard(Bitmap board, Context context) {
//        float widthHeigthRatio = getBoardWidth() / getBoardHeight();

//        int boardScaledWidth = (int) widthHeigthRatio * AppConstants.SCREEN_HEIGHT;

//        int boardScaledHeight = (int) widthHeigthRatio * AppConstants.SCREEN_WIDTH;
//        return Bitmap.createScaledBitmap(board, boardScaledWidth, boardScaledHeight, false);


        return Bitmap.createScaledBitmap(board, AppConstants.getInstance(context).SCREEN_WIDTH - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH * 2 + 20, AppConstants.getInstance(context).SCREEN_HEIGHT - 120, false);
    }

    private Bitmap scalePawn(Bitmap pawn) {


//        return Bitmap.createScaledBitmap(pawn, (AppConstants.SCREEN_WIDTH) / 10, (AppConstants.SCREEN_HEIGHT - 120) / 5, false);
        return Bitmap.createScaledBitmap(pawn, cell.getWidth(), cell.getHeight(), false);
    }

    private Bitmap scaleCell(Bitmap cell) {
        return Bitmap.createScaledBitmap(cell, cell.getWidth() * 2, cell.getWidth() * 2, false);
    }

}
