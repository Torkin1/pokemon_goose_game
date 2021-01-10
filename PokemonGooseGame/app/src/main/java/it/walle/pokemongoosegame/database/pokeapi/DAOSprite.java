package it.walle.pokemongoosegame.database.pokeapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import it.walle.pokemongoosegame.volley.RequestQueueHolder;

public class DAOSprite {


    private Context context;
    private RequestQueue requestQueue;//need to use a queue to mantain order on the requests

    //The class will be used almost everywhere where, we need to use sprites, just buy passing
    //context, and will return the loaded sprite that can be used

    public DAOSprite(Context context, RequestQueue requestQueue) {
        this.context = context;
        this.requestQueue = requestQueue;
    }

    public DAOSprite(Context context) {
        this(context, RequestQueueHolder.getInstance(context).getRequestQueue());
    }

    public void loadSprite(String spritePointer, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
        this.loadSprite(
                spritePointer,
                listener,
                errorListener,
                this.requestQueue
        );
    }

    private void loadSprite(String spritePointer, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener, RequestQueue requestQueue) {
        requestQueue
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
