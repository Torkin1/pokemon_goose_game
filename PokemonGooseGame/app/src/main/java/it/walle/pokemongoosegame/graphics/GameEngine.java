package it.walle.pokemongoosegame.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameEngine {
    Background backgroundImg;
    PokePawn poke_pawn;
    Board board;
    Cell cell;
    static int gameState;
    static int xspeed, yspeed;
    private int xspeedtemp;

    private int width_margin, height_margin;


    public GameEngine() {
        backgroundImg = new Background();//initialize bg
        poke_pawn = new PokePawn();//initilialize pawn
        board = new Board();//initilize the board
        cell = new Cell();


        xspeed = AppConstants.getBitmapBank().getBoardWidth() / 10;
        yspeed = AppConstants.getBitmapBank().getBoardHeight() / 5;
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

        if (backgroundImg.getX() < -(AppConstants.getBitmapBank().getBackgroundWidth() - AppConstants.SCREEN_WIDTH)) {
            canvas.drawBitmap(AppConstants.getBitmapBank().getBackground(),
                    backgroundImg.getX() + AppConstants.getBitmapBank().getBackgroundWidth(),
                    backgroundImg.getY(),
                    null);
        }
    }


    public void updateAndDrawPawn(Canvas canvas) {
        //TODO
        //Implement the feature where I check the pokeomn and position
        //as dummy I'll use a constant, but the position and wichi scree should be passed as Parameter!


        if (gameState == 1) {//ora che ho lo stato la parte sotto dell'if la metto qui dentro
            if (poke_pawn.getY() < (AppConstants.SCREEN_HEIGHT - AppConstants.getBitmapBank().getPawnHeight()) || poke_pawn.getBgImageVelocity() < 0) {//TODO better
                poke_pawn.setPokePawnImgVelocity(poke_pawn.getBgImageVelocity() + AppConstants.gravity);
                poke_pawn.setY(poke_pawn.getY() + poke_pawn.getBgImageVelocity());//all'inizio velocità è 0, e gravità 3, 0+3 = 3, la y poi cresce di 3 sempre
            }//questo mi porta a creare un mdoo per salire, in GameEngine, una costante Jump che cambia lo stato (vedere tutto ciò che lega il gameState
        }//in appContents creo la VELOCITY_WHEN_JUMPED;

        if (AppConstants.DISPLAYED_SCREEN == AppConstants.PAWNS_SCREEN) {

            canvas.drawBitmap(AppConstants.getBitmapBank().getPawn(),
                    width_margin + (AppConstants.CELL_MARGIN),
                    AppConstants.SCREEN_HEIGHT - (AppConstants.getBitmapBank().getCellWidth() + height_margin),
                    null);
        } else {
            //TODO
        }
    }

    public void updateAndDrawCell(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);


        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        int counter = AppConstants.DONE_CELLS;
        System.out.println("The cells are at the beginig: " + AppConstants.DONE_CELLS);
        int totalCells = 69;
        int cell_path_direction, page_number_cell_path_direction;

        int cols = (AppConstants.SCREEN_WIDTH - AppConstants.LEFT_GAME_MENU_WIDTH -
                AppConstants.CELL_MARGIN) / AppConstants.getBitmapBank().getCellWidth();

        int rows = (AppConstants.SCREEN_HEIGHT - AppConstants.CELL_MARGIN) / AppConstants.getBitmapBank().getCellHeight();

        width_margin = (AppConstants.SCREEN_WIDTH - AppConstants.LEFT_GAME_MENU_WIDTH -
                cols * (AppConstants.CELL_MARGIN + AppConstants.getBitmapBank().getCellWidth())) / 2;

        height_margin = (AppConstants.SCREEN_HEIGHT - rows * (AppConstants.CELL_MARGIN +
                AppConstants.getBitmapBank().getCellHeight())) / 2;

        cell.setCellImgX(width_margin);
        cell.setCellImgY(height_margin);

        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                if (counter < AppConstants.TOTAL_CELLS) {
                    counter++;

                    if (counter == 12 || counter == 22 || counter == 32 || counter == 42 || counter == 52 || counter == 62)
                        AppConstants.getBitmapBank().setCellResBlue();
                    else if (counter == 17 || counter == 27 || counter == 37 || counter == 47 || counter == 57 || counter == 67)
                        AppConstants.getBitmapBank().setCellResYellow();
                    else
                        AppConstants.getBitmapBank().setCellRes();

                    if (j % 2 == 0) {
                        cell_path_direction = cell.getCellImgX() + (AppConstants.CELL_MARGIN + AppConstants.getBitmapBank().getCellWidth()) * i;
                        page_number_cell_path_direction = cell.getCellImgX() + AppConstants.CELL_MARGIN * 2 + (AppConstants.CELL_MARGIN +
                                AppConstants.getBitmapBank().getCellWidth()) * i;
                    } else {
                        cell_path_direction = (cell.getCellImgX() + (AppConstants.CELL_MARGIN + AppConstants.getBitmapBank().getCellWidth()) * (cols - 1 - i));
                        page_number_cell_path_direction = cell.getCellImgX() + AppConstants.CELL_MARGIN * 2 + (AppConstants.CELL_MARGIN +
                                AppConstants.getBitmapBank().getCellWidth()) * (cols - 1 - i);
                    }
                    canvas.drawBitmap(AppConstants.getBitmapBank().getCell(),
                            cell_path_direction,
                            AppConstants.SCREEN_HEIGHT - (AppConstants.getBitmapBank().getCellWidth() + cell.getCellImgY()) -
                                    (AppConstants.getBitmapBank().getCellWidth() + AppConstants.CELL_MARGIN) * j, null);


                    canvas.drawText(String.valueOf(counter), page_number_cell_path_direction,
                            AppConstants.SCREEN_HEIGHT + AppConstants.CELL_MARGIN * 4 - (AppConstants.getBitmapBank().getCellWidth() + height_margin) -
                                    (AppConstants.getBitmapBank().getCellWidth() + AppConstants.CELL_MARGIN) * j, paint);

                } else {
                    AppConstants.TOTAL_SCREENS = AppConstants.DISPLAYED_SCREEN;
                    System.out.println("Ciao gli screen sono: " + AppConstants.TOTAL_SCREENS + " Ti trovi al: " + AppConstants.DISPLAYED_SCREEN);
                    break;//possible thing its to get the point where the last one was drawn, and get it as the end!
                }
            }

        }
        if(AppConstants.DONE_CELLS == 0)
            AppConstants.CELLS_IN_A_SCREEN = counter;

        if (!AppConstants.DRAWABLE) {
            System.out.println("è tutto falso dice: " + AppConstants.DRAWABLE);
            System.out.println("totali: "+AppConstants.TOTAL_CELLS + " counter is: " + counter + "if you need remainings: "+
                    AppConstants.DONE_CELLS);
            AppConstants.DONE_CELLS = counter;

            System.out.println("totals: "+AppConstants.TOTAL_CELLS + " counter is: " + counter + "if you need remainings after: "+
                    AppConstants.DONE_CELLS + "The screen is: " + AppConstants.DISPLAYED_SCREEN +
                    "Total of screen is " + AppConstants.TOTAL_SCREENS);
            AppConstants.DRAWABLE = !AppConstants.DRAWABLE;
        }
        if (AppConstants.DONE_CELLS > 0) {
            //make the button to go up availible, and and hide the pawns if no pawns are in the new board
            //set the buytton dawn availible if I'm Up,
            //use a different method not this for checking the up and dawn after the board is created!
            //remeber to keep trace of the board pages

//            GameView.class.getMethod(setPageUp).
        }
    }

    public void updateAndDrawBoard(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        canvas.drawBitmap(AppConstants.getBitmapBank().getBoard(), board.getBoardImageX(), board.getBoardImageY(), null);
        canvas.drawText("Some Text", board.getBoardImageX(), board.getBoardImageY(), paint);

//            canvas.drawText("Ciaooo", 300 ,100, Paint.FontMetrics.class);
//            canvas.translate(300,120);


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
