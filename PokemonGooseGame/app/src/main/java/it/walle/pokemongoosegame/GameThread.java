package it.walle.pokemongoosegame;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

import it.walle.pokemongoosegame.graphics.AppConstants;

public class GameThread extends Thread{
    SurfaceHolder surfaceHolder;//ref to the surfaceHolder
    boolean isRunning;//flag to detect if the Thread is running or not
    long startTime, loopTime; //loop and start time duration
    long DELAY = 33; //delay in millisecs, etrween screen refresh

    public  GameThread(SurfaceHolder surfaceHolder){
        //Passing a surfaceholder as param on the constructor
        this.surfaceHolder = surfaceHolder;
        isRunning = true; //means that i started the thread!
    }//It will do an ovveride of the run method and the start will call it from GameView.

    @Override
    public void run(){
        //qui uso un loop fino a che isRunning è vero, e inizio a tenere il tempo. Nel costruttore l'ho mesos a true
        //così entra nel loop di default, salvo il current time in millisecondi, e starto la varibile startTime
        //poi blocco la canvas con lo surfaceHolder Object poi userò un syncronized block per lo surfaceHolder, all'interno
        //del quale chiamo i metodi per aggiornare e disegnare l'immagine di bg, e definirò questi metodi nella classe GameEngine
        //Poi sblocco la canvas e posterò gli aggiornamneti, finalemnte calcolo il tempo del loop, loopTime e se è meno del Delay,
        //chiamo lo sleep() fino alla fine del dealy così è liscia l'animazione

        //loop until the boolean is false

        while (isRunning){
            startTime = SystemClock.uptimeMillis();
            //locking the canvas
            Canvas canvas = surfaceHolder.lockCanvas(null);
            if (canvas != null){
                System.out.println("Canvas != null");
                System.out.println(canvas.toString());
                synchronized (surfaceHolder){
                    System.out.println("Canvas != null after sync");
                    System.out.println("canvas form afte sync" + canvas.toString());
                    AppConstants.getGameEngine().updateAndDrawBackgroundImage(canvas);
                    //add here all the elements that have to be on screen
                    AppConstants.getGameEngine().updateAndDrawBoard(canvas);
                    AppConstants.getGameEngine().updateAndDrawPawn(canvas);



                    //unlock canvas
                    surfaceHolder.unlockCanvasAndPost(canvas);

                }
            }
            loopTime = SystemClock.uptimeMillis() - startTime;
            //Pausing here to make sure we update the right amount per second
            if(loopTime < DELAY){
                try {
                    Thread.sleep(DELAY - loopTime);
                }catch (InterruptedException e){
                    Log.e("Interrupted","Interrupted while sleeping");
                }
            }
        }
    }

    //return whether the thread is running
    public boolean isRunning(){
        return isRunning;
    }

    //Sets the thread state, false  = stopped, true = running
    public void setIsRunning(boolean state){
        isRunning = state;
    }//cambio anche il manifest, cambiando la activity  <activity android:name=".GameActivity">
    //   </activity> e poi vado dalla main faccio partire il game Activity
}
