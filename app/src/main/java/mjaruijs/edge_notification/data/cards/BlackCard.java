package mjaruijs.edge_notification.data.cards;

import android.graphics.drawable.Drawable;

public class BlackCard extends Card {

    private String item;

    public BlackCard(String appName, Drawable appIcon, String item) {
        super(appName, appIcon);
        this.item = item;
    }

    public String getItem() {
        return item;
    }
}
