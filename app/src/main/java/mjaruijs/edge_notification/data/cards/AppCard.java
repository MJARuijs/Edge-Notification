package mjaruijs.edge_notification.data.cards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.widget.EditText;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.adapters.BlacklistAdapter;
import mjaruijs.edge_notification.adapters.SublistAdapter;

public class AppCard extends Card {

    private int color;

    private Sublist sublist;
    private Dialog sublistDialog;
    private SublistAdapter sublistAdapter;
    private RecyclerView sublistView;

    private Blacklist blacklist;
    private Dialog blacklistDialog;
    private BlacklistAdapter blacklistAdapter;

    public AppCard(String appName, Drawable appIcon, int color) {
        super(appName, appIcon);
        this.color = color;
        blacklist = new Blacklist();
        sublist = new Sublist();
    }

    AppCard(String appName, Drawable appIcon, int color, Blacklist blacklist, Sublist sublist) {
        super(appName, appIcon);
        this.color = color;
        this.blacklist = blacklist;
        this.sublist = sublist;
    }

    public void removeFromSublist(String appName) {
        sublist.deleteCard(appName);
        sublistAdapter.notifyDataSetChanged();
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

    public void setSubColor(String subCardName, int color) {
        int[][] states = new int[][]{new int[0]};
        int[] colors = {color};
        ColorStateList colorList = new ColorStateList(states, colors);
        sublistView.findViewWithTag(subCardName).setBackgroundTintList(colorList);
    }

    public Sublist getSublist() {
        return sublist;
    }

    public Blacklist getBlacklist() {
        return blacklist;
    }

    private void initSubDialog(final Context context) {
        sublistAdapter = new SublistAdapter(sublist);

        sublistView = new RecyclerView(context);

        final LinearLayoutManager llm = new LinearLayoutManager(context);
        sublistView.setLayoutManager(llm);

        sublistView.setAdapter(sublistAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Alert_Dialog_Dark);
        builder.setTitle("SubCards");
        builder.setView(sublistView)
                .setPositiveButton("+", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showInputDialog(context, "sublist");
                        sublistAdapter.notifyDataSetChanged();
                    }

                });

        sublistDialog = builder.create();
    }

    private void initBlackDialog(final Context context) {
        blacklistAdapter = new BlacklistAdapter(blacklist);

        RecyclerView blacklistView = new RecyclerView(context);

        final LinearLayoutManager llm = new LinearLayoutManager(context);
        blacklistView.setLayoutManager(llm);

        blacklistView.setAdapter(blacklistAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Alert_Dialog_Dark);
        builder.setTitle("Blacklist");
        builder.setView(blacklistView)
                .setPositiveButton("+", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showInputDialog(context, "blacklist");
                blacklistAdapter.notifyDataSetChanged();
            }

        });

        blacklistDialog = builder.create();
    }

    public void showSubCards(Context context) {
        initSubDialog(context);
        if (sublistDialog != null) {
            sublistDialog.show();
        }
    }

    public void showBlacklist(Context context) {
        initBlackDialog(context);
        if (blacklistDialog != null) {
            blacklistDialog.show();
        }
    }

    private void showInputDialog(Context context, String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Name");

        // Set up the input
        final EditText input = new EditText(context);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        if (type.equals("sublist")) {
            // Set up the buttons
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sublist.addCard(new SubCard(input.getText().toString(), -1));
                }
            });
        } else if (type.equals("blacklist")) {
            // Set up the buttons
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    blacklist.addCard(new BlackCard(input.getText().toString()));
                }
            });
        }


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
