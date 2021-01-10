package it.walle.pokemongoosegame.game;

public class SkipTurnBean {
    private String playerUsername;       // Player who has to skip the turn
    private boolean hasSkipped;          // true if the player has to skip the turn

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public boolean isHasSkipped() {
        return hasSkipped;
    }//true if the player has to skip, name var should be, shouldskip, don't be rude
    //we are tired

    public void setHasSkipped(boolean hasSkipped) {
        this.hasSkipped = hasSkipped;
    }
}
