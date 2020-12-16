package it.walle.pokemongoosegame.game;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import it.walle.pokemongoosegame.boardfactory.BoardFactory;
import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;
import it.walle.pokemongoosegame.boardfactory.procedurallygenerated.UnableToCreateBoardException;
import it.walle.pokemongoosegame.entity.Game;
import it.walle.pokemongoosegame.entity.Player;
import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;

public class GameFactory {

    private static final String TAG = GameFactory.class.getName();
    private final Game game;
    private static GameFactory ref = null;

    public static GameFactory getReference(){
        if (ref == null){
            ref = new GameFactory();
        }
        return  ref;
    }

    private GameFactory(){
        this.game = new Game();
    }

    private void addNewPlayer(AddNewPlayerBean bean){

        // Creates a Player instance and binds it to the specified pokemon
        Player player = new Player();
        Pokemon pokemon = bean.getPokemon();
        player.setUsername(bean.getPlayerUsername());

        // Binds the pokemon to the Player instance
        player.setPokemon(pokemon);

        // Registers the player to the game;
        this.game.getGamers().add(player);
    }

    public void createGame(CreateGameBean bean) {

        // Adds all desired players with their corresponding pokemon to the game
        for (AddNewPlayerBean addNewPlayerBean : bean.getPlayerBeans()) {
            this.addNewPlayer(addNewPlayerBean);
        }

        // Creates a board asynchronously
        int CREATE_BOARD_OK = 0;
        int CREATE_BOARD_FAILURE = -1;

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                switch (msg.what) {
                    case BoardFactory.CREATE_BOARD_OK:

                        // Sets board
                        GameFactory.this.game.setBoard((Board) msg.obj);

                        // Notifies observer that the game is ready
                        bean.setGameLiveData(new MutableLiveData<>(game));
                        bean.getGameLiveData().observe(bean.getLifeCycleOwner(), bean.getGameObserver());
                        break;
                    case BoardFactory.CREATE_BOARD_FAILURE:
                        Log.e(TAG, ((Exception) msg.obj).getMessage(), (Exception) msg.obj);
                        break;
                }
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = Message.obtain();
                msg.setTarget(handler);
                try {
                    BoardFactory.getInstance(bean.getContext(), bean.getCreateBoardBean()).createBoard();

                    // Notifies handler that the board is ready
                    msg.what = CREATE_BOARD_OK;
                    msg.obj = bean.getCreateBoardBean().getBoard();
                } catch (UnableToCreateBoardException | ClassNotFoundException e) {

                    // Notifies handler that an error occurred
                    msg.what = CREATE_BOARD_FAILURE;
                    msg.obj = e;
                } finally {
                    msg.sendToTarget();
                }
            }
        }).start();
    }
}
