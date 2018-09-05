package com.efdalincesu.callmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.calls_layout, viewGroup, false);

        TextView number = view.findViewById(R.id.number);
        TextView saat = view.findViewById(R.id.saat);
        TextView tarih = view.findViewById(R.id.tarih);

        number.setText(list.get(i).getNumber());
        saat.setText(list.get(i).getSaat());
        tarih.setText(list.get(i).getTarih());

        return view;
    }
}
