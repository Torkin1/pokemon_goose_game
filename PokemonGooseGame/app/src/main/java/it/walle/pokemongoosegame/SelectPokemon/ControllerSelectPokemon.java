package it.walle.pokemongoosegame.SelectPokemon;

import android.content.Context;

import com.android.volley.Response;

import it.walle.pokemongoosegame.DAO.DAOPokemon;

public class ControllerSelectPokemon{

    private DAOPokemon daoPokemon;

    public ControllerSelectPokemon(Context context){
        this.daoPokemon = DAOPokemon.getReference(context);
    }

    public void LoadPokemon(SelectPokemonBean selectPokemonBean, Response.Listener listener){
        this.daoPokemon.LoadPokemon(listener, selectPokemonBean.getPokemonChoose());
    }

    public void LoadAllPokemonName(Response.Listener listener){
        this.daoPokemon.LoadAllPokemonName(listener);
    }

    public void SelectPokemon(SelectPokemonBean selectPokemonBean) {
        selectPokemonBean.getPlayer().setPokemon(selectPokemonBean.getPokemon());
    }
}