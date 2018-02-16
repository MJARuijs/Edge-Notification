package mjaruijs.edge_notification.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import mjaruijs.edge_notification.data.AppList;
import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.data.AppItem;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppViewHolder> {

    private AppList apps;

    public AppListAdapter(AppList apps) {
        this.apps = apps;
    }

    public void add(AppItem app) {
        apps.add(app);
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        AppItem appItem = apps.get(position);
        holder.appIcon.setImageDrawable(appItem.getIcon());
        holder.appName.setText(appItem.getName());
        holder.addButton.setTag(appItem.getName());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {

        ImageView appIcon;
        TextView appName;
        Button addButton;

        AppViewHolder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_text);
            addButton = itemView.findViewById(R.id.add_button);
        }
    }
}
