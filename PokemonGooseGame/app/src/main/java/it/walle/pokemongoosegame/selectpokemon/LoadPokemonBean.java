package it.walle.pokemongoosegame.selectpokemon;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;

import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;

public class LoadPokemonBean {
    private Context context;
    private String pokemonName;
    private Response.Listener<Pokemon> listener;
    private Response.ErrorListener errorListener;
    private RequestQueue pokemonRequestQueue;       // set this if you want to stack pokemon requests in a different queue from the main one

    public RequestQueue getPokemonRequestQueue() {
        return pokemonRequestQueue;
    }

    public void setPokemonRequestQueue(RequestQueue pokemonRequestQueue) {
        this.pokemonRequestQueue = pokemonRequestQueue;
    }

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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
