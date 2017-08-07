package mjaruijs.edge_notification.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.List;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.data.AppCard;
import mjaruijs.edge_notification.data.CardList;

public class LockScreenActivity extends AppCompatActivity {

    private ViewGroup mTopView;
    private StopCodeListener stopCodeListener;

    private List<AppCard> cards;
    private WindowManager wm;
    private final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, // TYPE_SYSTEM_ALERT is denied in apiLevel >=19
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.OPAQUE
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setType(
                WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);

        Intent colorIntent = getIntent();
        String appName = colorIntent.getStringExtra("color");

        cards = CardList.getCards();

        stopCodeListener = new StopCodeListener();

        IntentFilter filter = new IntentFilter("mjaruijs.edge_notification.STOP_CODE_LISTENER");
        registerReceiver(stopCodeListener, filter);

        wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        mTopView = (ViewGroup) getLayoutInflater().inflate(R.layout.notification_screen_layout, null);

        getWindow().setAttributes(params);
        wm.addView(mTopView, params);
        setColor(appName);
    }

    private void setColor(String appName) {
        AppCard selectedCard = getSelectedCard(appName);

        if (selectedCard != null) {
            Log.i(getClass().getSimpleName(), "cards contains " + appName + ". Color is: " + selectedCard.getNotificationColor());
            for (int i = 0; i < 4; i++) {
                ImageView color = (ImageView) mTopView.getChildAt(i);
                color.setBackgroundColor(selectedCard.getNotificationColor());
            }
        } else {
            for (int i = 0; i < 4; i++) {
                ImageView color = (ImageView) mTopView.getChildAt(i);
                color.setBackgroundColor(-1);   // Default color: White
            }
        }
        wm.updateViewLayout(mTopView, params);
    }


    // Handle events of calls and unlock screen if necessary
//    private class StateListener extends PhoneStateListener {
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//
//            super.onCallStateChanged(state, incomingNumber);
//            switch (state) {
//                case TelephonyManager.CALL_STATE_RINGING:
//                    //unlockHomeButton();
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    break;
//                case TelephonyManager.CALL_STATE_IDLE:
//                    break;
//            }
//        }
//    }

    private AppCard getSelectedCard(String appName) {
        for (AppCard card : cards) {
            if (card.getAppName().trim().toLowerCase().equals(appName.toLowerCase().trim())) {
                return card;
            }
        } return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(getClass().getSimpleName(), "DESTROYED");
        unregisterReceiver(stopCodeListener);
        getWindowManager().removeViewImmediate(mTopView);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    class StopCodeListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("command").equals("stop")) {
                Log.i(getClass().getSimpleName(), "received stop command");
                finish();
            }
        }
    }



}
