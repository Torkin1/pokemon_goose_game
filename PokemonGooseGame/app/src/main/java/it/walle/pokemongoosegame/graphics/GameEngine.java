package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;

import it.walle.pokemongoosegame.database.pokeapi.DAOSprite;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.game.CoreController;

public class GameEngine {

    private final static String TAG = GameEngine.class.getSimpleName();

    private static GameEngine ref = null;
    public synchronized static GameEngine getInstance(Context context){
        if (ref == null){
            ref = new GameEngine(context);
        }
        return  ref;
    }

    private int currentBoardPage = 0;    // Current drawn board page

    // board screen constants
    // Note that these constants cannot be instantiated in AppConstants since they need a reference to BitmapBank, which currrently needs an AppConstants reference to be instantiated.
    public final int
            CELLS_IN_A_SCREEN,
            CELLS_IN_A_ROW,
            CELLS_IN_A_COL;

    Background backgroundImg;
    static int gameState;
    static int xspeed, yspeed;
    private int xspeedtemp;
    BitmapBank bitmapBank;

    // Pawns used to track players board position
    private final Map<String, PokePawn> pawns = new HashMap<>();

    // Used by threads to do updates only when necessary
    private final Semaphore boardSemaphore, pawnSemaphore;

    private int width_margin, height_margin;

    public GameEngine(Context context) {
        bitmapBank = new BitmapBank(context.getResources(), context);
        backgroundImg = new Background();//initialize bg

        // Initializes semaphores
        boardSemaphore = new Semaphore(0);
        pawnSemaphore = new Semaphore(0);

        // initializes board screen constants
        CELLS_IN_A_ROW = (AppConstants.getInstance(context).SCREEN_WIDTH - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH -
                AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellWidth();
        CELLS_IN_A_COL = (AppConstants.getInstance(context).SCREEN_HEIGHT - AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellHeight();
        CELLS_IN_A_SCREEN = CELLS_IN_A_COL * CELLS_IN_A_ROW;

//TODO define a speed, tip: Use the cell dimension
//        xspeed = AppConstants.getBitmapBank().getBoardWidth() / 10;
//        yspeed = AppConstants.getBitmapBank().getBoardHeight() / 5;
        //states  0 = Not started, 1 = Playing, 2 = GameOVer;
        gameState = 0;//Game not started, so have to change it later

        // Creates a pawn for every player
        DAOSprite daoSprite = new DAOSprite(context);
        for (Player p : CoreController.getReference().getPlayers()){
            PokePawn pawn = new PokePawn();

            // Initializes pawn position to first cell
            pawn.setX(
                    width_margin + (AppConstants.getInstance(context).CELL_MARGIN)
            );
            pawn.setY(
                    AppConstants.getInstance(context).SCREEN_HEIGHT - (bitmapBank.getCellWidth() + height_margin)
            );

            // When sprite is ready stores a reference to it in the pawn
            daoSprite
                    .loadSprite(
                                    p.getPokemon().getSprites().getFront_default(),
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap response) {
                                            pawn.setSprite(new BitmapDrawable(context.getResources(), response));
                                        }
                                    },
                                    null
                            );

            // puts prepared pawn alongside other ones
            pawns.put(p.getUsername(), pawn);
        }

    }

    public Semaphore getBoardSemaphore() {
        return boardSemaphore;
    }

    public Semaphore getPawnSemaphore() {
        return pawnSemaphore;
    }

    public int getCurrentBoardPage() {
        return currentBoardPage;
    }

