package it.walle.pokemongoosegame.database.pokeapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import it.walle.pokemongoosegame.volley.RequestQueueHolder;

public class DAOSprite {

    private static DAOSprite ref;
    public static DAOSprite getReference(Context context){
        if (ref == null){
            ref = new DAOSprite(context);
        }
        return ref;
    }

    private Context context;

    public DAOSprite(Context context) {
        this.context = context;
    }

    public void loadSprite(String spritePointer, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener){
        RequestQueueHolder
                .getInstance(context)
                .getRequestQueue()
                .add(
                        new ImageRequest(
                                spritePointer,
                                listener,
                                0,
                                0,
                                ImageView.ScaleType.FIT_CENTER,
                                Bitmap.Config.ARGB_8888,
                                errorListener
                        )
                );
    }
}
