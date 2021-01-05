package it.walle.pokemongoosegame.entity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.game.PlayerNotInGameException;

public class Game{

    private final List<Player> inGamePlayers = new ArrayList<>();       // All players registered in the game
    private Board board;                                                // Board used in the game
    private final MutableLiveData<Integer> plate = new MutableLiveData<>(0);                                         // The plate of the game
    private int currentPlayerIndex = 0;                                 // Current player index
    private int nextPlayerIndex = currentPlayerIndex + 1;               // Next Player index
    private final List<Player> allPlayers = new ArrayList<>();          // All players currently in game
    private final List<Player> winners = new ArrayList<>();             // If a player wins the game is added to this list. Only cool guys allowed
    private final List<Player> losers = new ArrayList<>();              // If a player lose the game is added to this list. Only bad players allowed

    public int getNextPlayerIndex() {
        return nextPlayerIndex;
    }

    public void setNextPlayerIndex(int nextPlayerIndex) {
        this.nextPlayerIndex = nextPlayerIndex;
    }

    public List<Player> getWinners() {
        return winners;
    }

    public List<Player> getLosers() {
        return losers;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        // currentPlayerIndex must not be a negative value or a value greater than gamers.size
        if (currentPlayerIndex < 0 || currentPlayerIndex > inGamePlayers.size()){
            throw new IndexOutOfBoundsException();
        }
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public List<Player> getInGamePlayers(){
        return this.inGamePlayers;
    }

    public Player getPlayerByUsername(String username){
        for (Player p : inGamePlayers){
            if (p.getUsername().equals(username)){
                return p;
            }
        }
        throw new PlayerNotInGameException(username);
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public Board getBoard(){return this.board;}

    public void setPlate(Integer plate){ this.plate.setValue(plate); }

    public int getPlate(){ return this.plate.getValue(); }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

        public void observePlate(LifecycleOwner lifecycleOwner, Observer<Integer> observer){
            plate.observe(lifecycleOwner, observer);
        }
}
