package it.walle.pokemongoosegame.entity.effect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.ThrowDicesBean;

public class AChallengerApproaches extends YellowEffect {

    int BET = 20;

    @Override
    public void doEffect(InvocationContext invocationContext) {
        super.setEffect_image_dialogID(R.drawable.ash);
        super.setTitle(invocationContext.getContext().getString(R.string.a_challenger_approaches_yellow_effect_title));
        super.setDescription(invocationContext.getContext().getString(R.string.a_challenger_approaches_yellow_effect_description));



        Dialog dialog = generalDialog(invocationContext,

                invocationContext
                        .getContext()
                        .getString(R.string.a_challenger_approaches_yellow_effect_fight_button_name),

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        fight(invocationContext, R.string.a_challenger_approaches_yellow_effect_win_case, R.string.a_challenger_approaches_yellow_effect_lost_case);
                    }
                },

                invocationContext
                        .getContext()
                        .getString(R.string.a_challenger_approaches_yellow_effect_run_button_name),

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        fight(invocationContext, R.string.a_challenger_approaches_yellow_effect_run_win_case, R.string.a_challenger_approaches_yellow_effect_run_lost_case);
                    }
                });
        showDialog(dialog);
    }

    private void fight(InvocationContext invocationContext, int winTextId, int loseTextId){
        ThrowDicesBean throwDicesBean = new ThrowDicesBean();
        throwDicesBean.setNumOfFaces(6);
        throwDicesBean.setNumOfDices(2);
        CoreController.getReference().throwDices(throwDicesBean);

        //Players dice
        int my_roll = throwDicesBean.getExitNumbers().get(0);

        //opponents dice
        int enemy_roll = throwDicesBean.getExitNumbers().get(1);

        AlertDialog.Builder rollDiceDialog = new AlertDialog.Builder(invocationContext.getContext())
                .setTitle(R.string.a_challenger_approaches_yellow_effect_title);

        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());

        if(my_roll >= enemy_roll) {
            rollDiceDialog.setMessage(winTextId);
            player.setMoney(player.getMoney() + BET);
        }
        else {
            rollDiceDialog.setMessage(loseTextId);

            // Makes the player pay the bet
            int payed = player.pay(BET);
            CoreController.getReference().addToPlate(payed);
        }

        rollDiceDialog
                .create()
                .show();
    }

}
