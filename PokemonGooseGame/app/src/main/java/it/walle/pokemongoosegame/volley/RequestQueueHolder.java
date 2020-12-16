package it.walle.pokemongoosegame.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueHolder {
    private static RequestQueueHolder ref = null;
    public static RequestQueueHolder getInstance(Context context){
        if (ref == null){
            ref = new RequestQueueHolder(context);
        }
        return ref;
    }
    private RequestQueue requestQueue;      // Queue of volley requests awaiting to be sent
    private RequestQueueHolder(Context context){
        this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
