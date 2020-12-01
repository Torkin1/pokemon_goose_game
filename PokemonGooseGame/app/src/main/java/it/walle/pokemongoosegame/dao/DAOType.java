package it.walle.pokemongoosegame.dao;

import android.content.Context;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import it.walle.pokemongoosegame.entity.Type;
import it.walle.pokemongoosegame.volley.ServerCallback;
import it.walle.pokemongoosegame.volley.Volley;

public class DAOType {
    private static DAOType reference = null;
    private Context context;
    private Object results;
    private ServerCallback serverCallback;

    public static DAOType getReference(Context context){
        if(reference == null){
            reference = new DAOType(context);
        }
        return reference;
    }

    private DAOType(Context context){this.context = context;}

    public ServerCallback getServerCallback(){
        return this.serverCallback;
    }

    public void setResults(Object results){
        this.results = results;
    }

    public Object getResults(){
        return this.results;
    }


    public void LoadType(String type, Response.Listener<String> listener) {
        String url = "https://pokeapi.co/api/v2/type/%s";
        url = String.format(url, type);

         this.serverCallback = new ServerCallback() {
             @Override
             public void onSuccess(Object response, Response.Listener<String> listener) {
                 Type type = new Type();
                 Method[] methods = type.getClass().getDeclaredMethods();
                 try{
                     JSONObject jsonObject = new JSONObject((String) response);
                     type.setName(jsonObject.getString("name"));
                     JSONObject damageRelations = jsonObject.getJSONObject("damage_relations");

                     List<List<String>> damage = new ArrayList<>();

                     for(int i = 0; i < damageRelations.length(); i++) {
                         String key = "";
                         switch (i){
                             case 0:
                                 key = "double_damage_from";
                                 break;
                             case 1:
                                 key = "double_damage_to";
                                 break;
                             case 2:
                                 key = "half_damage_from";
                                 break;
                             case 3:
                                 key = "half_damage_to";
                                 break;
                             case 4:
                                 key = "no_damage_from";
                                 break;
                             case 5:
                                 key = "no_damage_to";
                                 break;
                         }
                         damage.add(i, new ArrayList<>());
                         for (int j = 0; j < damageRelations.getJSONArray(key).length(); j++) {
                             damage.get(i).
                                     add(j, damageRelations.
                                     getJSONArray(key).
                                     getJSONObject(j).
                                     getString("name"));
                         }
                         for(int j = 0; j < methods.length; j++){
                             if(methods[j].getName().toLowerCase().equals("set"+key)) {
                                 methods[j].invoke(type, damage.get(i));
                             }
                         }
                     }
                 }
                 catch (JSONException | IllegalAccessException | InvocationTargetException e){
                     e.printStackTrace();
                 }
                 DAOType.this.setResults(type);
             }
         };

        Volley volley = Volley.getReference(context);
        volley.apiCall(url, listener);
    }
}
