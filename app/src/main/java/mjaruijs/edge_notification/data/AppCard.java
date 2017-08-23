package mjaruijs.edge_notification.data;

import android.graphics.drawable.Drawable;

public class AppCard {

    private String appName;
    private Drawable appIcon;
    private int mainColor;
    private int secondColor;
    private boolean selected;

    public AppCard(String appName, Drawable appIcon, int mainColor, int secondColor) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.mainColor = mainColor;
        this.secondColor = secondColor;
        selected = false;
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

    public int getMainColor() {
        return mainColor;
    }

    public void setMainColor(int color) {
        this.mainColor = color;
    }

    public int getSecondColor() { return secondColor; }

    public void setSecondColor(int color) { this.secondColor = color; }
}
