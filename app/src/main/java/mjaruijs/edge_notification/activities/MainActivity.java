package mjaruijs.edge_notification.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mjaruijs.edge_notification.R;
import mjaruijs.edge_notification.adapters.AppList;
import mjaruijs.edge_notification.adapters.CardAdapter;
import mjaruijs.edge_notification.color_picker.ColorPickerPalette;
import mjaruijs.edge_notification.color_picker.ColorPickerSwatch;
import mjaruijs.edge_notification.data.AppItem;
import mjaruijs.edge_notification.data.Data;
import mjaruijs.edge_notification.data.IconMap;
import mjaruijs.edge_notification.data.PInfo;
import mjaruijs.edge_notification.data.cards.AppCard;
import mjaruijs.edge_notification.data.cards.AppCardList;
import mjaruijs.edge_notification.data.cards.SubCard;
import mjaruijs.edge_notification.fragments.SettingsFragment;
import mjaruijs.edge_notification.preferences.Preferences;

import static mjaruijs.edge_notification.color_picker.Color.AMBER;
import static mjaruijs.edge_notification.color_picker.Color.BLUE;
import static mjaruijs.edge_notification.color_picker.Color.BLUE_GREY;
import static mjaruijs.edge_notification.color_picker.Color.BROWN;
import static mjaruijs.edge_notification.color_picker.Color.CYAN;
import static mjaruijs.edge_notification.color_picker.Color.DEEP_ORANGE;
import static mjaruijs.edge_notification.color_picker.Color.DEEP_PURPLE;
import static mjaruijs.edge_notification.color_picker.Color.GREEN;
import static mjaruijs.edge_notification.color_picker.Color.GREY;
import static mjaruijs.edge_notification.color_picker.Color.INDIGO;
import static mjaruijs.edge_notification.color_picker.Color.LIGHT_BLUE;
import static mjaruijs.edge_notification.color_picker.Color.LIGHT_GREEN;
import static mjaruijs.edge_notification.color_picker.Color.LIME;
import static mjaruijs.edge_notification.color_picker.Color.ORANGE;
import static mjaruijs.edge_notification.color_picker.Color.PINK;
import static mjaruijs.edge_notification.color_picker.Color.PURPLE;
import static mjaruijs.edge_notification.color_picker.Color.RED;
import static mjaruijs.edge_notification.color_picker.Color.TEAL;
import static mjaruijs.edge_notification.color_picker.Color.WHITE;
import static mjaruijs.edge_notification.color_picker.Color.YELLOW;

public class MainActivity extends AppCompatActivity {

    private ColorPicker colorPicker;
    private CardAdapter appCardAdapter;
    private IconMap iconMap;
    private AppCardList cards;
    private Dialog dia;
    private AlertDialog deleteAllCardsDialog;
    private AlertDialog deleteMultipleCardsDialog;
    private AlertDialog duplicationDialog;

    private AppCard selectedCard;

    private RecyclerView appCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconMap = new IconMap();
        colorPicker = new ColorPicker();

        // PreferenceActivity
        Preferences preferences = new Preferences(getApplicationContext());

        if (!preferences.initialized) {
            preferences.initialize();
        }

        preferences.apply();

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Switch
        getFragmentManager().beginTransaction()
                .replace(R.id.switch_holder, new SettingsFragment())
                .commitAllowingStateLoss();

        // Recycle View
        appCardView = findViewById(R.id.recycle_view);

        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

        appCardView.setLayoutManager(llm);

