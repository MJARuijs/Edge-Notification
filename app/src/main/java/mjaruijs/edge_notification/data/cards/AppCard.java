package mjaruijs.edge_notification.data.cards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.widget.EditText;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.adapters.BlacklistAdapter;

public class AppCard extends Card {

    private int color;
    private Blacklist blacklist;
    private Dialog blacklistDialog;
    private BlacklistAdapter blacklistAdapter;

    public AppCard(String appName, Drawable appIcon, int color) {
        super(appName, appIcon);
        this.color = color;
        blacklist = new Blacklist();
    }

    AppCard(String appName, Drawable appIcon, int color, Blacklist blacklist) {
        super(appName, appIcon);
        this.color = color;
        this.blacklist = blacklist;
    }

    public void removeFromBlackList(String appName) {
        blacklist.deleteCard(appName);
        blacklistAdapter.notifyDataSetChanged();
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

    private void initDialog(final Context context) {
        blacklistAdapter = new BlacklistAdapter(blacklist);

        RecyclerView blacklistView = new RecyclerView(context);

        final LinearLayoutManager llm = new LinearLayoutManager(context);
        blacklistView.setLayoutManager(llm);

        blacklistView.setAdapter(blacklistAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Alert_Dialog_Dark);
        builder.setTitle("Pick an app");
        builder.setView(blacklistView)
                .setPositiveButton("+", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showInputDialog(context);
                blacklistAdapter.notifyDataSetChanged();
            }

        });

        blacklistDialog = builder.create();

    }

    public void showBlacklist(Context context) {
        initDialog(context);
        if (blacklistDialog != null) {
            blacklistDialog.show();
        }
    }

    private void showInputDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Name");

        // Set up the input
        final EditText input = new EditText(context);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                blacklist.addCard(new BlackCard(input.getText().toString()));
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
