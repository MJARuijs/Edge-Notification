package mjaruijs.edge_notification.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class SensorService extends Service implements SensorEventListener {

    private static final int SENSOR_SENSITIVITY = 4;
    private final String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //proximityClose = true;
                Log.i(TAG, "Close");
                //near
                //Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "Far");
               // proximityClose = false;
                //far
                //Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
