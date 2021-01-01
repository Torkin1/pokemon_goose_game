package it.walle.pokemongoosegame.entity.effect;

import android.util.Log;

import it.walle.pokemongoosegame.game.CoreController;
import it.walle.pokemongoosegame.game.WinnerBean;

public class Win extends Effect {

    @Override
    public void doEffect(InvocationContext invocationContext) {

        // Makes the trigger user to win the game
        WinnerBean winnerBean = new WinnerBean();
        winnerBean.setWinnerUsername(invocationContext.getTriggerUsername());
        CoreController.getReference().chooseWinner(winnerBean);

        Log.d("win", invocationContext.getTriggerUsername() + "wins");

        // TODO: make view to display the winner with their score

    }
}
