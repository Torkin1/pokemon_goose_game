/*package it.walle.pokemongoosegame.volley;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;

import it.walle.pokemongoosegame.dao.DAOPokemon;
import it.walle.pokemongoosegame.dao.DAOType;
import it.walle.pokemongoosegame.entity.pokeapi.Pokemon;
import it.walle.pokemongoosegame.entity.pokeapi.type.Type;

public class ResponseSelectPokemonVolley implements Response.Listener<String> {
    private Context context;

    public ResponseSelectPokemonVolley(Context context){
        this.context = context;
    }

    @Override
    public void onResponse(String response) {
        DAOPokemon.getReference(this.context).
                getServerCallbackPokemon().
                onSuccess(response,
                        new ResponseSelectPokemonVolley(context){
                            @Override
                            public void onResponse(String response) {
                                DAOType.getReference(context).
                                        getServerCallback().
                                        onSuccess(response, null);
                                Type type = (Type) DAOType.getReference(context).getResults();
                                MutableLiveData<Pokemon> pokemon = (MutableLiveData<Pokemon>) DAOPokemon.getReference(context).getResults();
                                pokemon.getValue().getType().add(type);

                                System.out.println("POKEMON NAME: " + pokemon.getValue().getName() + ", ID: " + pokemon.getValue().getId() + ", HP: " + pokemon.getValue().getHp());
                                for(int i = 0; i < pokemon.getValue().getType().size(); i++){
                                    System.out.println("TYPE: " + pokemon.getValue().getType().get(i).getName() + " DOUBLE FROM: " + pokemon.getValue().getType().get(i).getDouble_damage_from() + " HALF FROM: " + pokemon.getValue().getType().get(i).getHalf_damage_from() + " NO FROM: " + pokemon.getValue().getType().get(i).getNo_damage_from());
                                }
                            }
                        });
    }
}


 */