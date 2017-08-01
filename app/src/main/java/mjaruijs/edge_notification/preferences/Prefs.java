package mjaruijs.edge_notification.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {

    public boolean initialized;
    public boolean enabled;

    private SharedPreferences prefs;

    public Prefs(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void apply() {
        enabled = prefs.getBoolean(KEYS.ENABLED.toString(), false);
        initialized = prefs.getBoolean(KEYS.INITIALIZED.toString(), false);
    }

    public void setBool(String key, boolean value) {
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