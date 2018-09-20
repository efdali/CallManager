package com.efdalincesu.callmanager.View.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.efdalincesu.callmanager.Models.BlockedCall;
import com.efdalincesu.callmanager.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    ArrayList<BlockedCall> list;
    Context context;

    public ListViewAdapter(ArrayList<BlockedCall> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.calls_layout, viewGroup, false);

        TextView number = view.findViewById(R.id.number);
        TextView saat = view.findViewById(R.id.saat);
        TextView tarih = view.findViewById(R.id.tarih);
        ImageButton ara = view.findViewById(R.id.ara);
        ImageButton msj = view.findViewById(R.id.msj);

        ara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ara = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.get(i).getNumber()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
                context.startActivity(ara);
            }
        });

        msj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mesajGonder = new Intent(Intent.ACTION_VIEW);
                mesajGonder.setData(Uri.parse("sms:" + list.get(i).getNumber()));
                context.startActivity(mesajGonder);

            }
        });

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        number.setText(getNumber(list.get(i).getNumber()));
        saat.setText(list.get(i).getSaat());
        tarih.setText(list.get(i).getTarih());

        return view;
    }


    public String getNumber(String number) {

        Cursor phoneCur = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null);
        while (phoneCur.moveToNext()) {
            String phone = phoneCur.getString(
                    phoneCur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DATA));
            String name = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            if (number.contains(phone)) {
                return name;
            }
        }
        phoneCur.close();


        return number;
    }

}
