package it.walle.pokemongoosegame.graphics;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import it.walle.pokemongoosegame.R;


public class WIPDialog extends AlertDialog {


    public WIPDialog(@NonNull Context context) {
        super(context);
        setTitle(R.string.work_in_progress_dialog_title);
        setIcon(R.drawable.work_in_progress_icon);
        setMessage(context.getString(R.string.work_in_progress_dialog_description));
    }
}
