package mjaruijs.edge_notification.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppList {

    private List<AppItem> apps;

    public AppList() {
        apps = new ArrayList<>();
    }

    public void add(AppItem app) {
        apps.add(app);
    }

    public AppItem get(int position) {
        return apps.get(position);
    }

    public int size() {
        return apps.size();
    }

    public void sort() {
        apps.sort(new Comparator<AppItem>() {
            @Override
            public int compare(AppItem o1, AppItem o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
    }
}
