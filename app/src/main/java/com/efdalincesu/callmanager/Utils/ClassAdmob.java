package com.efdalincesu.callmanager.Utils;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class ClassAdmob {


    private static final String reklamID = "ca-app-pub-2323838972684036~2519678097";
    private static final String gecisID = "ca-app-pub-2323838972684036/9778666103";


    public static AdRequest getAdRequest(Context context) {

        MobileAds.initialize(context, reklamID);

        return new AdRequest.Builder().build();
    }

    public static InterstitialAd getInterstitialAd(Context context) {

        InterstitialAd ad=new InterstitialAd(context);
        ad.setAdUnitId(gecisID);
        return ad;
    }

}
