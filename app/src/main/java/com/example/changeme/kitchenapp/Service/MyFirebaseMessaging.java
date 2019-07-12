package com.example.changeme.kitchenapp.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.changeme.kitchenapp.MainActivity;
import com.example.changeme.kitchenapp.R;
import com.example.changeme.kitchenapp.common.Common;
import com.example.changeme.kitchenapp.helper.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by SHEGZ on 1/4/2018.
 */
public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData() !=null){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendNotificationAPI26(remoteMessage);
            }
            else{
            sendNotification(remoteMessage);
            }
        }

    }


    private void sendNotification(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        if(Common.currentuser!=null) {


            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultsounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultsounduri)
                    .setContentIntent(pendingIntent);

            NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            noti.notify(0, builder.build());

        }

    }


    private void sendNotificationAPI26(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        if(Common.currentuser!=null) {

            NotificationUtils.showtheNotification(this, message);

        }
    }

    }
