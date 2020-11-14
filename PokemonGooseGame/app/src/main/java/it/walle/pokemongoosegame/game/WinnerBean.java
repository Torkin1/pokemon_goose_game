package it.walle.pokemongoosegame.game;

public class WinnerBean {

    private String winnerUsername;  // The winner username
    private int score;              // The winner score

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getWinnerUsername() {
        return winnerUsername;
    }

    public void setWinnerUsername(String winnerUsername) {
        this.winnerUsername = winnerUsername;
    }
}
