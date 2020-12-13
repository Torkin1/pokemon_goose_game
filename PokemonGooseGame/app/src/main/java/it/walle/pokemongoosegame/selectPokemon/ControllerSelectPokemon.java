package it.walle.pokemongoosegame.selectPokemon;

import android.content.Context;

import com.android.volley.Response;

import it.walle.pokemongoosegame.database.pokeapi.DAOPokemon;
import it.walle.pokemongoosegame.entity.pokeapi.EntityPack;
import it.walle.pokemongoosegame.entity.pokeapi.EntityPointer;
import it.walle.pokemongoosegame.entity.pokeapi.allpokemon.CountPokemon;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;

public class ControllerSelectPokemon{

    private static  ControllerSelectPokemon ref;
    public static ControllerSelectPokemon getReference(Context context){
        if (ref == null){
            ref = new ControllerSelectPokemon(context);
        }
        return ref;
    }

    private final DAOPokemon daoPokemon;

    public ControllerSelectPokemon(Context context){
        this.daoPokemon = DAOPokemon.getReference(context);
    }

    public void loadPokemon(LoadPokemonBean bean) {

        this.daoPokemon
                .loadPokemonByName(
                        bean.getPokemonName(),
                        bean.getListener(),
                        bean.getErrorListener()
                );
    }

    public void loadAllPokemons(LoadPokemonBean bean) {

        // Queries all pokemon pointers
        daoPokemon.loadAllPokemonPointers(new Response.Listener<EntityPack>() {
            @Override
            public void onResponse(EntityPack response) {
                for (EntityPointer ep : response.getResults()){

                    // Queries the pokemon pointed by ep using the listener passed as input
                    daoPokemon.loadPokemonByName(ep.getName(), bean.getListener(), bean.getErrorListener());
                }
            }
        }, null);
    }

    public void selectPokemon(SelectPokemonBean selectPokemonBean) {
        selectPokemonBean
                .getPlayer()
                .setPokemon(
                        selectPokemonBean
                        .getPokemon()
                );
    }

    public void getNumOfPokemons(GetNumOfPokemonBean bean){
        daoPokemon
                .getNumOfPokemon(
                        bean.getListener(),
                        bean.getErrorListener()
                );
    }
}
