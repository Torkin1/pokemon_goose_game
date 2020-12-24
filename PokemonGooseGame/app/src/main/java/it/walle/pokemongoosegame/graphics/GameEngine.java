package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.concurrent.Semaphore;

import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.game.CoreController;

public class GameEngine {

    private static GameEngine ref = null;
    public static GameEngine getInstance(Context context){
        if (ref == null){
            ref = new GameEngine(context);
        }
        return  ref;
    }

    private int currentBoardPage = 0;    // Current drawn board page

    Background backgroundImg;
    PokePawn poke_pawn;
    Cell cell;
    static int gameState;
    static int xspeed, yspeed;
    private int xspeedtemp;
    BitmapBank bitmapBank;

    private int width_margin, height_margin;

    public GameEngine(Context context) {
        System.out.println("Nel Costruttore di GameEngine ");
        bitmapBank = new BitmapBank(context.getResources(), context);

        backgroundImg = new Background();//initialize bg
        System.out.println("Nel Costruttore di GameEngine dopo bg ");

        poke_pawn = new PokePawn(context);//initilialize pawn
        System.out.println("Nel Costruttore di GameEngine dopo la pawn");

        cell = new Cell(context);
        System.out.println("Nel Costruttore di GameEngine dopo la cell");

//TODO define a speed, tip: Use the cell dimension
//        xspeed = AppConstants.getBitmapBank().getBoardWidth() / 10;
//        yspeed = AppConstants.getBitmapBank().getBoardHeight() / 5;
        //states  0 = Not started, 1 = Playing, 2 = GameOVer;
        gameState = 0;//Game not started, so have to change it later

    }

    public int getCurrentBoardPage() {
        return currentBoardPage;
    }

    public void setCurrentBoardPage(int currentBoardPage) {
        this.currentBoardPage = currentBoardPage;
        Log.d("Burp", "page changed to " + this.getCurrentBoardPage());
    }

    public void updateAndDrawBackgroundImage(Canvas canvas, Context context) {

        backgroundImg.setX(backgroundImg.getX() - backgroundImg.getBgImageVelocity());
        //moving animation
        //if it ends have to start it back from 0
        if (backgroundImg.getX() < -bitmapBank.getBackgroundWidth())
            backgroundImg.setX(0);

        canvas.drawBitmap(bitmapBank.getBackground(),
                backgroundImg.getX(),
                backgroundImg.getY(),
                null);
        //if the image end, it will leave a long line, to prevent this I'll restart the bg

        if (backgroundImg.getX() < -(bitmapBank.getBackgroundWidth() - AppConstants.getInstance(context).SCREEN_WIDTH)) {
            canvas.drawBitmap(bitmapBank.getBackground(),
                    backgroundImg.getX() + bitmapBank.getBackgroundWidth(),
                    backgroundImg.getY(),
                    null);
        }
    }


    public void updateAndDrawPawn(Canvas canvas, Context context) {
        //TODO
        //Implement the feature where I check the pokeomn and position
        //as dummy I'll use a constant, but the position and wichi scree should be passed as Parameter!

        if (gameState == 1) {//ora che ho lo stato la parte sotto dell'if la metto qui dentro
            if (poke_pawn.getY() < (AppConstants.getInstance(context).SCREEN_HEIGHT - bitmapBank.getPawnHeight()) || poke_pawn.getBgImageVelocity() < 0) {//TODO better
                poke_pawn.setPokePawnImgVelocity(poke_pawn.getBgImageVelocity() + AppConstants.gravity);
                poke_pawn.setY(poke_pawn.getY() + poke_pawn.getBgImageVelocity());//all'inizio velocità è 0, e gravità 3, 0+3 = 3, la y poi cresce di 3 sempre
            }//questo mi porta a creare un mdoo per salire, in GameEngine, una costante Jump che cambia lo stato (vedere tutto ciò che lega il gameState
        }//in appContents creo la VELOCITY_WHEN_JUMPED;

        if (AppConstants.DISPLAYED_SCREEN == AppConstants.PAWNS_SCREEN) {

            canvas.drawBitmap(bitmapBank.getPawn(),
                    width_margin + (AppConstants.getInstance(context).CELL_MARGIN),
                    AppConstants.getInstance(context).SCREEN_HEIGHT - (bitmapBank.getCellWidth() + height_margin),
                    null);
        } else {
            //TODO
        }
    }

    public void updateAndDrawBoard(Canvas canvas, Context context) {

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);


        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        int cell_path_direction, page_number_cell_path_direction;

        int cols = (AppConstants.getInstance(context).SCREEN_WIDTH - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH -
                AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellWidth();

        int rows = (AppConstants.getInstance(context).SCREEN_HEIGHT - AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellHeight();

        width_margin = (AppConstants.getInstance(context).SCREEN_WIDTH - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH -
                cols * (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth())) / 2;

        height_margin = (AppConstants.getInstance(context).SCREEN_HEIGHT - rows * (AppConstants.getInstance(context).CELL_MARGIN +
                bitmapBank.getCellHeight())) / 2;

        cell.setCellImgX(width_margin);
        cell.setCellImgY(height_margin);

        int cellStartIndex = currentBoardPage * AppConstants.getInstance(context).CELLS_IN_A_SCREEN;
        int cellEndIndex = cellStartIndex + AppConstants.getInstance(context).CELLS_IN_A_SCREEN;
        int cellIndex = cellStartIndex;

        Board board = CoreController.getReference().getBoard();

        // Draws cells of current board page
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                if (cellIndex < board.getCells().size()) {

                    // Draws cell color
                    bitmapBank
                            .setCellRes(
                                    board
                                            .getCells()
                                            .get(cellIndex)
                                            .getClass()
                            );

                    // gives the board a "snake" orientation
                    if (j % 2 == 0) {
                        cell_path_direction = cell.getCellImgX() + (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth()) * i;
                        page_number_cell_path_direction = cell.getCellImgX() + AppConstants.getInstance(context).CELL_MARGIN * 2 + (AppConstants.getInstance(context).CELL_MARGIN +
                                bitmapBank.getCellWidth()) * i;
                    } else {
                        cell_path_direction = (cell.getCellImgX() + (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth()) * (cols - 1 - i));
                        page_number_cell_path_direction = cell.getCellImgX() + AppConstants.getInstance(context).CELL_MARGIN * 2 + (AppConstants.getInstance(context).CELL_MARGIN +
                                bitmapBank.getCellWidth()) * (cols - 1 - i);
                    }
                    canvas.drawBitmap(bitmapBank.getCell(),
                            cell_path_direction,
                            AppConstants.getInstance(context).SCREEN_HEIGHT - (bitmapBank.getCellWidth() + cell.getCellImgY()) -
                                    (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * j, null);

                    // Draws cell number
                    canvas.drawText(String.valueOf(cellIndex), page_number_cell_path_direction,
                            AppConstants.getInstance(context).SCREEN_HEIGHT + AppConstants.getInstance(context).CELL_MARGIN* 4 - (bitmapBank.getCellWidth() + height_margin) -
                                    (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * j, paint);

                    cellIndex ++;

                } else{

                    // There are no more cells to be drawn, cycle must end
                    i = cols;
                    j = rows;
                }
            }

        }


        if (!AppConstants.isDrawable) {
            System.out.println("è tutto falso dice: " + AppConstants.isDrawable);
            AppConstants.DONE_CELLS = cellIndex;
            AppConstants.isDrawable = !AppConstants.isDrawable;


        }


    }

}

