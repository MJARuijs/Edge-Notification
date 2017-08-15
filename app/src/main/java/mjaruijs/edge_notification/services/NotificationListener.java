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
            Log.i(TAG, "ID: " + sbn.getId() + "\t" + appName + "\n\n");
            Intent i = new Intent("mjaruijs.edge_notification.NOTIFICATION_LISTENER");
            i.putExtra("notification_event", appName + "\n\n");
            i.putExtra("notification", "posted");
            sendBroadcast(i);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (!sbn.getPackageName().contains("android")) {
            String appName = getAppName(sbn.getPackageName());
            Log.i(TAG,"********** onNotificationRemoved");
            Log.i(TAG, "ID: " + sbn.getId() + "\t" + appName);
            Intent i = new Intent("mjaruijs.edge_notification.NOTIFICATION_LISTENER");
            i.putExtra("notification_event", appName +"\n");
            i.putExtra("notification", "removed");
            sendBroadcast(i);
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
