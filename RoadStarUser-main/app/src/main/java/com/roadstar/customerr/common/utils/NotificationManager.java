package com.roadstar.customerr.common.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.roadstar.customerr.BuildConfig;
import com.roadstar.customerr.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static android.media.RingtoneManager.getDefaultUri;

/**
 * Created by bilal on 26/01/2018.
 */

public class NotificationManager {

    public static final int NOTIFICATION_ID = 1010;
    public static final String NOTIFICATION_CHANNEL = "mbv4_channel";

    private static final String NOTIFICATION_GROUP = "mbv4_group";
    private static final int SUMMARY_NOTIFICATION_ID = 1111;

    public static void createNotification(Context context, String event, String jobId,
                                          String mTitle, String mBody, String data) {

        String title = AppUtils.ifNotNullEmpty(mTitle) ? mTitle : context.getString(R.string.app_name);
        String body = AppUtils.ifNotNullEmpty(mBody) ? mBody : "You have a new notification";

        Intent intent;
//        if (!SharedPreferenceManager.getInstance().read(PreferenceUtils.COOKIE, "").equalsIgnoreCase("")) {
//            if (SharedPreferenceManager.getInstance().read(PreferenceUtils.IS_LOGGED_IN, false))
//                if (UserManager.getMoverType().equalsIgnoreCase(UserManager.TYPE_COMPANY))
//                    intent = new Intent(context, MainActivity_Merchant.class);
//                else
//                    intent = new Intent(context, MainActivity.class);
//            else
//                intent = new Intent(context, SplashActivity.class);
//        } else
//            intent = new Intent(context, SplashActivity.class);
//        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra(SocketService.BROADCAST_INTENT_TYPE, event);
//        intent.putExtra(SocketService.BROADCAST_INTENT_DATA, data);
//        intent.putExtra(SocketService.BROADCAST_JOB_ID, jobId);
//        intent.putExtra(AppConstants.FROM_WHICH_COMPONENT, AppConstants.COMPONENT_GCM);

//        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
//
//        PendingIntent pendingIntent = getActivity(context, uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        createNotification(context, pendingIntent, title, body, jobId);

    }

    public static void createNotification(Context context, PendingIntent intent, String title, String body, String jobID) {
        if (intent == null) {
            int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
            intent = PendingIntent.getActivity(context, uniqueInt,
                    new Intent(), // add this
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Uri defaultSoundUri = getDefaultUri(TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, BuildConfig.APPLICATION_ID);
        notificationBuilder.setSmallIcon(getNotificationIcon(context, notificationBuilder)).setContentTitle(title)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentText(body).setAutoCancel(true)
                .setGroup(NOTIFICATION_GROUP)
                .setPriority(android.app.NotificationManager.IMPORTANCE_HIGH)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setSound(defaultSoundUri).setContentIntent(intent);
//                .setNumber(SharedPreferenceManager.getSharedPreferenceInstance(context).read(ZemCarConsts.PREF_ZEM_NOT_COUNT, 0));
        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(getNotificationChannel(context));
        notificationBuilder.setAutoCancel(true);
        notificationManager.notify(createNotificationId(jobID), notificationBuilder.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Notification summaryNotification =
                    new NotificationCompat.Builder(context, BuildConfig.APPLICATION_ID).setSmallIcon(getNotificationIcon(context, notificationBuilder)).setContentTitle(title)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setContentTitle(context.getString(R.string.app_name) + " Notification(s)")
                            //set content text to support devices running API level < 24
                            .setContentText("You have new notifications")
                            //build summary info into InboxStyle template
                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                            //specify which group this notification belongs to
                            .setGroup(NOTIFICATION_GROUP)
                            //set this notification as the summary for the group
                            .setGroupSummary(true)
                            .setAutoCancel(true)
                            .build();
            notificationManager.notify(SUMMARY_NOTIFICATION_ID, summaryNotification);
        }
    }

    private static int getNotificationIcon(Context context, NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            return R.drawable.ic_launcher_background;

        } else {
            return R.mipmap.ic_launcher;
        }
    }

    private static NotificationChannel getNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return null;
        CharSequence name = context.getString(R.string.app_name);// The user-visible name of the channel.
        int importance = android.app.NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(BuildConfig.APPLICATION_ID, name, importance);
        notificationChannel.setShowBadge(true);
        return notificationChannel;
    }

    private static int createNotificationId(String jobId) {
        if (!AppUtils.ifNotNullEmpty(jobId))
            return NOTIFICATION_ID;
        if (jobId.length() > 12) {
            try {
                String subString = jobId.substring(jobId.length() - 10);
                int id = 0;
                for (int a : subString.toCharArray()) {
                    id += a;
                }
                return id;
            } catch (Exception e) {
                return NOTIFICATION_ID;
            }
        } else
            return NOTIFICATION_ID;
    }
}
