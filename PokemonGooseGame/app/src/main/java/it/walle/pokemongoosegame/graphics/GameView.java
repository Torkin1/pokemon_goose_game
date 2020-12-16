package it.walle.pokemongoosegame.graphics;

import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.cardview.widget.CardView;

import java.util.Random;

import it.walle.pokemongoosegame.GameThread;
import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;

public class GameView extends AppCompatActivity {

    private GameThread gameThread;


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

//        createCell(AppConstants.LEFT_GAME_MENU_WIDTH + 10, AppConstants.SCREEN_HEIGHT-AppConstants.LEFT_GAME_MENU_WIDTH,10,10);
//        createCell(AppConstants.LEFT_GAME_MENU_WIDTH*2 +220, AppConstants.SCREEN_HEIGHT-AppConstants.LEFT_GAME_MENU_WIDTH,10,10);

        //fare FullScreen l'activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gameThread = new GameThread(surface.getHolder());//now i can lock and unlock the canvas, draw and start the game


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
                pageUp();
            }
        });

        down_page_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageDown();
            }
        });

        surface.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//                holder.setFixedSize(AppConstants.getBitmapBank().getBoardWidth() , AppConstants.getBitmapBank().getBoardHeight()- 90);
                surfaceHolder = holder;
                surfaceHolder.setSizeFromLayout();
                // Do some drawing when surface is ready
                if (!gameThread.isRunning()) {
                    gameThread = new GameThread(surfaceHolder);
                    gameThread.start();
                } else {
                    gameThread.start();
                }
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

    private void pageUp() {
        if (AppConstants.DISPLAYED_SCREEN >= 1) {
            setPageDown();
            down_page_arrow.setClickable(true);
        }

        AppConstants.DISPLAYED_SCREEN = AppConstants.DISPLAYED_SCREEN + 1;//Doing ++ doesn't work use the + 1 method
        if (AppConstants.TOTAL_SCREENS < AppConstants.DISPLAYED_SCREEN)
            AppConstants.TOTAL_SCREENS = AppConstants.DISPLAYED_SCREEN;
        if ((AppConstants.TOTAL_CELLS - (AppConstants.DONE_CELLS + AppConstants.CELLS_IN_A_SCREEN * 2)) <= 0) {
            up_page_arrow.setClickable(false);
            up_page_arrow.setImageResource(R.drawable.up_arrow_off);
        }

        AppConstants.DRAWABLE = !AppConstants.DRAWABLE;
        System.out.println("page up... " + "Ciao gli screen sono: " + AppConstants.TOTAL_SCREENS + " Ti trovi al: " + AppConstants.DISPLAYED_SCREEN);


    }

    private void pageDown() {

        AppConstants.DISPLAYED_SCREEN = AppConstants.DISPLAYED_SCREEN - 1;
        if (AppConstants.DISPLAYED_SCREEN == 1)
            down_page_arrow.setClickable(false);


        up_page_arrow.setClickable(true);
        setPageUp();
        

        if (AppConstants.DISPLAYED_SCREEN >= 1)
            AppConstants.DONE_CELLS = AppConstants.DONE_CELLS - AppConstants.CELLS_IN_A_SCREEN;
        if (AppConstants.DISPLAYED_SCREEN == 1)
            down_page_arrow.setImageResource(R.drawable.down_arrow_off);

        System.out.println("page down... " + "Ciao gli screen sono: " + AppConstants.TOTAL_SCREENS + " Ti trovi al: " + AppConstants.DISPLAYED_SCREEN);


    }

    public void setPageUp() {
        up_page_arrow.setImageResource(R.drawable.up_arrow);
    }

    public void setPageDown() {
        down_page_arrow.setImageResource(R.drawable.down_arrow);
    }

    public void createCell(int left, int top, int right, int bottom) {
        CardView.LayoutParams lp = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.MATCH_PARENT);
        lp.setMarginStart(AppConstants.LEFT_GAME_MENU_WIDTH);
        lp.setMargins(left, top, right, bottom);
        View secondLayerView = LayoutInflater.from(this).inflate(R.layout.cell_holder, null, false);
        addContentView(secondLayerView, lp);
    }

    private void rotateDice() {
        int i = random.nextInt(5) + 1;
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
