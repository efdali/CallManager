package com.efdalincesu.callmanager.Models;

import java.util.ArrayList;

public class Alarm {

    private Date baslangicDate,bitisDate;
    private ArrayList<Integer> days;
    private boolean state;
    private String message;

    public Alarm(ArrayList<Integer> daysList,Date baslangicDate,Date bitisDate){

        this.baslangicDate = baslangicDate;
        this.bitisDate = bitisDate;
        this.message="Dersteyim";
        this.state=true;
        this.days=daysList;

    }
    public Alarm(Date baslangicDate, Date bitisDate, ArrayList<Integer> days) {
        state=true;
        message="Seni Hemen Arıyacağım!";
        this.baslangicDate = baslangicDate;
        this.bitisDate = bitisDate;
        this.days = days;
    }

    public Alarm(Date baslangicDate, Date bitisDate, ArrayList<Integer> days, String message) {
        state=true;
        this.baslangicDate = baslangicDate;
        this.bitisDate = bitisDate;
        this.days = days;
        this.message = message;
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
        if (baslangicDate.saat<bitisDate.saat){
            this.bitisDate = bitisDate;
        }else if (baslangicDate.saat==bitisDate.saat){
            if (baslangicDate.dakika<bitisDate.dakika){
                this.bitisDate=bitisDate;
            }else{
                if (bitisDate.dakika+10>=60){
                    bitisDate.saat=bitisDate.saat+1;
                    this.bitisDate=bitisDate;
                }else{
                    bitisDate.dakika+=10;
                    this.bitisDate=bitisDate;
                }
            }
        }else if (baslangicDate.saat>bitisDate.saat){

            bitisDate.saat=baslangicDate.saat+1 > 24 ? 00 : baslangicDate.saat+1 ;
            this.bitisDate=bitisDate;

        }
    }

    public ArrayList<Integer> getDays() {
        return days;
    }

    public int getDays(int position) {
        return days.get(position);
    }

    public void setDays(Object position) {
        days.remove(position);
    }
}
