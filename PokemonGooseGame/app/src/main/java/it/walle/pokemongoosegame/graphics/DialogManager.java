package it.walle.pokemongoosegame.graphics;

import android.app.Dialog;
import android.content.DialogInterface;

import java.util.LinkedList;
import java.util.Queue;

public class DialogManager {

    private static DialogManager ref = null;
    public synchronized static DialogManager getInstance(){
        if (ref == null){
            ref = new DialogManager();
        }
        return ref;
    }

    private final Queue<Dialog> displayedDialogs = new LinkedList<>();  // holds a FIFO queue of dialogs to be displayed

    public synchronized void enqueueDialog(Dialog dialog, DialogInterface.OnDismissListener onDismissListener){

        // Adds a custom on dismiss listener which pops dialog from the queue
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {   // Call this if you didn't set a on dismiss listener to your dialog
                @Override
                public void onDismiss(DialogInterface dialog) {

                    // Calls custom listener if one is available
                    if (onDismissListener != null){
                        onDismissListener.onDismiss(dialog);
                    }

                    // removes himself from the queue
                    displayedDialogs.remove((Dialog) dialog);

                    // Asks to display next dialog
                    if (!displayedDialogs.isEmpty()){
                        displayedDialogs.peek().show();
                    }
                }
        });

        // shows dialog if queue is empty
        if (displayedDialogs.isEmpty()){
            dialog.show();
        }

        // enqueues dialog
        displayedDialogs.offer(dialog);

    }

    public synchronized void enqueueDialog(Dialog dialog){   // Call this if you didn't set a on dismiss listener to your dialog
        enqueueDialog(dialog, null);
    }

    private synchronized void displayNext(){

        // Displays next dialog in queue if one is available
        Dialog next = displayedDialogs.poll();
        if (next != null){
            next.show();
        }
    }
}
