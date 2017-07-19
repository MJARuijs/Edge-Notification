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
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import mjaruijs.edge_notification.preferences.Prefs;

public class StarterService extends Service implements SensorEventListener {

    private static boolean initialized = false;
    private boolean proximityClose;
    private static final int SENSOR_SENSITIVITY = 4;
    private SensorManager sensorManager;
    private Display display;
    private String state;
    static final int FULL_WAKE_LOCK = 0x0000001a;
    private NotificationReceiver notificationReceiver;
    private Prefs prefs;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationAlertIntent = new Intent(getApplicationContext(), NotificationListener.class);

        prefs = new Prefs(getApplicationContext());
        prefs.apply();

        // Display State
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        state = stateToString(display.getState());

        // Notification Receiver
        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("mjaruijs.edge_notification.NOTIFICATION_LISTENER");

        if (prefs.enabled) {
            startService(notificationAlertIntent);
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
            registerReceiver(notificationReceiver, filter);
            initialized = true;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                proximityClose = true;
                Log.i("StarterService", "Close");
                //near
                //Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
            } else {
                proximityClose = false;
                Log.i("StarterService", "Far");
                //far
                //Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public String stateToString(int state) {
        switch (state) {
            case 0:
                return "UNKNOWN";
            case 1:
                return "OFF";
            case 2:
                return "ON";
            case 3:
                return "DOZE";
            case 4:
                return "DOZE_SUSPENDED";
            default:
                return Integer.toString(state);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(initialized) {
            sensorManager.unregisterListener(this);
            unregisterReceiver(notificationReceiver);
            initialized = !initialized;
        }
    }
    private void unlockScreen() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wakeLock = pm.newWakeLock(FULL_WAKE_LOCK
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                // | PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");

        wakeLock.acquire();
        Log.i("StarterService", "Unlocked");
        wakeLock.release();
    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            state = stateToString(display.getState());
            Log.i("StarterService", "Display state: " + state);

            if (state.equals("OFF") && prefs.enabled && proximityClose) {
                unlockScreen();
            }

        }
    }
}
