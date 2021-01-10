package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class ThatLittleBrat extends YellowEffect {

    @Override
    public void doEffect(InvocationContext invocationContext) {
        //setting the dialog
        super.setEffect_image_dialogID(R.drawable.thief);
        super.setTitle(invocationContext.getContext().getString(R.string.that_little_brat_yellow_effect_title));
        super.setDescription(invocationContext.getContext().getString(R.string.that_little_brat_yellow_effect_description));
        showDialog(generalDialog(invocationContext));

        //get current player
        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());

        int stolen = player.getMoney() / 2;

        //and make him pay!
        CoreController.getReference().setPlate(CoreController.getReference().getPlate() + stolen);
        player.pay(stolen);
    }
}
