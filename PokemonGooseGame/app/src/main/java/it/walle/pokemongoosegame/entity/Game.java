package it.walle.pokemongoosegame.entity;

import java.util.List;

public class Game{

    private List<User> gamers;
    private List<Integer> scores;
    private Board board;

    public Game(){
        this.gamers = null;
        this.scores = null;
        this.board = null;
    }

    public void setGamers(List<User> gamers){
        this.gamers = gamers;
    }

    public List<User> getGamers(){
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
