package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class CaughtWithoutMask extends YellowEffect{

    @Override
    public void doEffect(InvocationContext invocationContext) {
        setEffect_image_dialogID(R.drawable.officer);
        setTitle(invocationContext.getContext().getString(R.string.caught_you_without_mask_yellow_effect_title));
        setDescription(invocationContext.getContext().getString(R.string.caught_you_without_mask_yellow_effect_description));

        showDialog(generalDialog(invocationContext));
        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());
        int fee = player.getMoney()/2;

        //Always keep the right order, first you take the money and after you take the money from him
        CoreController.getReference().setPlate(CoreController.getReference().getPlate() + fee);
        player.setMoney(player.getMoney() - fee);

        player.getPokemon().setCurrentHp(player.getPokemon().getCurrentHp() - player.getPokemon().getMaxHp()*50/100);





        player.setIdleTurns(player.getNumOfIdleTurns() + 2);
    }
}
