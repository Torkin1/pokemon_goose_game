package it.walle.pokemongoosegame.gamefactory;

import it.walle.pokemongoosegame.entity.Game;

public class GameFactory {
    // Manages game development
    // Singleton
    private static GameFactory ref = null;
    public static GameFactory getInstance(){
        if (ref == null) {
            ref = new GameFactory();
        }
        return ref;
    }
    private GameFactory() {}

    // game state
    private Game game;

    public void createGame(GameFactoryBean bean){
        // TODO: sets up a game and stores a reference to it in the bean
    }
}
