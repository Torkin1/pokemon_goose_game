package it.walle.pokemongoosegame.selectPokemon;

import android.content.Context;

import com.android.volley.Response;

import it.walle.pokemongoosegame.dao.DAOPokemon;

public class ControllerSelectPokemon{

    private DAOPokemon daoPokemon;

    public ControllerSelectPokemon(Context context){
        this.daoPokemon = DAOPokemon.getReference(context);
    }

    public void LoadPokemon(SelectPokemonBean selectPokemonBean, Response.Listener listener, Response.ErrorListener errorListener) {
        String pokemon = selectPokemonBean.getPokemonChoose(); //pokemon scelto dall'utente di cui si vogliono tutte le informazioni da pokeapi tramite volley

        this.daoPokemon
                .LoadPokemon(
                        pokemon,
                        listener,
                        errorListener
                );
    }

    public void LoadAllPokemonName(Response.Listener listener, Response.ErrorListener errorListener) {
        this.daoPokemon
                .LoadAllPokemonName(
                        listener,
                        errorListener
                );
    }

    public void SelectPokemon(SelectPokemonBean selectPokemonBean) {
        selectPokemonBean
                .getPlayer()
                .setPokemon(
                        selectPokemonBean
                        .getPokemon()
                );
    }
}
