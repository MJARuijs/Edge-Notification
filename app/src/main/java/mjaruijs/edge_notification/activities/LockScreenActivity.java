package mjaruijs.edge_notification.activities;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.List;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.RoundedImageView;
import mjaruijs.edge_notification.data.AppCard;
import mjaruijs.edge_notification.data.CardList;
import mjaruijs.edge_notification.services.AccessibilityServiceImpl;

public class LockScreenActivity extends FragmentActivity {

    private AccessibilityServiceImpl accessibilityService;
    private View mNavBarView;
    private ViewGroup mTopView;
    private StopCodeListener stopCodeListener;
    private WindowManager mWindowManager;

    private List<AppCard> cards;
    private WindowManager wm;
//    private final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, // TYPE_SYSTEM_ALERT is denied in apiLevel >=19
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                    | WindowManager.LayoutParams.FLAG_FULLSCREEN
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
//                    | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
//            PixelFormat.TRANSPARENT
//    );
//    private final WindowManager.LayoutParams params = new WindowManager.LayoutParams (
//        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//        WindowManager.LayoutParams.FLAG_FULLSCREEN
//                //| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//               // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                |WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
//
//                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
//                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
//        PixelFormat.TRANSPARENT
//    );


     private WindowManager.LayoutParams params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        Log.i(getClass().getSimpleName(), "Name: " + w.getContext().toString());
        ViewOverlay overlay = w.getDecorView().getOverlay();

        overlay.add(getDrawable(R.drawable.vector_drawable));
//        Intent i = new Intent(getApplicationContext(), AccessibilityServiceImpl.class);
//        startService(i);
        PackageManager pm = getPackageManager();
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_POWER);

        boolean hasMenuKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_STB_POWER);
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        Log.i(getClass().getSimpleName(), "MENUKEY: " + hasMenuKey);
        Log.i(getClass().getSimpleName(), "BACKKEY: " + hasBackKey);
        Log.i(getClass().getSimpleName(), "HOMEKEY: " + hasHomeKey);



        View decorView = getWindow().getDecorView();

        //decorView.setForeground(getDrawable(R.drawable.vector_drawable));
        Log.i(getClass().getSimpleName(), "Visibility: " + getWindow().toString());
//        int uiOptions =
//                //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//
////                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                ;
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR, WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR);
//        decorView.setSystemUiVisibility(uiOptions);
        Log.i(getClass().getSimpleName(), "Visibility: " + decorView.getSystemUiVisibility());

//        decorView.setOnSystemUiVisibilityChangeListener
//                (new View.OnSystemUiVisibilityChangeListener() {
//                    @Override
//                    public void onSystemUiVisibilityChange(int visibility) {
//                        if ((visibility) == 0) {
//                            // TODO: The navigation bar is visible. Make any desired
//                            Log.i(getClass().getSimpleName(), "TRUE");
//
//                            // adjustments to your UI, such as showing the action bar or
//                            // other navigational controls.
//                        } else {
//                            // TODO: The navigation bar is NOT visible. Make any desired
//                            Log.i(getClass().getSimpleName(), "FALSE");
//
//                            // adjustments to your UI, such as hiding the action bar or
//                            // other navigational controls.
//                        }
//                    }
//                });
        // Check if the navigation bar is showing or not
        if(!hasBackKey && !hasMenuKey) {
            Log.i(getClass().getSimpleName(), "TRUE!!");
            params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, // TYPE_SYSTEM_ALERT is denied in apiLevel >=19
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                    | WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR
                    //| WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN
//                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                    |WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                    | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                     ,
            PixelFormat.TRANSLUCENT
    );
//            params = new android.view.WindowManager.LayoutParams(-1, 2960,0,0,    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN
////    |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
////    |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                                      |WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//
//                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
//                            | WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR
//                            |WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
//                            |WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
//                            |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//                    , PixelFormat.TRANSPARENT);

        } else {
            Log.i(getClass().getSimpleName(), "FALSE!!");

//            params = new android.view.WindowManager.LayoutParams(-1, 2960,0,100,    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN
////    |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
////    |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
//                            | WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR
//                            |WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
//                            |WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
//                            |WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, PixelFormat.TRANSPARENT);

        }
//        List activities = pm.getA
        //Log.i(getClass().getSimpleName(), "height: " + wm.getDefaultDisplay().getSize(new Point());
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
//        int uiOptions =  View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | 0x08000000;
//        decorView.setSystemUiVisibility(uiOptions);
        //this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        //getWindow().setFlags(SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION, SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        //int navBarSize = getResources().getDimensionPixelSize(R.dimen.nav_bar_size);

        // view that will be added/removed



