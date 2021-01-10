package it.walle.pokemongoosegame.game;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.boardfactory.CreateBoardBean;
import it.walle.pokemongoosegame.entity.Game;

public class CreateGameBean {

    private List<AddNewPlayerBean> playerBeans;     // Players to be added to the game
    private CreateBoardBean createBoardBean;        // Settings used by board factory to create a new board
    private Observer<Game> gameObserver;            // What to do when the game is ready
    private LifecycleOwner lifeCycleOwner;          // gameLiveData is observed until lifeCiycleOwner is destroyed
    private LiveData<Game> gameLiveData;            // returned game intance wrapped in a livedata
    private Context context;                        // The context of the calling activity

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public CreateGameBean() {
        playerBeans = new ArrayList<>();
    }

    public LifecycleOwner getLifeCycleOwner() {
        return lifeCycleOwner;
    }

    public void setLifeCycleOwner(LifecycleOwner lifeCycleOwner) {
        this.lifeCycleOwner = lifeCycleOwner;
    }

    public List<AddNewPlayerBean> getPlayerBeans() {
        return playerBeans;
    }

    public void setPlayerBeans(List<AddNewPlayerBean> playerBeans) {
        this.playerBeans = playerBeans;
    }

    public CreateBoardBean getCreateBoardBean() {
        return createBoardBean;
    }

    public void setCreateBoardBean(CreateBoardBean createBoardBean) {
        this.createBoardBean = createBoardBean;
    }

    public Observer<Game> getGameObserver() {
        return gameObserver;
    }

    public void setGameObserver(Observer<Game> gameObserver) {
        this.gameObserver = gameObserver;
    }

    public LiveData<Game> getGameLiveData() {
        return gameLiveData;
    }

    public void setGameLiveData(LiveData<Game> gameLiveData) {
        this.gameLiveData = gameLiveData;
    }
}
