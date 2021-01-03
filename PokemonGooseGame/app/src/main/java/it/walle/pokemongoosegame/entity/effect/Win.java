package it.walle.pokemongoosegame.entity.effect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.WinnerBean;

public class Win extends Effect {

    @Override
    public void doEffect(InvocationContext invocationContext) {
        super.setEffect_image_dialogID(R.drawable.win_dialog_image);
        super.setDescription(invocationContext.getContext().getString(R.string.win_description_dialog));
        super.setTitle(invocationContext.getContext().getString(R.string.win_title));
        super.generalDialog(invocationContext).show();
        // Makes the trigger user to win the game
        WinnerBean winnerBean = new WinnerBean();
        winnerBean.setWinnerUsername(invocationContext.getTriggerUsername());
        CoreController.getReference().chooseWinner(winnerBean);

        Log.d("win", invocationContext.getTriggerUsername() + "wins");

        generalDialog(invocationContext);
    }

}
