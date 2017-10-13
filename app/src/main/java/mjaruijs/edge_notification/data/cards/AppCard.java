package mjaruijs.edge_notification.data.cards;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.adapters.BlacklistAdapter;

public class AppCard extends Card {

    private int color;
    private Blacklist blacklist;
    private Dialog blacklistDialog;
    private BlacklistAdapter blacklistAdapter;

    public AppCard(String appName, Drawable appIcon, int color, Context context) {
        super(appName, appIcon);
        this.color = color;
        blacklist = new Blacklist();
        blacklistAdapter = new BlacklistAdapter(blacklist);
        initDialog(context);
    }

    public AppCard(String appName, Drawable appIcon, int color, Blacklist blacklist, Context context) {
        super(appName, appIcon);
        this.color = color;
        this.blacklist = blacklist;
        blacklistAdapter = new BlacklistAdapter(blacklist);
        initDialog(context);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Blacklist getBlacklist() {
        return blacklist;
    }

    private void initDialog(Context context) {
        RecyclerView blacklistView = new RecyclerView(context);
        blacklistView.setAdapter(blacklistAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Alert_Dialog_Dark);
        builder.setTitle("Pick an app");
        builder.setView(blacklistView)

//        builder.setAdapter(blacklistAdapter, new AlertDialog.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//
//        })
                .setPositiveButton("+", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        blacklistDialog = builder.create();

    }

    public void showBlacklist() {
        if (blacklistDialog != null) {
            blacklistDialog.show();
        }
    }

}
