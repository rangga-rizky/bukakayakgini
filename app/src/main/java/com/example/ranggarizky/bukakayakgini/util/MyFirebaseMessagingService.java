package com.example.ranggarizky.bukakayakgini.util;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ranggarizky.bukakayakgini.DetailRequestActivity;
import com.example.ranggarizky.bukakayakgini.DetailTawaranActivity;
import com.example.ranggarizky.bukakayakgini.MainActivity;
import com.example.ranggarizky.bukakayakgini.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by ranggarizky on 6/16/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        SessionManager sessionManager =  new SessionManager(getApplicationContext());
        if(sessionManager.isLogin()){
            if(remoteMessage.getData().get("message") != null){
                if(remoteMessage.getData().get("id") != null && remoteMessage.getData().get("other") != null){

                    sendNotification( remoteMessage.getData().get("message"),remoteMessage.getData().get("id") ,remoteMessage.getData().get("other"));
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    intent.setAction("com.ranggarizky.bukaKayakGini");
                    sendBroadcast(intent);
                }
            }
        }
    }

    private void sendNotification(String messageBody,String id,String other) {

        Bitmap bmp =  BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Intent intent;
        if(other.equals("newproductsub") ||other.equals("demand_expired")  ){
            intent = new Intent(this, DetailRequestActivity.class);
            intent.putExtra("id",id);;
        }else{
            intent = new Intent(this, DetailTawaranActivity.class);
            intent.putExtra("id",id);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(bmp)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("BukaKayakGini")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}


