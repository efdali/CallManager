package com.efdalincesu.callmanager.View.Adapters;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.efdalincesu.callmanager.Models.Alarm;
import com.efdalincesu.callmanager.Models.Date;
import com.efdalincesu.callmanager.R;
import com.efdalincesu.callmanager.Utils.AllManager;
import com.efdalincesu.callmanager.Utils.IDays;
import com.efdalincesu.callmanager.Utils.MyDialog;
import com.efdalincesu.callmanager.View.MainActivity;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayAdapter<String> adapter;
    ArrayList<Alarm> alarmList;
    Context context;
    ArrayList<String> days;
    Resources res;

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

        final Alarm alarm = alarmList.get(position);

        days = new ArrayList<>();
        res = context.getResources();

        holder.basSaat.setText(alarm.getBaslangicDate().getTarih());
        holder.bitSaat.setText(alarm.getBitisDate().getTarih());
        holder.aSwitch.setChecked(alarm.isState());

        holder.basSaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog dialog = new MyDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Date date = new Date(hourOfDay, minute);
                        alarm.setBaslangicDate(date);
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
                        alarm.setBitisDate(date);
                        holder.bitSaat.setText(date.getTarih());
                    }
                });
            }
        });

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.setState(isChecked);
            }
        });


        final ArrayList<String> list = new AllManager(context).getMessages();

        adapter = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, list);

        holder.spinner.setAdapter(adapter);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alarm.setMessage(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spinner.setSelection(getIndex(holder.spinner, alarmList.get(position).getMessage()));
        for (int i : alarm.getDayList()) {
            if (i == 1) {
                days.add(res.getString(R.string.pazar));
                holder.pazar.setChecked(true);
            } else if (i == 2) {
                days.add(res.getString(R.string.pazartesi));
                holder.pazartesi.setChecked(true);
            } else if (i == 3) {
                days.add(res.getString(R.string.sali));
                holder.sali.setChecked(true);
            } else if (i == 4) {
                days.add(res.getString(R.string.carsamba));
                holder.carsamba.setChecked(true);
            } else if (i == 5) {
                days.add(res.getString(R.string.persembe));
                holder.persembe.setChecked(true);
            } else if (i == 6) {
                days.add(res.getString(R.string.cuma));
                holder.cuma.setChecked(true);
            } else if (i == 7) {
                days.add(res.getString(R.string.cumartesi));
                holder.cumartesi.setChecked(true);
            }

        }

        holder.daysTextView.setText(daysToString(days));

        holder.sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alarmList.remove(alarm);

                ((Activity) context).finish();
                context.startActivity(new Intent(context, MainActivity.class));


            }
        });

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                int day = 0;
                int id = buttonView.getId();
                String dayS = "";
                switch (id) {
                    case R.id.pazar:
                        day = IDays.SUNDAY;
                        dayS = "Pazar";
                        break;
                    case R.id.pazartesi:
                        day = IDays.MONDAY;
                        dayS = "Ptesi";
                        break;
                    case R.id.sali:
                        day = IDays.TUESDAY;
                        dayS = "Salı";
                        break;
                    case R.id.carsamba:
                        day = IDays.WEDNESDAY;
                        dayS = "Çarş";
                        break;
                    case R.id.persembe:
                        day = IDays.THURSDAY;
                        dayS = "Perş";
                        break;
                    case R.id.cuma:
                        day = IDays.FRIDAY;
                        dayS = "Cuma";
                        break;
                    case R.id.cumartesi:
                        day = IDays.SATURDAY;
                        dayS = "Ctesi";
                        break;
                }

                if (isChecked) {
                    alarm.getDayList().add(day);
                } else {
                    alarm.removeDays(day);
                }
            }
        };
        holder.openLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.daysTextView.setVisibility(View.INVISIBLE);
                holder.openLayout.setVisibility(View.GONE);

            }
        });

        holder.closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.daysTextView.setVisibility(View.VISIBLE);
                holder.linearLayout.setVisibility(View.GONE);
                holder.openLayout.setVisibility(View.VISIBLE);
                days = new ArrayList<>();
                for (int i : alarm.getDayList()) {

                    if (i == 1) {
                        days.add(res.getString(R.string.pazar));
                    } else if (i == 2) {
                        days.add(res.getString(R.string.pazartesi));
                    } else if (i == 3) {
                        days.add(res.getString(R.string.sali));
                    } else if (i == 4) {
                        days.add(res.getString(R.string.carsamba));
                    } else if (i == 5) {
                        days.add(res.getString(R.string.persembe));
                    } else if (i == 6) {
                        days.add(res.getString(R.string.cuma));
                    } else if (i == 7) {
                        days.add(res.getString(R.string.cumartesi));
                    }

                }
                holder.daysTextView.setText(daysToString(days));
            }
        });

        holder.pazar.setOnCheckedChangeListener(listener);
        holder.pazartesi.setOnCheckedChangeListener(listener);
        holder.sali.setOnCheckedChangeListener(listener);
        holder.carsamba.setOnCheckedChangeListener(listener);
        holder.persembe.setOnCheckedChangeListener(listener);
        holder.cuma.setOnCheckedChangeListener(listener);
        holder.cumartesi.setOnCheckedChangeListener(listener);


    }

    private String daysToString(ArrayList<String> days) {
        String day = "";
        for (int i = 0; i < days.size(); i++) {
            day += days.get(i);
            if (i < days.size() - 1) {
                day += ",";
            }
        }
        return day;
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

        TextView basSaat, bitSaat, daysTextView;
        LinearLayout linearLayout;
        Spinner spinner;
        ToggleButton pazartesi, sali, carsamba, persembe, cuma, cumartesi, pazar;
        Switch aSwitch;
        ImageButton openLayout, closeLayout;
        Button sil;

        public ViewHolder(View itemView) {
            super(itemView);


            basSaat = itemView.findViewById(R.id.basSaat);
            bitSaat = itemView.findViewById(R.id.bitSaat);
            daysTextView = itemView.findViewById(R.id.daysTextView);
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
            openLayout = itemView.findViewById(R.id.openLayout);
            closeLayout = itemView.findViewById(R.id.closeLayout);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}

