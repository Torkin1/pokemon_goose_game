package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BackgroundThread extends Thread {
    private static final String TAG = BackgroundThread.class.getSimpleName();
    final SurfaceView surfaceView;//ref to the surfaceHolder
    private boolean isRunning = false;//flag to detect if the Thread is running or not
    long startTime, loopTime; //loop and start time duration
    long DELAY = 33; //delay in millisecs, etrween screen refresh
    Context context;

    public BackgroundThread(SurfaceView surfaceView, Context context) {
        //Passing a surfaceholder as param on the constructor
        this.surfaceView = surfaceView;
        this.context = context;
    }//It will do an ovveride of the run method and the start will call it from GameView.

    @Override
    public void run() {
        //qui uso un loop fino a che isRunning è vero, e inizio a tenere il tempo. Nel costruttore l'ho mesos a true
        //così entra nel loop di default, salvo il current time in millisecondi, e starto la varibile startTime
        //poi blocco la canvas con lo surfaceHolder Object poi userò un syncronized block per lo surfaceHolder, all'interno
        //del quale chiamo i metodi per aggiornare e disegnare l'immagine di bg, e definirò questi metodi nella classe GameEngine
        //Poi sblocco la canvas e posterò gli aggiornamneti, finalemnte calcolo il tempo del loop, loopTime e se è meno del Delay,
        //chiamo lo sleep() fino alla fine del dealy così è liscia l'animazione

        //loop until the boolean is false
        isRunning = true;
        Log.d(TAG, "I've just have born");

        while (isRunning) {


            // TODO: call update methods only if there are some updates
            startTime = SystemClock.uptimeMillis();
            //locking the canvas

            Canvas canvas = surfaceView.getHolder().lockCanvas(null);
            if (canvas != null) {
                synchronized (surfaceView.getHolder()) {

                    // Updates background
                    GameEngine.getInstance(context, surfaceView).updateAndDrawBackgroundImage(canvas, context);

                    //unlock canvas
                    surfaceView.getHolder().unlockCanvasAndPost(canvas);

                }
            }
            loopTime = SystemClock.uptimeMillis() - startTime;
            //Pausing here to make sure we update the right amount per second
            if (loopTime < DELAY) {
                try {
                    Thread.sleep(DELAY - loopTime);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupted while sleeping");
                }
            }
        }
        Log.d(TAG, "I'm dying lol");
    }

    //return whether the thread is running
    public boolean isRunning() {
        return isRunning;
    }

    //Sets the thread state, false  = stopped, true = running
    public void setIsRunning(boolean state) {
        isRunning = state;
    }
}
