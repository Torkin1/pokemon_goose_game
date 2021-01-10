package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class NoPlaceLikeHome extends YellowEffect {
    @Override
    public void doEffect(InvocationContext invocationContext) {
        //setting the dialog
        super.setEffect_image_dialogID(R.drawable.no_place_like_home);
        super.setTitle(invocationContext.getContext().getString(R.string.no_place_like_home_yellow_effect_title));
        super.setDescription(invocationContext.getContext().getString(R.string.no_place_like_home_yellow_effect));
        showDialog(generalDialog(invocationContext));

        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());

        //only if the type is the same, the bonus will be added
        for (int i = 0; i < player.getPokemon().getTypes().length; i++) {
            if (player.getPokemon().getTypes()[i].toString().
                    compareTo(CoreController.getReference().getBoard().getCells().
                            get(invocationContext.getWhereTriggered()).getType()) == 0) {
                int plus_hp = player.getPokemon().getMaxHp() / 30;
                player.getPokemon().heal(plus_hp);
            }
        }
    }
}
