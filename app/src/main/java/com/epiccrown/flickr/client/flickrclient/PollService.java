package com.epiccrown.flickr.client.flickrclient;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.List;


public class PollService extends IntentService {

    private static final long POLL_INTERVAL = 1000 * 60;

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public PollService() {
        super("PollService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected()) return;


        String lastResultId = MyPreferences.getLastResultId(this);
        List<GalleryItem> items;

        items = new URLRequestToString().fetchRecentPhotos();

        if (items.size() == 0) {
            return;
        }
        String resultId = items.get(0).getmId();
        if (!resultId.equals(lastResultId)) {
            Intent i = FlickrClient.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
            String CHANNEL_ID = "polling_service_flickr";// The id of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            Notification notification;
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "Flickr Client", importance);
                 notification = new NotificationCompat.Builder(this)
                        .setTicker("Check out new photos on Flickr!")
                        .setSmallIcon(R.drawable.new_photos)
                        .setContentTitle("FlickrClient")
                        .setContentText("Check out new photos on Flickr!")
                        .setContentIntent(pi)
                         .setChannelId(CHANNEL_ID)
                        .setAutoCancel(true)
                        .build();
                mNotificationManager.createNotificationChannel(mChannel);
            }else{
                 notification = new NotificationCompat.Builder(this)
                        .setTicker("Check out new photos on Flickr!")
                        .setSmallIcon(R.drawable.new_photos)
                        .setContentTitle("FlickrClient")
                        .setContentText("Check out new photos on Flickr!")
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();
            }


            mNotificationManager.notify(0, notification);

        }
        MyPreferences.setLastResultId(this, resultId);

    }

    private boolean isNetworkAvailableAndConnected() {

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

}
