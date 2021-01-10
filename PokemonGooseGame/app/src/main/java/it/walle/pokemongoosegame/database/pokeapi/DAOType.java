package it.walle.pokemongoosegame.database.pokeapi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;

import it.walle.pokemongoosegame.entity.pokeapi.EntityPack;
import it.walle.pokemongoosegame.entity.pokeapi.type.Type;
import it.walle.pokemongoosegame.volley.GsonQueryRequest;
import it.walle.pokemongoosegame.volley.RequestQueueHolder;

public class DAOType {
    private static DAOType reference = null;
    private final Context context;

    //class used to load all the needed types, we'll use them for effect, pokemon entity, create an image for the type

    public static DAOType getReference(Context context) {
        if (reference == null) {
            reference = new DAOType(context);
        }
        return reference;
    }

    private DAOType(Context context) {
        this.context = context;
    }

    public void loadTypeByName(String name, Response.Listener<Type> listener, Response.ErrorListener errorListener) {

        // Gets the api url to request the type
        String url = PokeAPIGetter.getReference().getTypeByName(name);

        // Sends request to pokeapi
        RequestQueueHolder
                .getInstance(context)
                .getRequestQueue()
                .add(
                        new GsonQueryRequest<Type>(
                                Request.Method.GET,
                                url,
                                listener,
                                errorListener,
                                Type.class
                        ));
    }

    public void loadAllTypePointers(Response.Listener<EntityPack> listener, Response.ErrorListener errorListener) {
        String url = PokeAPIGetter.getReference().getAllTypePointers();

        RequestQueueHolder
                .getInstance(context)
                .getRequestQueue()
                .add(
                        new GsonQueryRequest<EntityPack>(
                                Request.Method.GET,
                                url,
                                listener,
                                errorListener,
                                EntityPack.class
                        )
                );
    }
}
