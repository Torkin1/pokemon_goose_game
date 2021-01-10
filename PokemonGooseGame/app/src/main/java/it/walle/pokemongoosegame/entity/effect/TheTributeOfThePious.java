package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class TheTributeOfThePious extends YellowEffect {
    int TAXES = 10;//constant of the class

    @Override
    public void doEffect(InvocationContext invocationContext) {
        //setting the dialog
        super.setEffect_image_dialogID(R.drawable.the_pious);
        super.setDescription(invocationContext.getContext().getString(R.string.the_tribute_of_the_pious_yellow_effect_description));
        super.setTitle(invocationContext.getContext().getString(R.string.the_tribute_of_the_pious_yellow_effect_title));
        showDialog(generalDialog(invocationContext));

        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());

        int current_hp = player.getPokemon().getCurrentHp();
        int malus_hp = current_hp - player.getPokemon().getMaxHp() * 10 / 100;

        //if there are enough money pay the taxes to the plate
        if (player.getMoney() > TAXES) {
            int payed = player.pay(TAXES);
            CoreController.getReference().addToPlate(payed);
        } else
            player.getPokemon().setCurrentHp(malus_hp);
    }
}
