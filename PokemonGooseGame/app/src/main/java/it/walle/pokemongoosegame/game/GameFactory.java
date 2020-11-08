package it.walle.pokemongoosegame.game;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;

import it.walle.pokemongoosegame.boardfactory.BoardFactory;
import it.walle.pokemongoosegame.entity.Game;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.Pokemon;

public class GameFactory {

    private static final String TAG = GameFactory.class.getName();
    private Game game;
    private final Context context;

    public GameFactory(Context context){
        this.context = context;
    }

    public void addNewPlayer(AddNewPlayerBean bean){
        // Creates a Player instance and binds it to the specified pokemon
        Player player = new Player();
        Pokemon pokemon = new Pokemon();
        player.setUsername(bean.getUsername());

        // TODO: queries pokeapi for details to insert in Pokemon instance

        // Binds the pokemon to the Player instance
        player.setPokemon(pokemon);

        // Registers the player to the game;
        this.game.getGamers().add(player);
    }

    public void addNewBoard(AddNewBoardBean bean) throws BoardFactoryCreationFailureException {
        try {

            // Creates a new Board
            BoardFactory.getInstance(this.context, bean.getCreateBoardBean()).createBoard(bean.getCreateBoardBean());

            // Binds the board to the game
            this.game.setBoard(bean.getCreateBoardBean().getBoard());
        }
        catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            throw new BoardFactoryCreationFailureException(e);
        }
    }

    public Game getGame(){
        return this.game;
    }
}
