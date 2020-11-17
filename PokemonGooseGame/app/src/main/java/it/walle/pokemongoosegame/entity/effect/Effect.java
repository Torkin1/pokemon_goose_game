package it.walle.pokemongoosegame.entity.effect;

public abstract class Effect {

    private String name;                                            // The readable name of the effect
    private String description;                                     // A textual description of the effect
    public abstract void doEffect(InvocationContext context);       // Implementation of the actual effect

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.getDescription();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}