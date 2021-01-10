package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class CaughtWithoutMask extends YellowEffect{
    //class constants
    int MALUS_POSITION = -6;
    int FEE = 25;


    @Override
    public void doEffect(InvocationContext invocationContext) {
        //setting the dialog
        setEffect_image_dialogID(R.drawable.officer);
        setTitle(invocationContext.getContext().getString(R.string.caught_you_without_mask_yellow_effect_title));
        setDescription(invocationContext.getContext().getString(R.string.caught_you_without_mask_yellow_effect_description));

        showDialog(generalDialog(invocationContext));
        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());

        //Always keep the right order, first you take the money and after you put them on the plate
        int payed = player.pay(FEE);
        CoreController.getReference().addToPlate(payed);

        player.setCurrentPosition(player.getCurrentPosition() + MALUS_POSITION);
        player.setIdleTurns(player.getNumOfIdleTurns() + 1);
    }
}
