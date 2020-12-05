package it.walle.pokemongoosegame.volley;

import android.content.Context;

import com.android.volley.Response;

import it.walle.pokemongoosegame.database.pokeapi.DAOPokemon;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;
import it.walle.pokemongoosegame.entity.pokeapi.type.Type;

// TODO: Does this class have any use?

public class ResponseSelectPokemonVolley implements Response.Listener<Pokemon> {
    private Context context;
    private Response.Listener listener; //listner per la ricezione del tipo di pokemon

    public ResponseSelectPokemonVolley(Context context, Response.Listener<Type> listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onResponse(Pokemon response) {

        //Chiamata alla callback di DAOPokemon per conoscere i tipi del pokemon
        DAOPokemon
                .getReference(this.context)
                .getServerCallback()
                .onSuccess(response,
                        this.listener);
    }
}
