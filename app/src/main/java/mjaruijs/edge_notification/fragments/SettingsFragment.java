package mjaruijs.edge_notification.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.preferences.Prefs;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Prefs prefs = new Prefs(getActivity().getApplicationContext());
        prefs.apply();

    }
}
