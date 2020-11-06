package it.walle.pokemongoosegame.entity.board.cell;

import it.walle.pokemongoosegame.entity.effect.Effect;

public class Cell {
    private String type;
    private Effect entryEffect;
    private Effect exitEffect;
    private Effect stayEffect;

    public Effect getExitEffect() {
        return exitEffect;
    }

    public void setExitEffect(Effect exitEffect) {
        this.exitEffect = exitEffect;
    }

    public Effect getStayEffect() {
        return stayEffect;
    }

    public void setStayEffect(Effect stayEffect) {
        this.stayEffect = stayEffect;
    }

    public Effect getEntryEffect() {
        return entryEffect;
    }

    public void setEntryEffect(Effect entryEffect) {
        this.entryEffect = entryEffect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
