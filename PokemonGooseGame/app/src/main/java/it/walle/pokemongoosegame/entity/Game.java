package it.walle.pokemongoosegame.entity;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.entity.board.Board;
import it.walle.pokemongoosegame.game.PlayerNotInGameException;

public class Game{

    private final List<Player> players = new ArrayList<>();              // All players in the game
    private Board board;                                                // Board used in the game
    private Integer plate = 0;                                          // The plate of the game
    private int currentPlayerIndex = 0;                                 // Current player index
    private int nextPlayerIndex = currentPlayerIndex + 1;               // Next Player index
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
        if (currentPlayerIndex < 0 || currentPlayerIndex > players.size()){
            throw new IndexOutOfBoundsException();
        }
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public List<Player> getPlayers(){
        return this.players;
    }

    public Player getPlayerByUsername(String username){
        for (Player p : players){
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
