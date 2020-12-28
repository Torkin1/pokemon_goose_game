package it.walle.pokemongoosegame.utils;

import android.util.Log;

import java.lang.reflect.Field;


import it.walle.pokemongoosegame.R;
import it.walle.pokemongoosegame.entity.board.cell.Cell;

public class DrawableGetter {

    private static DrawableGetter ref;
    public static DrawableGetter getReference(){
        if (ref == null){
            ref = new DrawableGetter();
        }
        return ref;
    }

    private final static String TAG = DrawableGetter.class.getSimpleName();

    private int getDrawableId(String drawableName) throws DrawableNotFoundException {
        // Retrieves drawable id corresponding to the provided name
        Field[] allDrawableFields = R.drawable.class.getFields();
        for (Field f : allDrawableFields){
            if (f.getName().compareTo(drawableName) == 0){
                try {
                    return (Integer) f.get(null);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        throw new DrawableNotFoundException(drawableName);
    }

    public Integer getTypeDrawableId(String typeName) throws DrawableNotFoundException {

        return getDrawableId(typeName);
    }

    public int getCellBgDrawableId(Class<? extends Cell> cellClass) throws DrawableNotFoundException {
        final String DRAWABLE_BG_BASE_NAME = "cell_bg_";
        String drawableBgName = DRAWABLE_BG_BASE_NAME + cellClass.getSimpleName().toLowerCase();
        return getDrawableId(drawableBgName);
    }

    public int getDiceDrawableId(int face) throws DrawableNotFoundException {
        final String DRAWABLE_DICE_BASE_NAME = "dice";
        String diceDrawableName = DRAWABLE_DICE_BASE_NAME + face;
        return getDrawableId(diceDrawableName);
    }
}
