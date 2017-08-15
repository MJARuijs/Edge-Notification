package mjaruijs.edge_notification.services;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityServiceImpl extends AccessibilityService {



    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(getClass().getSimpleName(), "Fullscreen: " + event.isFullScreen());
    }

    @Override
    public void onInterrupt() {

    }

    class RequestReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("request").equals("window")) {

            }
        }
    }

}
