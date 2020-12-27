package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PawnThread extends Thread {
    private static final String TAG = PawnThread.class.getSimpleName();
    final SurfaceView surfaceView;//ref to the surfaceHolder
    private boolean isRunning;//flag to detect if the Thread is running or not
    Context context;

    public PawnThread(SurfaceView surfaceView, Context context) {
        //Passing a surfaceholder as param on the constructor
        this.surfaceView = surfaceView;
        this.context = context;
    }//It will do an ovveride of the run method and the start will call it from GameView.

    @Override
    public void run() {

        isRunning = true;
        Log.d(TAG, "I've just have born");

        while (isRunning) {

            //locking the canvas
            Canvas canvas = surfaceView.getHolder().lockCanvas();
            if (canvas != null) {
                synchronized (surfaceView.getHolder()) {
                    try {

                        // Updates pawns position if there are some changes
                        GameEngine.getInstance(context, surfaceView).getPawnSemaphore().acquire();
                        GameEngine.getInstance(context, surfaceView).updateAndDrawPawns(canvas, context);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    //unlock canvas
                    surfaceView.getHolder().unlockCanvasAndPost(canvas);

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
