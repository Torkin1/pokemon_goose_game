package it.walle.pokemongoosegame.selectPokemon;

import com.android.volley.Response;

import it.walle.pokemongoosegame.entity.pokeapi.allpokemon.CountPokemon;

public class GetNumOfPokemonBean {
    private Response.Listener<CountPokemon> listener;
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
}
