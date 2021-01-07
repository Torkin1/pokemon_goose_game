package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class ItsOnFireYo extends YellowEffect {

    @Override
    public void doEffect(InvocationContext invocationContext) {
        setEffect_image_dialogID(R.drawable.rap_jigglypuff);
        setTitle(invocationContext.getContext().getString(R.string.ITS_ON_FIRE_YO_TITLE));
        setDescription(invocationContext.getContext().getString(R.string.ITS_ON_FIRE_YO_DESC));

        generalDialog(invocationContext).show();

        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());
        player.setIdleTurns(player.getNumOfIdleTurns() + 1);
    }
}