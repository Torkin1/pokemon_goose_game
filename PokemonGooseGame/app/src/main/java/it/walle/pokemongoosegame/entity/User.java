package it.walle.pokemongoosegame.entity;

public class User{

    private String username;
    private int pokemonID;

    public User(){
        this.pokemonID = 0;
        this.username = null;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setPokemonID(int pokemonID){
        this.pokemonID = pokemonID;
    }

    public int getPokemonID(){
        return this.pokemonID;
    }
}
