package com.efdalincesu.callmanager.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.efdalincesu.callmanager.Models.Alarm;
import com.efdalincesu.callmanager.Utils.AllManager;

import java.lang.reflect.Method;

public class CallReceiver extends BroadcastReceiver {
    TelephonyManager telephonyManager;
    String incomingNumber;
    boolean isSend = false;
    Context context;
    AllManager allManager;

    @Override
    public void onReceive(Context context, Intent ıntent) {

        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        allManager = new AllManager(context);
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


    public void saveBlockedCall() {

        allManager.commitCalls(incomingNumber);

    }

    private class MyPhoneStateListener extends PhoneStateListener {


        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);

            if (state == TelephonyManager.CALL_STATE_RINGING) {
                incomingNumber = phoneNumber;
                Alarm alarm = allManager.bul();
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
