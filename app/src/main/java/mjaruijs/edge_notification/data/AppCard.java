package mjaruijs.edge_notification.data;

import android.content.Context;
import android.graphics.drawable.Drawable;

import mjaruijs.edge_notification.color_picker.ColorPickerSwatch;

public class AppCard {

    private String appName;
    private Drawable appIcon;
    private int notificationColor;
    private ColorPickerSwatch colorPickerSwatch;

    public AppCard(Context context, String appName, Drawable appIcon, int notificationColor) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.notificationColor = notificationColor;
        colorPickerSwatch = new ColorPickerSwatch(context, notificationColor);
    }

    public String getAppName() {
        return appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public int getNotificationColor() {
        return notificationColor;
    }

    public void setNotificationColor(int color) {
        this.notificationColor = color;
    }

}
