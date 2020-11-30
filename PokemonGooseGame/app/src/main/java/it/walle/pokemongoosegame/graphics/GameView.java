package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import it.walle.pokemongoosegame.GameActivity;
import it.walle.pokemongoosegame.GameThread;
import it.walle.pokemongoosegame.MainActivity;
import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Pokemon;
import it.walle.pokemongoosegame.entity.board.Board;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

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

    //with this i chage the context!
    public GameView(Context context) {
        super(context);
        initView(); //class that will initiliazie surfaceview and the threads
    }

    void initView(){
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setFocusable(true);
        gameThread = new GameThread(surfaceHolder);//now i can lock and unlock the canvas, draw and start the game
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //if there is no gameThread I'll create one, otherwise I'll start
        if(!gameThread.isRunning()){
            gameThread = new GameThread(holder);
            gameThread.start();
        }
        else {
            gameThread.start();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //don't use it yet

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //I'll stop the thread
        if(gameThread.isRunning()){
            gameThread.setIsRunning(false);
            boolean retry = true;
            while (retry){
                try {
                    gameThread.join();//waits the thread to die
                    retry = false;

                }catch (InterruptedException e){

                }
            }

        }

    }

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




}
