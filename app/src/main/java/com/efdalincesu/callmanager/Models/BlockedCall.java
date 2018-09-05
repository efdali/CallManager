package com.efdalincesu.callmanager.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BlockedCall {

    private String number, tarih, saat;

    public BlockedCall(String number) {
        this.number = number;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.tarih = dateFormat.format(new Date());
        dateFormat = new SimpleDateFormat("HH:mm");
        this.saat = dateFormat.format(new Date());
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }
}
