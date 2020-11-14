package it.walle.pokemongoosegame.entity.effect;

public class InvocationContext {
    private String triggerUsername;         // Who triggered the effect
    private int whereTriggered;             // Where the effect was triggered

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
}
