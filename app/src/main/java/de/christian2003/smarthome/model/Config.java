package de.christian2003.smarthome.model;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.christian2003.smarthome.App;


/**
 * Class implements a config for the app. This config manages the shared preferences for easier
 * access within code.
 * Get the singleton instance of this class through {@link #getInstance()}.
 */
public class Config {

    /**
     * Field stores the name for the shared preferences instance.
     */
    private static final String PREFERENCES = "smart_home";

    /**
     * Field stores the key with which to save the server URL.
     */
    private static final String KEY_SERVER_URL = "server_url";

    /**
     * Attribute stores the singleton instance of this class.
     */
    @Nullable
    private static Config instance;


    /**
     * Attribute stores the shared preferences instance.
     */
    @NonNull
    private final SharedPreferences preferences;

    /**
     * Attribute stores the editor for the shared preferences.
     */
    @NonNull
    private final SharedPreferences.Editor editor;


    /**
     * Constructor instantiates a new instance of this class.
     */
    private Config() {
        preferences = App.getContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    /**
     * Method returns the server URL.
     *
     * @param defValue  Default value to return if no value is saved beforehand.
     * @return          Server URL.
     */
    public String getServerUrl(@Nullable String defValue) {
        return preferences.getString(KEY_SERVER_URL, defValue);
    }

    /**
     * Method changes the server URL.
     *
     * @param url   New server URL.
     */
    public void setServerUrl(@Nullable String url) {
        editor.putString(KEY_SERVER_URL, url);
        editor.apply();
    }


    /**
     * Method returns the singleton instance for the config.
     *
     * @return  Singleton instance of the config.
     */
    @NonNull
    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

}
