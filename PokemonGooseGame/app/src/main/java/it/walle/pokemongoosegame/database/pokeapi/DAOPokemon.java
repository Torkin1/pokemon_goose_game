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
    private ServerCallback serverCallback;

    public static DAOPokemon getReference(){
        if(reference == null){
            reference = new DAOPokemon();
        }
        return reference;
    }

    public ServerCallback getServerCallback(){
        return this.serverCallback;
    }

    public void getNumOfPokemon(Context context, Response.Listener<CountPokemon> listener, Response.ErrorListener errorListener){

        String url = PokeAPIGetter.getReference().getNumOfPokemon();

        //Richiesta volley per ottenere il numero di pokemon totale
        RequestQueueHolder
                .getInstance(context)
                .getRequestQueue()
                .add(
                        new GsonQueryRequest<CountPokemon>(
                                Request.Method.GET,
                                url,
                                listener,
                                errorListener,
                                CountPokemon.class
                        ));
    }

    public void loadAllPokemonPointers(Context context, Response.Listener<EntityPack> listener, Response.ErrorListener errorListener){

        //Creazione del listner per ottenere il numero di pokemon totale
        Response.Listener<CountPokemon> listenerForNumOfPokemon = new Response.Listener<CountPokemon>(){
            @Override
            public void onResponse(CountPokemon response) {
                int numOfPokemon = response.getCount();

                //Richiesta volley per ottenere tutti i nomi dei pokemon con il limite di "numOfPokemon"

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

    public void loadPokemonByName(Context context, String pokemonName, Response.Listener<Pokemon> listener, Response.ErrorListener errorListener){

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

    public void loadPokemonByName(Context context, String pokemonName, Response.Listener<Pokemon> listener, Response.ErrorListener errorListener, RequestQueue queue){
        String url = PokeAPIGetter.getReference().getPokemonByName(pokemonName);

        //Creazione di una callback per ottenere tutti i tipi di un pokemon
        this.serverCallback = new ServerCallback() {
            @Override
            public void onSuccess(Object response, Response.Listener<Type> listener) {
                Pokemon pokemon = (Pokemon) response;
                for(int i = 0; i < pokemon.getTypes().length; i++) {
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

        //Richiesta volley per ottenere il pokemon

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
