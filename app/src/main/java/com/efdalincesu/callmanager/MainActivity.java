package com.efdalincesu.callmanager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.efdalincesu.callmanager.Adapters.RecyclerViewAdapter;
import com.efdalincesu.callmanager.Models.Alarm;
import com.efdalincesu.callmanager.Models.Date;
import com.efdalincesu.callmanager.Service.MyService;
import com.efdalincesu.callmanager.Utils.AllManager;
import com.efdalincesu.callmanager.Utils.ClassAdmob;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerViewAdapter adapter;
    Toolbar toolbar;
    RecyclerView.LayoutManager layoutManager;
    InterstitialAd interstitialAd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson;
    Resources res;

    AllManager allManager;
    ArrayList<Alarm> alarms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MyService.class));
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = ClassAdmob.getAdRequest(this);
        mAdView.loadAd(adRequest);
        initialize();
        res = getResources();
        preferences = allManager.preferences;
        editor = allManager.editor;

        if (!preferences.getBoolean("IS_ICON_CREATED", false)) {
            createShortcut();
            editor.putBoolean("IS_ICON_CREATED", true).commit();
        }

        getPermission(Manifest.permission.READ_PHONE_STATE);
        getPermission(Manifest.permission.SEND_SMS);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        allManager.commitAlarms(alarms);
    }

    @Override
    protected void onPause() {
        super.onPause();
        allManager.commitAlarms(alarms);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add) {

            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else {
                interstitialAd.loadAd(ClassAdmob.getAdRequest(this));
                interstitialAd.show();
            }

            createManager();

        } else if (id == R.id.blockedCalls) {

            Intent intent = new Intent(this, BlockedActivity.class);
            startActivity(intent);


        } else if (id == R.id.msg) {

            Intent intent = new Intent(this, MessageActivity.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }

    public void createManager() {

        ArrayList<Integer> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        list.add(calendar.get(Calendar.DAY_OF_WEEK));
        DateFormat format = new SimpleDateFormat("HH");
        int hour = Integer.valueOf(format.format(calendar.getTime()));
        int minute = calendar.get(Calendar.MINUTE);
        alarms.add(new Alarm(list, new Date(hour, minute), new Date(hour + 2 >= 24 ? 23 : hour + 2, hour + 2 >= 24 ? 59 : minute)));
        allManager.commitAlarms(alarms);
        adapter.notifyDataSetChanged();

    }

    public void initialize() {

        interstitialAd = ClassAdmob.getInterstitialAd(this);
        interstitialAd.loadAd(ClassAdmob.getAdRequest(this));

        allManager = new AllManager(this);
        alarms = allManager.getAlarms();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(alarms, this);
        recyclerView.setAdapter(adapter);

    }


    public void getPermission(String permission) {

        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }


    }


    public void createShortcut() {
        Intent intentShortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        Parcelable appicon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher_background);
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, appicon);
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), MainActivity.class));
        intentShortcut.putExtra("duplicate", false);
        sendBroadcast(intentShortcut);
    }

}
