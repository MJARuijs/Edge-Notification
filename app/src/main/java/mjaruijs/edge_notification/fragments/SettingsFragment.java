package mjaruijs.edge_notification.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.preferences.Prefs;
import mjaruijs.edge_notification.services.EdgeLightingService;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener{

    private Prefs prefs;
    private Intent starterService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.enable_switch);
        prefs = new Prefs(getActivity().getApplicationContext());
        prefs.apply();
        findPreference("enabled").setOnPreferenceChangeListener(this);
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).registerOnSharedPreferenceChangeListener(this);

        starterService = new Intent(getActivity().getApplicationContext(), EdgeLightingService.class);
    }

    private void restartService() {
        getActivity().stopService(starterService);
        getActivity().startService(starterService);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {


        if (preference.getKey().equals("enabled")) {
            prefs.setBool("enabled", (boolean) newValue);
            Log.i(getClass().getSimpleName(), "NEW VALUE: " + newValue);
            restartService();
        }
        prefs.apply();
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
