package com.efdalincesu.callmanager.Utils;

import android.app.TimePickerDialog;
import android.content.Context;

import com.efdalincesu.callmanager.Models.Date;

public class MyDialog extends TimePickerDialog {
    Date date;
    public MyDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);

    }

    public MyDialog(Context context, OnTimeSetListener listener){
        super(context,listener,0,0,true);
        setTitle("Saati Seçiniz!");
        show();

    }

}
