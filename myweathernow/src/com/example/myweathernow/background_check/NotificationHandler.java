package com.example.myweathernow.background_check;

import android.app.*;
import android.content.*;
import android.support.v4.app.*;
import com.example.myweathernow.*;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class NotificationHandler {
    private int notificationID; // l'id dell'utente?
    private Context c;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    public NotificationHandler(Context c, int notificationID) {
        this.notificationID = notificationID;
        this.c = c;
        this.mBuilder = new NotificationCompat.Builder(this.c);
        this.mNotificationManager =
                (NotificationManager) this.c.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void show(String notification) {
        mBuilder.setSmallIcon(R.drawable.weather_sample);
        mBuilder.setContentTitle("MyWeatherNow Notification");
        mBuilder.setContentText(notification);
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public void update(String notification) {
        mBuilder.setContentTitle("MyWeatherNow Notification");
        mBuilder.setContentText(notification);
        mBuilder.setSmallIcon(R.drawable.weather_sample);
        // Because the ID remains unchanged, the existing notification is
        // updated.
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public void hide() {
        mNotificationManager.cancel(notificationID);
    }


}
