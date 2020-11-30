package it.walle.pokemongoosegame.graphics;

import android.graphics.Canvas;

public class GameEngine {
    Background backgroundImg;
    static int gameState;

    public GameEngine() {
        backgroundImg = new Background();
        //states  0 = Not started, 1 = Playing, 2 = GameOVer;
        gameState = 0;//Game not started, so have to change it later
    }

    public void updateAndDrawBackgroundImage(Canvas canvas) {
        backgroundImg.setX(backgroundImg.getX() - backgroundImg.getBgImageVelocity());
        //moving animation
        //if it ends have to start it back from 0
        if (backgroundImg.getX() < -AppConstants.getBitmapBank().getBackgroundWidth())
            backgroundImg.setX(0);
        canvas.drawBitmap(AppConstants.getBitmapBank().getBackground(),
                backgroundImg.getX(),
                backgroundImg.getY(),
                null);
        //if the image end, it will leave a long line, to prevent this I'll restart the bg

        if(backgroundImg.getX() < -(AppConstants.getBitmapBank().getBackgroundWidth() - AppConstants.SCREEN_WIDTH)){
            canvas.drawBitmap(AppConstants.getBitmapBank().getBackground(),
                    backgroundImg.getX() + AppConstants.getBitmapBank().getBackgroundWidth(),
                    backgroundImg.getY(),
                    null);
        }
    }
}
