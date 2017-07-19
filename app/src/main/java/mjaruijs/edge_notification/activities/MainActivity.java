package mjaruijs.edge_notification.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import mjaruijs.edge_notification.data.AppCard;
import mjaruijs.edge_notification.data.AppItem;
import mjaruijs.edge_notification.data.CardList;
import mjaruijs.edge_notification.data.IconMap;
import mjaruijs.edge_notification.data.PInfo;
import mjaruijs.edge_notification.fragments.SettingsFragment;
import mjaruijs.edge_notification.preferences.Prefs;
import mjaruijs.edge_notification.services.AppList;
import mjaruijs.edge_notification.services.RVAdapter;
import mjaruijs.edge_notification.services.StarterService;

public class MainActivity extends AppCompatActivity  {
    private RVAdapter appCardAdapter;
    private IconMap iconMap;
    private CardList cards;
    String TAG = getClass().getSimpleName();
    private Intent starterService;
    private Dialog dia;
    public String[] strings;
    public Drawable[] icons;
    //public static final int SCREEN_BRIGHT_WAKE_LOCK = 0x0000000a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        starterService = new Intent(getApplicationContext(), StarterService.class);

       iconMap = new IconMap();

        // Preferences
        Prefs prefs = new Prefs(getApplicationContext());
        prefs.apply();

//        if(!prefs.initialized) {
//            startActivity(new Intent(getApplicationContext(), IntroActivity.class));
//            finish();
//        } else {

            // Toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Switch
            getFragmentManager().beginTransaction()
                    .replace(R.id.preferences_holder, new SettingsFragment())
                    .commitAllowingStateLoss();

            Intent starterServiceIntent = new Intent(getApplicationContext(), StarterService.class);

            stopService(starterServiceIntent);
            startService(starterServiceIntent);

            // Recycle View
            RecyclerView appCardView = (RecyclerView) findViewById(R.id.recycle_view);

            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());

            appCardView.setLayoutManager(llm);
            File file = getFilesDir();

            // Get a list of installed apps.
            ArrayList<PInfo> applicationPackages = new ArrayList<>();
            try {
                applicationPackages = getInstalledApps(false);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            CardList.initialize(file);
            cards = CardList.readFromXML(iconMap);

            appCardAdapter = new RVAdapter(cards);

            AppList apps = new AppList(MainActivity.this);
            appCardView.setAdapter(appCardAdapter);

            strings = new String[applicationPackages.size()];
            icons = new Drawable[applicationPackages.size()];

            for (int i = 0; i < applicationPackages.size(); i++) {
                strings[i] = applicationPackages.get(i).appName;
                icons[i] = applicationPackages.get(i).icon;
                apps.add(new AppItem(applicationPackages.get(i).appName, applicationPackages.get(i).icon));
            }

            apps.sort();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Alert_Dialog_Dark);
            builder.setTitle("Pick an app");
            builder.setAdapter(apps, new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            dia = builder.create();

            // Floating Action Button
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dia.show();

                }
            });

        //}
    }

    public void onClick(View v){
        TextView textView = (TextView) v.findViewById(R.id.app_text);
        ImageView icon = (ImageView) v.findViewById(R.id.app_icon);

        cards.addCard(new AppCard(textView.getText().toString(),
                icon.getDrawable(),
                Color.WHITE));
        dia.hide();
        appCardAdapter.notifyDataSetChanged();
    }

    private void restartService() {
        stopService(starterService);
        startService(starterService);
    }

    // TODO: Add a checkbox that allows the user to add system apps too.
    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) throws PackageManager.NameNotFoundException {
        ArrayList<PInfo> res = new ArrayList<>();

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES;

        PackageManager pm = getPackageManager();
        List<PackageInfo> applications = pm.getInstalledPackages(flags);

        for (PackageInfo appInfo : applications) {
            if ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
                PInfo newInfo = new PInfo();
                newInfo.appName = appInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                newInfo.icon = appInfo.applicationInfo.loadIcon(getPackageManager());
                res.add(newInfo);
                iconMap.add(newInfo.appName, newInfo.icon);
            }
        } return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_permission:
                Intent i = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(i);
                return true;

            case R.id.delete_all:
                cards.clear();
                appCardAdapter.notifyDataSetChanged();
                restartService();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cards.writeToFile();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

}
