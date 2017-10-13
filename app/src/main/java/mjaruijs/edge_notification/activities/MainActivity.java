package mjaruijs.edge_notification.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.adapters.CardAdapter;
import mjaruijs.edge_notification.color_picker.ColorPickerPalette;
import mjaruijs.edge_notification.color_picker.ColorPickerSwatch;
import mjaruijs.edge_notification.data.AppItem;
import mjaruijs.edge_notification.data.Data;
import mjaruijs.edge_notification.data.IconMap;
import mjaruijs.edge_notification.data.PInfo;
import mjaruijs.edge_notification.data.cards.AppCard;
import mjaruijs.edge_notification.data.cards.AppCardList;
import mjaruijs.edge_notification.fragments.SettingsFragment;
import mjaruijs.edge_notification.preferences.Prefs;
import mjaruijs.edge_notification.adapters.AppList;
import mjaruijs.edge_notification.services.MainService;

public class MainActivity extends AppCompatActivity  {

    private AlertDialog colorAlertDialog;
    private CardAdapter appCardAdapter;
    private IconMap     iconMap;
    private AppCardList cards;
    private String      TAG = getClass().getSimpleName();
    private Dialog      dia;
    public  String[]    strings;
    public  Drawable[]  icons;
    private int[]       colors;
    private AppCard selectedCard;

    private RecyclerView appCardView;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconMap = new IconMap();

        // Preferences
        prefs = new Prefs(getApplicationContext());
        prefs.apply();

