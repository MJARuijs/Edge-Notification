package mjaruijs.edge_notification.services;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!sbn.getPackageName().contains("android")) {
            Log.i(TAG, "********** onNotificationPosted");
            Log.i(TAG, "ID: " + sbn.getId() + "\t" + sbn.getPackageName() + "\n\n");
            Intent i = new Intent("mjaruijs.edge_notification.NOTIFICATION_LISTENER");
            i.putExtra("notification_event", sbn.getNotification().tickerText + "\n\n");
            i.putExtra("notification", "posted");
            sendBroadcast(i);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (!sbn.getPackageName().contains("android")) {
            Log.i(TAG,"********** onNotificationRemoved");
            Log.i(TAG, "ID: " + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
            Intent i = new Intent("mjaruijs.edge_notification.NOTIFICATION_LISTENER");
            i.putExtra("notification_event", sbn.getNotification().tickerText +"\n");
            i.putExtra("notification", "removed");

            sendBroadcast(i);
        }
    }
}
