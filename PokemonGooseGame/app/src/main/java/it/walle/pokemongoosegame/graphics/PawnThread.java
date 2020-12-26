package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class PawnThread extends Thread {
    private static final String TAG = PawnThread.class.getSimpleName();
    final SurfaceHolder surfaceHolder;//ref to the surfaceHolder
    private boolean isRunning;//flag to detect if the Thread is running or not
    Context context;

    public PawnThread(SurfaceHolder surfaceHolder, Context context) {
        //Passing a surfaceholder as param on the constructor
        this.surfaceHolder = surfaceHolder;
        this.context = context;
    }//It will do an ovveride of the run method and the start will call it from GameView.

    @Override
    public void run() {

        isRunning = true;
        Log.d(TAG, "I've just have born");

        while (isRunning) {

            //locking the canvas
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                synchronized (surfaceHolder) {
                    try {

                        // Updates pawns position if there are some changes
                        GameEngine.getInstance(context).getPawnSemaphore().acquire();
                        GameEngine.getInstance(context).updateAndDrawPawns(canvas, context);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    //unlock canvas
                    surfaceHolder.unlockCanvasAndPost(canvas);

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
