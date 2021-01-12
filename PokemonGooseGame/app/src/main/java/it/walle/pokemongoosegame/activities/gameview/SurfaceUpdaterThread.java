package it.walle.pokemongoosegame.activities.gameview;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

public abstract class SurfaceUpdaterThread extends Thread {

    final String TAG = this.getClass().getSimpleName();
    protected final SurfaceView surfaceView;//ref to the surfaceHolder
    private boolean isRunning = false;//flag to detect if the Thread is running or not
    protected Context context;

    public SurfaceUpdaterThread(SurfaceView surfaceView , Context context) {
        //Passing a surfaceholder as param on the constructor
        this.surfaceView = surfaceView;
        this.context = context;
    }//It will do an ovveride of the run method and the start will call it from GameActivity.

    @Override
    public void run() {//the thread will live until isRunning it's true, after it will end
        isRunning = true;
        while (isRunning){
            doUpdate();
        }

    }

    public abstract void doUpdate();

    //return whether the thread is running
    public boolean isRunning() {
        return isRunning;
    }

    //Sets the thread state, false  = stopped, true = running
    public void setIsRunning(boolean state) {
        isRunning = state;
    }
}
