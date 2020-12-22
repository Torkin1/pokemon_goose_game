package it.walle.pokemongoosegame.utils;

import android.util.Log;

import java.lang.reflect.Field;


import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.pokeapi.type.Type;

public class TypeDrawableGetter {

    private static TypeDrawableGetter ref;
    public static TypeDrawableGetter getReference(){
        if (ref == null){
            ref = new TypeDrawableGetter();
        }
        return ref;
    }

    private final static String TAG = TypeDrawableGetter.class.getSimpleName();

    public Integer getTypeDrawableId(String typeName) throws TypeDrawableNotFoundException {

        // Retrieves drawable id corresponding to the provided type
        Field[] allDrawableFields = R.drawable.class.getFields();
        for (Field f : allDrawableFields){
            if (f.getName().compareTo(typeName) == 0){
                try {
                    return (Integer) f.get(null);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        throw new TypeDrawableNotFoundException(typeName);
    }
}