//        mNavBarView = LayoutInflater.from(this).inflate(R.layout.nav_bar_layout, null);
//
//        // will show current application's label
//       // tvAppName = (TextView) mNavBarView.findViewById(R.id.tv_app_name);
//
//        // shows an image from http://lorempixel.com
////        ImageView ivImage = (ImageView) mNavBarView.findViewById(R.id.iv_image);
//
//
//
//        // PORTRAIT orientation
//        WindowManager.LayoutParams lpNavView = new WindowManager.LayoutParams();
//
//        // match the screen's width
//        lpNavView.width = WindowManager.LayoutParams.MATCH_PARENT;
//
//        // height was looked up in the framework's source code
//        lpNavView.height = 200;
//
//        // start from the left edge
//        lpNavView.x = 0;
//
//        // see the comment for WindowManager.LayoutParams#y to
//        // understand why this is needed.
//        lpNavView.y = -200;
//
//        // we need this to draw over other apps
//        lpNavView.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//
//        // Lets us draw outside screen bounds
//        lpNavView.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
//
//        // Since we are using Gravity.BOTTOM to position the view,
//        // any value we specify to WindowManager.LayoutParams#y
//        // will be measured from the bottom edge of the screen.
//        // At y = 0, the view's bottom edge will sit just above
//        // the navigation bar. A positive value such as y = 50 will
//        // make our view 50 pixels above the top edge of the nav bar.
//        // That's why we choose a negative value equal to the nav bar's height.
//        lpNavView.gravity = Gravity.BOTTOM;

        // add the view
//        wm.addView(mNavBarView, lpNavView);
//        getWindow().setNavigationBarColor(R.drawable.outline);

        Intent colorIntent = getIntent();
        String appName = colorIntent.getStringExtra("color");

        cards = CardList.getCards();

        stopCodeListener = new StopCodeListener();

        IntentFilter filter = new IntentFilter("mjaruijs.edge_notification.STOP_CODE_LISTENER");
        registerReceiver(stopCodeListener, filter);



        mTopView = (ViewGroup) getLayoutInflater().inflate(R.layout.notification_screen_layout, null);
//        mTopView.setForegroundGravity(Gravity.BOTTOM);
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        View noti_view = LayoutInflater.from(this).inflate(R.layout.notification_screen_layout, null);
        //getWindow().setContentView(noti_view, params);
        //getWindow().setAttributes(params);
        //wm.updateViewLayout(mTopView, params);
        wm.addView(mTopView, params);
//        outLineTest();

//        setColor(appName);
        //ImageView right = (ImageView) findViewById(R.id.notification_flash_right);
        //right.startAnimation(anim1);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);

//                inst.sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HOME));
//                inst.sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HOME));
            }
        }).start();
    }

    private void setColor(String appName) {
//        AppCard selectedCard = getSelectedCard(appName);
//
//        Animation fadeOut = new AlphaAnimation(1, 0);
//
//        Animation translation = new TranslateAnimation(0, 0, 0, 2560);
//        translation.setDuration(1000);
//
//        fadeOut.setDuration(750);
////        fadeOut.setRepeatCount(Animation.INFINITE);
////        fadeOut.setRepeatMode(Animation.REVERSE);
//
//        //Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_out);
//        //Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//
//        if (selectedCard != null) {
//            Log.i(getClass().getSimpleName(), "cards contains " + appName + ". Color is: " + selectedCard.getNotificationColor());
//            for (int i = 0; i < 4; i++) {
//                ImageView color = (ImageView) mTopView.getChildAt(0);
//
//                Log.i("Height", "height: " + color.getHeight());
//                //color.setBackgroundColor(selectedCard.getNotificationColor());
//                //color.startAnimation(fadeOut);
//
//            }
//        } else {
//            for (int i = 0; i < 4; i++) {
//            ImageView color = (ImageView) mTopView.getChildAt(0);
//                Log.i("Height", "height: " + color.getHeight());
//
//                //color.setBackgroundColor(-1);   // Default color: White
//            //color.startAnimation(fadeOut);
//
//            }
//        }
//
//        wm.updateViewLayout(mTopView, params);
//        //outLineTest();
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

    private void outLineTest() {
        RoundedImageView color = (RoundedImageView) mTopView.getChildAt(0);
        Bitmap outline = Bitmap.createBitmap(10, 2560, Bitmap.Config.ARGB_8888);
        Bitmap holder = Bitmap.createBitmap(1432, 2552, Bitmap.Config.ARGB_8888);
        color.setImageBitmap(outline);

        //Bitmap l = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.delete_icon);
        //Drawable fromFile = Drawable.createFromPath("delete_icon.png");
//boolean k = l.isMutable();
        //Bitmap bitmap = ((BitmapDrawable)color.getDrawable()).getBitmap();
        //int r = bitmap.describeContents();
        //BitmapDrawable test = new BitmapDrawable(Resources.getSystem(), RoundedImageView.getCroppedBitmap(bitmap, 100));

        //color.setImageDrawable(fromFile);
        color.setImageBitmap(RoundedImageView.getCroppedBitmap(outline, 100));
        //color.setBackgroundColor(-1);
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

    @Nullable
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
        //getWindow().closeAllPanels();
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
