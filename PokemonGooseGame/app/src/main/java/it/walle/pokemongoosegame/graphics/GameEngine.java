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
import it.walle.pokemongoosegame.game.PlayerNotInGameException;

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

    // Note that the following constants cannot be instantiated in AppConstants since they need a reference to BitmapBank, which currrently needs an AppConstants reference to be instantiated.
    // board screen constants
    public final int
            CELLS_IN_A_SCREEN,
            CELLS_IN_A_ROW,
            CELLS_IN_A_COL,
            WIDTH_MARGIN,
            HEIGHT_MARGIN;

    // Pawn screen constants
    public final int
        PAWN_BASE_X,
        PAWN_BASE_Y;

    Background backgroundImg;
    static int gameState;
    static int xspeed, yspeed;
    private int xspeedtemp;
    BitmapBank bitmapBank;

    // Pawns used to track players board position
    private final Map<String, PokePawn> pawns = new HashMap<>();

    // Matrix which will hold graphic cells of current board screen.
    // The indexes of this matrix correspond to the indexes used to display  the grapic cells of current board screen, with displayedCells[0][0] corresponding to the bottom left cell
    private final GraphicCell[][] displayedCells;

    // Used by threads to do updates only when necessary
    private final Semaphore boardSemaphore, pawnSemaphore;


    public GameEngine(Context context) {
        bitmapBank = new BitmapBank(context.getResources(), context);
        backgroundImg = new Background();//initialize bg

        // initializes board screen constants
        CELLS_IN_A_ROW = (AppConstants.getInstance(context).SCREEN_WIDTH - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH -
                AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellWidth();
        CELLS_IN_A_COL = (AppConstants.getInstance(context).SCREEN_HEIGHT - AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellHeight();
        CELLS_IN_A_SCREEN = CELLS_IN_A_COL * CELLS_IN_A_ROW;

        // initializes displayed board cells
        displayedCells = new GraphicCell[CELLS_IN_A_COL][CELLS_IN_A_ROW];

        // Initializes surface updater thread semaphores
        boardSemaphore = new Semaphore(0);
        pawnSemaphore = new Semaphore(0);

        // Initializes pawn screen constants to bottom left corner of surface view
        PAWN_BASE_X = AppConstants.getInstance(context).CELL_MARGIN;
        PAWN_BASE_Y = AppConstants.getInstance(context).SCREEN_HEIGHT - bitmapBank.getCellWidth();

        WIDTH_MARGIN = (AppConstants.getInstance(context).SCREEN_WIDTH - AppConstants.getInstance(context).LEFT_GAME_MENU_WIDTH -
                CELLS_IN_A_ROW * (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth())) / 2;

        HEIGHT_MARGIN = (AppConstants.getInstance(context).SCREEN_HEIGHT - CELLS_IN_A_COL * (AppConstants.getInstance(context).CELL_MARGIN +
                bitmapBank.getCellHeight())) / 2;

//TODO define a speed, tip: Use the cell dimension
//        xspeed = AppConstants.getBitmapBank().getBoardWidth() / 10;
//        yspeed = AppConstants.getBitmapBank().getBoardHeight() / 5;
        //states  0 = Not started, 1 = Playing, 2 = GameOVer;
        gameState = 0;//Game not started, so have to change it later

        // Creates a pawn for every player
        DAOSprite daoSprite = new DAOSprite(context);
        for (Player p : CoreController.getReference().getPlayers()){
            PokePawn pawn = new PokePawn();

            // When sprite is ready stores a reference to it in the pawn
            // FIXME: sprite is not drawn on screen (disegna solo quel granchio di merda)
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

    public Map<String, PokePawn> getPawns() {
        return pawns;
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

    private void findOccupiedDisplayedCell(FindOccupiedDisplayedCellBean bean){

            // Finds displayed cell currently occupied by the player with its board screen coordinates
            int playerPosition = CoreController.getReference().getPlayerByUsername(bean.getPlayerUsername()).getCurrentPosition();
            GraphicCell currentCell;
            boolean found = false;
            for (int r = 0; r < displayedCells.length && !found; r ++){
                for (int c = 0; c < displayedCells[0].length; c ++){
                    currentCell = displayedCells[r][c];
                    if (currentCell != null && currentCell.getCellIndex() == playerPosition){
                        bean.setCell(currentCell);
                        bean.setCol(c);
                        bean.setRow(r);
                        found = true;
                        break;
                    }
                }
            }
    }

    public void updateAndDrawPawns(Canvas canvas, Context context) {
        //Implement the feature where I check the pokeomn and position
        //as dummy I'll use a constant, but the position and wichi scree should be passed as Parameter!

        // Updates position of every pawn
        BitmapBank bitmapBank = new BitmapBank(context.getResources(), context);

        // Clears previously drawn board
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        synchronized (displayedCells){
            // locks displayed cells matrix

            // Updates position of every pawn
            pawns.forEach(new BiConsumer<String, PokePawn>() {
                @Override
                public void accept(String playerUsername, PokePawn pokePawn) {

                    // Updates pawn position only if it's on current board screen
                    try {
                        if (CoreController.getReference().getPlayerByUsername(playerUsername).getCurrentPosition() / CELLS_IN_A_SCREEN == currentBoardPage) {

                            // gets Graphic cell position of corresponding board cell occupied by the player owning the pawn
                            FindOccupiedDisplayedCellBean findOccupiedDisplayedCellBean = new FindOccupiedDisplayedCellBean();
                            findOccupiedDisplayedCellBean.setPlayerUsername(playerUsername);
                            Log.d(TAG, "before calling find cell ");
                            findOccupiedDisplayedCell(findOccupiedDisplayedCellBean);
                            Log.d(TAG, "cell in bean has index " + findOccupiedDisplayedCellBean.getCell().getCellIndex());

                            // Updates position of pawn
                            pokePawn.setX(
                                /*calculateXByCol(
                                        context,
                                        findOccupiedDisplayedCellBean.getCell(),
                                        findOccupiedDisplayedCellBean.getCol()
                                )
                                 */
                                    findOccupiedDisplayedCellBean.getCell().getCellImgX()
                            );
                            pokePawn.setY(
                                /*calculateYByRow(
                                        context,
                                        findOccupiedDisplayedCellBean.getCell(),
                                        findOccupiedDisplayedCellBean.getRow()
                                )*/
                                    findOccupiedDisplayedCellBean.getCell().getCellImgY()
                            );
                            Log.d(TAG, "pawn creation: used row " + findOccupiedDisplayedCellBean.getRow() + " , Y is " + pokePawn.getY());

                            // draws pawn
                            canvas.drawBitmap(bitmapBank.getPawn(),
                                    pokePawn.getX(),
                                    pokePawn.getY(),
                                    null);
                        }
                    } catch (PlayerNotInGameException e){
                        // Player not in players, no need to draw its pawn
                    }
                    }
            });

        }

    }

    private int calculateXByCol(Context context, GraphicCell graphicCell, int colToOccupy){

        return graphicCell.getCellImgX() + (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth()) * colToOccupy;
    }

    private int calculateYByRow(Context context, GraphicCell graphicCell, int row){
        return AppConstants.getInstance(context).SCREEN_HEIGHT - (bitmapBank.getCellWidth() + graphicCell.getCellImgY()) -
                (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * row;
    }

    public void updateAndDrawBoard(Canvas canvas, Context context) {


            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);

            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            int cell_path_direction, page_number_cell_path_direction;

            Board board = CoreController.getReference().getBoard();

            int cellIndex = currentBoardPage * CELLS_IN_A_SCREEN;


            synchronized (displayedCells){
                int cols = displayedCells[0].length;
                int rows = displayedCells.length;

                // clears state of displayed cells matrix
                for (int r = 0; r < rows; r ++){
                    displayedCells[r] = new GraphicCell[cols];
                }

                // Clears previously drawn board
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);

                // Draws cells of current board page
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {

                        // Initializes cell with starting position values
                        GraphicCell graphicCell = new GraphicCell();
                        graphicCell.setCellImgX(WIDTH_MARGIN);
                        graphicCell.setCellImgY(HEIGHT_MARGIN);

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
                            int colToOccupy;
                            if (i % 2 == 0) {
                                colToOccupy = j;
                            } else {
                                colToOccupy = cols - 1 - j;
                            }
                            cell_path_direction = calculateXByCol(context, graphicCell, colToOccupy);
                            page_number_cell_path_direction = graphicCell.getCellImgX() + AppConstants.getInstance(context).CELL_MARGIN * 2 + (AppConstants.getInstance(context).CELL_MARGIN +
                                    bitmapBank.getCellWidth()) * colToOccupy;

                            // Sets graphic cell fields with newly calculated values
                            graphicCell.setCellIndex(cellIndex);
                            graphicCell.setCellImgX(cell_path_direction);
                            graphicCell.setCellImgY(calculateYByRow(context, graphicCell, i));

                            // Draws cell
                            canvas.drawBitmap(
                                    bitmapBank.getCell(),
                                    graphicCell.getCellImgX(),
                                    graphicCell.getCellImgY(),
                                    null);

                            // Draws cell number
                            canvas.drawText(
                                    String.valueOf(cellIndex),
                                    page_number_cell_path_direction,
                                    AppConstants.getInstance(context).SCREEN_HEIGHT + AppConstants.getInstance(context).CELL_MARGIN * 4 - (bitmapBank.getCellWidth() + HEIGHT_MARGIN) - (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * i,
                                    paint
                            );

                            // Stores drawn cell in displayed cells matrix
                            displayedCells[i][colToOccupy] = graphicCell;

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


}

