package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class Pokemon{

    // PokeAPI read-only fields
    private int id;
    private String name;
    private Stats[] stats;
    private TypePointerPokemon[] types;
    private Sprites sprites;

    // Dynamic fields
    private final MutableLiveData<Integer> currentHp = new MutableLiveData<>();   // Remaining pokemon health
    private int maxHp;                                                            // Max pokemon health

    public Pokemon(){}
    //all the getters and setters

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stats[] getStats() {
        return stats;
    }

    public void setStats(Stats[] stats) {
        this.stats = stats;
    }

    public TypePointerPokemon[] getTypes() {
        return types;
    }

    public void setTypes(TypePointerPokemon[] types) {
        this.types = types;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public Integer getCurrentHp() {
        return currentHp.getValue();
    }

    //set hp, if less than 0, then its 0
    public void setCurrentHp(Integer currentHp) {
        if(currentHp < 0)
            this.currentHp.setValue(0);
        else
            this.currentHp.setValue(currentHp);
    }
    //there is always an obs. on the hp status, in case it's 0
    public void observeCurrentHp(LifecycleOwner lifecycleOwner, Observer<Integer> observer){
        this.currentHp.observe(lifecycleOwner, observer);
    }

    //class to increase the hp, and control if its or not higher than max hp
    public void heal(int healAmount){
        if(healAmount + getCurrentHp() > getMaxHp())
            setCurrentHp(getMaxHp());
        else
            setCurrentHp(healAmount + getCurrentHp());
    }
}