        if(!prefs.initialized) {
            startActivity(new Intent(getApplicationContext(), IntroActivityFS.class));
            finish();
        } else {

            // Toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Switch
            getFragmentManager().beginTransaction()
                    .replace(R.id.switch_holder, new SettingsFragment())
                    .commitAllowingStateLoss();

            if (prefs.enabled){
                startService(new Intent(this, MainService.class));
            }

            // Recycle View
            appCardView = (RecyclerView) findViewById(R.id.recycle_view);

            final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

            appCardView.setLayoutManager(llm);

            // Get a list of installed apps.
            ArrayList<PInfo> applicationPackages = new ArrayList<>();
            try {
                applicationPackages = getInstalledApps(false);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            File file = Environment.getExternalStorageDirectory();
            Data.initialize(file, iconMap, this);

            cards = Data.getCards();

            appCardAdapter = new CardAdapter(cards);

            AppList apps = new AppList(MainActivity.this);
            appCardView.setAdapter(appCardAdapter);

            colors = new int[20];
            initColors();
            strings = new String[applicationPackages.size()];
            icons = new Drawable[applicationPackages.size()];

            for (int i = 0; i < applicationPackages.size(); i++) {
                strings[i] = applicationPackages.get(i).appName;
                icons[i] = applicationPackages.get(i).icon;
                apps.add(new AppItem(applicationPackages.get(i).appName, applicationPackages.get(i).icon));
            }

            //TODO: Allow the user to select multiple apps from the appList at once, adding them all to the appCardList.
            apps.sort();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Alert_Dialog_Dark);
            builder.setTitle("Pick an app");
            builder.setAdapter(apps, new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            dia = builder.create();
            dia.setCancelable(true);

            // Color Picker
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            ColorPickerPalette colorPickerPalette = (ColorPickerPalette) layoutInflater.inflate(R.layout.custom_picker, null);
            colorPickerPalette.init(colors.length, 5, new ColorPickerSwatch.OnColorSelectedListener() {
                @Override
                public void onColorSelected(int color) {
                    cards.getByName(selectedCard.getAppName()).setColor(color);
                    int[][] states = new int[][]{new int[0]};
                    int[] colors = {color};
                    ColorStateList colorList = new ColorStateList(states, colors);
                    appCardView.findViewWithTag(selectedCard.getAppName()).setBackgroundTintList(colorList);
                    colorAlertDialog.dismiss();
                }
            });

            colorPickerPalette.drawPalette(colors, colors[19]);
            colorAlertDialog = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle(R.string.color_picker_default_title)
                    .setView(colorPickerPalette)
                    .create();


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dia.show();
                }
            });

        }
    }

    public void onClick(View v){
        TextView textView = (TextView) v.findViewById(R.id.app_text);
        ImageView icon = (ImageView) v.findViewById(R.id.app_icon);
        if (!cards.contains(textView.getText().toString())) {

            String appName = textView.getText().toString();
            cards.addCard(new AppCard(appName, icon.getDrawable(), Color.WHITE, this));
            appCardAdapter.notifyDataSetChanged();
            dia.hide();
        } else {
            final Dialog warningDialog;
            warningDialog = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Duplication!")
                    .setMessage("This app is already in your list!")
                    .setPositiveButton("Ok", new Dialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            warningDialog.show();
        }
    }

    public void onClickDeleteCard(final View v) {
        final String tag = v.getTag().toString().replace("_Del_Btn", "");
        final String nameTag = tag + "_Name";
        final String iconTag = tag + "_Icon";
        final String deleteBtnTag = tag + "_Del_Btn";
        final String deleteBackGrdTag = tag + "_Del_Backgrd";

        if (cards.multipleSelected()) {
            final List<AppCard> selectedCards = cards.getSelectedCards();
            final AlertDialog warningDialog;
            warningDialog = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Multiple Items Selected!")
                    .setMessage("Do you want to delete all selected items?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (AppCard card : selectedCards) {
                                String nameTag = card.getAppName() + "_Name";
                                String iconTag = card.getAppName() + "_Icon";
                                String deleteBtnTag = card.getAppName() + "_Del_Btn";
                                String deleteBackGrdTag = card.getAppName() + "_Del_Backgrd";
                                if (card.isSelected()) {
                                    appCardView.findViewWithTag(nameTag).setVisibility(View.VISIBLE);
                                    appCardView.findViewWithTag(iconTag).setVisibility(View.VISIBLE);
                                    appCardView.findViewWithTag(card.getAppName()).setVisibility(View.VISIBLE);
                                    appCardView.findViewWithTag(deleteBtnTag).setVisibility(View.INVISIBLE);
                                    appCardView.findViewWithTag(deleteBackGrdTag).setVisibility(View.INVISIBLE);
                                    cards.deleteCard(card.getAppName());
                                }
                            }
                            appCardAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("Delete Only One", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i(TAG, "Deleting " + tag);
                            for (AppCard card : selectedCards) {
                                String nameTag = card.getAppName() + "_Name";
                                String iconTag = card.getAppName() + "_Icon";
                                String deleteBtnTag = card.getAppName() + "_Del_Btn";
                                String deleteBackGrdTag = card.getAppName() + "_Del_Backgrd";
                                appCardView.findViewWithTag(nameTag).setVisibility(View.VISIBLE);
                                appCardView.findViewWithTag(iconTag).setVisibility(View.VISIBLE);
                                appCardView.findViewWithTag(card.getAppName()).setVisibility(View.VISIBLE);
                                appCardView.findViewWithTag(deleteBtnTag).setVisibility(View.INVISIBLE);
                                appCardView.findViewWithTag(deleteBackGrdTag).setVisibility(View.INVISIBLE);
                            }
                            cards.deleteCard(v.getTag().toString());
                            appCardAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            warningDialog.show();
        } else {
            appCardView.findViewWithTag(nameTag).setVisibility(View.VISIBLE);
            appCardView.findViewWithTag(iconTag).setVisibility(View.VISIBLE);
            appCardView.findViewWithTag(tag).setVisibility(View.VISIBLE);
            appCardView.findViewWithTag(deleteBtnTag).setVisibility(View.INVISIBLE);
            appCardView.findViewWithTag(deleteBackGrdTag).setVisibility(View.INVISIBLE);
            cards.deleteCard(v.getTag().toString());
        }
        appCardAdapter.notifyDataSetChanged();
    }

    public void onClickColorButton(View v) {
        selectedCard = cards.getByName(v.getTag().toString());
        colorAlertDialog.show();
    }

    // TODO: Add a checkbox that allows the user to add system apps too.
    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) throws PackageManager.NameNotFoundException {
        ArrayList<PInfo> res = new ArrayList<>();

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES;

        PackageManager pm = getPackageManager();

        List<PackageInfo> applications = pm.getInstalledPackages(flags);

        if (getSysPackages) {
            for (PackageInfo appInfo : applications) {
                    PInfo newInfo = new PInfo();
                    newInfo.appName = appInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                    newInfo.icon = appInfo.applicationInfo.loadIcon(getPackageManager());
                    res.add(newInfo);
                    iconMap.add(newInfo.appName, newInfo.icon);
            }
        } else {
            for (PackageInfo appInfo : applications) {
                if (((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1)
                        || (appInfo.applicationInfo.loadLabel(getPackageManager()).toString().equals("Gmail"))
                        || (appInfo.applicationInfo.loadLabel(getPackageManager()).toString().equals("YouTube"))
                        || (appInfo.packageName.contains("facebook")) && !appInfo.applicationInfo.loadLabel(getPackageManager()).toString().contains("App")
                        || (appInfo.applicationInfo.loadLabel(getPackageManager()).toString().contains("Messenger")))
                {
                    PInfo newInfo = new PInfo();
                    newInfo.appName = appInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                    newInfo.icon = appInfo.applicationInfo.loadIcon(getPackageManager());
                    res.add(newInfo);
                    iconMap.add(newInfo.appName, newInfo.icon);
                }
            }
        } return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void initColors() {
        colors[0]  = Color.argb(255, 246,  64,  44);    // Red
        colors[1]  = Color.argb(255, 235,  20,  96);    // Pink
        colors[2]  = Color.argb(255, 156,  26, 177);    // purple
        colors[3]  = Color.argb(255, 102,  51, 185);    // Deep purple
        colors[4]  = Color.argb(255,  61,  77, 183);    // Indigo
        colors[5]  = Color.argb(255,  16, 147, 245);    // Blue
        colors[6]  = Color.argb(255,   0, 166, 246);    // Light blue
        colors[7]  = Color.argb(255,   0, 187, 213);    // Cyan
        colors[8]  = Color.argb(255,   0, 150, 135);    // Teal
        colors[9]  = Color.argb(255,  70, 175,  74);    // Green
        colors[10] = Color.argb(255, 136, 196,  64);    // Light green
        colors[11] = Color.argb(255, 205, 221,  30);    // Lime
        colors[12] = Color.argb(255, 255, 236,  22);    // Yellow
        colors[13] = Color.argb(255, 255, 192,   0);    // Amber
        colors[14] = Color.argb(255, 255, 152,   0);    // Orange
        colors[15] = Color.argb(255, 255,  85,   5);    // Deep orange
        colors[16] = Color.argb(255, 122,  85,  71);    // Brown
        colors[17] = Color.argb(255, 157, 157, 157);    // Grey
        colors[18] = Color.argb(255,  94, 124, 139);    // Blue Grey
        colors[19] = Color.argb(255, 255, 255, 255);    // White
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_preferences:
                Intent i = new Intent(this, Preferences.class);
                startActivity(i);
            case R.id.menu_permission:
                Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                permissionIntent.setData(uri);
                startActivity(permissionIntent);
                return true;
            case R.id.delete_all:
                showWarningDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showWarningDialog() {
        final AlertDialog warningDialog;
        warningDialog = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setTitle("Warning!")
                .setMessage("Are you sure you want to delete all Cards?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cards.clear();
                        appCardAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .create();
        warningDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (prefs.initialized) {
            dia.dismiss();
            Data.writeToFile();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (prefs.initialized) {
            dia.dismiss();
            Data.writeToFile();
        }
        super.onSaveInstanceState(outState);
    }
}
