package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.game.CoreController;

public class NoPlaceLikeHome extends YellowEffect {
    @Override
    public void doEffect(InvocationContext invocationContext) {
        super.setEffect_image_dialogID(R.drawable.no_place_like_home);
        super.setDescription(invocationContext.getContext().getString(R.string.no_place_like_home_yellow_effect));
        super.setTitle(invocationContext.getContext().getString(R.string.no_place_like_home_yellow_effect_title));
        super.generalDialog(invocationContext).show();

        Player player = CoreController.getReference().getPlayerByUsername(invocationContext.getTriggerUsername());

        int plus_hp = player.getPokemon().getMaxHp() / 10;
        player.getPokemon().heal(plus_hp);
    }
}