    public void setCurrentBoardPage(int currentBoardPage) {
        this.currentBoardPage = currentBoardPage;
        boardSemaphore.release();
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


    public void updateAndDrawPawns(Canvas canvas, Context context) {
        //Implement the feature where I check the pokeomn and position
        //as dummy I'll use a constant, but the position and wichi scree should be passed as Parameter!

        // Updates position of every pawn
        BitmapBank bitmapBank = new BitmapBank(context.getResources(), context);

        // Clears previously drawn board
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        // Updates position of every pawn
        pawns.forEach(new BiConsumer<String, PokePawn>() {
            @Override
            public void accept(String playerUsername, PokePawn pokePawn) {

                /*TODO: implement movement of the pawns
                // movement can be done either to the left or to the right
                */
                /*
                // Updates pawn X position
                pokePawn.setX(pokePawn.getX() + bitmapBank.getCellWidth());

                // Updates pawn Y position
                if (pokePawn.getY() < (AppConstants.getInstance(context).SCREEN_HEIGHT - bitmapBank.getPawnHeight()) || pokePawn.getBgImageVelocity() < 0) {//TODO better
                    pokePawn.setPokePawnImgVelocity(pokePawn.getBgImageVelocity() + AppConstants.gravity);
                    pokePawn.setY(pokePawn.getY() + pokePawn.getBgImageVelocity());//all'inizio velocità è 0, e gravità 3, 0+3 = 3, la y poi cresce di 3 sempre
                }//questo mi porta a creare un mdoo per salire, in GameEngine, una costante Jump che cambia lo stato (vedere tutto ciò che lega il gameState


                 */
            //in appContents creo la VELOCITY_WHEN_JUMPED;

                if (CoreController.getReference().getPlayerByUsername(playerUsername).getCurrentPosition() / CELLS_IN_A_SCREEN == currentBoardPage){
                    canvas.drawBitmap(bitmapBank.getPawn(),
                            pokePawn.getX(),
                            pokePawn.getY(),
                            null);
                }
            }
        });
    }

    public void updateAndDrawBoard(Canvas canvas, Context context) {

        // Clears previously drawn board
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);


        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        int cell_path_direction, page_number_cell_path_direction;

        int cols = CELLS_IN_A_ROW;

        int rows = CELLS_IN_A_COL;

        width_margin = (AppConstants.getInstance(context).SCREEN_WIDTH - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH -
                cols * (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth())) / 2;

        height_margin = (AppConstants.getInstance(context).SCREEN_HEIGHT - rows * (AppConstants.getInstance(context).CELL_MARGIN +
                bitmapBank.getCellHeight())) / 2;

        int cellIndex = currentBoardPage * CELLS_IN_A_SCREEN;

        Board board = CoreController.getReference().getBoard();

        // Draws cells of current board page
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                // Initializes cell with starting position values
                GraphicCell graphicCell = new GraphicCell();
                graphicCell.setCellImgX(width_margin);
                graphicCell.setCellImgY(height_margin);

                if (cellIndex < board.getCells().size()) {

                    // Sets cell color
                    bitmapBank
                            .setCellRes(
                                    board
                                            .getCells()
                                            .get(cellIndex)
                                            .getClass()
                            );

                    // gives the board a "snake" orientation
                    if (i % 2 == 0) {
                        cell_path_direction = graphicCell.getCellImgX() + (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth()) * j;
                        page_number_cell_path_direction = graphicCell.getCellImgX() + AppConstants.getInstance(context).CELL_MARGIN * 2 + (AppConstants.getInstance(context).CELL_MARGIN +
                                bitmapBank.getCellWidth()) * j;
                    } else {
                        cell_path_direction = (graphicCell.getCellImgX() + (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth()) * (cols - 1 - j));
                        page_number_cell_path_direction = graphicCell.getCellImgX() + AppConstants.getInstance(context).CELL_MARGIN * 2 + (AppConstants.getInstance(context).CELL_MARGIN +
                                bitmapBank.getCellWidth()) * (cols - 1 - j);
                    }

                    // Sets graphic cell fields with newly calculated values
                    graphicCell.setCellIndex(cellIndex);
                    graphicCell.setCellImgX(cell_path_direction);
                    graphicCell.setCellImgY(AppConstants.getInstance(context).SCREEN_HEIGHT - (bitmapBank.getCellWidth() + graphicCell.getCellImgY()) -
                            (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * i);

                    // Draws cell
                    canvas.drawBitmap(
                            bitmapBank.getCell(),
                            graphicCell.getCellImgX(),
                            graphicCell.getCellImgY(),
                            null);

                    // Draws cell number
                    canvas.drawText(String.valueOf(cellIndex), page_number_cell_path_direction,
                            AppConstants.getInstance(context).SCREEN_HEIGHT + AppConstants.getInstance(context).CELL_MARGIN * 4 - (bitmapBank.getCellWidth() + height_margin) -
                                    (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * i, paint);

                    cellIndex++;

                } else {

                    // There are no more cells to be drawn, cycle must end
                    j = cols;
                    i = rows;
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

