package it.walle.pokemongoosegame.database.pokeapi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;

import it.walle.pokemongoosegame.entity.pokeapi.type.Type;
import it.walle.pokemongoosegame.volley.GsonQueryRequest;
import it.walle.pokemongoosegame.volley.RequestQueueHolder;

public class DAOType {
    private static DAOType reference = null;
    private Context context;
    private final static String TAG = DAOType.class.getSimpleName();

    public static DAOType getReference(Context context){
        if(reference == null){
            reference = new DAOType(context);
        }
        return reference;
    }

    private DAOType(Context context){this.context = context;}

    public void LoadTypeByName(String name, Response.Listener<Type> listener, Response.ErrorListener errorListener) {

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
}
