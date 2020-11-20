package it.walle.pokemongoosegame.game;

import java.util.Map;

public class NextTurnBean {
    private Map<String, Integer> whoWaited;        // Username, idleTurns pair

    public Map<String, Integer> getWhoWaited() {
        return whoWaited;
    }

    public void setWhoWaited(Map<String, Integer> whoWaited) {
        this.whoWaited = whoWaited;
    }
}
