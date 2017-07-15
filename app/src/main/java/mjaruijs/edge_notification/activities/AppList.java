package mjaruijs.edge_notification.activities;

import android.app.Activity;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.data.AppItem;

class AppList implements ListAdapter {

    private Activity context;
    private List<AppItem> apps;

    AppList(Activity context) {

        this.context = context;
        apps = new ArrayList<>();
    }

    void add(AppItem app) {
        apps.add(app);
    }

    void sort() {
        Collections.sort(apps);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    public AppItem getAppItem(int position) { return apps.get(position); }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, false);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.app_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.app_image);
        txtTitle.setText(apps.get(position).getName());
        imageView.setImageDrawable(apps.get(position).getIcon());

        return rowView;
    }

    public View getView(int position) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, false);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.app_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.app_image);
        txtTitle.setText(apps.get(position).getName());
        imageView.setImageDrawable(apps.get(position).getIcon());
        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("AppList", "TESTTTT");

            }
        });
        return rowView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
