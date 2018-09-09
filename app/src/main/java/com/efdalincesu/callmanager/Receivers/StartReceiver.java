package com.efdalincesu.callmanager.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.efdalincesu.callmanager.Service.MyService;

public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent ıntent) {
        Intent intent=new Intent(context, MyService.class);
        context.startService(intent);
    }
}
