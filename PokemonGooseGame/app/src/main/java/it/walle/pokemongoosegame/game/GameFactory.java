package it.walle.pokemongoosegame.game;

import android.content.Context;

import it.walle.pokemongoosegame.boardfactory.BoardFactory;
import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;
import it.walle.pokemongoosegame.entity.Game;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;

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
        Pokemon pokemon = bean.getPokemon();
        player.setUsername(bean.getPlayerUsername());

        // Binds the pokemon to the Player instance
        player.setPokemon(pokemon);

        // Registers the player to the game;
        this.game.getGamers().add(player);
    }

    public void addNewBoard(CreateBoardBean bean) throws BoardFactoryReferencingFailureException {
        try {

            // Creates a new Board
            BoardFactory.getInstance(this.context, bean).createBoard();

            // Binds the board to the game
            this.game.setBoard(bean.getBoard());
        }
        catch (ClassNotFoundException e) {
            throw new BoardFactoryReferencingFailureException(e);
        }
    }

    public Game getGame(){
        return this.game;
    }
}
