package it.walle.pokemongoosegame.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.walle.pokemongoosegame.entity.board.Board;

public class Game{

    private List<Player> gamers = new ArrayList<>();                    // All players in the game
    private Board board;                                                // Board used in the game
    private int plate;                                                  // The plate of the game
    private int currentPlayerIndex;                                     // Current player index
    private int nextPlayerIndex;                                        // Next Player index
    private final List<Player> winners = new ArrayList<>();             // If a player wins the game is added to this list. Only cool guys allowed

    public int getNextPlayerIndex() {
        return nextPlayerIndex;
    }

    public void setNextPlayerIndex(int nextPlayerIndex) {
        this.nextPlayerIndex = nextPlayerIndex;
    }

    public List<Player> getWinners() {
        return winners;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        // currentPlayerIndex must not be a negative value or a value greater than gamers.size
        if (currentPlayerIndex < 0 || currentPlayerIndex > gamers.size()){
            throw new IndexOutOfBoundsException();
        }
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public List<Player> getGamers(){
        return this.gamers;
    }

    public Player getPlayerByUsername(String username){
        for (Player p : gamers){
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

    public void setPlate(int plate){ this.plate = plate; }

    public int getPlate(){ return this.plate; }
}
