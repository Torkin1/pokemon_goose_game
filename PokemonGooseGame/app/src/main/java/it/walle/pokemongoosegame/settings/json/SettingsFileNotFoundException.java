package it.walle.pokemongoosegame.settings.json;

import java.io.File;
import java.io.FileNotFoundException;

public class SettingsFileNotFoundException extends Throwable {
    public SettingsFileNotFoundException(String src, FileNotFoundException e) {
        super(src + " not found", e);
    }
}
