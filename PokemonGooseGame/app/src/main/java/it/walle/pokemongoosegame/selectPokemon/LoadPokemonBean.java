package it.walle.pokemongoosegame.selectPokemon;

import com.android.volley.Response;

import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;

public class LoadPokemonBean {
    private String pokemonName;
    private Response.Listener<Pokemon> listener;
    private Response.ErrorListener errorListener;

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public Response.Listener<Pokemon> getListener() {
        return listener;
    }

    public void setListener(Response.Listener<Pokemon> listener) {
        this.listener = listener;
    }

    public Response.ErrorListener getErrorListener() {
        return errorListener;
    }

    public void setErrorListener(Response.ErrorListener errorListener) {
        this.errorListener = errorListener;
    }
}
