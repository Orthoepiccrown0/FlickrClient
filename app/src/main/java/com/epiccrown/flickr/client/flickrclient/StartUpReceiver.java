package com.epiccrown.flickr.client.flickrclient;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

public class StartUpReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "NIGGAASSBITCH";
    NotificationChannel channel;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("RECEIVED BROADCAST","BROADCAST FROM "+intent.getAction());
        //Toast.makeText(context,"EEEEBAAAA",Toast.LENGTH_LONG).show();
        boolean isOn = MyPreferences.getAlarmOn(context);
        PollService.setServiceAlarm(context,isOn);
//        createNotificationChannel(context);
//        createNotification(context);


    }

    private void createNotification(Context context) {
        Intent intent = new Intent(context,FlickrClient.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications_on)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(5023, mBuilder.build());
    }


    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "TestChannel";
                String description = "TestChannel";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }catch (Exception ex){ex.printStackTrace();}
    }
}
