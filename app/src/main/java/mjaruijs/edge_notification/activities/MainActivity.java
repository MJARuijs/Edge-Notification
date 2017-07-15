package mjaruijs.edge_notification.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import java.util.ArrayList;
import java.util.List;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.data.AppCard;
import mjaruijs.edge_notification.data.AppItem;
import mjaruijs.edge_notification.data.CardList;
import mjaruijs.edge_notification.data.PInfo;
import mjaruijs.edge_notification.fragments.SettingsFragment;
import mjaruijs.edge_notification.preferences.Prefs;
import mjaruijs.edge_notification.services.RVAdapter;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;

    private AlertDialog.Builder builder;

    private NotificationReceiver notificationReceiver;
    private Display display;
    private String state;

    private boolean proximityClose = false;
    String TAG = getClass().getSimpleName();
    public static final int FULL_WAKE_LOCK = 0x0000001a;

    public String[] strings;

    public Drawable[] icons;
    //public static final int SCREEN_BRIGHT_WAKE_LOCK = 0x0000000a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "CREATED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Preferences
        Prefs prefs = new Prefs(getApplicationContext());
        prefs.apply();
        getFragmentManager().beginTransaction()
                .replace(R.id.preferences_holder, new SettingsFragment())
                .commitAllowingStateLoss();

        // Recycle View
        RecyclerView appCardView = (RecyclerView) findViewById(R.id.recycle_view);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        appCardView.setLayoutManager(llm);

        CardList cards = new CardList();
        cards.addCard(new AppCard("WhatsApp", R.mipmap.ic_launcher, Color.GREEN));
        cards.addCard(new AppCard("FaceBook", R.mipmap.ic_launcher, Color.BLUE));

        RVAdapter appCardAdapter = new RVAdapter(cards);
        appCardView.setAdapter(appCardAdapter);

        // Get a list of installed apps.
        ArrayList<PInfo> applicationPackages = getInstalledApps(false);

        final AppList apps = new AppList(MainActivity.this);

        strings = new String[applicationPackages.size()];
        icons = new Drawable[applicationPackages.size()];

        for(int i = 0; i < applicationPackages.size(); i++) {
            strings[i] = applicationPackages.get(i).appname;
            icons[i] = applicationPackages.get(i).icon;
            apps.add(new AppItem(applicationPackages.get(i).appname, applicationPackages.get(i).icon));
        }
        apps.sort();
        builder = new AlertDialog.Builder(MainActivity.this, R.style.Alert_Dialog_Dark);
        builder.setTitle("Pick an app");
        builder.setAdapter(apps, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.i(TAG, "TEST");
//                AppItem app = (AppItem) apps.getItem(which);
//                String strName = app.getName();
//                Drawable icon = app.getIcon();
//
//                AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
//
//                builderInner.setMessage(strName);
//                builderInner.setIcon(icon);
//                builderInner.setTitle("Your Selected Item is");
//                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog,int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builderInner.show();
            }
        });

        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                builder.show();
            }
        });

        // Proximity Sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // Display State
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        state = stateToString(display.getState());

        // Notification Receiver
        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("mjaruijs.edge_notification.NOTIFICATION_LISTENER");
        registerReceiver(notificationReceiver, filter);
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<>();

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES;

        PackageManager pm = getPackageManager();
        List<PackageInfo> applications = pm.getInstalledPackages(flags);

        for (PackageInfo appInfo : applications) {
            if ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
                Log.i(TAG, "APP: " + appInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                PInfo newInfo = new PInfo();
                newInfo.appname = appInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                newInfo.icon = appInfo.applicationInfo.loadIcon(getPackageManager());
                res.add(newInfo);
            }
        } return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_permission:
                Intent i = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
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

    private void unlockScreen() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wakeLock = pm.newWakeLock(FULL_WAKE_LOCK
                | LayoutParams.FLAG_TURN_SCREEN_ON
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
               // | PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");

        wakeLock.acquire();
        Log.i(TAG, "Unlocked");
        wakeLock.release();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                proximityClose = true;
                Log.i(TAG, "Close");
                //near
                //Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "Far");
                proximityClose = false;
                //far
                //Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            state = stateToString(display.getState());
            Log.i(TAG, "Display state: " + state);

            if (state.equals("OFF")  && proximityClose) {
                unlockScreen();
            }

        }
    }
}
