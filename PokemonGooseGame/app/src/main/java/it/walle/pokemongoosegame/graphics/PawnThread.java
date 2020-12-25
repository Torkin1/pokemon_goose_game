package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class PawnThread extends Thread {
    private static final String TAG = PawnThread.class.getSimpleName();
    final SurfaceHolder surfaceHolder;//ref to the surfaceHolder
    boolean isRunning;//flag to detect if the Thread is running or not
    Context context;

    public PawnThread(SurfaceHolder surfaceHolder, Context context) {
        //Passing a surfaceholder as param on the constructor
        this.surfaceHolder = surfaceHolder;
        this.context = context;
        isRunning = true; //means that i started the thread!
    }//It will do an ovveride of the run method and the start will call it from GameView.

    @Override
    public void run() {

        while (isRunning) {

            //locking the canvas
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                synchronized (surfaceHolder) {
                    try {
                        GameEngine.getInstance(context).getPawnSemaphore().acquire();
                        GameEngine.getInstance(context).updateAndDrawPawn(canvas, context);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    //unlock canvas
                    surfaceHolder.unlockCanvasAndPost(canvas);

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
