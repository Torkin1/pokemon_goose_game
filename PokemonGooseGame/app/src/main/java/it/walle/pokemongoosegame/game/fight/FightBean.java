package it.walle.pokemongoosegame.game.fight;

import it.walle.pokemongoosegame.entity.Player;

public class FightBean {
    private String assaultedUsername;   // The attacked player username
    private String assaulterUsername ;  // The attacker player username

    public String getAssaultedUsername() {
        return assaultedUsername;
    }

    public void setAssaultedUsername(String assaultedUsername) {
        this.assaultedUsername = assaultedUsername;
    }

    public String getAssaulterUsername() {
        return assaulterUsername;
    }

    public void setAssaulterUsername(String assaulterUsername) {
        this.assaulterUsername = assaulterUsername;
    }
}
