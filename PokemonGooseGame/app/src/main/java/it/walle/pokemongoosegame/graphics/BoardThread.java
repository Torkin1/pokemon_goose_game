package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import it.walle.pokemongoosegame.entity.Game;

public class BoardThread extends SurfaceUpdaterThread {


    public BoardThread(SurfaceView surfaceView, Context context) {
        super(surfaceView, context);
    }

    @Override
    public synchronized void start() {

        // Updates board the first time
        GameEngine.getInstance().getBoardSemaphore().release();

        super.start();
    }

    @Override
    public void doUpdate() {

        //loop until the boolean is false

            //locking the canvas
            Canvas canvas = surfaceView.getHolder().lockCanvas();
            if (canvas != null) {
                synchronized (surfaceView.getHolder()) {

                    try {
                        // Updates board if there are some changes
                        GameEngine.getInstance().getBoardSemaphore().acquire();
                        GameEngine.getInstance().updateAndDrawBoard(canvas, context);

                        // awakens pawns updater thread in order to updated pawns position in relation to new board screen
                        GameEngine.getInstance().getPawnSemaphore().release();
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    //unlock canvas
                    surfaceView.getHolder().unlockCanvasAndPost(canvas);

                }
            }
    }
}
