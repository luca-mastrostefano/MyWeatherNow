package com.example.myweathernow.background_check;

import android.app.*;
import android.content.*;
import com.example.myweathernow.*;
import com.example.myweathernow.persistency.*;
import com.example.myweathernow.util.*;

import java.util.*;

/**
 * Created by lucamastrostefano on 04/01/15.
 */
public class NotificationHandler {
    private int notificationID = 1;
    private Context c;
    private Notification.Builder mBuilder;
    private NotificationManager mNotificationManager;

    public NotificationHandler(Context c) {
        this.c = c;
        this.mBuilder = new Notification.Builder(this.c);
        this.mNotificationManager =
                (NotificationManager) this.c.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void show(WeatherManager weatherManager) {
        if (weatherManager != null) {
            Map.Entry<PhraseMaker.Slot, String> suggestion = PhraseMaker.getPhrase(weatherManager, APIManager.Day.TODAY);
            PhraseMaker.Slot phase = suggestion.getKey();
            if (phase.ordinal() > PhraseMaker.Slot.VERY_LOW.ordinal()) {
                mBuilder.setSmallIcon(R.drawable.open_umbrella_color);
                mBuilder.setContentTitle("UmbrellApp");
                mBuilder.setStyle(new Notification.BigTextStyle().bigText(suggestion.getValue()));
                mBuilder.setContentText(suggestion.getValue());
                Intent openAppIntent = new Intent(this.c, UmbrellAppHome.class);
                PendingIntent openAppPendingIntent = PendingIntent.getActivity(this.c, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(openAppPendingIntent);
                mBuilder.setOngoing(true);
                mNotificationManager.notify(notificationID, mBuilder.build());
            } else {
                hide();
            }
        }
    }

    public void hide() {
        mNotificationManager.cancel(notificationID);
    }


}
