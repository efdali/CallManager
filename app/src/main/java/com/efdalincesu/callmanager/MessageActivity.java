package com.efdalincesu.callmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.efdalincesu.callmanager.Utils.AllManager;
import com.efdalincesu.callmanager.Utils.ClassAdmob;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashSet;

public class MessageActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ListView listView;
    ArrayAdapter<String> adapter;
    HashSet<String> msgSet;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ArrayList<String> adapterList;
    Resources res;

    AllManager allManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        res = getResources();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = ClassAdmob.getAdRequest(this);
        mAdView.loadAd(adRequest);

        setTitle(res.getString(R.string.messages_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab_add);
        listView = findViewById(R.id.listView);

        registerForContextMenu(listView);

        allManager =new AllManager(this);

        msgSet= allManager.getMessagesHashSet();

        preferences = allManager.preferences;
        editor = allManager.editor;


        adapterList = allManager.getMessages();




        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, adapterList);


        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showDialog(getResources().getString(R.string.new_message_dialog), -1);

            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle(res.getString(R.string.menu_title));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final int position = contextMenuInfo.position;
        int id = item.getItemId();

        if (!controlStrings(position)) {
            if (id == R.id.edit) {

                showDialog(res.getString(R.string.edit), position);

            } else if (id == R.id.delete) {

                msgSet.remove(adapterList.get(position));
                adapterList.remove(adapterList.get(position));
                saveShared();

            }
        } else {
            Toast.makeText(this, res.getString(R.string.not_change), Toast.LENGTH_SHORT).show();
        }


        return super.onContextItemSelected(item);
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

    public void showDialog(String title, final int position) {

        final EditText editText = new EditText(getApplicationContext());
        if (position != -1) {
            editText.setText(adapterList.get(position));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MessageActivity.this, R.style.myDialog));
        builder.setTitle(title);
        builder.setView(editText);
        builder.setCancelable(false);
        builder.setPositiveButton(res.getString(R.string.dialog_add_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String text = editText.getText().toString();
                if (text.equals("")) {
                    Toast.makeText(getApplicationContext(), res.getString(R.string.null_text), Toast.LENGTH_SHORT).show();
                } else {
                    if (position == -1) {
                        msgSet.add(text);
                        adapterList.add(text);
                    } else {
                        msgSet.remove(adapterList.get(position));
                        msgSet.add(text);
                        adapterList.set(position, text);
                    }
                    saveShared();
                }

            }
        }).show();

    }

    public void saveShared() {
        allManager.commitMessages(msgSet);
        adapter.notifyDataSetChanged();
    }

    public boolean controlStrings(int position) {

        String[] strings = this.getResources().getStringArray(R.array.hizliMsj);

        String string = adapterList.get(position);

        for (String s : strings) {
            if (s.equals(string)) {
                return true;
            }
        }

        return false;
    }

}
