package mjaruijs.edge_notification.services;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.preferences.Prefs;

public class MTileService extends TileService {

//    private boolean state = false;
    private final int STATE_OFF = 1;
    private final int STATE_ON = 2;
    private final String LOG_TAG = "MyTileService";

    private int toggleState = STATE_OFF;
    @Override
    public void onTileAdded() {
        Log.d(LOG_TAG, "onTileAdded");
    }

    @Override
    public void onTileRemoved() {
        Log.d(LOG_TAG, "onTileRemoved");
    }

    @Override
    public void onClick() {
        Icon icon;
        Prefs prefs = new Prefs(getApplicationContext());
        if (toggleState == STATE_ON) {
            toggleState = STATE_OFF;
            prefs.setBool("enabled", false);
            getQsTile().setLabel("Turn On");
            Log.i(LOG_TAG, "CLICKED" + getQsTile().getState());
        } else {
            toggleState = STATE_ON;
            prefs.setBool("enabled", true);
            getQsTile().setLabel("Turn Off");
            Log.i(LOG_TAG, "CLICKED" + getQsTile().getState());
        }
        prefs.apply();
        Tile tile = getQsTile();
        tile.setState(toggleState);
        icon = Icon.createWithResource(getApplicationContext(), R.drawable.circle_button);
        tile.setIcon(icon);
        tile.updateTile();
        restartService();
    }
    private void restartService() {
        stopService(new Intent(this, MainService.class));
        startService(new Intent(this, MainService.class));
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_TAG, "CREATED");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "DESTROYED");
    }

    @Override
    public void onStartListening () {
        Log.d(LOG_TAG, "onStartListening");
    }

    @Override
    public void onStopListening () {
        Log.d(LOG_TAG, "onStopListening");
    }
}
