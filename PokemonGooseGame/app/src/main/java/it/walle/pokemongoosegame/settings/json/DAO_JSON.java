package it.walle.pokemongoosegame.settings.json;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DAO_JSON<T> {
    // T is the class or a superclass of the object which holds the settings

    private final Gson gsonParser;
    protected final Context context;
    private static final String LOG_TAG = DAO_JSON.class.getName();

    public DAO_JSON(Context context){
        this.gsonParser = new Gson();
        this.context = context;
    }

    private String getPath(Class<? extends T> holderClass, String name){
        // Calculates path where files are stored
        return (new StringBuilder()).append(holderClass.getSimpleName()).append(File.separator).append(name).toString();
    }

    protected void storeSettings(T holder, String destName){
        // TODO: Stores settings loaded from a T object in a JSON file in internal storage
    }

    protected T loadSettings(Class<? extends T> holderClass, String srcName) throws SettingsFileNotFoundException, IOException {
        T holder;
        StringBuilder jsonTextBuilder = new StringBuilder();

        // Calculates the position of the file and opens a File object related to it
        File src = new File(context.getFilesDir(), this.getPath(holderClass, srcName));

        // Opens a JsonReader and parses src with it.
        try (JsonReader jr = new JsonReader((new FileReader(src)))) {
            // Tries to parse json object in java object defined by holderClass. Such type must be kind of T
            holder = this.gsonParser.fromJson(jr, holderClass);
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw new SettingsFileNotFoundException(srcName, e);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return holder;
    }
}
