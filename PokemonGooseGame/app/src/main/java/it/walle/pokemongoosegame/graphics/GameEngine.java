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

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;
import java.util.zip.CheckedOutputStream;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.database.pokeapi.DAOSprite;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.board.cell.BlueCell;
import it.walle.pokemongoosegame.entity.board.cell.Cell;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.LoserBean;
import it.walle.pokemongoosegame.game.PlayerNotInGameException;
import it.walle.pokemongoosegame.utils.DrawableGetter;
import it.walle.pokemongoosegame.utils.DrawableNotFoundException;

public class GameEngine {

    private final static String TAG = GameEngine.class.getSimpleName();

    private static GameEngine ref = null;

    public synchronized static GameEngine getInstance(Context context, int boardHeight, int boardWidth) {

        // Call this at least once before using this class
        if (ref == null) {
            ref = new GameEngine(context, boardHeight, boardWidth);
        }
        return ref;
    }

    public synchronized static GameEngine getInstance() {
        if (ref == null) {
            throw new IllegalStateException("GameEngine.getInstance(Context, svBoard) must be called at least once in your app");
        }
        return ref;
    }

    public synchronized static void reset() {

        // Resets GameEngine state
        ref = null;
    }

    private int currentBoardPage = 0;    // Current drawn board page

    // Note that the following constants cannot be instantiated in AppConstants since they need a reference to BitmapBank, which currently needs an AppConstants reference to be instantiated.
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
    int in_cell_counter = 0;


    // Pawns used to track players board position
    private final Map<String, PokePawn> pawns = new HashMap<>();

    // Matrix which will hold graphic cells of current board screen.
    // The indexes of this matrix correspond to the indexes used to display  the grapic cells of current board screen, with displayedCells[0][0] corresponding to the bottom left cell
    private final GraphicCell[][] displayedCells;

    // Used by threads to do updates only when necessary
    private final Semaphore boardSemaphore, pawnSemaphore;


