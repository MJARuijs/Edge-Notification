package mjaruijs.edge_notification.data;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.HashMap;

public class IconMap {

    private HashMap<String, Drawable> appMap;

    public IconMap() {
        appMap = new HashMap<>();
    }

    public void add(String appName, Drawable icon) {
        Log.i(getClass().getSimpleName(), "Adding " +appName);
        appMap.put(appName, icon);
    }

//    public boolean containsKey(String appName) {
//        return appMap.containsKey(appName);
//    }

    public Drawable getValue(String appName) {
        return appMap.get(appName);
    }
}
