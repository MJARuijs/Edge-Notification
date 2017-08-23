package mjaruijs.edge_notification.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.preferences.Prefs;

public class Preferences extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // Preferences
        Prefs prefs = new Prefs(getApplicationContext());
        prefs.apply();

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
