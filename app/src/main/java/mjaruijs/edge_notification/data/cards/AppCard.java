package mjaruijs.edge_notification.data.cards;

import android.graphics.drawable.Drawable;

public class AppCard extends Card {

    private int color;

    public AppCard(String appName, Drawable appIcon, int color) {
        super(appName, appIcon);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
