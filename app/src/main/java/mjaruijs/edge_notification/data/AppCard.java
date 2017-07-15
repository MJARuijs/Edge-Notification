package mjaruijs.edge_notification.data;

public class AppCard {

    private String appName;
    private int appIcon;
    private int notificationColor;

    public AppCard(String appName, int appIcon, int notificationColor) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.notificationColor = notificationColor;
    }

    public String getAppName() {
        return appName;
    }

    public int getAppIcon() {
        return appIcon;
    }

    public int getNotificationColor() {
        return notificationColor;
    }
}