    public GameEngine(Context context, int boardHeight, int boardWidth) {
        bitmapBank = new BitmapBank(context.getResources(), context);
        backgroundImg = new Background();//initialize bg

        BOARD_HEIGHT = boardHeight;
        BOARD_WIDTH = boardWidth;

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
        for (Player p : CoreController.getReference().getPlayers()) {
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

    private void findOccupiedDisplayedCell(FindOccupiedDisplayedCellBean bean) {

        // Finds displayed cell currently occupied by the player with its board screen coordinates
        int playerPosition = CoreController.getReference().getPlayerByUsername(bean.getPlayerUsername()).getCurrentPosition();
        GraphicCell currentCell;
        boolean found = false;
        for (int r = 0; r < displayedCells.length && !found; r++) {
            for (int c = 0; c < displayedCells[0].length; c++) {
                currentCell = displayedCells[r][c];
                if (currentCell != null && currentCell.getCellIndex() == playerPosition) {
                    bean.setCell(currentCell);
                    bean.setCol(c);
                    bean.setRow(r);
                    found = true;
                    break;
                }
            }
        }
    }

    protected void setCounter(int counter) {

    }

    public void updateAndDrawPawns(Canvas canvas, Context context) {
        //Implement the feature where I check the pokeomn and position

        // Updates position of every pawn
        BitmapBank bitmapBank = new BitmapBank(context.getResources(), context);

        // Clears previously drawn board
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        //Initializing the health bar under the pokemon
        int hbar_width, hbar_height = 20, margin = 2;
        Paint borderPaint, healthPaint;

        //Giving to the border and hp bg defined colors
        borderPaint = new Paint();
        healthPaint = new Paint();

        int borderColor = ContextCompat.getColor(context, R.color.healthBarBorder);
        int healthColor = ContextCompat.getColor(context, R.color.healthBarHealth);

        borderPaint.setColor(borderColor);
        healthPaint.setColor(healthColor);


        //defin the sprite semaphore
        Semaphore spriteSemaphore = new Semaphore(0);

        // locks displayed cells matrix
        synchronized (displayedCells) {

            //hBar variables
            hbar_width = bitmapBank.getCellWidth();

            // Updates position of every pawn
            pawns.forEach(new BiConsumer<String, PokePawn>() {
                @Override
                public void accept(String playerUsername, PokePawn pokePawn) {

                    try {

                        Player player = CoreController.getReference().getPlayerByUsername(playerUsername);

                        // gets Graphic cell position of corresponding board cell occupied by the player owning the pawn
                        FindOccupiedDisplayedCellBean findOccupiedDisplayedCellBean = new FindOccupiedDisplayedCellBean();
                        findOccupiedDisplayedCellBean.setPlayerUsername(playerUsername);
                        findOccupiedDisplayedCell(findOccupiedDisplayedCellBean);

                        // Updates pawn position only if it's on current board screen
                        if (findOccupiedDisplayedCellBean.getCell() != null) {

                            // Pawn is on screen, it can be drawn
                            // Updates position of pawn
                            pokePawn.setX(
                                    findOccupiedDisplayedCellBean.getCell().getCellImgX()
                            );
                            pokePawn.setY(
                                    findOccupiedDisplayedCellBean.getCell().getCellImgY()
                            );

                            // When sprite is ready stores a reference to it in the pawn
                            if (pokePawn.getSprite() == null) {
                                DAOSprite daoSprite = new DAOSprite(context);
                                daoSprite
                                        .loadSprite(
                                                player.getPokemon().getSprites().getFront_default(),
                                                new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap response) {
                                                        String[] list_players_in_a_cell = CoreController.getReference()
                                                                .getAllPlayersInACellUsernames(player.getCurrentPosition());
                                                        int players_in_a_cell = list_players_in_a_cell
                                                                .length;
                                                        // sets pawn sprite with pokemon sprite and draws pawn, then informs pawn updater thread that we are done

                                                        int spirte_dim = bitmapBank.getCellWidth() / players_in_a_cell;
                                                        pokePawn.setSprite(bitmapBank.scalePawn(response, bitmapBank.getCellWidth(), bitmapBank.getCellWidth()));

                                                        Bitmap resized_sprite = bitmapBank.scalePawn(pokePawn.getSprite(), spirte_dim, spirte_dim);

                                                        canvas.drawBitmap(resized_sprite,
                                                                pokePawn.getX() + (float) bitmapBank.getCellWidth() / players_in_a_cell * in_cell_counter,
                                                                pokePawn.getY() + (float) bitmapBank.getCellWidth() / players_in_a_cell * in_cell_counter,
                                                                null);
                                                        spriteSemaphore.release();
                                                        if (players_in_a_cell > 1) {
                                                            in_cell_counter++;
                                                        } else {
                                                            in_cell_counter = 0;
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                        // Network error, sprite will not be available, we inform pawn updater thread that we have done
                                                        Log.e(TAG, error.getMessage(), error);
                                                        spriteSemaphore.release();
                                                    }
                                                }
                                        );
                            } else {

                                // Sprite is already loaded, draws pawn
                                String[] list_players_in_a_cell = CoreController.getReference()
                                        .getAllPlayersInACellUsernames(player.getCurrentPosition());
                                int players_in_a_cell = list_players_in_a_cell.length;


                                int spirte_dim = bitmapBank.getCellWidth() / players_in_a_cell;
                                pokePawn.setSprite(bitmapBank.scalePawn(pokePawn.getSprite(), bitmapBank.getCellWidth(), bitmapBank.getCellWidth()));

                                Bitmap resized_sprite = bitmapBank.scalePawn(pokePawn.getSprite(), spirte_dim, spirte_dim);

                                boolean in_this_cell = false;
                                for (int i = 0; i < players_in_a_cell; i++) {
                                    if (player.getUsername().compareTo(list_players_in_a_cell[i]) == 0 && list_players_in_a_cell.length > 1) {
                                        in_cell_counter = i;
                                        in_this_cell = true;
                                        break;
                                    }
                                }
                                if (list_players_in_a_cell.length < 2 || !in_this_cell)
                                    in_cell_counter = 0;

                                canvas.drawBitmap(resized_sprite,
                                        pokePawn.getX() + (float) bitmapBank.getCellWidth() / players_in_a_cell * in_cell_counter,
                                        pokePawn.getY() + (float) bitmapBank.getCellWidth() / players_in_a_cell * in_cell_counter,
                                        null);

                                in_cell_counter = 0;

                                spriteSemaphore.release();
                            }

                            // Draws health bar
                            //Distance, position and hp percentage variables
                            float x = (float) pokePawn.getX();
                            float y = (float) pokePawn.getY();
                            float distanceToPlayer = 30;

                            float healthPointPercentage = (float) player.getPokemon().getCurrentHp() / CoreController.MAX_HEALTH_POKEMON;

                            //draw Border
                            float borderLeft, borderTop, borderRight, borderBottom;
                            borderLeft = x - (float) hbar_width / 2;
                            borderRight = x + (float) hbar_width / 2;
                            borderBottom = y + distanceToPlayer;
                            borderTop = borderBottom - hbar_height;

                            canvas.drawRect(borderLeft + (float) (bitmapBank.getCellHeight() / 2), borderTop, borderRight + (float) bitmapBank.getCellHeight() / 2, borderBottom, borderPaint);

                            // Draw health
                            float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
                            healthWidth = hbar_width - 4 * margin;
                            healthHeight = hbar_height - 4 * margin;
                            healthLeft = borderLeft + 2 * margin;
                            healthRight = healthLeft + healthWidth * healthPointPercentage;
                            healthBottom = borderBottom - 2 * margin;
                            healthTop = healthBottom - healthHeight;

                            canvas.drawRect(healthLeft + (float) bitmapBank.getCellHeight() / 2, healthTop, healthRight + (float) bitmapBank.getCellHeight() / 2, healthBottom, healthPaint);

                        } else {

                            // pawn is not on screen, no need to update, releases token
                            spriteSemaphore.release();
                        }


                    } catch (PlayerNotInGameException | IllegalStateException e) {
                        // Player not in players, no need to draw its pawn
                        spriteSemaphore.release();
                    }
                }
            });
            try {

                // waits for listeners to download sprites
                spriteSemaphore.acquire(pawns.size());
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage(), e);
            }

        }

    }

