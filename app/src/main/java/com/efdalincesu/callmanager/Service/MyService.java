package com.efdalincesu.callmanager.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.efdalincesu.callmanager.View.MainActivity;
import com.efdalincesu.callmanager.Models.Alarm;
import com.efdalincesu.callmanager.R;
import com.efdalincesu.callmanager.Utils.AllManager;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private final int NOTIFICATION_ID = 1;
    private final long time = 10000;
    Timer timer;
    Handler handler;
    Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        handler = new Handler(Looper.getMainLooper());
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                controlNotification();
            }
        }, 0, time);
        res=getResources();
    }

    public void controlNotification() {

        handler.post(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                AllManager allManager = new AllManager(getApplicationContext());
                Alarm alarm = allManager.bul();
                if (alarm != null) {

                    NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                    bigTextStyle.bigText(alarm.getBaslangicDate().getTarih() + "-" + alarm.getBitisDate().getTarih() +  " "+res.getString(R.string.created_manager));
                    bigTextStyle.setBigContentTitle(res.getString(R.string.reminder));
                    bigTextStyle.setSummaryText(alarm.getMessage());

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                    builder.setOngoing(true);
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    builder.setAutoCancel(true);
                    builder.setContentTitle(res.getString(R.string.reminder));
                    builder.setContentText(res.getString(R.string.busy));
                    builder.setContentIntent(pendingIntent);
                    builder.setStyle(bigTextStyle);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                    Notification notification = builder.build();
                    manager.notify(NOTIFICATION_ID, notification);
                } else {
                    manager.cancel(NOTIFICATION_ID);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {

        return null;
    }
}
