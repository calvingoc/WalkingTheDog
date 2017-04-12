package online.cagocapps.walkingthedog.notifications;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.sql.Driver;
import java.util.concurrent.TimeUnit;

/**
 * Created by Calvin on 4/12/2017.
 */

public class ReminderUtilities {
    private static final int REMINDER_INTERVAL_MINUTES = 15;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;
    private static final String REMINDER_JOB_TAG = "on_walk_reminder_tag";
    private static boolean sInitialized;

    synchronized public static void  schedulteWalkReminder(@NonNull final Context context){
        if (sInitialized) return;
        com.firebase.jobdispatcher.Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(WalkReminderFirebaseJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS, REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(constraintReminderJob);
        sInitialized = true;
    }

    synchronized public static void endWalkReminders(@NonNull final Context context){
        if (sInitialized){
            com.firebase.jobdispatcher.Driver driver = new GooglePlayDriver(context);
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
            dispatcher.cancel(REMINDER_JOB_TAG);
            sInitialized = false;
        }
    }
}
