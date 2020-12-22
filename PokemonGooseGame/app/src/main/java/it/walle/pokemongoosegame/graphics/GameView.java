package it.walle.pokemongoosegame.graphics;

import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
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

import it.walle.pokemongoosegame.GameThread;
import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.ThrowDicesBean;

public class GameView extends AppCompatActivity {

    private GameThread gameThread;
    private static final String TAG = GameView.class.getSimpleName();


    //per gli effetti sonori
    private SoundPool soundPool;

    //sound effect
    private int sound_back, sound_click;

    //per tenere il punteggio è dummy, preferibilmente creare un oggetto
    private int score = 0;

    private Board board;
    private Pokemon pokemon;

    //a common holder for the surface to call it from the methods
    SurfaceHolder surfaceHolder;


    //usato per tenere i dati del gioco, si aggiorna ogni nuvoa versione!
    private SharedPreferences prefs;

    //the dice image
    ImageView diceImage, up_page_arrow, down_page_arrow;

    //random number
    Random random = new Random();

    //a text view for the result fo the dice
    TextView dice_res;
    String d_res = "Your Score is: ";

    RelativeLayout game_menu_layout;//getting the realtive layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        SurfaceView surface = (SurfaceView) findViewById(R.id.surface);

        // TODO: per favore  spiegare perchè questo codice è commentato, o cancellare se è inutile

//        createCell(AppConstants.LEFT_GAME_MENU_WIDTH + 10, AppConstants.SCREEN_HEIGHT-AppConstants.LEFT_GAME_MENU_WIDTH,10,10);
//        createCell(AppConstants.LEFT_GAME_MENU_WIDTH*2 +220, AppConstants.SCREEN_HEIGHT-AppConstants.LEFT_GAME_MENU_WIDTH,10,10);

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
                scrollBoardPage(- 1);
            }
        });

        surface.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // TODO: per favore  spiegare perchè questo codice è commentato, o cancellare se è inutile
//                holder.setFixedSize(AppConstants.getBitmapBank().getBoardWidth() , AppConstants.getBitmapBank().getBoardHeight()- 90);
                surfaceHolder = holder;
                surfaceHolder.setSizeFromLayout();
                // Do some drawing when surface is ready
                if (!gameThread.isRunning()) {
                    gameThread = new GameThread(surfaceHolder, GameView.this);
                }
                gameThread.start();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //I'll stop the thread
                if (gameThread.isRunning()) {
                    gameThread.setIsRunning(false);
                    boolean retry = true;
                    while (retry) {
                        try {
                            gameThread.join();//waits the thread to die
                            retry = false;

                        } catch (InterruptedException e) {

                        }
                    }
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //TODO
            }
        });


    }

    private void updateArrowVisibility(){
        // Calculates how many cells a board should have to use all cells of this page plus the cells of passed pages.
        // If this number is higher than the  number of cells of the actual board, it means that the current page is the last page, so the up arrow button is disabled
        if (((GameEngine.getInstance().getCurrentBoardPage() + 1) * AppConstants.getInstance(this).CELLS_IN_A_SCREEN) >= CoreController.getReference().getBoard().getCells().size() && up_page_arrow.isClickable()){
            up_page_arrow.setClickable(false);
            up_page_arrow.setImageResource(R.drawable.up_arrow_off);
        } else
        {
            up_page_arrow.setClickable(true);
            up_page_arrow.setImageResource(R.drawable.up_arrow);
        }

        // If it's the first board page disables the arrow down button
        if (GameEngine.getInstance().getCurrentBoardPage() == 0 && down_page_arrow.isClickable()){
            down_page_arrow.setClickable(false);
            down_page_arrow.setImageResource(R.drawable.down_arrow_off);
        } else {
            down_page_arrow.setClickable(true);
            down_page_arrow.setImageResource(R.drawable.down_arrow);
        }
    }

    private void scrollBoardPage(int pages){
        GameEngine.getInstance().setCurrentBoardPage(GameEngine.getInstance().getCurrentBoardPage() + pages);
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

}

// TODO: per favore spiegare perchè questo codice è commentato, o cancellare se è inutile

//        //with this i chage the context!
//    public GameView(Context context) {
//        super(context);
//        initView(); //class that will initiliazie surfaceview and the threads
//    }
//
//    void initView(){
//        SurfaceHolder surfaceHolder = getHolder();
//        surfaceHolder.addCallback(this);
//        setFocusable(true);
//        gameThread = new GameThread(surfaceHolder);//now i can lock and unlock the canvas, draw and start the game
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        //if there is no gameThread I'll create one, otherwise I'll start
//        if(!gameThread.isRunning()){
//            gameThread = new GameThread(holder);
//            gameThread.start();
//        }
//        else {
//            gameThread.start();
//        }
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        //don't use it yet
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        //I'll stop the thread
//        if(gameThread.isRunning()){
//            gameThread.setIsRunning(false);
//            boolean retry = true;
//            while (retry){
//                try {
//                    gameThread.join();//waits the thread to die
//                    retry = false;
//
//                }catch (InterruptedException e){
//
//                }
//            }
//
//        }
//
//    }

//    public GameView(GameActivity activity, int screenX, int screenY) {
//        super(activity);
//        this.activity = activity;
//
//        //nascode le preferenze alle altre app
//        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
//
//
//        //inizilizzo il suono
//        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_GAME)
//                .build();
//        soundPool = new SoundPool.Builder()
//                .setAudioAttributes(audioAttributes)
//                .build();
//
//
//        //prendere da  file
//        sound_back = soundPool.load(activity, R.raw.back_sound_poke, 1);
//        sound_click = soundPool.load(activity, R.raw.beep_sound_poke, 1);
//
//        this.screenX = screenX;
//        this.screenY = screenY;
//        //calcolo il valore dello screenRatio
//        screenRatioX = 1920f / screenX; //dim schermo in pixel
//        screenRatioY = 1080f / screenY;
//
//        background1 = new Background(screenX, screenY, getResources());
//
//        paint = new Paint();
//
//        paint.setTextSize(128);//pixels
//        paint.setColor(Color.WHITE);
//
//        //Creo una variabile random da usare per dado e/o altre funzionalità e casualità
//        random = new Random();
//
//    }


//}
