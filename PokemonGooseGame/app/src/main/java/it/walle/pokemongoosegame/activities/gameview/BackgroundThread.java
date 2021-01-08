package it.walle.pokemongoosegame.activities.gameview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceView;

public class BackgroundThread extends SurfaceUpdaterThread {
    private static final String TAG = BackgroundThread.class.getSimpleName();

    public BackgroundThread(SurfaceView surfaceView, Context context) {
        super(surfaceView, context);
    }

    @Override
    public void doUpdate() {
        long startTime, loopTime; //loop and start time duration
        long DELAY = 33; //delay in millisecs, etrween screen refresh
        // TODO: call update methods only if there are some updates
        startTime = SystemClock.uptimeMillis();
        //locking the canvas

        Canvas canvas = surfaceView.getHolder().lockCanvas(null);
        if (canvas != null) {
            synchronized (surfaceView.getHolder()) {

                // Updates background
                GameEngine.getInstance().updateAndDrawBackgroundImage(canvas, context);

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
    }

