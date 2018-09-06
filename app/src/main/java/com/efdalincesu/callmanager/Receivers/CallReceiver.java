package com.efdalincesu.callmanager.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.efdalincesu.callmanager.Models.Alarm;
import com.efdalincesu.callmanager.Utils.AllManager;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CallReceiver extends BroadcastReceiver {
    TelephonyManager telephonyManager;
    String incomingNumber;
    boolean isSend = false;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    AllManager allManager;
    @Override
    public void onReceive(Context context, Intent ıntent) {

        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener();
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    public void sendMessage(String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(incomingNumber, null, message, null, null);

        } catch (Exception e) {

        }

    }

    private void rejectCall() {

        try {

            // Get the getITelephony() method
            Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method method = classTelephony.getDeclaredMethod("getITelephony");
            // Disable access check
            method.setAccessible(true);
            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = method.invoke(telephonyManager);
            // Get the endCall method from ITelephony
            Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception e) {
        }
    }

    public Alarm bul() {

        allManager =new AllManager(context);
        preferences = allManager.preferences;
        editor = allManager.editor;
        ArrayList<Alarm> arrayList = allManager.getAlarms();

        Calendar calendar = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("HH");
        int saat = Integer.valueOf(format.format(calendar.getTime()));
        int dakika = calendar.get(Calendar.MINUTE);
        int gun = calendar.get(Calendar.DAY_OF_WEEK);
        int baslangicSaat, baslangicDakika, bitisSaat, bitisDakika;
        boolean state;

        for (Alarm alarm : arrayList) {
            baslangicSaat = alarm.getBaslangicDate().saat;
            baslangicDakika = alarm.getBaslangicDate().dakika;
            bitisSaat = alarm.getBitisDate().saat;
            bitisDakika = alarm.getBitisDate().dakika;
            state = alarm.isState();
            for (int i : alarm.getDayList()) {

                if (i == gun) {
                    if (state) {

                        if (baslangicSaat == saat && bitisSaat == saat) {
                            if (baslangicDakika <= dakika && bitisDakika >= saat) {
                                return alarm;
                            }
                        } else if (baslangicSaat == saat) {

                            if (baslangicDakika <= dakika) {
                                return alarm;
                            }
                        } else if (baslangicSaat < saat) {

                            if (bitisSaat > saat) {
                                return alarm;
                            } else if (bitisSaat == saat) {
                                if (bitisDakika >= dakika) {
                                    return alarm;
                                }
                            }
                        }
                    }
                }
            }

        }
        return null;

    }

    public void saveBlockedCall() {

        allManager.commitCalls(incomingNumber);

    }

    private class MyPhoneStateListener extends PhoneStateListener {


        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);

            if (state == TelephonyManager.CALL_STATE_RINGING) {
                incomingNumber = phoneNumber;
                Alarm alarm = bul();
                if (alarm != null) {
                    String message = alarm.getMessage();
                    rejectCall();
                    if (!isSend) {
                        sendMessage(message);
                        saveBlockedCall();
                        isSend = true;
                    }
                }
            } else {
                isSend = false;
            }


        }

    }

}
