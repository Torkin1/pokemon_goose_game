package it.walle.pokemongoosegame.entity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;

public class Player {

    // Params needed to start a game. Once set, their values aren't likely to be changed
    private String username;        // Key value
    private Pokemon pokemon;        // Pokemon used by the player in the game.

    // Params nedded to play a game. Their values are likely to change during a game
    private final MutableLiveData<Integer> money = new MutableLiveData<>(0);              // Money collected by the player during game
    private int idleTurns;          // Number of turns the player shall stay idle
    private int currentPosition;    // Index of cell currently occupied by the player in the board

    public int getMoney() {
        return money.getValue();
    }

    public void setMoney(Integer money) {
        // Negative money value is nonsense
        if (money < 0) {
            money = 0;
        }
        this.money.setValue(money);
    }

    public int pay(int fee){
        // makes the player pay up to the specified fee and returns how much they actually payed
        int toPay = Math.min(getMoney(), fee);
        setMoney(getMoney() - toPay);
        return toPay;
    }

    public int getNumOfIdleTurns() {
        return idleTurns;
    }

    public void setIdleTurns(int turns) {
        // Negative turns value is nonsense
        if (turns < 0) {
            turns = 0;
        }
        this.idleTurns = turns;
    }

    public Player() {
        this.username = null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        // Can't go back prior to first cell
        if (currentPosition < 0)
            this.currentPosition = 0;
        else
            this.currentPosition = currentPosition;
    }

    //always keep on eye on the money! ALWAYS
    public void observeMoney(LifecycleOwner lifecycleOwner, Observer<Integer> observer) {
        money.observe(lifecycleOwner, observer);
    }
}
