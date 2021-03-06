package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class ItsATrap extends YellowEffect {
    @Override
    public void doEffect(InvocationContext invocationContext) {
        //setting the dialog
        super.setEffect_image_dialogID(R.drawable.its_a_trap);
        super.setDescription(invocationContext.getContext().getString(R.string.its_a_trap_yellow_effect_description));
        super.setTitle(invocationContext.getContext().getString(R.string.its_a_trap_yellow_effect_title));
        showDialog(generalDialog(invocationContext));

        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());

        //the effect was doing a malus in the up of 20%
        int current_hp = player.getPokemon().getCurrentHp();
        int malus_hp = current_hp - player.getPokemon().getMaxHp() * 20 / 100;
        player.getPokemon().setCurrentHp(malus_hp);

    }
}
