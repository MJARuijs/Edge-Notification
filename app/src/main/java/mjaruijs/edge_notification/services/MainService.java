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

import mjaruijs.edge_notification.activities.LockScreenActivity;
import mjaruijs.edge_notification.preferences.Prefs;

public class MainService extends Service implements SensorEventListener {

    private static boolean initialized = false;
    private boolean proximityClose;
    private static final int SENSOR_SENSITIVITY = 4;
    private SensorManager sensorManager;
    private Display display;
    private String state;
    static final int FULL_WAKE_LOCK = 0x0000001a;
    private NotificationReceiver notificationReceiver;
    private Prefs prefs;
    private boolean sendStopCode;
    private boolean requestStopCode;
    private  AccessibilityServiceImpl accessibilityService;
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
        sendStopCode = false;
        requestStopCode = false;

        // Display State
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        state = stateToString(display.getState());

        // Accessibility Service
        Intent accessibilityIntent = new Intent(getApplicationContext(), AccessibilityServiceImpl.class);

        accessibilityService = new AccessibilityServiceImpl();

        // Notification Receiver
        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("mjaruijs.edge_notification.NOTIFICATION_LISTENER");


        if (prefs.enabled) {
            Log.i("STARTERSERVICE", "Prefs enabled");
            startService(notificationAlertIntent);
            startService(accessibilityIntent);
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
                Log.i("MainService", "Close");
            } else {
                proximityClose = false;
                Log.i("MainService", "Far");
                if (sendStopCode) {
                    sendStopCode = false;
                    Intent i = new Intent("mjaruijs.edge_notification.STOP_CODE_LISTENER");
                    i.putExtra("command", "stop");
                    Log.i(getClass().getSimpleName(), "Sending STOPCODE");
                    sendBroadcast(i);
                }
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

    private void unlockScreen(String appName) {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wakeLock = pm.newWakeLock(FULL_WAKE_LOCK
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");

        wakeLock.acquire();

        Log.i("MainService", "Unlocked");
        wakeLock.release();

        sendStopCode = true;
        showNotificationFlash(appName);
    }

    private void showNotificationFlash(String appName) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.OverlayDialog);
//
//        //builder.setContentView(R.layout.notification_screen_layout);
//        builder.setTitle("DIA");
////        dialog.show();
//        Dialog dia = builder.create();
//        dia.setContentView(R.layout.notification_screen_layout);
//        dia.show();
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setSmallIcon(android.R.drawable.btn_star);
//        builder.setContentTitle("This is title of notification");
//        builder.setContentText("This is a notification Text");
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//
//        Intent intent = new Intent(MainService.this, LockScreenActivity.class);
//        RemoteViews v = new RemoteViews(getPackageName(), R.layout.notification_screen_layout);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 113,intent, PendingIntent.FLAG_UPDATE_CURRENT);
////        builder.setContent(v);
//        builder.setContentIntent(pendingIntent);
//        builder.setAutoCancel(true);
//
//        builder.setFullScreenIntent(pendingIntent, true);
//
//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.notify((int)System.currentTimeMillis(), builder.build());

//        Log.i(getClass().getSimpleName(), "DISPLAY NAME: " + wm.getDefaultDisplay().;
        Intent i = new Intent(MainService.this, LockScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("color", appName);
        startActivity(i);
//        WindowManager  wm = (WindowManager) getApplicationContext()
//                .getSystemService(Context.WINDOW_SERVICE);
//
////
//        View view = LayoutInflater.from(this).inflate(R.layout.notification_screen_layout, null);
////        View parent = LayoutInflater.inflate(R.layout.activity_main, null);
//        View parentView = accessibilityService.getWindows().get(0);
//
//        PopupWindow pw = new PopupWindow(view, 1440, 2960, false);
//        pw.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.OverlayDialog);
//
//        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
//        View dialogView = inflater.inflate(R.layout.notification_screen_layout, null);
//
//        builder.setView(dialogView);
//
//        AlertDialog alert = builder.create();
//        alert.setView(dialogView);
////        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
////        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//
//        alert.show();
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        Window window = alert.getWindow();
//        lp.copyFrom(window.getAttributes());
//        lp.width = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//
//        window.setAttributes(lp);
    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String appName = intent.getStringExtra("notification_event");
            if (intent.getStringExtra("notification").equals("posted")) {
                state = stateToString(display.getState());
                if (state.equals("OFF") && prefs.enabled && proximityClose) {
                    Log.i(getClass().getSimpleName(), "Display OFF, and close");
                    unlockScreen(appName);
                }
                if (state.equals("ON") && prefs.enabled) {
                    Log.i(getClass().getSimpleName(), "Display ON, and close");
                    // TODO: Determine what needs to happen when the screen is already turned on.
                    requestStopCode = true;
                    showNotificationFlash(appName);
                }
            }
            if (intent.getStringExtra("notification").equals("removed") && requestStopCode) {
                requestStopCode = false;
                Intent i = new Intent("mjaruijs.edge_notification.STOP_CODE_LISTENER");
                i.putExtra("command", "stop");
                Log.i(getClass().getSimpleName(), "Sending STOPCODE");
                sendBroadcast(i);
            }
        }
    }
}
