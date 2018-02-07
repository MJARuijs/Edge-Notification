package mjaruijs.edge_notification.services;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;

import java.util.List;

public class NotificationListener extends NotificationListenerService {

    private List<PackageInfo> applications;

    @Override
    public void onCreate() {
        super.onCreate();
        PackageManager pm = getPackageManager();
        applications = pm.getInstalledPackages(PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
        startService(new Intent(getApplicationContext(), QuickSettingsService.class));
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!sbn.getPackageName().contains("android")) {
            String appName = getAppName(sbn.getPackageName());
            Intent intent = new Intent("mjaruijs.edge_notification.DRAW_EDGE");
            intent.putExtra("action", appName);
            intent.putExtra("notification_event", "posted");
            if (sbn.getNotification().tickerText != null) {
                intent.putExtra("ticker", sbn.getNotification().tickerText.toString());
            } else {
                intent.putExtra("ticker", "null");
            }
            intent.putExtra("notify_posted_time", sbn.getPostTime());
            sendBroadcast(intent);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (!sbn.getPackageName().contains("android")) {
            String appName = getAppName(sbn.getPackageName());
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
        }
        return null;
    }
}
