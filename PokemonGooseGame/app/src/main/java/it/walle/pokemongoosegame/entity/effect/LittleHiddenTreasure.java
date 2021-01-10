package it.walle.pokemongoosegame.entity.effect;

import android.util.Log;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.WinnerBean;

public class LittleHiddenTreasure extends YellowEffect{
    private static final int ADDED_COINS = 50;//Class Constant

    @Override
    public void doEffect(InvocationContext invocationContext) {
        //setting the dialog

        super.setEffect_image_dialogID(R.drawable.little_hidden_treasure);
        super.setDescription(invocationContext.getContext().getString(R.string.little_hidden_treasure_description_dialog));
        super.setTitle(invocationContext.getContext().getString(R.string.little_hidden_treasure_title_dialog));
        showDialog(generalDialog(invocationContext));

        //setting the money, using the setMoney because no controls are needed
        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());
        player.setMoney(player.getMoney() + ADDED_COINS);

        generalDialog(invocationContext);
    }
}
