package com.efdalincesu.callmanager.Models;

import android.util.Log;

import java.util.ArrayList;

public class Alarm {

    private Date baslangicDate, bitisDate;
    private ArrayList<Integer> days;
    private boolean state;
    private String message;

    public Alarm(ArrayList<Integer> daysList, Date baslangicDate, Date bitisDate) {

        this.baslangicDate = baslangicDate;
        this.bitisDate = bitisDate;
        this.message = "Dersteyim";
        this.state = true;
        if (daysList!=null) {
            this.days = daysList;
        }else {
            this.days=new ArrayList<>();
        }

    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getBaslangicDate() {
        return baslangicDate;
    }

    public void setBaslangicDate(Date baslangicDate) {
        this.baslangicDate = baslangicDate;
    }

    public Date getBitisDate() {
        return bitisDate;
    }

    public void setBitisDate(Date bitisDate) {
        if (baslangicDate.saat < bitisDate.saat) {
            this.bitisDate = bitisDate;
        } else if (baslangicDate.saat == bitisDate.saat) {
            if (baslangicDate.dakika < bitisDate.dakika) {
                this.bitisDate = bitisDate;
            } else {
                if (bitisDate.dakika + 10 >= 60) {
                    bitisDate.saat = bitisDate.saat + 1;
                    this.bitisDate = bitisDate;
                } else {
                    bitisDate.dakika += 10;
                    this.bitisDate = bitisDate;
                }
            }
        } else if (baslangicDate.saat > bitisDate.saat) {

            bitisDate.saat = baslangicDate.saat + 1 > 24 ? 00 : baslangicDate.saat + 1;
            this.bitisDate = bitisDate;

        }
    }

    public ArrayList<Integer> getDayList() {
        return days;
    }

    public void addDays(Integer day) {

        for (int i = 0; i < days.size(); i++) {

            if (days.get(i).equals(day)) {
                Log.d("eklendi","vardi ekelenemedi");
            } else {
                days.add(day);
            }
        }
    }

    public void removeDays(Object position) {
        days.remove(position);
    }
}
