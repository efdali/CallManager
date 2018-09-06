package com.efdalincesu.callmanager.Adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.efdalincesu.callmanager.Models.Alarm;
import com.efdalincesu.callmanager.Models.Date;
import com.efdalincesu.callmanager.R;
import com.efdalincesu.callmanager.Utils.AllManager;
import com.efdalincesu.callmanager.Utils.IDays;
import com.efdalincesu.callmanager.Utils.MyDialog;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayAdapter<String> adapter;
    ArrayList<Alarm> alarmList;
    Context context;
    int pos;

    public RecyclerViewAdapter(ArrayList<Alarm> alarmList, Context context) {
        this.alarmList = alarmList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        pos = position;
        holder.basSaat.setText(alarmList.get(position).getBaslangicDate().getTarih());
        holder.bitSaat.setText(alarmList.get(position).getBitisDate().getTarih());
        holder.aSwitch.setChecked(alarmList.get(position).isState());

        holder.basSaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialog = new MyDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date date = new Date(hourOfDay, minute);
                        alarmList.get(position).setBaslangicDate(date);
                        holder.basSaat.setText(date.getTarih());
                    }
                });
            }
        });

        holder.bitSaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialog = new MyDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date date = new Date(hourOfDay, minute);
                        alarmList.get(position).setBitisDate(date);
                        holder.bitSaat.setText(date.getTarih());
                    }
                });
            }
        });

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarmList.get(position).setState(isChecked);
            }
        });


        ArrayList<String> list = new AllManager(context).getMessages();

        adapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, list);

        holder.spinner.setAdapter(adapter);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alarmList.get(pos).setMessage(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spinner.setSelection(getIndex(holder.spinner, alarmList.get(position).getMessage()));

        for (int i : alarmList.get(position).getDayList()) {

            if (i == 1) {
                holder.pazar.setChecked(true);
            } else if (i == 2) {
                holder.pazartesi.setChecked(true);
            } else if (i == 3) {
                holder.sali.setChecked(true);
            } else if (i == 4) {
                holder.carsamba.setChecked(true);
            } else if (i == 5) {
                holder.persembe.setChecked(true);
            } else if (i == 6) {
                holder.cuma.setChecked(true);
            } else if (i == 7) {
                holder.cumartesi.setChecked(true);
            }
        }


        holder.sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm alarm = null;
                try {
                    alarm = alarmList.get(position);
                    alarmList.remove(alarm);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();


                } catch (Exception e) {
                    Toast.makeText(context, "Silinemedi! Sistemsel bir hata... " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                int day = 0;
                int id = buttonView.getId();

                switch (id) {
                    case R.id.pazar:
                        day = IDays.SUNDAY;
                        break;
                    case R.id.pazartesi:
                        day = IDays.MONDAY;
                        break;
                    case R.id.sali:
                        day = IDays.TUESDAY;
                        break;
                    case R.id.carsamba:
                        day = IDays.WEDNESDAY;
                        break;
                    case R.id.persembe:
                        day = IDays.THURSDAY;
                        break;
                    case R.id.cuma:
                        day = IDays.FRIDAY;
                        break;
                    case R.id.cumartesi:
                        day = IDays.SATURDAY;
                        break;
                }
                if (isChecked) {
                    alarmList.get(position).getDayList().add(day);
                } else {
                    alarmList.get(position).removeDays(day);
                }
            }
        };

        holder.pazar.setOnCheckedChangeListener(listener);
        holder.pazartesi.setOnCheckedChangeListener(listener);
        holder.sali.setOnCheckedChangeListener(listener);
        holder.carsamba.setOnCheckedChangeListener(listener);
        holder.persembe.setOnCheckedChangeListener(listener);
        holder.cuma.setOnCheckedChangeListener(listener);
        holder.cumartesi.setOnCheckedChangeListener(listener);

    }

    private int getIndex(Spinner spinner, String message) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(message)) {
                index = i;
            }
        }
        return index;
    }


    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView basSaat, bitSaat;
        Spinner spinner;
        ToggleButton pazartesi, sali, carsamba, persembe, cuma, cumartesi, pazar;
        Switch aSwitch;
        Button sil;

        public ViewHolder(View itemView) {
            super(itemView);


            basSaat = itemView.findViewById(R.id.basSaat);
            bitSaat = itemView.findViewById(R.id.bitSaat);
            spinner = itemView.findViewById(R.id.spinner);
            pazartesi = itemView.findViewById(R.id.pazartesi);
            sali = itemView.findViewById(R.id.sali);
            carsamba = itemView.findViewById(R.id.carsamba);
            persembe = itemView.findViewById(R.id.persembe);
            cuma = itemView.findViewById(R.id.cuma);
            cumartesi = itemView.findViewById(R.id.cumartesi);
            pazar = itemView.findViewById(R.id.pazar);
            aSwitch = itemView.findViewById(R.id.switchButton);
            sil = itemView.findViewById(R.id.sil);

        }
    }
}

