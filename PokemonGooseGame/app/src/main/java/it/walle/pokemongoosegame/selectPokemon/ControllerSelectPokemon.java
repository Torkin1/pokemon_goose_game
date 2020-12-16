package it.walle.pokemongoosegame.selectPokemon;

import android.content.Context;

import com.android.volley.Response;

import java.util.List;

import it.walle.pokemongoosegame.database.pokeapi.DAOPokemon;
import it.walle.pokemongoosegame.entity.pokeapi.EntityPack;
import it.walle.pokemongoosegame.entity.pokeapi.EntityPointer;
import it.walle.pokemongoosegame.entity.pokeapi.allpokemon.CountPokemon;
import it.walle.pokemongoosegame.entity.pokeapi.pokemon.Pokemon;
import it.walle.pokemongoosegame.game.AddNewPlayerBean;

public class ControllerSelectPokemon{

    private final DAOPokemon daoPokemon;
    private List<AddNewPlayerBean> addNewPlayerBeans;

    public List<AddNewPlayerBean> getAddNewPlayerBeans() {
        return addNewPlayerBeans;
    }

    public void setAddNewPlayerBeans(List<AddNewPlayerBean> addNewPlayerBeans) {
        this.addNewPlayerBeans = addNewPlayerBeans;
    }

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

    //FIXME: SelectPokemonBean has no method called getPlayer
    /*public void selectPokemon(SelectPokemonBean selectPokemonBean) {
        selectPokemonBean
                .getPlayer()
                .setPokemon(
                        selectPokemonBean
                        .getPokemon()
                );
    }
    */

    public void getNumOfPokemons(GetNumOfPokemonBean bean){
        daoPokemon
                .getNumOfPokemon(
                        bean.getListener(),
                        bean.getErrorListener()
                );
    }
}
