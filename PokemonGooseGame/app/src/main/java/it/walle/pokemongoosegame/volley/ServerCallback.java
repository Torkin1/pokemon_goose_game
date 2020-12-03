package it.walle.pokemongoosegame.volley;

import com.android.volley.Response;

import it.walle.pokemongoosegame.entity.pokeapi.type.Type;

public interface ServerCallback {

    void onSuccess(Object result, Response.Listener<Type> listener);

}
