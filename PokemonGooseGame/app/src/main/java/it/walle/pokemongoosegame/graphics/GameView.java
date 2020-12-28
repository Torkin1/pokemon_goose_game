package it.walle.pokemongoosegame.graphics;

import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.MoveBean;
import it.walle.pokemongoosegame.game.ThrowDicesBean;
import it.walle.pokemongoosegame.utils.DrawableGetter;
import it.walle.pokemongoosegame.utils.DrawableNotFoundException;

public class GameView extends AppCompatActivity {

    // Threads for drawing elements on surface views
    private BackgroundThread backgroundThread;
    private BoardThread boardThread;
    private PawnThread pawnThread;

    private
    SurfaceView svBackground;
    SurfaceView svBoard;
    SurfaceView svPawn;

    private static final String TAG = GameView.class.getSimpleName();

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

    //a text view for the result fo the dice
    TextView dice_res;
    String d_res = "Your Score is: ";

    RelativeLayout game_menu_layout;//getting the realtive layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

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

        dice_res = findViewById(R.id.text_dice_result);
        up_page_arrow = findViewById(R.id.page_up_img);
        down_page_arrow = findViewById(R.id.page_down_img);

        up_page_arrow.setImageResource(R.drawable.up_arrow);

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

                // Moves pawn
                // TODO: This is probably not the best place where to place the movement of the player, since in this way we are assuming that every time the dice is rolled the current player pawn must be moved
                movePlayer(CoreController.getReference().getCurrentPlayerUsername(), res);
            }
        });

        up_page_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Burp", "Burp");
                scrollBoardPage(1);
                AppConstants.isDrawable = !AppConstants.isDrawable;
            }
        });

        down_page_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GameEngine.getInstance(getApplicationContext(), svBoard).getCurrentBoardPage() != 0)
                    scrollBoardPage(-1);
            }
        });


        // Starts drawing threads for background, board and pawn
        svBackground.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                svBackground.getHolder().setSizeFromLayout();
                // Do some drawing when surface is ready

                backgroundThread = new BackgroundThread(svBackground, GameView.this);
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


        svBoard.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                svBoard.getHolder().setSizeFromLayout();
                // Do some drawing when surface is ready
                boardThread = new BoardThread(svBoard, GameView.this);
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

        // TODO: Uncomment this when code for drawing pawns is implemented.

        svPawn.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                svPawn.getHolder().setSizeFromLayout();

                // Do some drawing when surface is ready

                pawnThread = new PawnThread(svPawn, GameView.this);
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

    }

    @Override
    protected void onPause() {

        // kills surface updater threads
        if (backgroundThread.isRunning()) {
            backgroundThread.setIsRunning(false);
        }

        if (boardThread.isRunning()) {
            boardThread.setIsRunning(false);
            boardThread.interrupt();
        }

        if (pawnThread.isRunning()) {
            pawnThread.setIsRunning(false);
            pawnThread.interrupt();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // creates new instances of updater threads in order to be restarted
        backgroundThread = new BackgroundThread(svBackground, this);
        boardThread = new BoardThread(svBoard, this);
        pawnThread = new PawnThread(svPawn, this);

    }

    private void updateArrowVisibility() {
        // Calculates how many cells a board should have to use all cells of this page plus the cells of passed pages.
        // If this number is higher than the  number of cells of the actual board, it means that the current page is the last page, so the up arrow button is disabled
        if (((GameEngine.getInstance(this, svBoard).getCurrentBoardPage() + 1) * GameEngine.getInstance(this, svBoard).CELLS_IN_A_SCREEN) >= CoreController.getReference().getBoard().getCells().size() && up_page_arrow.isClickable()) {
            up_page_arrow.setClickable(false);
            up_page_arrow.setImageResource(R.drawable.up_arrow_off);
        } else {
            up_page_arrow.setClickable(true);
            up_page_arrow.setImageResource(R.drawable.up_arrow);
        }

        // If it's the first board page disables the arrow down button
        if (GameEngine.getInstance(this, svBoard).getCurrentBoardPage() == 0 && down_page_arrow.isClickable()) {
            down_page_arrow.setClickable(false);
            down_page_arrow.setImageResource(R.drawable.down_arrow_off);
        } else {
            down_page_arrow.setClickable(true);
            down_page_arrow.setImageResource(R.drawable.down_arrow);
        }
    }

    private void scrollBoardPage(int pages) {

        // Updates current board page adding pages to it
        GameEngine.getInstance(this, svBoard).setCurrentBoardPage(GameEngine.getInstance(this, svBoard).getCurrentBoardPage() + pages);
        updateArrowVisibility();
    }

    private void rotateDice(int i) {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dice_rotation);
        diceImage.startAnimation(anim);
        dice_res.setText(String.format(d_res + "%d", i));

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
        CoreController.getReference().moveFromCell(moveFromCellBean);

        // Moves player in target cell
        MoveBean moveInCellBean = new MoveBean();
        moveInCellBean.setPlayerUsername(ownerUsername);
        moveInCellBean.setBoardIndex(currentPosition + steps);
        CoreController.getReference().moveInCell(moveInCellBean);

        // updates pawn positions
        GameEngine.getInstance(this, svBoard).getPawnSemaphore().release();


    }


}

