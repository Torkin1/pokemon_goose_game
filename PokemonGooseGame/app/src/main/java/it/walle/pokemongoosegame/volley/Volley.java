package it.walle.pokemongoosegame.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class Volley {

        private static Volley reference = null;
        private Context context;

        public static Volley getReference(Context context){
            if(reference == null){
                reference = new Volley(context);
            }
            return reference;
        }

        private Volley(Context context){
            this.context = context;
        }

        public void apiCall(String url, Response.Listener listner) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    listner
                , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error
                }
            });
            RequestQueueHolder.getInstance(context).getRequestQueue().add(stringRequest);
        }
    }