        // Get a list of installed apps.
        ArrayList<PInfo> applicationPackages = new ArrayList<>();
        try {
            applicationPackages = getInstalledApps();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        File file = Environment.getExternalStorageDirectory();
        Data.initialize(file, iconMap);

        cards = Data.getCards(file, iconMap);

        appCardAdapter = new CardAdapter(cards);

        final AppList apps = new AppList(MainActivity.this);
        appCardView.setAdapter(appCardAdapter);

        for (int i = 0; i < applicationPackages.size(); i++) {
            apps.add(new AppItem(applicationPackages.get(i).appName, applicationPackages.get(i).icon));
        }

        apps.sort();
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Alert_Dialog_Dark);
        builder.setTitle("Pick an app");
        builder.setAdapter(apps, new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dia.dismiss();
            }
        });

        dia = builder.create();
        dia.setCancelable(true);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dia.show();
            }
        });
    }

    public void onClickAddButton(View v) {
        Button button = v.findViewById(R.id.add_button);

        if (!cards.contains(button.getTag().toString())) {
            String appName = button.getTag().toString();
            Drawable icon = iconMap.getValue(appName);

            cards.addCard(new AppCard(appName, icon, Color.WHITE));
            appCardAdapter.notifyDataSetChanged();
        } else {
            duplicationDialog = new AlertDialog.Builder(this, R.style.Alert_Dialog_Dark)
                    .setTitle("Duplication!")
                    .setMessage("This app is already in your list!")
                    .setPositiveButton("Ok", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            duplicationDialog.show();
        }
    }

    public void onClickDeleteSubCard(View view) {
        selectedCard.removeFromSublist(view.getTag().toString());
    }

    public void onClickDeleteBlackCard(View view) {
        selectedCard.removeFromBlackList(view.getTag().toString());
    }

    public void onClickDeleteCard(final View v) {
        final String tag = v.getTag().toString().replace("_Del_Btn", "");
        final String nameTag = tag + "_Name";
        final String iconTag = tag + "_Icon";
        final String deleteBtnTag = tag + "_Del_Btn";
        final String deleteBackGrdTag = tag + "_Del_Backgrd";

        if (cards.multipleSelected()) {
            final List<AppCard> selectedCards = cards.getSelectedCards();
            deleteMultipleCardsDialog = new AlertDialog.Builder(this, R.style.Alert_Dialog_Dark)
                    .setTitle("Multiple Items Selected!")
                    .setMessage("Do you want to delete all selected items?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (AppCard card : selectedCards) {
                                card.setSelected(false);
                                String tag = card.getAppName();
                                String nameTag = card.getAppName() + "_Name";
                                String iconTag = card.getAppName() + "_Icon";
                                String deleteBtnTag = card.getAppName() + "_Del_Btn";
                                String deleteBackGrdTag = card.getAppName() + "_Del_Backgrd";
                                appCardView.findViewWithTag(tag).setVisibility(View.VISIBLE);
                                appCardView.findViewWithTag(nameTag).setVisibility(View.VISIBLE);
                                appCardView.findViewWithTag(iconTag).setVisibility(View.VISIBLE);
                                appCardView.findViewWithTag(deleteBtnTag).setVisibility(View.INVISIBLE);
                                appCardView.findViewWithTag(deleteBackGrdTag).setVisibility(View.INVISIBLE);
                                cards.deleteCard(card.getAppName());
                            }
                            cards.deselectCards();

                            appCardAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (AppCard card : selectedCards) {
                                card.setSelected(false);
                                String tag = card.getAppName();
                                String nameTag = card.getAppName() + "_Name";
                                String iconTag = card.getAppName() + "_Icon";
                                String deleteBtnTag = card.getAppName() + "_Del_Btn";
                                String deleteBackGrdTag = card.getAppName() + "_Del_Backgrd";
                                appCardView.findViewWithTag(tag).setVisibility(View.VISIBLE);
                                appCardView.findViewWithTag(nameTag).setVisibility(View.VISIBLE);
                                appCardView.findViewWithTag(iconTag).setVisibility(View.VISIBLE);
                                appCardView.findViewWithTag(deleteBtnTag).setVisibility(View.INVISIBLE);
                                appCardView.findViewWithTag(deleteBackGrdTag).setVisibility(View.INVISIBLE);
                            }
                            cards.deselectCards();
                            appCardAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    })
                    .create();
            deleteMultipleCardsDialog.show();
        } else {
            appCardView.findViewWithTag(tag).setVisibility(View.VISIBLE);
            appCardView.findViewWithTag(nameTag).setVisibility(View.VISIBLE);
            appCardView.findViewWithTag(iconTag).setVisibility(View.VISIBLE);
            appCardView.findViewWithTag(deleteBtnTag).setVisibility(View.INVISIBLE);
            appCardView.findViewWithTag(deleteBackGrdTag).setVisibility(View.INVISIBLE);
            cards.deleteCard(v.getTag().toString());
            cards.deselectCards();
            appCardAdapter.notifyDataSetChanged();
        }

    }

    public void onClickSubColorButton(View view) {
        colorPicker.init(this, selectedCard, view.getTag().toString());
        colorPicker.show();
    }

    public void onClickColorButton(View v) {
        selectedCard = cards.getByName(v.getTag().toString());
        colorPicker.init(this, selectedCard, null);
        colorPicker.show();
    }

    public void showSubCards(View view) {
        selectedCard = cards.getByName(view.getTag().toString());
        selectedCard.showSubCards(this);
    }

    public void showBlacklist(View view) {
        selectedCard = cards.getByName(view.getTag().toString());
        selectedCard.showBlacklist(this);
    }

    private ArrayList<PInfo> getInstalledApps() throws PackageManager.NameNotFoundException {
        ArrayList<PInfo> res = new ArrayList<>();

        int flags = PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES;

        PackageManager pm = getPackageManager();

        List<PackageInfo> applications = pm.getInstalledPackages(flags);

        for (PackageInfo appInfo : applications) {
            if (((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1)
                    || (appInfo.applicationInfo.loadLabel(getPackageManager()).toString().equals("Gmail"))
                    || (appInfo.applicationInfo.loadLabel(getPackageManager()).toString().equals("YouTube"))
                    || (appInfo.packageName.contains("facebook")) && !appInfo.applicationInfo.loadLabel(getPackageManager()).toString().contains("App")
                    || (appInfo.applicationInfo.loadLabel(getPackageManager()).toString().contains("Messenger"))) {
                PInfo newInfo = new PInfo();
                newInfo.appName = appInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                newInfo.icon = appInfo.applicationInfo.loadIcon(getPackageManager());
                res.add(newInfo);
                iconMap.add(newInfo.appName, newInfo.icon);
            }
        }
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                showWarningDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showWarningDialog() {
        final List<AppCard> selectedCards = cards.getSelectedCards();
        deleteAllCardsDialog = new AlertDialog.Builder(this, R.style.Alert_Dialog_Dark)
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
                        for (AppCard card : selectedCards) {
                            card.setSelected(false);
                            String tag = card.getAppName();
                            String nameTag = card.getAppName() + "_Name";
                            String iconTag = card.getAppName() + "_Icon";
                            String deleteBtnTag = card.getAppName() + "_Del_Btn";
                            String deleteBackGrdTag = card.getAppName() + "_Del_Backgrd";
                            appCardView.findViewWithTag(tag).setVisibility(View.VISIBLE);
                            appCardView.findViewWithTag(nameTag).setVisibility(View.VISIBLE);
                            appCardView.findViewWithTag(iconTag).setVisibility(View.VISIBLE);
                            appCardView.findViewWithTag(deleteBtnTag).setVisibility(View.INVISIBLE);
                            appCardView.findViewWithTag(deleteBackGrdTag).setVisibility(View.INVISIBLE);
                        }
                        cards.deselectCards();
                        cards.clear();
                        appCardAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .create();
        deleteAllCardsDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        cleanUp();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        cleanUp();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        cleanUp();
        super.onSaveInstanceState(outState);
    }

    private void cleanUp() {
        if (duplicationDialog != null) {
            duplicationDialog.dismiss();
        }

        if (deleteAllCardsDialog != null) {
            deleteAllCardsDialog.dismiss();
        }

        if (deleteMultipleCardsDialog != null) {
            deleteMultipleCardsDialog.dismiss();
        }

        if (selectedCard != null) {
            selectedCard.destroy();
        }

        closeOptionsMenu();

        dia.dismiss();
        Data.writeToFile();
        if (colorPicker.initialized && colorPicker.colorAlertDialog.isShowing()) {
            colorPicker.colorAlertDialog.dismiss();
        }

    }

    class ColorPicker {

        private AlertDialog colorAlertDialog;
        private ColorPickerPalette colorPickerPalette;
        private int[] colors;
        private boolean initialized = false;

        @SuppressLint("InflateParams")
        void init(Context context, final AppCard selectedCard, final String subCardName) {
            initialized = true;
            colors = new int[20];
            initColors();

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            colorPickerPalette = (ColorPickerPalette) layoutInflater.inflate(R.layout.custom_picker, null);
            colorPickerPalette.init(colors.length, 5, new ColorPickerSwatch.OnColorSelectedListener() {
                @Override
                public void onColorSelected(int color) {
                    if (subCardName == null) {
                        selectedCard.setColor(color);
                        int[][] states = new int[][]{new int[0]};
                        int[] colors = {color};
                        ColorStateList colorList = new ColorStateList(states, colors);
                        appCardView.findViewWithTag(selectedCard.getAppName()).setBackgroundTintList(colorList);
                    } else {
                        SubCard subCard = selectedCard.getSublist().get(subCardName);

                        if (subCard != null) {
                            subCard.setColor(color);
                            selectedCard.setSubColor(subCardName, color);
                        }
                    }
                    colorAlertDialog.dismiss();
                }
            });

            if (subCardName == null) {
                colorPickerPalette.drawPalette(colors, selectedCard.getColor());
            } else {
                colorPickerPalette.drawPalette(colors, selectedCard.getSublist().get(subCardName).getColor());
            }
            colorAlertDialog = new AlertDialog.Builder(context, R.style.Alert_Dialog_Dark)
                    .setTitle(R.string.color_picker_default_title)
                    .setView(colorPickerPalette)
                    .create();
        }

        void show() {
            colorAlertDialog.show();
        }

        private void initColors() {
            colors[0] = Color.argb(255, RED.r(), RED.g(), RED.b());
            colors[1] = Color.argb(255, PINK.r(), PINK.g(), PINK.b());
            colors[2] = Color.argb(255, PURPLE.r(), PURPLE.g(), PURPLE.b());
            colors[3] = Color.argb(255, DEEP_PURPLE.r(), DEEP_PURPLE.g(), DEEP_PURPLE.b());
            colors[4] = Color.argb(255, INDIGO.r(), INDIGO.g(), INDIGO.b());
            colors[5] = Color.argb(255, BLUE.r(), BLUE.g(), BLUE.b());
            colors[6] = Color.argb(255, LIGHT_BLUE.r(), LIGHT_BLUE.g(), LIGHT_BLUE.b());
            colors[7] = Color.argb(255, CYAN.r(), CYAN.g(), CYAN.b());
            colors[8] = Color.argb(255, TEAL.r(), TEAL.g(), TEAL.b());
            colors[9] = Color.argb(255, GREEN.r(), GREEN.g(), GREEN.b());
            colors[10] = Color.argb(255, LIGHT_GREEN.r(), LIGHT_GREEN.g(), LIGHT_GREEN.b());
            colors[11] = Color.argb(255, LIME.r(), LIME.g(), LIME.b());
            colors[12] = Color.argb(255, YELLOW.r(), YELLOW.g(), YELLOW.b());
            colors[13] = Color.argb(255, AMBER.r(), AMBER.g(), AMBER.b());
            colors[14] = Color.argb(255, ORANGE.r(), ORANGE.g(), ORANGE.b());
            colors[15] = Color.argb(255, DEEP_ORANGE.r(), DEEP_ORANGE.g(), DEEP_ORANGE.b());
            colors[16] = Color.argb(255, BROWN.r(), BROWN.g(), BROWN.b());
            colors[17] = Color.argb(255, GREY.r(), GREY.g(), GREY.b());
            colors[18] = Color.argb(255, BLUE_GREY.r(), BLUE_GREY.g(), BLUE_GREY.b());
            colors[19] = Color.argb(255, WHITE.r(), WHITE.g(), WHITE.b());
        }

    }

}