    private int calculateXByCol(Context context, GraphicCell graphicCell, int colToOccupy) {

        return graphicCell.getCellImgX() + (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth()) * colToOccupy;
    }

    private int calculateYByRow(Context context, GraphicCell graphicCell, int row) {
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
        synchronized (displayedCells) {
            int cols = displayedCells[0].length;
            int rows = displayedCells.length;

            // clears state of displayed cells matrix
            for (int r = 0; r < rows; r++) {
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
                        if (boardCell instanceof BlueCell) {
                            BlueCell boardBlueCell = (BlueCell) boardCell;
                            canvas.drawText(boardBlueCell.getTitle(), page_number_cell_path_direction,
                                    AppConstants.getInstance(context).SCREEN_HEIGHT + AppConstants.getInstance(context).CELL_MARGIN * 4 - (bitmapBank.getCellWidth() + HEIGHT_MARGIN) -
                                            (bitmapBank.getCellWidth() + AppConstants.getInstance(context).CELL_MARGIN) * i, paint);
                        }


                        // Draws type icon, if it's available
                        int typeDrawableId = 0;
                        String id_name = board.getCells().get(cellIndex).getType();
                        Bitmap type_icon;
                        if (id_name != null) {

                            //using the draable gett to get the right image type
                            try {//hope this works otherwise we had to remake this again!! (I dont wanna)
                                typeDrawableId = DrawableGetter.getReference().getTypeDrawableId(id_name);

                            } catch (DrawableNotFoundException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }

                            type_icon = ((BitmapDrawable) ResourcesCompat.getDrawable(context.getResources(), typeDrawableId, null)).getBitmap();

                            type_icon = bitmapBank.scaleTypeIcon(type_icon);

                            //Please don`t touch the x and y, please just don`t
                            canvas.drawBitmap(type_icon,
                                    page_number_cell_path_direction,
                                    graphicCell.getCellImgY() +
                                            (bitmapBank.getCellWidth() / 2 + type_icon.getHeight()),
                                    null);

                        }


                        // Draws cell board index
                        canvas.drawText(String.valueOf(cellIndex),
                                (page_number_cell_path_direction + bitmapBank.getCellWidth() * 2 / 3),
                                graphicCell.getCellImgY() +
                                        (bitmapBank.getCellWidth() / 2 + bitmapBank.getCellWidth() / 3),
                                paint);

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

            //Has this an utily anymore? I think we should delete this, I made this.. so I like it but we`re not
            //using it anymore, please kill me
            if (!AppConstants.isDrawable) {
                AppConstants.DONE_CELLS = cellIndex;
                AppConstants.isDrawable = !AppConstants.isDrawable;


            }
        }
    }


}

