package mjaruijs.edge_notification.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.support.annotation.Nullable;

import mjaruijs.edge_notification.preferences.Prefs;

public class MainService extends Service implements SensorEventListener {

    private Prefs prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = new Prefs(getApplicationContext());
        prefs.apply();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
