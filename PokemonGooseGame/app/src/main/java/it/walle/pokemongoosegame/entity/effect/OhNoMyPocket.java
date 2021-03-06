package it.walle.pokemongoosegame.entity.effect;

import android.util.Log;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class OhNoMyPocket extends YellowEffect{
    //Classes constants
    int LOST_COINS = 20;
    int MALUS_POSITION = 6;
    @Override
    public void doEffect(InvocationContext invocationContext) {
        //setting the dialog

        super.setEffect_image_dialogID(R.drawable.oh_no);
        super.setTitle(invocationContext.getContext().getString(R.string.oh_no_my_pocket_yellow_effect_title));
        super.setDescription(invocationContext.getContext().getString(R.string.oh_no_my_pocket_yellow_effect_description));
        showDialog(generalDialog(invocationContext));

        //getting the current player and using the pay method, in case the player has the money pays it, otherwise not
        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());
        int payed = player.pay(LOST_COINS);
        CoreController.getReference().addToPlate(payed);

        player.setCurrentPosition(player.getCurrentPosition() - MALUS_POSITION);
    }
}
