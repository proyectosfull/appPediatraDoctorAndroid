package com.diazmiranda.juanjose.pediatra.firebase;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.diazmiranda.juanjose.pediatra.R;

public class MessageNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static String NOTIFICATION_TAG = "Message";
    public static String title;
    public static String text = "Un usuario ha agendado una consulta";
    public static Integer icon;

    public static void notify(final Context context, final int number, Intent intent, String canal) {
        final Resources res = context.getResources();
        final Bitmap picture = BitmapFactory.decodeResource(res, icon);
        NOTIFICATION_TAG += " " + number;
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, canal)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //.setLargeIcon(picture)
                // .setTicker(title)
                .setNumber(number)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(PendingIntent.getActivity(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT))

                // Show expanded text content on devices running Android 4.1 or
                // later.
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(title)
                        .setSummaryText("Nueva consulta recibida"))

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        notify(context, number, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, int n, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, n, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #cancel(Context)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}