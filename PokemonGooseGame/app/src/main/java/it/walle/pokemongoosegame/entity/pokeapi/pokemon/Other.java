package it.walle.pokemongoosegame.entity.pokeapi.pokemon;

import com.google.gson.annotations.SerializedName;

public class Other {
    private Sprites dream_world;
    @SerializedName("official-artwork")
    private Sprites official_artwork;

    public Sprites getDream_world() {
        return dream_world;
    }

    public void setDream_world(Sprites dream_world) {
        this.dream_world = dream_world;
    }

    public Sprites getOfficial_artwork() {
        return official_artwork;
    }

    public void setOfficial_artwork(Sprites official_artwork) {
        this.official_artwork = official_artwork;
    }
}
