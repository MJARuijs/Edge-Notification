package mjaruijs.edge_notification.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class SensorService extends Service implements SensorEventListener {

    private static final int SENSOR_SENSITIVITY = 4;
    private final String TAG = getClass().getSimpleName();
    private StopCodeListener stopCodeListener;
    private SensorManager sensorManager;

    @Override
    public void onCreate() {
        Log.i(getClass().getSimpleName(), "CREATED");
        stopCodeListener = new StopCodeListener();
        IntentFilter filter = new IntentFilter("mjaruijs.edge_notification.STOP_CODE_LISTENER_SERVICE");
        registerReceiver(stopCodeListener, filter);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);

    }

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
                Intent i = new Intent("mjaruijs.edge_notification.STOP_CODE_LISTENER");
                i.putExtra("SensorStatus", "CLOSE");
                sendBroadcast(i);
                //near
                //Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "Far");
                Intent i = new Intent("mjaruijs.edge_notification.STOP_CODE_LISTENER");
                i.putExtra("SensorStatus", "FAR");
                sendBroadcast(i);
                // proximityClose = false;
                //far
                //Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(getClass().getSimpleName(), "SensorService Destroyed");
        unregisterReceiver(stopCodeListener);
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class StopCodeListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("command").equals("stop")) {
                Log.i(TAG, "STOP CODE RECEIVED");
            }
        }
    }
}
