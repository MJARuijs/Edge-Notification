package mjaruijs.edge_notification.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import mjaruijs.edge_notification.R;

public class LockScreenActivity extends AppCompatActivity {

    private StopCodeListener stopCodeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setType(
                WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);

        stopCodeListener = new StopCodeListener();
        IntentFilter filter = new IntentFilter("mjaruijs.edge_notification.STOP_CODE_LISTENER");
        registerReceiver(stopCodeListener, filter);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, // TYPE_SYSTEM_ALERT is denied in apiLevel >=19
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT
        );

        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        ViewGroup mTopView = (ViewGroup) getLayoutInflater().inflate(R.layout.notification_screen_layout, null);
        getWindow().setAttributes(params);
        wm.addView(mTopView, params);

        stopCodeListener = new StopCodeListener();

        init();
    }

    private void init() {
       // mLockscreenUtils = new LockScreenUtils();
    }

    // Handle events of calls and unlock screen if necessary
    private class StateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    //unlockHomeButton();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stopCodeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().closeAllPanels();
    }

    class StopCodeListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("command").equals("stop")) {
                Log.i(getClass().getSimpleName(), "received stop command");
                getWindow().closeAllPanels();
            }
        }
    }
}
