package mjaruijs.edge_notification.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.preferences.Preferences;
import mjaruijs.edge_notification.services.EdgeLightingService;
import mjaruijs.edge_notification.services.NotificationListener;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private Intent notificationListener;
    private Intent edgeLightingService;
    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.enable_switch);
        preferences = new Preferences(getActivity().getApplicationContext());
        preferences.apply();
        notificationListener = new Intent(getContext(), NotificationListener.class);
        edgeLightingService = new Intent(getContext(), EdgeLightingService.class);
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals("enabled")) {
            return;
        }

        if (getContext() != null) {
            preferences.setBoolean(key, !preferences.enabled);
            preferences.apply();

            if (preferences.enabled) {
                getContext().startService(notificationListener);
                getContext().startService(edgeLightingService);
            } else {
                getContext().stopService(notificationListener);
                getContext().stopService(edgeLightingService);
            }
        }

        ((SwitchPreference)findPreference(key)).setChecked(preferences.enabled);
    }

}
