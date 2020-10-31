package it.walle.pokemongoosegame.settings.json;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SettingsDAO_JSON<T> {
    // T is the class of the object which holds the settings

    private final Gson gsonParser;
    protected final Context context;
    private static final String LOG_TAG = SettingsDAO_JSON.class.getName();

    public SettingsDAO_JSON(Context context){
        this.gsonParser = new Gson();
        this.context = context;
    }

    protected void storeSettings(T holder, String dest){
        // TODO: Stores settings loaded from a T object in a JSON file in internal storage
    }

    protected T loadSettings(String srcName) throws SettingsFileNotFoundException, IOException {
        T settingsHolder = null;
        File src = new File(context.getFilesDir(), srcName);
        StringBuilder jsonTextBuilder = new StringBuilder();

        // Opens a JsonReader and parses src with it.
        try (JsonReader jr = new JsonReader((new FileReader(src)))) {
            // Wraps T in a TypeToken object in order to pass to gsonParser.fromJson the actual T type
            settingsHolder = this.gsonParser.fromJson(jr, new TypeToken<T>() {}.getType());
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw new SettingsFileNotFoundException(srcName, e);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
        return settingsHolder;
    }
}
