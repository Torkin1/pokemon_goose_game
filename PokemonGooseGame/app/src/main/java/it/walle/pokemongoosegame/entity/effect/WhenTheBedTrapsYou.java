package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class WhenTheBedTrapsYou extends YellowEffect{
    @Override
    public void doEffect(InvocationContext invocationContext) {
        setEffect_image_dialogID(R.drawable.sleep);
        setTitle(invocationContext.getContext().getString(R.string.when_the_bed_traps_you_yellow_effect_title));
        setDescription(invocationContext.getContext().getString(R.string.when_the_bed_traps_you_yellow_effect_description));

        showDialog(generalDialog(invocationContext));

        //used a heal class to prevent to much heal, more than max hp
        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());
        player.getPokemon().heal(player.getPokemon().getCurrentHp() + player.getPokemon().getMaxHp()*30/100);
        player.setIdleTurns(player.getNumOfIdleTurns() + 1);
    }
}
