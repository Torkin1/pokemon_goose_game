package it.walle.pokemongoosegame;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import it.walle.pokemongoosegame.graphics.GameEngine;

public class GameThread extends Thread {
    SurfaceHolder surfaceHolder;//ref to the surfaceHolder
    boolean isRunning;//flag to detect if the Thread is running or not
    long startTime, loopTime; //loop and start time duration
    long DELAY = 33; //delay in millisecs, etrween screen refresh
    Context context;

    public GameThread(SurfaceHolder surfaceHolder, Context context) {
        //Passing a surfaceholder as param on the constructor
        this.surfaceHolder = surfaceHolder;
        this.context = context;
        isRunning = true; //means that i started the thread!
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
        System.out.println("Sono prima del while");

        while (isRunning) {

            // TODO: call update methods only if there are some updates
            startTime = SystemClock.uptimeMillis();
            //locking the canvas

            Canvas canvas = surfaceHolder.lockCanvas(null);
            if (canvas != null) {
                synchronized (surfaceHolder) {


                    GameEngine.getInstance(context).updateAndDrawBackgroundImage(canvas, context);

                    //TODO draw only if necessary
//                    if(AppConstants.DRAWABLE)


                    // FIXME: Quando ridisegna il background, non disegna il tabellone e la pedina se questi non vengono ridisegnati in continuazione
                    GameEngine.getInstance(context).updateAndDrawBoard(canvas, context);
                    GameEngine.getInstance(context).updateAndDrawBoard(canvas, context);



                    //unlock canvas
                    surfaceHolder.unlockCanvasAndPost(canvas);

                }
            }
            loopTime = SystemClock.uptimeMillis() - startTime;
            //Pausing here to make sure we update the right amount per second
            if (loopTime < DELAY) {
                try {
                    Thread.sleep(DELAY - loopTime);
                } catch (InterruptedException e) {
                    Log.e("Interrupted", "Interrupted while sleeping");
                }
            }
        }
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
