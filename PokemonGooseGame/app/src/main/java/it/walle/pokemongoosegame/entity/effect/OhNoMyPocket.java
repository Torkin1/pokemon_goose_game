package it.walle.pokemongoosegame.entity.effect;

import android.util.Log;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class OhNoMyPocket extends YellowEffect{
    int ADDED_COINS = 20;
    int MALUS_POSITION = -6;
    @Override
    public void doEffect(InvocationContext invocationContext) {
        super.setEffect_image_dialogID(R.drawable.oh_no);
        super.setDescription(invocationContext.getContext().getString(R.string.oh_no_my_pocket_yellow_effect_description));
        super.setTitle(invocationContext.getContext().getString(R.string.oh_no_my_pocket_yellow_effect_title));
        super.generalDialog(invocationContext).show();

        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());
        player.setMoney(player.getMoney() + ADDED_COINS);

        player.setCurrentPosition(player.getCurrentPosition() + MALUS_POSITION);
        generalDialog(invocationContext);
    }
}
