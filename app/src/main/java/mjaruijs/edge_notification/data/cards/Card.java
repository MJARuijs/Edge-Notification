package mjaruijs.edge_notification.data.cards;

import android.graphics.drawable.Drawable;

public abstract class Card {

    private String appName;
    private Drawable appIcon;
    private boolean selected;

    Card(String appName, Drawable appIcon) {
        this.appName = appName;
        this.appIcon = appIcon;
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

    boolean isSelected() {
        return selected;
    }

}
