package com.efdalincesu.callmanager.Utils;

import com.efdalincesu.callmanager.Models.Alarm;

import java.util.ArrayList;

public class AlarmManager {
    private static final AlarmManager ourInstance = new AlarmManager();
    private ArrayList<Alarm> arrayList = new ArrayList<>();

    private AlarmManager() {
    }

    public static synchronized AlarmManager getInstance() {
        return ourInstance;
    }

    public void setArrayList(ArrayList<Alarm> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<Alarm> getAlarm() {
        return arrayList;
    }

    public Alarm getAlarm(int position) {
        return arrayList.get(position);
    }

    public void addArrayList(Alarm alarm) {
        arrayList.add(alarm);
    }

    public void deleteAlarm(Alarm alarm) {
        arrayList.remove(alarm);
    }

    public void deleteAlarm(int position) {
        arrayList.remove(position);
    }


}
