package it.walle.pokemongoosegame.entity;

import java.util.List;

public class Game {

    // Models a game
    private List<Player> players;
    private Board board;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
