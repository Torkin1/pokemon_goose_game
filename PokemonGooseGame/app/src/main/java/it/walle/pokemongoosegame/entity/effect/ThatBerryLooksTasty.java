package it.walle.pokemongoosegame.entity.effect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.ThrowDicesBean;

public class ThatBerryLooksTasty extends YellowEffect{
    @Override
    public void doEffect(InvocationContext invocationContext) {
        super.setEffect_image_dialogID(R.drawable.that_berry_looks_tasty);
        super.setDescription(invocationContext.getContext().getString(R.string.that_berry_looks_tasty_dialog_description));
        super.setTitle(invocationContext.getContext().getString(R.string.that_berry_looks_tasty_dialog_title));
        Dialog dialog = generalDialog(invocationContext,

                invocationContext
                        .getContext()
                        .getString(R.string.that_berry_looks_tasty_dialog_positive_button),

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ThrowDicesBean throwDicesBean = new ThrowDicesBean();
                        throwDicesBean.setNumOfDices(1);
                        throwDicesBean.setNumOfFaces(6);

                        CoreController.getReference().throwDices(throwDicesBean);

                        AlertDialog.Builder rollDiceDialog = new AlertDialog.Builder(invocationContext.getContext())
                                .setTitle(R.string.that_berry_looks_tasty_roll_dialog_title);

                        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());
                        int currentHp = player.getPokemon().getCurrentHp();
                        int bonusOrMalus = player.getPokemon().getMaxHp() * 5 / 100;


                        if(throwDicesBean.getExitNumbers().get(0) == 1) {
                            rollDiceDialog.setMessage(R.string.that_berry_looks_tasty_roll_dialog_message_case_1);
                            player.getPokemon().setCurrentHp(currentHp - bonusOrMalus);
                        }
                        else if(throwDicesBean.getExitNumbers().get(0) == 6){
                            rollDiceDialog.setMessage(R.string.that_berry_looks_tasty_roll_dialog_message_case_6);

                            int newHp = currentHp + bonusOrMalus;

                            if(currentHp != player.getPokemon().getMaxHp()){

                                if(newHp > player.getPokemon().getMaxHp()){
                                    newHp = player.getPokemon().getMaxHp();
                                    player.getPokemon().setCurrentHp(newHp);
                                }

                                player.getPokemon().setCurrentHp(newHp);
                                player.setMoney(player.getMoney() + 10);
                            }
                        }
                        else {
                            rollDiceDialog.setMessage(R.string.that_berry_looks_tasty_roll_dialog_message_case_else);

                            int newHp = currentHp + bonusOrMalus;

                            if(currentHp != player.getPokemon().getMaxHp()){

                                if(newHp > player.getPokemon().getMaxHp()){
                                    newHp = player.getPokemon().getMaxHp();
                                    player.getPokemon().setCurrentHp(newHp);
                                }

                                player.getPokemon().setCurrentHp(newHp);
                            }
                        }

                        rollDiceDialog
                                .create()
                                .show();
                    }
                },

                invocationContext
                        .getContext()
                        .getString(R.string.that_berry_looks_tasty_dialog_negative_button),

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        showDialog(dialog);
    }
}
