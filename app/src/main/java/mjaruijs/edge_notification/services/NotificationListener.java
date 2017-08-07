package mjaruijs.edge_notification.services;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {

   // private MyReceiver notificationReceiver;
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
            Log.i(TAG, "ID: " + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
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

//    class MyReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent.getStringExtra("command").equals("clearAll")) {
//                NotificationListener.this.cancelAllNotifications();
//            }
//            else if(intent.getStringExtra("command").equals("list_item")) {
//                Intent i1 = new  Intent("mjaruijs.edge_notification.NOTIFICATION_LISTENER");
//                i1.putExtra("notification_event","=====================");
//                sendBroadcast(i1);
//                int i=1;
//                for (StatusBarNotification sbn : NotificationListener.this.getActiveNotifications()) {
//                    Intent i2 = new  Intent("mjaruijs.edge_notification.NOTIFICATION_LISTENER");
//                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
//                    sendBroadcast(i2);
//                    i++;
//                }
//                Intent i3 = new  Intent("mjaruijs.edge_notification.NOTIFICATION_LISTENER");
//                i3.putExtra("notification_event","===== Notification List ====");
//                sendBroadcast(i3);
//            }
//
//        }
//    }
}
