package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import it.walle.pokemongoosegame.entity.Game;

public class BoardThread extends Thread {
    private static final String TAG = BoardThread.class.getSimpleName();
    final SurfaceView surfaceView;//ref to the surfaceHolder
    private boolean isRunning = false;//flag to detect if the Thread is running or not
    Context context;

    public BoardThread(SurfaceView surfaceView , Context context) {
        //Passing a surfaceholder as param on the constructor
        this.surfaceView = surfaceView;
        this.context = context;
    }//It will do an ovveride of the run method and the start will call it from GameView.

    @Override
    public void run() {

        isRunning = true;
        Log.d(TAG, "I've just have born");

        // An initial token is needed when activity restarts
        GameEngine.getInstance(context, surfaceView).getBoardSemaphore().release();

        //loop until the boolean is false
        while (isRunning) {

            //locking the canvas
            Canvas canvas = surfaceView.getHolder().lockCanvas();
            if (canvas != null) {
                synchronized (surfaceView.getHolder()) {

                    try {
                        // Updates board if there are some changes
                        GameEngine.getInstance(context, surfaceView).getBoardSemaphore().acquire();
                        GameEngine.getInstance(context, surfaceView).updateAndDrawBoard(canvas, context);

                        // awakens pawns updater thread in order to updated pawns position in relation to new board screen
                        GameEngine.getInstance(context, surfaceView).getPawnSemaphore().release();
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
