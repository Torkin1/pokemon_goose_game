package it.walle.pokemongoosegame.entity;

import java.util.List;

import it.walle.pokemongoosegame.entity.board.Board;

public class Game{

    private List<Player> gamers;
    private List<Integer> scores;
    private Board board;

    public Game(){
        this.gamers = null;
        this.scores = null;
        this.board = null;
    }

    public void setGamers(List<Player> gamers){
        this.gamers = gamers;
    }

    public List<Player> getGamers(){
        return this.gamers;
    }

    public void setScores(List<Integer> scores){
        this.scores = scores;
    }

    public List<Integer> getScores(){
        return this.scores;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public Board getBoard(){return this.board;}
}
