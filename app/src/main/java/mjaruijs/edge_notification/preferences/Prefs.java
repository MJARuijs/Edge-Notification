package mjaruijs.edge_notification.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import mjaruijs.edge_notification.services.AppList;

public class Prefs {

    public boolean enabled;
    public AppList apps;
    private SharedPreferences prefs;

    public Prefs(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void apply() {
        enabled = prefs.getBoolean(KEYS.ENABLED.toString(), true);

    }

    public enum KEYS {
        ENABLED("enabled");

        private final String id;

        KEYS(final String text) { this.id = text; }

        @Override
        public String toString() { return id; }
    }
}