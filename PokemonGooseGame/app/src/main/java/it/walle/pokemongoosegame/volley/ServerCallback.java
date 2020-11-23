package it.walle.pokemongoosegame.volley;

import com.android.volley.Response;

public interface ServerCallback {
    void onSuccess(Object result, Response.Listener<String> listener);
}
