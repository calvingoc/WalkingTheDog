package online.cagocapps.walkingthedog.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import online.cagocapps.walkingthedog.R;
import online.cagocapps.walkingthedog.TrackWalk;

/**
 * Created by Calvin on 4/12/2017.
 */

public class NotificationUtils {
    private static final int ON_WALK_NOTIFICATION_ID = 1138;
    private static final int ON_WALK_PENDING_INTENT = 1223123;
    private static final int ACTION_IGNORE_NOTIFICATION_ID = 123999;

    public static void clearNotification(Context context){
        NotificationManager notManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notManager.cancel(ON_WALK_NOTIFICATION_ID);
    }

    public static void remindUserOnWalk(Context context){
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_pets_black_24dp)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text))
                .setContentIntent(contentIntent(context))
                .addAction(ignoreReminderAction(context))
                .addAction(endWalkAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notBuilder.setPriority(Notification.PRIORITY_HIGH);
        }
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(ON_WALK_NOTIFICATION_ID, notBuilder.build());
    }

    private static NotificationCompat.Action endWalkAction(Context context){
        Intent endIntent = new Intent(context, TrackWalk.class);
        endIntent.setAction(ReminderTasks.END_WALK_TASK);
        PendingIntent endPendingIntent = PendingIntent.getActivity(context,
                ON_WALK_PENDING_INTENT,
                endIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action ignoreAction = new NotificationCompat.Action(R.drawable.ic_pets_black_24dp,
                "No",
                endPendingIntent);
        return ignoreAction;
    }

    private static NotificationCompat.Action ignoreReminderAction(Context context){
        Intent ignoreIntent = new Intent(context, WalkReminderIntentService.class);
        ignoreIntent.setAction(ReminderTasks.ACTION_DISMISS);
        PendingIntent ignorePendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_NOTIFICATION_ID,
                ignoreIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action ignoreAction = new NotificationCompat.Action(R.drawable.ic_cancel_black_24px,
                "Yes, keep walking",
                ignorePendingIntent);
        return ignoreAction;
    }

    private static PendingIntent contentIntent(Context context){
        Intent startActivityIntent = new Intent(context, TrackWalk.class);
        return PendingIntent.getActivity(context,
                ON_WALK_PENDING_INTENT,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context){
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res,R.drawable.app_thumbnail);
    }
}
