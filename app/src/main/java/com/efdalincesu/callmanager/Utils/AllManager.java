package com.efdalincesu.callmanager.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.efdalincesu.callmanager.MainActivity;
import com.efdalincesu.callmanager.Models.Alarm;
import com.efdalincesu.callmanager.Models.BlockedCall;
import com.efdalincesu.callmanager.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

public class AllManager {



    public static final String SHARED_NAME = "callManager";
    public static final String SHARED_ALARMS = "alarms";
    public static final String SHARED_MESSAGES = "messages";
    public static final String SHARED_CALLS = "blockedCall";

    Context context;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    public AllManager(Context context) {
        this.context = context;
        this.preferences=openShared();
        this.editor=preferences.edit();
    }

    public SharedPreferences openShared(){

        preferences=context.getSharedPreferences(SHARED_NAME,Context.MODE_PRIVATE);

        return preferences;
    }

    public void commitAlarms(ArrayList<Alarm> alarmList){

        Gson gson=new Gson();
        String stringObject = gson.toJson(alarmList);
        editor.putString(SHARED_ALARMS, stringObject);
        editor.commit();

    }

    public ArrayList<Alarm> getAlarms(){

        Gson gson=new Gson();
        ArrayList<Alarm> alarms=null;
        String obj = preferences.getString(SHARED_ALARMS, null);
        if (obj != null) {
            Type type = new TypeToken<ArrayList<Alarm>>() {}.getType();
            alarms= gson.fromJson(obj, type);
        }else{
            alarms=new ArrayList<>();
        }

        return alarms;
    }

    public ArrayList<BlockedCall> getCalls(){

        String obj = preferences.getString(SHARED_CALLS, null);
        Gson gson=new Gson();
        ArrayList<BlockedCall> blockedCalls = null;
        if (!(obj == null)) {
            Type type = new TypeToken<ArrayList<BlockedCall>>() {
            }.getType();
            blockedCalls = gson.fromJson(obj, type);
        } else {
            blockedCalls = new ArrayList<>();
        }

        return blockedCalls;
    }

    public void commitCalls(String incomingNumber){

        Gson gson=new Gson();
        String obj = preferences.getString(SHARED_CALLS, null);
        ArrayList<BlockedCall> blockedCalls = null;
        if (!(obj == null)) {
            Type type = new TypeToken<ArrayList<BlockedCall>>() {
            }.getType();
            blockedCalls = gson.fromJson(obj, type);
        } else {
            blockedCalls = new ArrayList<>();
        }

        blockedCalls.add(new BlockedCall(incomingNumber));

        String stringObject = gson.toJson(blockedCalls);
        editor.putString(SHARED_CALLS, stringObject);
        editor.commit();


    }

    public HashSet<String> getMessagesHashSet(){

        HashSet<String> set = (HashSet<String>) preferences.getStringSet(SHARED_MESSAGES, new HashSet<String>());

        return set;
    }

    public ArrayList<String> getMessages(){


        String gelen[] = context.getResources().getStringArray(R.array.hizliMsj);

        HashSet<String> set = getMessagesHashSet();
        ArrayList<String> list = new ArrayList<>();
        String[] dizi = set.toArray(new String[0]);
        for (int i = 0; i < gelen.length; i++) {
            list.add(gelen[i]);
        }
        for (int i = 0; i < dizi.length; i++) {
            list.add(dizi[i]);
        }

        return list;
    }

    public void commitMessages(HashSet<String> set){

        editor.putStringSet(SHARED_MESSAGES,set);
        editor.commit();
    }

}
