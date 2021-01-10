package it.walle.pokemongoosegame.activities.addplayer.selectpokemon;

import com.android.volley.Response;

import java.util.List;

import it.walle.pokemongoosegame.database.pokeapi.DAOPokemon;
import it.walle.pokemongoosegame.entity.pokeapi.EntityPack;
import it.walle.pokemongoosegame.entity.pokeapi.EntityPointer;
import it.walle.pokemongoosegame.game.AddNewPlayerBean;

public class ControllerSelectPokemon{

    private final DAOPokemon daoPokemon;
    private List<AddNewPlayerBean> addNewPlayerBeans;

    public List<AddNewPlayerBean> getAddNewPlayerBeans() {
        return addNewPlayerBeans;
    }//list with new player


    public void setAddNewPlayerBeans(List<AddNewPlayerBean> addNewPlayerBeans) {
        this.addNewPlayerBeans = addNewPlayerBeans;
    }

    public ControllerSelectPokemon(){
        this.daoPokemon = DAOPokemon.getReference();
    }

    public void loadPokemon(LoadPokemonBean bean) {

        this.daoPokemon
                .loadPokemonByName(
                        bean.getContext(),
                        bean.getPokemonName(),
                        bean.getListener(),
                        bean.getErrorListener()
                );
    }

    public void loadAllPokemons(LoadPokemonBean bean) {

        // Queries all pokemon pointers
        daoPokemon.loadAllPokemonPointers(
                bean.getContext(),
                new Response.Listener<EntityPack>() {
            @Override
            public void onResponse(EntityPack response) {
                for (EntityPointer ep : response.getResults()){

                    // Queries the pokemon pointed by ep using the listener passed as input
                    if (bean.getPokemonRequestQueue() != null){
                        daoPokemon.loadPokemonByName(bean.getContext(), ep.getName(), bean.getListener(), bean.getErrorListener(), bean.getPokemonRequestQueue());
                    } else {
                        daoPokemon.loadPokemonByName(bean.getContext(), ep.getName(), bean.getListener(), bean.getErrorListener());
                    }
                }
            }
        },
                null);
    }

    public void getNumOfPokemons(GetNumOfPokemonBean bean){
        daoPokemon
                .getNumOfPokemon(
                        bean.getContext(),
                        bean.getListener(),
                        bean.getErrorListener()
                );
    }
}
