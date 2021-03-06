package com.hoptec.sanket.services;

/**
 * Created by shivesh on 21/2/17.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


import com.hoptec.sanket.Home;
import com.hoptec.sanket.R;
import com.hoptec.sanket.utils.NotificationExtras;
import com.hoptec.sanket.utl;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

     private static final String TAG = "MyAndroidFCMService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try { Log.d(TAG, "FCM From: " + remoteMessage.getFrom());

            Log.d(TAG, "FCM Notification Message Body: " + remoteMessage.getNotification().getBody());
        } catch (Exception e) {
           // e.printStackTrace();
        }

        ctx=this;
         utl.init(ctx);


        createNotification(remoteMessage);

    }

    Gson js;
    Context ctx;

    int nott=1;
    private void createNotification( RemoteMessage remoteMessage) {
        try {
         //   String mess=remoteMessage.getNotification().getBody();


            Intent intent = new Intent( this , Home. class );


            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String dd="";

            boolean isLoggedIn=(utl.readUserData()!=null);
            Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification.Builder b = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(""+remoteMessage.getData().get("title"))
                    //.setColor(getResources().getColor(R.color.green_400))
                    .setContentText(""+remoteMessage.getData().get("text"))
                    .setAutoCancel( true )
                    .setSound(notificationSoundURI)
                    .setContentInfo("Sanket")
                    .setContentIntent(isLoggedIn?resultIntent: PendingIntent.getActivity( this , 0, new Intent(ctx, Home.class),
                            PendingIntent.FLAG_ONE_SHOT));


            Notification n = NotificationExtras.buildWithBackgroundColor(ctx, b, getResources().getColor(R.color.grey_500));
            NotificationManagerCompat.from(this).notify(++nott
                    , n);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
