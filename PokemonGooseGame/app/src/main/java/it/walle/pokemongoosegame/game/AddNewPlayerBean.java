package it.walle.pokemongoosegame.game;

public class AddNewPlayerBean {
    private String playerUsername;  // The player username
    private int pokemonId;          // The pokemon id

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }
}
