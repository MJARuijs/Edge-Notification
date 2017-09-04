package mjaruijs.edge_notification.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

import mjaruijs.edge_notification.data.CardList;
import mjaruijs.edge_notification.data.IconMap;
import mjaruijs.edge_notification.preferences.Prefs;

public class MainService extends Service {

    private Prefs prefs;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationListener = new Intent(getApplicationContext(), NotificationListener.class);
        Intent edgeLightingService = new Intent(getApplicationContext(), EdgeLightingService.class);
        File file = Environment.getExternalStorageDirectory();
        CardList.initialize(file);
        IconMap iconMap = new IconMap();
        CardList.readFromXML(iconMap);

        prefs = new Prefs(getApplicationContext());
        prefs.apply();
        Log.i(getClass().getSimpleName(), "CREATED");
        if (prefs.enabled) {
            Log.i("STARTERSERVICE", "Prefs enabled");
            startService(notificationListener);
            startService(edgeLightingService);
        } else if (prefs.initialized) {
            stopService(notificationListener);
            stopService(edgeLightingService);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
