package it.walle.pokemongoosegame.selectpokemon;

import android.content.Context;

import com.android.volley.Response;

import it.walle.pokemongoosegame.entity.pokeapi.allpokemon.CountPokemon;

public class GetNumOfPokemonBean {
    private Context context;//need the context on it
    private Response.Listener<CountPokemon> listener;//verry useful, we need to know the number of pokemon
    private Response.ErrorListener errorListener;

    public Response.Listener<CountPokemon> getListener() {
        return listener;
    }

    public void setListener(Response.Listener<CountPokemon> listener) {
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
