package mjaruijs.edge_notification.services;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public class NotificationListener extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private List<PackageInfo> applications;

    @Override
    public void onCreate() {
        super.onCreate();
        PackageManager pm = getPackageManager();
        applications = pm.getInstalledPackages(PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
        Log.i(getClass().getSimpleName(), "CONNECTED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!sbn.getPackageName().contains("android")) {
            String appName = getAppName(sbn.getPackageName());
            Log.i(TAG, "********** onNotificationPosted");
            Log.i(TAG, "ID: " + sbn.getId() + "\t" + appName + "\t\n" + sbn.getNotification().tickerText);
//            Intent i = new Intent("mjaruijs.edge_notification.NOTIFICATION_LISTENER");
//            i.putExtra("notification_event", appName + "\n\n");
            Intent intent = new Intent("mjaruijs.edge_notification.DRAW_EDGE");
            intent.putExtra("action", appName);
            intent.putExtra("notification_event", "posted");
            if (sbn.getNotification().tickerText != null) {
                Log.i(TAG, sbn.getNotification().tickerText.toString());
                intent.putExtra("ticker", sbn.getNotification().tickerText.toString());
            } else {
                intent.putExtra("ticker", "null");
            }
            intent.putExtra("notify_posted_time", sbn.getPostTime());
//            if (!sbn.getNotification().tickerText.toString().contains("WhatApp")) {
//                Log.i(getClass().getSimpleName(), "TICKER: " + sbn.getNotification().tickerText);
                sendBroadcast(intent);
//            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (!sbn.getPackageName().contains("android")) {
            String appName = getAppName(sbn.getPackageName());
            Log.i(TAG,"********** onNotificationRemoved");
            Log.i(TAG, "ID: " + sbn.getId() + "\t" + appName);
            Intent intent = new Intent("mjaruijs.edge_notification.DRAW_EDGE");
            intent.putExtra("notification_event", appName +"\n");
            intent.putExtra("notification_event", "removed");
            sendBroadcast(intent);
        }
    }

    @Nullable
    private String getAppName(String packageName) {
        for (PackageInfo pInfo : applications) {
            if (pInfo.applicationInfo.packageName.equals(packageName)) {
                return pInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            }
        } return null;
    }
}
