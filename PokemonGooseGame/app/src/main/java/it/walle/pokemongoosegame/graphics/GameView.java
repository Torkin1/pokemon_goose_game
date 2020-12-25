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

import java.util.Random;

import it.walle.pokemongoosegame.BackgroundThread;
import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.ThrowDicesBean;

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

        Log.d("Burp", "on create called");

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
                rotateDice();
            }
        });


        //TODO redo this arrow control

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
                scrollBoardPage(- 1);
            }
        });


        // Starts drawing threads for background, board and pawn
        svBackground.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                svBackground.getHolder().setSizeFromLayout();
                // Do some drawing when surface is ready

                backgroundThread = new BackgroundThread(svBackground.getHolder(), GameView.this);
                backgroundThread.start();




            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //I'll stop the thread
                if (backgroundThread.isRunning()) {
                    backgroundThread.setIsRunning(false);
                }

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
                boardThread = new BoardThread(svBoard.getHolder(), GameView.this);
                boardThread.start();

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (boardThread.isRunning()){
                    boardThread.setIsRunning(false);
                    boardThread.interrupt();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //TODO
            }
        });

        // TODO: Uncomment this when code for drawing pawns is implemented.
        /*
        svPawn.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                surfaceHolder = holder;
                surfaceHolder.setSizeFromLayout();
                // Do some drawing when surface is ready

                System.out.println("Sono prima dell'If di surfaceCreateded il valore di GameView.this è " + GameView.this + " e" +
                        "l'holder è " + surfaceHolder);

                System.out.println("gameThread.isRunning e': " + gameThread.isRunning() );

                if (!pawnThread.isRunning()) {
                    pawnThread = new PawnThread(svPawn.getHolder(), GameView.this);
                }
                pawnThread.start();

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //I'll stop the thread
                if (pawnThread.isRunning()) {
                    pawnThread.setIsRunning(false);
                    boolean retry = true;
                    while (retry) {
                        try {
                            pawnThread.join();//waits the thread to die
                            retry = false;

                        } catch (InterruptedException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //TODO
            }
        });
         */

    }

    private void updateArrowVisibility(){
        // Calculates how many cells a board should have to use all cells of this page plus the cells of passed pages.
        // If this number is higher than the  number of cells of the actual board, it means that the current page is the last page, so the up arrow button is disabled
        if (((GameEngine.getInstance(this).getCurrentBoardPage() + 1) * AppConstants.getInstance(this).CELLS_IN_A_SCREEN) >= CoreController.getReference().getBoard().getCells().size() && up_page_arrow.isClickable()){
            up_page_arrow.setClickable(false);
            up_page_arrow.setImageResource(R.drawable.up_arrow_off);
        } else
        {
            up_page_arrow.setClickable(true);
            up_page_arrow.setImageResource(R.drawable.up_arrow);
        }

        // If it's the first board page disables the arrow down button
        if (GameEngine.getInstance(this).getCurrentBoardPage() == 0 && down_page_arrow.isClickable()){
            down_page_arrow.setClickable(false);
            down_page_arrow.setImageResource(R.drawable.down_arrow_off);
        } else {
            down_page_arrow.setClickable(true);
            down_page_arrow.setImageResource(R.drawable.down_arrow);
        }
    }

    private void scrollBoardPage(int pages){
        GameEngine.getInstance(this).setCurrentBoardPage(GameEngine.getInstance(this).getCurrentBoardPage() + pages);
        updateArrowVisibility();
    }

    private void rotateDice() {
        ThrowDicesBean throwDicesBean = new ThrowDicesBean();
        throwDicesBean.setNumOfFaces(6);
        throwDicesBean.setNumOfDices(1);
        CoreController.getReference().throwDices(throwDicesBean);

        int i = throwDicesBean.getExitNumbers().get(0);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dice_rotation);
        diceImage.startAnimation(anim);
        dice_res.setText(String.format(d_res + "%d", i));

        switch (i) {
            case 1:
                diceImage.setImageResource(R.drawable.dice1);
                break;
            case 2:
                diceImage.setImageResource(R.drawable.dice2);
                break;
            case 3:
                diceImage.setImageResource(R.drawable.dice3);
                break;
            case 4:
                diceImage.setImageResource(R.drawable.dice4);
                break;
            case 5:
                diceImage.setImageResource(R.drawable.dice5);
                break;
            case 6:
                diceImage.setImageResource(R.drawable.dice6);
                break;
        }
    }


    @Override
    protected void onResume() {
        Log.d("Burp", "on resume called");
        super.onResume();

        // Restarts threads
        backgroundThread = new BackgroundThread(svBackground.getHolder(), this);
        boardThread = new BoardThread(svBoard.getHolder(), this);

    }
}

