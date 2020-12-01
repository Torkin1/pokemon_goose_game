package it.walle.pokemongoosegame.dao;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.entity.Pokemon;
import it.walle.pokemongoosegame.entity.Type;
import it.walle.pokemongoosegame.volley.Volley;
import it.walle.pokemongoosegame.volley.ServerCallback;

public class DAOPokemon {

    private static DAOPokemon reference = null;
    private static final int LIMIT = 1050;
    private Context context;
    private Object results;
    private ServerCallback serverCallbackPokemon;

    public static DAOPokemon getReference(Context context){
        if(reference == null){
            reference = new DAOPokemon(context);
        }
        return reference;
    }

    private DAOPokemon(Context context){
        this.context = context;
    }

    public ServerCallback getServerCallbackPokemon(){ return this.serverCallbackPokemon; }


    public void setResults(Object results){
        this.results = results;
    }

    public Object getResults(){
        return this.results;
    }


    public void LoadAllPokemonName(Response.Listener listner){
        String url = "https://pokeapi.co/api/v2/pokemon?limit=%d";
        url = String.format(url, LIMIT);
        this.serverCallbackPokemon = new ServerCallback() {
            @Override
            public void onSuccess(Object response, Response.Listener<String> listener) {

                List<String> pokemonNamesList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    JSONArray resultsList = jsonObject.getJSONArray("results");
                    for(int i = 0; i < resultsList.length(); i++) {
                        pokemonNamesList.add(i, (String) resultsList.getJSONObject(i).get("name"));
                    }

                    DAOPokemon.this.setResults(pokemonNamesList);
                    if (DAOPokemon.this.results != null && ((List<String>)DAOPokemon.this.results).size() > 0) {
                        Log.w("CA", "" + ((List<String>)DAOPokemon.this.results).size());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Volley volleyPokemon = Volley.getReference(context);
        volleyPokemon.apiCall(url, listner);
    }

    public void LoadPokemon(Response.Listener listener, String pokemonName){
        String url = "https://pokeapi.co/api/v2/pokemon/%s";
        url = String.format(url, pokemonName);

        this.serverCallbackPokemon = new ServerCallback() {
            @Override
            public void onSuccess(Object response, Response.Listener<String> listener) {

                try {
                    JSONObject jsonObject = new JSONObject((String) response);
                    int pokemonId = jsonObject.getInt("id");

                    String pokemonName = jsonObject.getString("name");

                    JSONArray stats = jsonObject.getJSONArray("stats");
                    int pokemonHp = stats.getJSONObject(0).getInt("base_stat");

                    JSONArray typesResponseList = jsonObject.getJSONArray("types");

                    List<Type> pokemonType = new ArrayList<>();

                    Pokemon pokemon = new Pokemon(pokemonId, pokemonName, pokemonHp, pokemonType);
                    MutableLiveData<Pokemon> pokemonMutableLiveData = new MutableLiveData<>();
                    pokemonMutableLiveData.setValue(pokemon);
                    DAOPokemon.this.setResults(pokemonMutableLiveData);

                    for(int i = 0; i < typesResponseList.length(); i++) {
                        DAOType.getReference(context).
                                LoadType(typesResponseList.
                                                getJSONObject(i).
                                                getJSONObject("type").
                                                getString("name"),
                                         listener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Volley volley = Volley.getReference(context);
        volley.apiCall(url, listener);
    }

}
