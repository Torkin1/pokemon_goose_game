package it.walle.pokemongoosegame.entity.effect;

import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.WinnerBean;

public class Win extends Effect {

    @Override
    public void doEffect(InvocationContext context) {

        // Makes the trigger user to win the game
        WinnerBean winnerBean = new WinnerBean();
        winnerBean.setWinnerUsername(context.getTriggerUsername());
        CoreController.getReference().chooseWinner(winnerBean);

        // TODO: make view to display the winner with their score
    }
}
