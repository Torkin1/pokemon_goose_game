package it.walle.pokemongoosegame.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PawnThread extends SurfaceUpdaterThread {
    private static final String TAG = PawnThread.class.getSimpleName();

    public PawnThread(SurfaceView surfaceView, Context context) {
        super(surfaceView, context);
    }

    @Override
    public void doUpdate() {
        //locking the canvas
        Canvas canvas = surfaceView.getHolder().lockCanvas();
        if (canvas != null) {
            synchronized (surfaceView.getHolder()) {
                try {

                    // Updates pawns position if there are some changes
                    GameEngine.getInstance().getPawnSemaphore().acquire();
                    GameEngine.getInstance().updateAndDrawPawns(canvas, context);
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                //unlock canvas
                surfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
}
