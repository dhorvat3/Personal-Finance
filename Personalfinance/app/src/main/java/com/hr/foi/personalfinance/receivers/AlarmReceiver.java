package com.hr.foi.personalfinance.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.hr.foi.personalfinance.MainActivity;
import com.hr.foi.personalfinance.R;

/**
 * Created by Blaža on 29.1.2017..
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notificationBuilder = new Notification.Builder(context);
        String newLine = System.getProperty("line.separator");
        String notificationTitle = intent.getStringExtra("notificationTitle");
        String notificationMessage = intent.getStringExtra("notificationMessage");
        String notificationDate = intent.getStringExtra("notificationDate");
        Intent redirectIntent = new Intent(context, MainActivity.class);

        redirectIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        redirectIntent.putExtra("redirect", "TasksFragment");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), redirectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        notificationBuilder.setContentTitle(notificationTitle);
        notificationBuilder.setContentText(notificationMessage);
        notificationBuilder.setStyle(new Notification.BigTextStyle().bigText(notificationMessage + newLine + newLine + notificationDate));
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
