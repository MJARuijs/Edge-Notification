package mjaruijs.edge_notification.services;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.List;

import mjaruijs.edge_notification.EdgeLightView;
import mjaruijs.edge_notification.data.AppCard;
import mjaruijs.edge_notification.data.CardList;
import mjaruijs.edge_notification.preferences.Prefs;
import mjaruijs.edge_notification.values.Variables;

public class EdgeLightingService extends Service implements SensorEventListener {

    static final int FULL_WAKE_LOCK = 0x0000001a;
    private EdgeLightView edgeView;
    private EdgeReceiver edgeReceiver = new EdgeReceiver(this);
    private WindowManager wm;
    private Variables vars;
    private List<AppCard> cards;
    private Display display;
    private Prefs prefs;
    private SensorManager sensorManager;
    private Intent notificationListener;
    private static final int SENSOR_SENSITIVITY = 4;
    private boolean initialized = false;
    private boolean proximityClose;
    private boolean gyroscopeFlat;
    private boolean awaitingStopSign;
    private float[] mGravity;
    private double mAccel;
    private double mAccelCurrent;
    private double mAccelLast;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private WindowManager.LayoutParams layoutParams(int width, int height) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.flags = 16779008;
        layoutParams.format = -3;
        layoutParams.gravity = 8388659;
        return layoutParams;
    }

    public void removeViewFromWM() {
        try {
            if (edgeView != null) {
                this.wm.removeViewImmediate(edgeView);
                edgeView.close();
                edgeView = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addViewToWM(String name) {
        if (this.edgeView == null || this.edgeView.getVisibility() != View.VISIBLE) {
            try {
                if (edgeView != null) {
                    this.wm.removeViewImmediate(edgeView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean screenOff = false;
            if (display.getState() == 1 && gyroscopeFlat) {
                screenOff = true;
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

                PowerManager.WakeLock wakeLock = pm.newWakeLock(FULL_WAKE_LOCK
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");

                wakeLock.acquire();
                Log.i(getClass().getSimpleName(), "WAKELOCK");
                wakeLock.release();
            }

            int height = 0;
            int width = 0;
            this.edgeView = vars.initView(screenOff);

            int mainColor = setMainColor(name);
            int secondColor = setSecondColor(name);
            Log.i(getClass().getSimpleName(), "COLORS: " + mainColor + " : " + secondColor);
            this.vars.m15255a(mainColor, secondColor);
            this.vars.m15252a(15f);
            this.vars.m15256b(100);
            Display defaultDisplay = this.wm.getDefaultDisplay();
            DisplayMetrics displayMetrics;
            ImageView view = new ImageView(getApplicationContext());
            if (hasNavBar(getApplicationContext())) {
                displayMetrics = new DisplayMetrics();
                defaultDisplay.getRealMetrics(displayMetrics);
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
                view.setMinimumWidth(width);
                view.setMinimumHeight(height);
            }
            view.setBackgroundColor(Color.BLACK);
            Log.i(getClass().getSimpleName(), "ADDING VIEW TO VM");
            if (proximityClose) {
                awaitingStopSign = true;
                edgeView.setBackgroundColor(Color.BLACK);
            }

            this.wm.addView(this.edgeView, layoutParams(width, height));
        }
    }

    private int setMainColor(String name) {
        AppCard selectedCard = getSelectedCard(name);
        if (selectedCard != null) {
            return selectedCard.getMainColor();
        } else {
            return Color.BLACK;
        }
    }

    private int setSecondColor(String name) {
        AppCard selectedCard = getSelectedCard(name);
        if (selectedCard != null) {
            return selectedCard.getSecondColor();
        } else {
            return Color.BLACK;
        }
    }

    @Nullable
    private AppCard getSelectedCard(String appName) {
        for (AppCard card : cards) {
            if (card.getAppName().trim().toLowerCase().equals(appName.toLowerCase().trim())) {
                return card;
            }
        } return null;
    }

    private void addReceiverActions() {
        try {
            if (edgeView != null) {
                unregisterReceiver(edgeReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("mjaruijs.edge_notification.DRAW_EDGE");
        registerReceiver(edgeReceiver, filter);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = new Prefs(getApplicationContext());
        prefs.apply();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        cards = CardList.getCards();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        notificationListener = new Intent(this, NotificationListener.class);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        if (prefs.enabled) {
            startService(notificationListener);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
        }
        initialized = true;
        Log.i(getClass().getSimpleName(), "SERVICE CREATED");
    }

    @Override
    public void onDestroy() {
        if (initialized && prefs.enabled) {
            unregisterReceiver(edgeReceiver);
            sensorManager.unregisterListener(this);
            stopService(notificationListener);
        }
        try {
            if (this.edgeView != null) {
                this.edgeView.close();
                this.wm.removeViewImmediate(edgeView);
            }
            this.wm = null;
            super.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialized = false;
    }

    private boolean hasNavBar(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        int i2 = displayMetrics.widthPixels;
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics2);
        return i2 - displayMetrics2.widthPixels > 0 || i - displayMetrics2.heightPixels > 0;
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        if (prefs.enabled) {
            this.wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            this.vars = new Variables(this);
            addReceiverActions();
            super.onStartCommand(intent, i, i2);
        }
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                proximityClose = true;
                Log.i(getClass().getSimpleName(), "Close");
            } else {
                proximityClose = false;
                Log.i(getClass().getSimpleName(), "Far");
                if (awaitingStopSign) {
                    awaitingStopSign = false;
                    removeViewFromWM();
                }
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = Math.sqrt(x*x + y*y + z*z);
            double delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            gyroscopeFlat = mAccel < 0.1;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Log.i(getClass().getSimpleName(), "NEW VALUE: " + accuracy);
        }
    }
    class EdgeReceiver extends BroadcastReceiver {

        final EdgeLightingService edgeService;

        EdgeReceiver(EdgeLightingService edgeService) { this.edgeService = edgeService; }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("mjaruijs.edge_notification.DRAW_EDGE".equals(action)) {
                String name = intent.getStringExtra("action");

                if (intent.getStringExtra("notification_event").equals("posted")) {
                        String ticker = intent.getStringExtra("ticker");
                        action = intent.getStringExtra("action");
                    Log.i(getClass().getSimpleName(), "ticker " + ticker);

                    long parseLong = Long.parseLong(intent.getExtras().get("notify_posted_time").toString());

                        if (!(action.equals("") && parseLong == 0) && !ticker.equals("WhatsApp Web")) {
                            Log.i(getClass().getSimpleName(), "ADDING TO VIEW ");
                            this.edgeService.addViewToWM(name);
                        }

                }
                if (intent.getStringExtra("notification_event").equals("removed")) {
                    this.edgeService.removeViewFromWM();
                }
            }
        }
    }
}


