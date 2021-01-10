package it.walle.pokemongoosegame.database.pokeapi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import it.walle.pokemongoosegame.entity.pokeapi.EntityPack;
import it.walle.pokemongoosegame.entity.pokeapi.allpokemon.CountPokemon;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;
import it.walle.pokemongoosegame.entity.pokeapi.type.Type;
import it.walle.pokemongoosegame.volley.GsonQueryRequest;
import it.walle.pokemongoosegame.volley.RequestQueueHolder;
import it.walle.pokemongoosegame.volley.ServerCallback;

public class DAOPokemon {

    private static DAOPokemon reference = null;
    private ServerCallback serverCallback;//declare a serverCallback so teh situation can be monitored even if online

    public static DAOPokemon getReference() {
        if (reference == null) {
            reference = new DAOPokemon();
        }
        return reference;
    }

    public ServerCallback getServerCallback() {
        return this.serverCallback;
    }

    public void getNumOfPokemon(Context context, Response.Listener<CountPokemon> listener, Response.ErrorListener errorListener) {

        String url = PokeAPIGetter.getReference().getNumOfPokemon();

        //Volley request to get the number of the pokemon, how many there are in total
        RequestQueueHolder
                .getInstance(context)
                .getRequestQueue()
                .add(
                        new GsonQueryRequest<CountPokemon>(//to make anything easier we parse them to Gson
                                Request.Method.GET,
                                url,
                                listener,
                                errorListener,
                                CountPokemon.class
                        ));
    }

    public void loadAllPokemonPointers(Context context, Response.Listener<EntityPack> listener, Response.ErrorListener errorListener) {

        //Creating the listener to get the pokemon numbers
        Response.Listener<CountPokemon> listenerForNumOfPokemon = new Response.Listener<CountPokemon>() {
            @Override
            public void onResponse(CountPokemon response) {
                int numOfPokemon = response.getCount();

                //Volley request to get the names of all the pokemon, the limits is "numOfPokemon"

                String url = PokeAPIGetter.getReference().getAllPokemonPointers(numOfPokemon);

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
                                ));
            }
        };

        getNumOfPokemon(context, listenerForNumOfPokemon, null);
    }

    public void loadPokemonByName(Context context, String pokemonName, Response.Listener<Pokemon> listener, Response.ErrorListener errorListener) {

        loadPokemonByName(
                context,
                pokemonName,
                listener,
                errorListener,
                RequestQueueHolder
                        .getInstance(context)
                        .getRequestQueue()
        );
    }

    public void loadPokemonByName(Context context, String pokemonName, Response.Listener<Pokemon> listener, Response.ErrorListener errorListener, RequestQueue queue) {
        String url = PokeAPIGetter.getReference().getPokemonByName(pokemonName);

        //Creating a callback to obtain all pokemon types
        this.serverCallback = new ServerCallback() {
            @Override
            public void onSuccess(Object response, Response.Listener<Type> listener) {
                Pokemon pokemon = (Pokemon) response;
                for (int i = 0; i < pokemon.getTypes().length; i++) {
                    DAOType.getReference(context)
                            .loadTypeByName(
                                    pokemon.getTypes()[i]
                                            .getType()
                                            .getName(),
                                    listener,
                                    null
                            );
                }
            }
        };

        //Volley requesto to obtain the pokemon

        queue.add(
                new GsonQueryRequest<Pokemon>(
                        Request.Method.GET,
                        url,
                        listener,
                        errorListener,
                        Pokemon.class
                ));
    }
}
