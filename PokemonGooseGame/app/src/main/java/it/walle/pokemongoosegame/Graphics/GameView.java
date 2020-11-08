package it.walle.pokemongoosegame.Graphics;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;

import it.walle.pokemongoosegame.GameActivity;
import it.walle.pokemongoosegame.MainActivity;
import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Pokemon;
import it.walle.pokemongoosegame.entity.board.Board;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private Boolean isPlaying, isGameOver = false;
    private int screenX, screenY;
    private Paint paint;

    //per gli effetti sonori
    private SoundPool soundPool;

    //effetto suono
    private int sound;

    //per poter usare su dispositivi di grandezze diverse uso queste var float
    protected static float screenRatioX, screenRatioY; //per renderlo compatibile

    //per tenere il punteggio è dummy, preferibilmente creare un oggetto
    private int score = 0;

    private Board board;
    private Pokemon pokemon;

    //un oggetto di tipo gameAcitvity
    private GameActivity activity;

    //usato per tenere i dati del gioco, si aggiorna ogni nuvoa versione!
    private SharedPreferences prefs;
    public GameView(Context context) {
        super(context);
    }

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        this.activity = activity;

        //nascode le preferenze alle altre app
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        //inizilizzo il suono
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else{
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        //prendere da  file
        sound = soundPool.load(activity, R.raw.click_sound, 1 );

        this.screenX = screenX;
        this.screenY = screenY;
        //calcolo il valore dello screenRatio
        screenRatioX = 1920f / screenX; //dim schermo in pixel
        screenRatioY = 1080f / screenY;

    }



    @Override
    public void run() {

    }

    private void update(){
        //TODO
    }

    private void draw(){
        //TODO
    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void saveIfHighScore() {//per registrare lo score + alto utile come esempio di prefs!
        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }


    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start(); //call the thread that start the run function
    }

    public void pause() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
