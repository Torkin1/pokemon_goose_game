package it.walle.pokemongoosegame.game;

import androidx.annotation.NonNull;

import it.walle.pokemongoosegame.entity.Game;

public class GameController {
    // Orchestrates game, sequence of turns, effects, ...

    @NonNull
    private Game game;   // Contains all infos about the game

    public GameController(SetupGameBean bean){
        this.game = bean.getGame();
    }

    public void doTurn(){
        // TODO: Does the turn of the current player
    }

    public void nextTurn(){
        // TODO: Changes the current player to the next player
    }

    public void endGame(){
        // TODO: Chooses a winner and ends game
    }


}
