package it.walle.pokemongoosegame.graphics;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.LoserBean;
import it.walle.pokemongoosegame.game.MoveBean;
import it.walle.pokemongoosegame.game.SkipTurnBean;
import it.walle.pokemongoosegame.game.ThrowDicesBean;
import it.walle.pokemongoosegame.utils.DrawableGetter;
import it.walle.pokemongoosegame.utils.DrawableNotFoundException;

public class GameView extends AppCompatActivity {

    private static final String TAG = GameView.class.getSimpleName();

    // Threads for drawing elements on surface views
    private final List<SurfaceUpdaterThread> surfaceUpdaterThreads = new ArrayList<>();

    private
    SurfaceView svBackground;
    SurfaceView svBoard;
    SurfaceView svPawn;

    // current Player's Coin counter
    TextView text_coins_value, text_plate_value;

    // Health points of all players in game
    private final List<LiveData<Integer>> healths = new ArrayList<>();

    //per gli effetti sonori
    private SoundPool soundPool;

    //sound effect
    private int sound_back, sound_click;

    //per tenere il punteggio è dummy, preferibilmente creare un oggetto
    private int score = 0;

    //usato per tenere i dati del gioco, si aggiorna ogni nuvoa versione!
    private SharedPreferences prefs;

    //the dice image
    private ImageView diceImage, up_page_arrow, down_page_arrow;

    //Variabile che controlla se una pedina si deve muovere oppure no.
    boolean pawnMove = true;

