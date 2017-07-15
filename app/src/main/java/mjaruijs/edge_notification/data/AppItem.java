package mjaruijs.edge_notification.data;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class AppItem implements Comparable<AppItem> {

    private String name;
    private Drawable icon;

    public AppItem(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    @Override
    public int compareTo(@NonNull AppItem other) {
        return (this.name).compareTo(other.name);
    }
}
