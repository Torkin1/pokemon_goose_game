package it.walle.pokemongoosegame.entity.effect;

import android.content.Context;

public class InvocationContext {
    private String triggerUsername;         // Who triggered the effect
    private int whereTriggered;             // Where the effect was triggered
    private Context context;

    //setters and getters
    public String getTriggerUsername() {
        return triggerUsername;
    }

    public void setTriggerUsername(String triggerUsername) {
        this.triggerUsername = triggerUsername;
    }

    public int getWhereTriggered() {
        return whereTriggered;
    }

    public void setWhereTriggered(int whereTriggered) {
        this.whereTriggered = whereTriggered;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
