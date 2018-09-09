package com.efdalincesu.callmanager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.efdalincesu.callmanager.Adapters.ListViewAdapter;
import com.efdalincesu.callmanager.Models.BlockedCall;
import com.efdalincesu.callmanager.Utils.AllManager;
import com.efdalincesu.callmanager.Utils.ClassAdmob;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class BlockedActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<BlockedCall> blockedCalls;
    ListViewAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = ClassAdmob.getAdRequest(this);
        mAdView.loadAd(adRequest);

        setTitle(getResources().getString(R.string.blocked_calls));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));

        listView = findViewById(R.id.listview);

        blockedCalls = new AllManager(this).getCalls();

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
