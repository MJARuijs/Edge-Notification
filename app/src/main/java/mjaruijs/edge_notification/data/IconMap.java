package mjaruijs.edge_notification.data;

import android.graphics.drawable.Drawable;

import java.util.HashMap;

public class IconMap {

    private HashMap<String, Drawable> appMap;

    public IconMap() {
        appMap = new HashMap<>();
    }

    public void add(String appName, Drawable icon) {
        appMap.put(appName, icon);
    }

//    public boolean containsKey(String appName) {
//        return appMap.containsKey(appName);
//    }

    Drawable getValue(String appName) {
        return appMap.get(appName);
    }
}
