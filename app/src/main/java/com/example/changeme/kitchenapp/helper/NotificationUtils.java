package com.example.changeme.kitchenapp.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.changeme.kitchenapp.MainActivity;
import com.example.changeme.kitchenapp.R;


public class NotificationUtils {

    private static  final int request_order = 1319;
    private static  final String request_id = "new_request";

    private static PendingIntent contenIntent(Context context){
        Intent startIntent= new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, request_order,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private static Bitmap largeIcon(Context context){
        Resources rs = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(rs, R.drawable.icondone);
        return  largeIcon;
        }

    public static void showtheNotification(Context context, String body){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(request_id, context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context,request_id)
                .setSmallIcon(R.drawable.icondone)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contenIntent(context))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(request_order, notificationBuilder.build());

        }
}


