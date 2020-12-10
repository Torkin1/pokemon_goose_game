package it.walle.pokemongoosegame.graphics;

import android.graphics.Canvas;
import android.widget.RelativeLayout;

public class GameEngine {
    Background backgroundImg;
    PokePawn poke_pawn;
    Board board;
    static int gameState;
    static int xspeed, yspeed;
    private int xspeedtemp;



    public GameEngine() {
        backgroundImg = new Background();//initialize bg
        poke_pawn = new PokePawn();//initilialize pawn
        board = new Board();//initilize the board



        xspeed = AppConstants.getBitmapBank().getBoardWidth()/10;
        yspeed = AppConstants.getBitmapBank().getBoardHeight()/5;
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



    public void updateAndDrawPawn(Canvas canvas){
            if(gameState == 1){//ora che ho lo stato la parte sotto dell'if la metto qui dentro
                if (poke_pawn.getY() < (AppConstants.SCREEN_HEIGHT - AppConstants.getBitmapBank().getPawnHeight()) || poke_pawn.getBgImageVelocity() < 0) {
                    poke_pawn.setPokePawnImgVelocity(poke_pawn.getBgImageVelocity() + AppConstants.gravity);
                    poke_pawn.setY(poke_pawn.getY() + poke_pawn.getBgImageVelocity());//all'inizio velocità è 0, e gravità 3, 0+3 = 3, la y poi cresce di 3 sempre
                }//questo mi porta a creare un mdoo per salire, in GameEngine, una costante Jump che cambia lo stato (vedere tutto ciò che lega il gameState
            }//in appContents creo la VELOCITY_WHEN_JUMPED;
            canvas.drawBitmap(AppConstants.getBitmapBank().getPawn(), poke_pawn.getX(), poke_pawn.getY(), null);

    }

    public void updateAndDrawBoard(Canvas canvas){

            canvas.drawBitmap(AppConstants.getBitmapBank().getBoard(), board.getBoardImageX() , board.getBoardImageY(), null);


//        if(xspeed < 0){//if speed < 0 has left orientation
////            poke_pawn has to be reflected
//        }
//        if(gameState == 0){
//            //start game thing
//            canvas.drawBitmap(AppConstants.getBitmapBank().getPawn(),
//                    poke_pawn.getX() + AppConstants.getBitmapBank().getBackgroundWidth(),
//                    poke_pawn.getY(),
//                    null);
//        }
//        else if (gameState == 1){
//
//        }
//
//        else if(gameState == 2){
////336
//        }
//
//        else{
//            //ERRORE
//        }


    }



}
