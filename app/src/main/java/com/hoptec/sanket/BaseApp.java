package com.hoptec.sanket;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by shivesh on 28/6/17.
 */

public class BaseApp extends Application {



    public static Firebase fb;
    public static Firebase ses;

    public static String SESSION;
    @Override
    public void onCreate()
    {

        Constants.init(this);
        super.onCreate();
        SESSION=""+ utl.getDateTime();
        try
        {

            Firebase.setAndroidContext(this);
            fb=new Firebase(Constants.fireURL());
            ses=fb.child("logs").child(utl.dev()).child(SESSION);
            utl.init(this);
            utl.logEvent("APP START",null);

            FirebaseAnalytics.getInstance(this);
            FirebaseAnalytics.getInstance(this).setUserId(utl.dev());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
