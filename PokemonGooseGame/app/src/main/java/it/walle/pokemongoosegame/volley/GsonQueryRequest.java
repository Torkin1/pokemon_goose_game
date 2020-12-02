package it.walle.pokemongoosegame.volley;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

// Volley request of a json which will be converted to a Gson object
public class GsonQueryRequest<T> extends Request<T> {

    private final Gson gsonParser;
    private final Class<T> containerClass;
    private final Response.Listener<T> listener;

    public GsonQueryRequest(
            int method,                                         // Http method of request
            String url,                                         // Requested URL
            Response.Listener<T> listener,                      // What the requester shall do with the fetched data
            @Nullable Response.ErrorListener errorListener,     // What the caller should do in case of a failed response
            Class<T> containerClass                             // The Class of the object which will hold the data parsed from json

    ) {
        super(method, url, errorListener);
        this.containerClass = containerClass;
        this.listener = listener;
        this.gsonParser = new Gson();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        // If no response is available returns a failed response
        if (response == null){
            return Response.error(new NetworkError());
        }

        String jsonQueryResult;
        try {
            // Parses data from the json string received and stores it in a containerClass instance
            jsonQueryResult = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            // Returns the response with the parsed data
            return Response.success(
                    gsonParser.fromJson(jsonQueryResult, containerClass),
                    HttpHeaderParser.parseCacheHeaders(response)
            );
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            Log.e(this.getClass().getName(), e.toString());
            return Response.error(new NetworkError());
        }

    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}