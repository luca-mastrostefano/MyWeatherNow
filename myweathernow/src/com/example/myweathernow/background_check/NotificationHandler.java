package com.example.myweathernow.background_check;

import android.app.*;
import android.content.*;
import android.support.v4.app.*;
import com.example.myweathernow.*;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class NotificationHandler {
    private int notificationID = 1;
    private Context c;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;

    public NotificationHandler(Context c) {
        this.c = c;
        this.mBuilder = new NotificationCompat.Builder(this.c);
        this.mNotificationManager =
                (NotificationManager) this.c.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void show(String notification) {
        mBuilder.setSmallIcon(R.drawable.weather_sample);
        mBuilder.setContentText(notification);
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    public void hide() {
        mNotificationManager.cancel(notificationID);
    }


}
