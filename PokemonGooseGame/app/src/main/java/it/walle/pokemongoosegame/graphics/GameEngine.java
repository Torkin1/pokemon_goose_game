package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.database.pokeapi.DAOSprite;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.board.cell.BlueCell;
import it.walle.pokemongoosegame.entity.board.cell.Cell;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.PlayerNotInGameException;
import it.walle.pokemongoosegame.utils.DrawableGetter;
import it.walle.pokemongoosegame.utils.DrawableNotFoundException;

public class GameEngine {

    private final static String TAG = GameEngine.class.getSimpleName();

    private static GameEngine ref = null;
    public synchronized static GameEngine getInstance(Context context, SurfaceView svBoard){
        if (ref == null){
            ref = new GameEngine(context, svBoard);
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
            HEIGHT_MARGIN,
            BOARD_WIDTH,
    BOARD_HEIGHT;

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


    public GameEngine(Context context, SurfaceView svBoard) {
        bitmapBank = new BitmapBank(context.getResources(), context);
        backgroundImg = new Background();//initialize bg

        BOARD_HEIGHT = svBoard.getHeight();
        BOARD_WIDTH = svBoard.getWidth();

        // initializes board screen constants
        CELLS_IN_A_ROW = (BOARD_WIDTH -
                AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellWidth();
        CELLS_IN_A_COL = (BOARD_HEIGHT - AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellHeight();
        CELLS_IN_A_SCREEN = CELLS_IN_A_COL * CELLS_IN_A_ROW;

        // initializes displayed board cells
        displayedCells = new GraphicCell[CELLS_IN_A_COL][CELLS_IN_A_ROW];

        // Initializes surface updater thread semaphores
        boardSemaphore = new Semaphore(0);
        pawnSemaphore = new Semaphore(0);

        WIDTH_MARGIN = (BOARD_WIDTH -
                CELLS_IN_A_ROW * (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth())) / 2;

        HEIGHT_MARGIN = (BOARD_HEIGHT - CELLS_IN_A_COL * (AppConstants.getInstance(context).CELL_MARGIN +
                bitmapBank.getCellHeight())) / 2;

//TODO define a speed, tip: Use the cell dimension
//        xspeed = AppConstants.getBitmapBank().getBoardWidth() / 10;
//        yspeed = AppConstants.getBitmapBank().getBoardHeight() / 5;
        //states  0 = Not started, 1 = Playing, 2 = GameOVer;
        gameState = 0;//Game not started, so have to change it later

        // Creates a pawn for every player
        for (Player p : CoreController.getReference().getPlayers()){
            PokePawn pawn = new PokePawn();

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

        // Updates position of every pawn
        BitmapBank bitmapBank = new BitmapBank(context.getResources(), context);

        // Clears previously drawn board
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        Semaphore spriteSemaphore = new Semaphore(0);

        // locks displayed cells matrix
        synchronized (displayedCells){

            // Updates position of every pawn
            pawns.forEach(new BiConsumer<String, PokePawn>() {
                @Override
                public void accept(String playerUsername, PokePawn pokePawn) {

                    // Updates pawn position only if it's on current board screen
                    try {

                        // gets Graphic cell position of corresponding board cell occupied by the player owning the pawn
                        FindOccupiedDisplayedCellBean findOccupiedDisplayedCellBean = new FindOccupiedDisplayedCellBean();
                        findOccupiedDisplayedCellBean.setPlayerUsername(playerUsername);
                        findOccupiedDisplayedCell(findOccupiedDisplayedCellBean);

                        if (findOccupiedDisplayedCellBean.getCell() != null){
                            // Updates position of pawn
                            pokePawn.setX(
                                    findOccupiedDisplayedCellBean.getCell().getCellImgX()
                            );
                            pokePawn.setY(
                                    findOccupiedDisplayedCellBean.getCell().getCellImgY()
                            );

                            // When sprite is ready stores a reference to it in the pawn
                            // FIXME: sprite is not drawn on screen (disegna solo quel granchio di merda)
                            if (pokePawn.getSprite() == null){
                                DAOSprite daoSprite = new DAOSprite(context);
                                Player player = CoreController.getReference().getPlayerByUsername(playerUsername);
                                daoSprite
                                        .loadSprite(
                                                player.getPokemon().getSprites().getFront_default(),
                                                new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap response) {

                                                        // sets pawn sprite with pokemon sprite and draws pawn
                                                        pokePawn.setSprite(bitmapBank.scalePawn(response));
                                                        canvas.drawBitmap(pokePawn.getSprite(),
                                                                pokePawn.getX(),
                                                                pokePawn.getY(),
                                                                null);
                                                        spriteSemaphore.release();
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e(TAG, error.getMessage(), error);
                                                        spriteSemaphore.release();
                                                    }
                                                }
                                        );
                            } else {

                                // draws pawn
                                canvas.drawBitmap(pokePawn.getSprite(),
                                        pokePawn.getX(),
                                        pokePawn.getY(),
                                        null);
                                spriteSemaphore.release();
                            }

                        } else {
                            // pawns is not on screen, no needed to update, releases token
                            spriteSemaphore.release();
                        }



                    } catch (PlayerNotInGameException e){
                        // Player not in players, no need to draw its pawn
                    }
                    }
            });
            try{

                // waits for listeners to download sprites if they were fired
                spriteSemaphore.acquire(pawns.size());
            } catch (InterruptedException e){
                Log.e(TAG, e.getMessage(), e);
            }

        }

    }

    private int calculateXByCol(Context context, GraphicCell graphicCell, int colToOccupy){

        return graphicCell.getCellImgX() + (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth()) * colToOccupy;
    }

    private int calculateYByRow(Context context, GraphicCell graphicCell, int row){
        return BOARD_HEIGHT - (bitmapBank.getCellWidth() + graphicCell.getCellImgY()) -
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

            // locks displayed cells matrix
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

                        // Checks if there are more cells to be drawn
                        if (cellIndex < board.getCells().size()) {

                            Cell boardCell = board.getCells().get(cellIndex);

                            // Sets cell color
                            bitmapBank.setCellRes(boardCell.getClass());

                            // Initializes cell with starting position values
                            GraphicCell graphicCell = new GraphicCell();
                            graphicCell.setCellImgX(WIDTH_MARGIN);
                            graphicCell.setCellImgY(HEIGHT_MARGIN);

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

                            //Draws the title if the cell it's a kind of BlueCell
                            if (boardCell instanceof BlueCell){
                                BlueCell boardBlueCell = (BlueCell) boardCell;
                                canvas.drawText(boardBlueCell.getTitle(), page_number_cell_path_direction,
                                        AppConstants.getInstance(context).SCREEN_HEIGHT + AppConstants.getInstance(context).CELL_MARGIN * 4 - (bitmapBank.getCellWidth() + HEIGHT_MARGIN) -
                                                (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * i, paint);
                            }


                            // Draws cell board index
                            canvas.drawText(String.valueOf(cellIndex),
                                    (page_number_cell_path_direction + bitmapBank.getCellWidth() * 2 / 3),
                                    AppConstants.getInstance(context).SCREEN_HEIGHT + AppConstants.getInstance(context).CELL_MARGIN * 4 -
                                            (bitmapBank.getCellWidth() - HEIGHT_MARGIN + AppConstants.getInstance(context).CELL_MARGIN) -
                                            (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * i,
                                    paint);

                            // Draws type icon, if it's available
                            int typeDrawableId = 0;
                            String id_name = board.getCells().get(cellIndex).getType();
                            if (id_name != null) {

                                try {
                                    typeDrawableId = DrawableGetter.getReference().getTypeDrawableId(id_name);
                                    Bitmap type_icon = ((BitmapDrawable) ResourcesCompat.getDrawable(context.getResources(), typeDrawableId, null)).getBitmap();
                                    type_icon = bitmapBank.scaleTypeIcon(type_icon);
                                    canvas.drawBitmap(type_icon,
                                            page_number_cell_path_direction,
                                            AppConstants.getInstance(context).SCREEN_HEIGHT  - bitmapBank.getCellWidth()/4 -
                                                    AppConstants.getInstance(context).CELL_MARGIN - HEIGHT_MARGIN  -
                                                    (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * i,
                                            null);

                                } catch (DrawableNotFoundException e) {
                                    Log.e(TAG, e.getMessage(), e);
                                }


                            }

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

