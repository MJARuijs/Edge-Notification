package mjaruijs.edge_notification.data;

import android.graphics.drawable.Drawable;

/**
 * Created by MJA Ruijs on 18-Jul-17.
 */

public class Icon {
    private int id;
    private Drawable icon;

    public Icon (int id, Drawable icon) {
        this.id = id;
        this.icon = icon;
    }

    public Icon (Drawable icon) {
        this.icon = icon;
    }

    public Icon() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public Drawable getIcon() {
        return icon;
    }


}
