package com.efdalincesu.callmanager.Models;

public class Date {


    public int saat,dakika;

    public Date(int saat, int dakika) {
        this.saat = saat;
        this.dakika = dakika;
    }
    public String getTarih(){

        return ((saat < 10) ? "0"+saat : saat)+":"+(dakika < 10 ? "0"+dakika: dakika);
    }

}
