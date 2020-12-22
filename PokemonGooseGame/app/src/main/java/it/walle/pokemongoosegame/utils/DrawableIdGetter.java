package it.walle.pokemongoosegame.utils;

import android.util.Log;

import java.lang.reflect.Field;


import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.board.cell.Cell;

public class DrawableIdGetter {

    private static DrawableIdGetter ref;
    public static DrawableIdGetter getReference(){
        if (ref == null){
            ref = new DrawableIdGetter();
        }
        return ref;
    }

    private final static String TAG = DrawableIdGetter.class.getSimpleName();

    private int getDrawableId(String name) throws DrawableNotFoundException {
        Field[] allDrawableFields = R.drawable.class.getFields();
        for (Field f : allDrawableFields){
            if (f.getName().compareTo(name) == 0){
                try {
                    return (Integer) f.get(null);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        throw new DrawableNotFoundException(name);
    }

    public Integer getTypeDrawableId(String typeName) throws DrawableNotFoundException {

        // Retrieves drawable id corresponding to the provided type
        return getDrawableId(typeName);
    }

    public int getCellBGDrawable(Class<? extends Cell> cellClass) throws DrawableNotFoundException {

        // Retrieves cell background drawable id corresponding to the provided Cell type
        String bgName = "cell_bg_" + cellClass.getSimpleName();
        return getDrawableId(bgName);
    }
}
