package mjaruijs.edge_notification.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static mjaruijs.edge_notification.preferences.Preferences.KEYS.INITIALIZED;

public class Preferences {

    public boolean initialized;
    public boolean enabled;

    private SharedPreferences prefs;

    public Preferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void apply() {
        enabled = prefs.getBoolean(KEYS.ENABLED.toString(), false);
        initialized = prefs.getBoolean(INITIALIZED.toString(), false);
    }

    public void initialize() {
        setBoolean(INITIALIZED.id, true);
    }

    public void setBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public enum KEYS {
        ENABLED("enabled"),
        INITIALIZED("initialized");

        private final String id;

        KEYS(final String text) { this.id = text; }

        @Override
        public String toString() { return id; }
    }
}