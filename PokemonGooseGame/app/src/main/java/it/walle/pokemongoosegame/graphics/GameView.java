package it.walle.pokemongoosegame.graphics;

import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.media.SoundPool;
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


    //usato per tenere i dati del gioco, si aggiorna ogni nuvoa versione!
    private SharedPreferences prefs;

    //the dice image
    ImageView diceImage;

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

        //fare FullScreen l'activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        gameThread = new GameThread(surface.getHolder());//now i can lock and unlock the canvas, draw and start the game



        dice_res = findViewById(R.id.text_dice_result);

        diceImage = findViewById(R.id.dice_image);
        diceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateDice();
            }
        });

        surface.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//                holder.setFixedSize(AppConstants.getBitmapBank().getBoardWidth() , AppConstants.getBitmapBank().getBoardHeight()- 90);
                holder.setSizeFromLayout();
                // Do some drawing when surface is ready
                if(!gameThread.isRunning()){
                    gameThread = new GameThread(holder);
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

    private void rotateDice() {
        int i = random.nextInt(5)+1;
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.dice_rotation);
        diceImage.startAnimation(anim);
        dice_res.setText(String.format(d_res + "%d" , i));
        switch (i){
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
