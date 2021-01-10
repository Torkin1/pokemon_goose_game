package it.walle.pokemongoosegame.entity.effect;

import android.app.Dialog;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class AnotherDayAnotherVictory extends YellowEffect {
    private static final int ADDED_COINS = 30;//classes Constant

    @Override
    public void doEffect(InvocationContext invocationContext) {
        //setting the dialog
        setEffect_image_dialogID(R.drawable.another_day_another_victory);
        setDescription(invocationContext.getContext().getString(R.string.another_day_another_victory_description_dialog));
        setTitle(invocationContext.getContext().getString(R.string.another_day_another_victory_title_dialog));
        showDialog(generalDialog(invocationContext));

        //applying effect consequences
        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());
        player.setMoney(player.getMoney() + ADDED_COINS);
    }
}
