package mjaruijs.edge_notification.services;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Environment;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import java.io.File;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.data.Data;
import mjaruijs.edge_notification.data.IconMap;
import mjaruijs.edge_notification.preferences.Preferences;

public class QuickSettingsService extends TileService {

    private Preferences preferences;
    private Intent notificationListener;
    private Intent edgeLightingService;

    @Override
    public void onCreate() {
        super.onCreate();
        IconMap iconMap = new IconMap();
        File file = Environment.getExternalStorageDirectory();
        Data.initialize(file, iconMap);
        preferences = new Preferences(getApplicationContext());
        preferences.apply();
        notificationListener = new Intent(getApplicationContext(), NotificationListener.class);
        edgeLightingService = new Intent(getApplicationContext(), EdgeLightingService.class);
        if (preferences.enabled) {
            startService(notificationListener);
            startService(edgeLightingService);
        } else {
            stopService(notificationListener);
            stopService(edgeLightingService);
        }
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        setTileState();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        preferences.apply();
        setTileState();
    }

    @Override
    public void onClick() {
        super.onClick();
        toggle();
        setTileState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(notificationListener);
        stopService(edgeLightingService);
    }

    private void setTileState() {
        Tile tile = getQsTile();

        if (preferences.enabled) {
            tile.setLabel("Lighting enabled");
            tile.setState(Tile.STATE_ACTIVE);
            tile.setIcon(Icon.createWithResource(this, R.drawable.lighting_on));
        } else {
            tile.setLabel("Lighting disabled");
            tile.setState(Tile.STATE_INACTIVE);
            tile.setIcon(Icon.createWithResource(this, R.drawable.lighting_off));
        }

        tile.updateTile();
    }

    private void toggle() {
        preferences.setBoolean("enabled", !preferences.enabled);
        preferences.apply();

        if (preferences.enabled) {
            startService(notificationListener);
            startService(edgeLightingService);
        } else {
            stopService(notificationListener);
            stopService(edgeLightingService);
        }
    }

}
