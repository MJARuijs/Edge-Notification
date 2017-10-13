package mjaruijs.edge_notification.data;

import android.graphics.drawable.Drawable;

import mjaruijs.edge_notification.data.cards.Card;

public class SubCard extends Card {

    private String username;
    private int color;

    public SubCard(String name, Drawable icon, String username, int color) {
        super(name, icon);
        this.username = username;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
