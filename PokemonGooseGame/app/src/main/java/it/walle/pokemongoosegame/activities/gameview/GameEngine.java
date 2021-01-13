package it.walle.pokemongoosegame.activities.gameview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import androidx.core.content.ContextCompat;
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
            CELLS_IN_A_SCREEN,//Costants that will tell how many cells can be drawn in the screen
            CELLS_IN_A_ROW,//Costant that will tell how many rows can be drawn in the screen
            CELLS_IN_A_COL,//Costant that will tell how many cols can be drawn in the screen
            WIDTH_MARGIN,//Costant that will tell how much space can be between the cells in width
            HEIGHT_MARGIN,//Costant that will tell how much space can be between the cells in height
            BOARD_WIDTH,//board width
            BOARD_HEIGHT;//board height

    Background backgroundImg;//istance of the background image
    BitmapBank bitmapBank;//istance of the bitmapbank
    int in_cell_counter = 0;//counter used to know many pawns are in the cell


    // Pawns used to track players board position
    private final Map<String, PokePawn> pawns = new HashMap<>();

    // Matrix which will hold graphic cells of current board screen.
    // The indexes of this matrix correspond to the indexes used to display  the graphic cells of current board screen, with displayedCells[0][0] corresponding to the bottom left cell
    private final GraphicCell[][] displayedCells;

    // Used by threads to do updates only when necessary
    private final Semaphore boardSemaphore, pawnSemaphore;

    //The constructor
    public GameEngine(Context context, int boardHeight, int boardWidth) {
        bitmapBank = new BitmapBank(context.getResources(), context);
        backgroundImg = new Background();//initialize bg

        //the dimension of the board are passed and initialized here as costants
        BOARD_HEIGHT = boardHeight;
        BOARD_WIDTH = boardWidth;

        // initializes board screen constants
        CELLS_IN_A_ROW = (BOARD_WIDTH -
                AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellWidth();
        CELLS_IN_A_COL = (BOARD_HEIGHT - AppConstants.getInstance(context).CELL_MARGIN) / bitmapBank.getCellHeight();
        CELLS_IN_A_SCREEN = CELLS_IN_A_COL * CELLS_IN_A_ROW;

        WIDTH_MARGIN = (BOARD_WIDTH -
                CELLS_IN_A_ROW * (AppConstants.getInstance(context).CELL_MARGIN + bitmapBank.getCellWidth())) / 2;

        HEIGHT_MARGIN = (BOARD_HEIGHT - CELLS_IN_A_COL * (AppConstants.getInstance(context).CELL_MARGIN +
                bitmapBank.getCellHeight())) / 2;

        // initializes displayed board cells
        displayedCells = new GraphicCell[CELLS_IN_A_COL][CELLS_IN_A_ROW];

        // Initializes surface updater thread semaphores
        boardSemaphore = new Semaphore(0);
        pawnSemaphore = new Semaphore(0);

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

        //always changing the position of the bg, so creates the movement illusion
        backgroundImg.setX(backgroundImg.getX() - backgroundImg.getBgImageVelocity());
        //moving animation
        //if it ends have to start it back from 0 (well the image isn't infinite so...)
        if (backgroundImg.getX() < -bitmapBank.getBackgroundWidth())
            backgroundImg.setX(0);

        //draw the bg
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

private void pawnDrawer(Bitmap sprite, Player player, PokePawn pokePawn, Canvas canvas, Context context, String playerUsername, int in_cell_counter){

    //From the corecontroller i take a list with all the players in a cell
    String[] list_players_in_a_cell = CoreController.getReference()
            .getAllPlayersInACellUsernames(player.getCurrentPosition());
    //saving the length of this list as a variable so it's easier to use it
    int players_in_a_cell = list_players_in_a_cell.length;

    //saving the dimension of the sprite as an int variable,
    // the choice is made by using a full cell width divided by the person on that cell
    int spirte_dim = bitmapBank.getCellWidth() / players_in_a_cell;
    //here I'll change the dimension of the sprite, but at the full dimension, so I'll store it in a good quality
    pokePawn.setSprite(bitmapBank.scalePawn(sprite, bitmapBank.getCellWidth(), bitmapBank.getCellWidth()));

    //Now I'll resize the bitmap after saving it, this will be drawn
    Bitmap resized_sprite = bitmapBank.scalePawn(pokePawn.getSprite(), spirte_dim, spirte_dim);

    //drawing the resized bitmap, in and moved by a distance the one from the other
    canvas.drawBitmap(resized_sprite,
            pokePawn.getX() + (float) bitmapBank.getCellWidth() / players_in_a_cell * in_cell_counter,
            pokePawn.getY() + (float) bitmapBank.getCellWidth() / players_in_a_cell * in_cell_counter,
            null);


    //Initializing the health bar under the pokemon, first costants and variable
    int hbar_width, HBAR_HEIGHT = 20, MARGIN = 2;
    Paint borderPaint, healthPaint;

    //Giving to the border and hp bar default colors
    borderPaint = new Paint();
    healthPaint = new Paint();

    int borderColor = ContextCompat.getColor(context, R.color.healthBarBorder);
    int healthColor = ContextCompat.getColor(context, R.color.healthBarHealth);

    borderPaint.setColor(borderColor);
    healthPaint.setColor(healthColor);

    // Draws health bar
    //Distance, position and hp percentage variables and costants
    float x = (float) pokePawn.getX()+ (float) bitmapBank.getCellWidth() / players_in_a_cell * in_cell_counter;
    float y = (float) pokePawn.getY() + (float) bitmapBank.getCellWidth() / players_in_a_cell * in_cell_counter;
    float DISTANCE_TO_THE_PLAYER = 30;

    float healthPointPercentage = (float) player.getPokemon().getCurrentHp() / CoreController.MAX_HEALTH_POKEMON;

    //draw Border
    float borderLeft, borderTop, borderRight, borderBottom;
    hbar_width = spirte_dim;
    borderLeft = x - (float) hbar_width / 2;
    borderRight = x + (float) hbar_width / 2;
    borderBottom = y + DISTANCE_TO_THE_PLAYER;
    borderTop = borderBottom - HBAR_HEIGHT;
    //costant and variable that make the player of this turn, border bigger
    int INCREASE_AMOUNT = 7;
    int increase = 0;

    //check if the player is the one playing this turn and I'll make this hp border different
    if(CoreController.getReference().getCurrentPlayerUsername().compareTo(playerUsername) == 0){
        borderColor = ContextCompat.getColor(context, R.color.healthBarBorder2);
        borderPaint.setColor(borderColor);
        increase = 1;
    }

    //Draws the border of the heatlh bar
    canvas.drawRect(borderLeft + (float) (bitmapBank.getCellHeight() / 2 - INCREASE_AMOUNT*increase),
            borderTop - INCREASE_AMOUNT*increase,
            borderRight + (float) bitmapBank.getCellHeight() / 2 + INCREASE_AMOUNT*increase,
            borderBottom + INCREASE_AMOUNT*increase,
            borderPaint);

    // Draw health
    float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
    healthWidth = hbar_width - 4 * MARGIN;
    healthHeight = HBAR_HEIGHT - 4 * MARGIN;
    healthLeft = borderLeft + 2 * MARGIN;
    healthRight = healthLeft + healthWidth * healthPointPercentage;
    healthBottom = borderBottom - 2 * MARGIN;
    healthTop = healthBottom - healthHeight;

    //Hp amout different percentage, chaging the hpBar color by the percentage
    int r, g, b;
    int n = (int)(healthPointPercentage*100);
    g = (255 * n) / 100;
    r = (255 * (100 - n)) / 100;
    b = 0;

    healthPaint.setColor(Color.rgb(r,g,b));

    //draws the hpBar
    canvas.drawRect(healthLeft + (float) bitmapBank.getCellHeight() / 2, healthTop, healthRight + (float) bitmapBank.getCellHeight() / 2, healthBottom, healthPaint);


}

    public void updateAndDrawPawns(Canvas canvas, Context context) {
        //Implement the feature where I check the pokeomn and position

        // Updates position of every pawn
        BitmapBank bitmapBank = new BitmapBank(context.getResources(), context);

        // Clears previously drawn board
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);


        //defin the sprite semaphore
        Semaphore spriteSemaphore = new Semaphore(0);

        // locks displayed cells matrix
        synchronized (displayedCells) {

            // Updates position of every pawn using a for each, for each (word play) pokePawn
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
                            //using a dao that get them from the DB, the onResponse down, used the first time
                            // a game starts, drawing all the pawns on the board
                            if (pokePawn.getSprite() == null) {
                                DAOSprite daoSprite = new DAOSprite(context);
                                daoSprite
                                        .loadSprite(
                                                player.getPokemon().getSprites().getFront_default(),
                                                new Response.Listener<Bitmap>() {
                                                    @Override
                                                    public void onResponse(Bitmap response) {
                                                        GameEngine.this.pawnDrawer(response,player, pokePawn, canvas, context, playerUsername, in_cell_counter);

                                                        //control how many pawns are in that cell with this pawn
                                                        if (CoreController.getReference()
                                                                .getAllPlayersInACellUsernames(player.getCurrentPosition()).length > 1) {
                                                            in_cell_counter++;
                                                        } else {
                                                            in_cell_counter = 0;
                                                        }

                                                        //release the sempahore of drawing pawn and hpbar
                                                        spriteSemaphore.release();

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

                                //now a different approach, because now there are various entrance and checks, and
                                //possibly more cell with more than one pokemon, so it checks if the pawn that is now
                                //onDraw, it's alone or with more cells, and checks how many, using the  position on the
                                //array with the players in a cell
                                boolean in_this_cell = false;
                                for (int i = 0; i < list_players_in_a_cell.length; i++) {
                                    if (player.getUsername().compareTo(list_players_in_a_cell[i]) == 0 && list_players_in_a_cell.length > 1) {
                                        in_cell_counter = i;
                                        in_this_cell = true;
                                        break;
                                    }
                                }
                                //to prevent errors while in the list there's only one pawn, that's
                                //the one in now onDraw, to be drawn at the right size
                                if (list_players_in_a_cell.length < 2 || !in_this_cell)
                                    in_cell_counter = 0;

                                //calling the method to draw
                                GameEngine.this.pawnDrawer(pokePawn.getSprite(), player, pokePawn, canvas, context, playerUsername, in_cell_counter);

                                //in case the counter wasn't updated, better start from 0 the counter
                                in_cell_counter = 0;

                                //release the semaphore
                                spriteSemaphore.release();
                            }


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
        //variables for the text with style and colors
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);

        //variables that decide the direcion, the page number
        int cell_path_direction, page_number_cell_path_direction;
        Board board = CoreController.getReference().getBoard();//initilize the board
        int cellIndex = currentBoardPage * CELLS_IN_A_SCREEN;//to know at which exact cell I'm in

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
                        //sets the direction in this variable
                        cell_path_direction = calculateXByCol(context, graphicCell, colToOccupy);
                        //how much should move, and which direction
                        page_number_cell_path_direction = graphicCell.getCellImgX() +
                                AppConstants.getInstance(context).CELL_MARGIN * 2 +
                                (AppConstants.getInstance(context).CELL_MARGIN +
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
                                    AppConstants.getInstance(context).SCREEN_HEIGHT +
                                            AppConstants.getInstance(context).CELL_MARGIN * 4 -
                                            (bitmapBank.getCellWidth() + HEIGHT_MARGIN) -
                                            (bitmapBank.getCellWidth() +
                                                    AppConstants.getInstance(context).CELL_MARGIN) * i,
                                    paint);
                        }


                        // Draws type icon, if it's available
                        int typeDrawableId = 0;
                        String id_name = board.getCells().get(cellIndex).getType();
                        Bitmap type_icon;
                        if (id_name != null) {

                            //using the drawable get to get the right image type
                            try {//hope this works otherwise we had to remake this again!! (I dont wanna)
                                typeDrawableId = DrawableGetter.getReference().getTypeDrawableId(id_name);
                            } catch (DrawableNotFoundException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                            //Add the bitmap with the icon to this variable
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

                        //next cell
                        cellIndex++;

                    } else {

                        // There are no more cells to be drawn, cycle must end
                        j = cols;
                        i = rows;
                    }
                }

            }
        }
    }


}

