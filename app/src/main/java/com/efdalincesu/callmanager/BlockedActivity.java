package com.efdalincesu.callmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.efdalincesu.callmanager.Adapters.ListViewAdapter;
import com.efdalincesu.callmanager.Models.BlockedCall;
import com.efdalincesu.callmanager.Utils.ClassAdmob;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BlockedActivity extends AppCompatActivity {

    ListView listView;
    SharedPreferences preferences;
    Gson gson;
    ArrayList<BlockedCall> blockedCalls;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = ClassAdmob.getAdRequest(this);
        mAdView.loadAd(adRequest);

        setTitle(getResources().getString(R.string.blocked_calls));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listview);
        preferences = getSharedPreferences(MainActivity.SHARED_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        String obj = preferences.getString(MainActivity.SHARED_CALLS, null);

        blockedCalls = null;
        if (!(obj == null)) {
            Type type = new TypeToken<ArrayList<BlockedCall>>() {
            }.getType();
            blockedCalls = gson.fromJson(obj, type);
        } else {
            blockedCalls = new ArrayList<>();
        }

        adapter = new ListViewAdapter(blockedCalls, this);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(this, MainActivity.class);
            NavUtils.navigateUpTo(this, intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
