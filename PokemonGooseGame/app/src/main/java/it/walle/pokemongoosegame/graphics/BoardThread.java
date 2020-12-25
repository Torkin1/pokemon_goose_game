package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class BoardThread extends Thread {
    private static final String TAG = BoardThread.class.getSimpleName();
    final SurfaceHolder surfaceHolder;//ref to the surfaceHolder
    private boolean isRunning = false;//flag to detect if the Thread is running or not
    Context context;

    public BoardThread(SurfaceHolder surfaceHolder, Context context) {
        //Passing a surfaceholder as param on the constructor
        this.surfaceHolder = surfaceHolder;
        this.context = context;
    }//It will do an ovveride of the run method and the start will call it from GameView.

    @Override
    public void run() {

        isRunning = true;
        //loop until the boolean is false
        while (isRunning) {

            //locking the canvas
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                synchronized (surfaceHolder) {

                    // Clears previously drawn board
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);

                    try {
                        GameEngine.getInstance(context).getBoardSemaphore().acquire();
                        GameEngine.getInstance(context).updateAndDrawBoard(canvas, context);
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