    RelativeLayout game_menu_layout;//getting the realtive layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // binds players pokemon healths to observers
        for (Player p : CoreController.getReference().getPlayers()){
            p.getPokemon().observeCurrentHp(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer currentHp) {

                    // If pokemon health reaches 0 or below, the player owning the pokemon loses the game
                    if (currentHp <= 0){
                        LoserBean bean = new LoserBean();
                        bean.setPlayerUsername(p.getUsername());
                        CoreController.getReference().chooseLoser(bean);

                        // Informs player that they have lost the game
                        (new AlertDialog.Builder(GameView.this))
                                .setTitle(String.format(getString(R.string.ALERT_POKEMON_FAINTED_TITLE), p.getUsername()))
                                .setMessage(String.format(getString(R.string.ALERT_POKEMON_FAINTED_MSG), p.getPokemon().getName()))
                                .setIcon(ContextCompat.getDrawable(GameView.this, R.drawable._f47432d67f0546c05a0719573105396_removebg_preview))
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {

                                        // updates pawns
                                        GameEngine.getInstance().getPawnSemaphore().release();

                                        // If the loser is the current player, proceeds to next player
                                        if (CoreController.getReference().getCurrentPlayerUsername().compareTo(p.getUsername() ) == 0 && CoreController.getReference().getPlayers().size() > 1){
                                            CoreController.getReference().nextTurn();
                                            playerTurn();
                                        }

                                    }
                                })
                                .create()
                                .show();

                    }
                }
            });
        }

        text_coins_value = findViewById(R.id.text_coins_value);
        text_plate_value = findViewById(R.id.text_plate_value);

        //Binds current plate with the text
        CoreController.getReference().observePlate(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                //Chage the value of the plate
                text_plate_value.setText(String.valueOf(integer));
            }
        });

        // Binds current player coins to text_coins_value
        for (Player p : CoreController.getReference().getPlayers()){
            p.observeMoney(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer coins) {

                    // updates view only if it's the current player
                    if (p.getUsername().compareTo(CoreController.getReference().getCurrentPlayerUsername()) == 0){
                        text_coins_value.setText(String.valueOf(coins));
                    }
                }
            });
        }



        // Initializes surfaces
        svBackground = (SurfaceView) findViewById(R.id.svBackground);
        svBoard = findViewById(R.id.svBoard);
        svPawn = findViewById(R.id.svPawn);

        // Sets board surface transparent and on top of background and svPawn transparent and on top of board
        svBoard.setZOrderMediaOverlay(true);
        svBoard.getHolder().setFormat(PixelFormat.TRANSPARENT);

        // Sets pawns surface transparent and on top of board
        svPawn.setZOrderMediaOverlay(true);
        svPawn.getHolder().setFormat(PixelFormat.TRANSPARENT);

        //fare FullScreen l'activity
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        up_page_arrow = findViewById(R.id.page_up_img);
        down_page_arrow = findViewById(R.id.page_down_img);

        up_page_arrow.setImageResource(R.drawable.up_arrow);

        // waits for the svBoard to be drawn on screen
        svBoard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                // This listener is called the first time the view is drawn, but it will be called other times. To prevent this, we remove the listener
                svBoard.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // initializes game engine. The reference to GameEngine must be obtained this way at least once
                GameEngine.reset();
                GameEngine.getInstance(GameView.this, svBoard.getHeight(), svBoard.getWidth());

                // Starts drawing threads for background, board and pawn
                svBoard.getHolder().addCallback(new SurfaceHolder.Callback() {

                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        svBoard.getHolder().setSizeFromLayout();

                        // Do some drawing when surface is ready
                        SurfaceUpdaterThread boardThread = new BoardThread(svBoard, GameView.this);
                        surfaceUpdaterThreads.add(boardThread);
                        boardThread.start();

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {

                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                        //TODO
                    }
                });


                svBackground.getHolder().addCallback(new SurfaceHolder.Callback() {

                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        svBackground.getHolder().setSizeFromLayout();

                        // Do some drawing when surface is ready
                        SurfaceUpdaterThread backgroundThread = new BackgroundThread(svBackground, GameView.this);
                        surfaceUpdaterThreads.add(backgroundThread);
                        backgroundThread.start();

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {


                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                        //TODO
                    }
                });

                svPawn.getHolder().addCallback(new SurfaceHolder.Callback() {

                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        svPawn.getHolder().setSizeFromLayout();

                        // Do some drawing when surface is ready
                        SurfaceUpdaterThread pawnThread = new PawnThread(svPawn, GameView.this);
                        surfaceUpdaterThreads.add(pawnThread);
                        pawnThread.start();
                    }


                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {


                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                        //TODO
                    }
                });

                // sets up dice
                diceImage = findViewById(R.id.dice_image);
                diceImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Rolls dice
                        ThrowDicesBean throwDicesBean = new ThrowDicesBean();
                        throwDicesBean.setNumOfFaces(6);
                        throwDicesBean.setNumOfDices(1);
                        CoreController.getReference().throwDices(throwDicesBean);
                        int res = throwDicesBean.getExitNumbers().get(0);

                        // displays roll result
                        rotateDice(res);

                        //Controlla se il dado doveva essere lanciato per muovere la pedina
                        if(pawnMove){
                            //Disabilita un secondo lancio del dado che fa muovere la pedina in uno stesso turno
                            diceImage.setEnabled(false);

                            // Moves pawn
                            movePlayer(CoreController.getReference().getCurrentPlayerUsername(), res);
                        }
                    }
                });

                // sets click listeners for page arrows
                up_page_arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrollBoardPage(1);
                        AppConstants.isDrawable = !AppConstants.isDrawable;
                    }
                });

                down_page_arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (GameEngine.getInstance().getCurrentBoardPage() != 0)
                            scrollBoardPage(-1);
                    }
                });

                // All is ready, starts player turn
                playerTurn();
            }
        });


    }

    @Override
    protected void onPause() {

        // kills surface updater threads
        for (SurfaceUpdaterThread t : surfaceUpdaterThreads){
            if (t.isRunning()){
                t.setIsRunning(false);
                t.interrupt();
            }
        }

        super.onPause();
    }

    private void updateArrowVisibility() {
        // Calculates how many cells a board should have to use all cells of this page plus the cells of passed pages.
        // If this number is higher than the  number of cells of the actual board, it means that the current page is the last page, so the up arrow button is disabled
        if (((GameEngine.getInstance().getCurrentBoardPage() + 1) * GameEngine.getInstance().CELLS_IN_A_SCREEN) >= CoreController.getReference().getBoard().getCells().size() && up_page_arrow.isClickable()) {
            up_page_arrow.setClickable(false);
            up_page_arrow.setImageResource(R.drawable.up_arrow_off);
        } else {
            up_page_arrow.setClickable(true);
            up_page_arrow.setImageResource(R.drawable.up_arrow);
        }

        // If it's the first board page disables the arrow down button
        if (GameEngine.getInstance().getCurrentBoardPage() == 0 && down_page_arrow.isClickable()) {
            down_page_arrow.setClickable(false);
            down_page_arrow.setImageResource(R.drawable.down_arrow_off);
        } else {
            down_page_arrow.setClickable(true);
            down_page_arrow.setImageResource(R.drawable.down_arrow);
        }
    }

    private void scrollBoardPage(int pages) {

        // Updates current board page adding pages to it
        GameEngine.getInstance().setCurrentBoardPage(GameEngine.getInstance().getCurrentBoardPage() + pages);
        updateArrowVisibility();
    }

    private void rotateDice(int i) {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dice_rotation);
        diceImage.startAnimation(anim);

        try {
            diceImage.setImageResource(DrawableGetter.getReference().getDiceDrawableId(i));
        } catch (DrawableNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void movePlayer(String ownerUsername, int steps) {

        int currentPosition = CoreController.getReference().getPlayerByUsername(ownerUsername).getCurrentPosition();

        // Moves player out of starting cell
        MoveBean moveFromCellBean = new MoveBean();
        moveFromCellBean.setPlayerUsername(ownerUsername);
        moveFromCellBean.setBoardIndex(currentPosition);
        moveFromCellBean.setContext(this);
        CoreController.getReference().moveFromCell(moveFromCellBean);

        // Moves player in target cell
        MoveBean moveInCellBean = new MoveBean();
        moveInCellBean.setPlayerUsername(ownerUsername);
        moveInCellBean.setBoardIndex(currentPosition + steps);
        moveInCellBean.setContext(this);
        CoreController.getReference().moveInCell(moveInCellBean);

        // updates pawn positions
        GameEngine.getInstance().getPawnSemaphore().release();


        //settare il turno successivo e far giocare il player successivo
        if (CoreController.getReference().getPlayers().size() != 0){
            CoreController.getReference().nextTurn();
            playerTurn();
        } else {
            //termina la partita
            CoreController.getReference().endGame();

            //TODO: start leaderBoardActivity
        }
    }

    private void playerTurn(){
        CoreController coreController = CoreController.getReference();


        //Controlla se ci sono ancora giocatori nella partita
        if(coreController.getPlayers().size() != 0){

            String playerTurn = coreController.getCurrentPlayerUsername();
            String coins = Integer.toString(coreController.getCurrentPlayerCoins());

            //Cambiare nome e monete del giocatore in turno
            TextView tvPlayerTurn = findViewById(R.id.text_player_turn);
            tvPlayerTurn.setText(playerTurn);
            text_coins_value.setText(coins);

            //Risolvi stayInCellEffect
            MoveBean stayInCellBean = new MoveBean();
            stayInCellBean.setPlayerUsername(playerTurn);
            stayInCellBean.setBoardIndex(coreController.getCurrentPlayerPosition());
            coreController.stayInCell(stayInCellBean);

            //Controlla se il giocatore deve saltare il turno
            SkipTurnBean skipTurnBean = new SkipTurnBean();
            skipTurnBean.setPlayerUsername(playerTurn);
            coreController.skipTurn(skipTurnBean);
            if(!skipTurnBean.isHasSkipped()){

                //Chiedi all'utente di lanciare il dado ed abilita il lancio
                new ToastWithIcon(this,
                        ContextCompat
                                .getDrawable(this,
                                        R.drawable.throw_dice_toast),
                        String.format(getString(R.string.TOAST_THROW_DICE), playerTurn),
                        Toast.LENGTH_SHORT)
                        .show();
                diceImage.setEnabled(true);
            }
            else{
                new AlertDialog.Builder(this)
                        .setTitle(playerTurn)
                        .setMessage(R.string.DIALOG_MESSAGE_SKIP_TURN)
                        .setPositiveButton(R.string.BUTTON_POSITIVE_SELECT_BOARD, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }

        }

    }


}

