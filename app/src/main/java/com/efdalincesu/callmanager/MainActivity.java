package com.efdalincesu.callmanager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.efdalincesu.callmanager.Adapters.RecyclerViewAdapter;
import com.efdalincesu.callmanager.Models.Alarm;
import com.efdalincesu.callmanager.Models.Date;
import com.efdalincesu.callmanager.Utils.AlarmManager;
import com.efdalincesu.callmanager.Utils.ClassAdmob;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_NAME = "callManager";
    public static final String SHARED_ALARMS = "alarms";
    public static final String SHARED_MESSAGES = "messages";
    public static final String SHARED_CALLS = "blockedCall";

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter adapter;
    InterstitialAd interstitialAd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = ClassAdmob.getAdRequest(this);
        mAdView.loadAd(adRequest);
        initialize();
        res = getResources();

        if(!preferences.getBoolean("IS_ICON_CREATED", false)){
            createShortcut();
            editor.putBoolean("IS_ICON_CREATED", true).commit();
        }

        getPermission(Manifest.permission.READ_PHONE_STATE);
        getPermission(Manifest.permission.SEND_SMS);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedCommit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedCommit();
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
        AlarmManager.getInstance().addArrayList(new Alarm(list, new Date(hour, minute), new Date(hour + 2 > 24 ? 24 : hour + 2, minute)));
//            Toast.makeText(this,res.getString(R.string.created_manager),Toast.LENGTH_LONG).show();
        sharedCommit();
        adapter.notifyDataSetChanged();

    }

    public void initialize() {

        interstitialAd = ClassAdmob.getInterstitialAd(this);
        interstitialAd.loadAd(ClassAdmob.getAdRequest(this));

        preferences = getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
        String obj = preferences.getString(SHARED_ALARMS, null);
        if (!(obj == null)) {
            Type type = new TypeToken<ArrayList<Alarm>>() {
            }.getType();
            AlarmManager.getInstance().setArrayList((ArrayList<Alarm>) gson.fromJson(obj, type));
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(AlarmManager.getInstance().getAlarm(), this);
        recyclerView.setAdapter(adapter);

    }

    public void createShortcut(){
        Intent intentShortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        Parcelable appicon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher_background);
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, appicon);
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), MainActivity.class));
        intentShortcut.putExtra("duplicate", false);
        sendBroadcast(intentShortcut);
    }

    public void getPermission(String permission) {

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }


    }

    public void sharedCommit() {

        String stringObject = gson.toJson(AlarmManager.getInstance().getAlarm());
        editor.putString(SHARED_ALARMS, stringObject);
        editor.commit();

    }


}
