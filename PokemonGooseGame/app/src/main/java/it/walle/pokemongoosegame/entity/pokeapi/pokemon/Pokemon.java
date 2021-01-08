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

    public void setCurrentHp(Integer currentHp) {
        if(currentHp < 0)
            this.currentHp.setValue(0);
        else
            this.currentHp.setValue(currentHp);
    }

    public void observeCurrentHp(LifecycleOwner lifecycleOwner, Observer<Integer> observer){
        this.currentHp.observe(lifecycleOwner, observer);
    }

    public void heal(int healAmount){
        if(healAmount + getCurrentHp() > getMaxHp())
            setCurrentHp(getMaxHp());
        else
            setCurrentHp(healAmount + getCurrentHp());
    }
}
