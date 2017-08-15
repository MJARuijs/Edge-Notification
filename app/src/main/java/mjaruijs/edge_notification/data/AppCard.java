package mjaruijs.edge_notification.data;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class AppCard{

    private String appName;
    private Drawable appIcon;
    private int notificationColor;
    private boolean selected;

    public AppCard(String appName, Drawable appIcon, int notificationColor) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.notificationColor = notificationColor;
        selected = false;
        Log.i(getClass().getSimpleName(), "Color int: " + notificationColor);
    }

    public String getAppName() {
        return appName;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getNotificationColor() {
        return notificationColor;
    }

    public void setNotificationColor(int color) {
        this.notificationColor = color;
    }

}
