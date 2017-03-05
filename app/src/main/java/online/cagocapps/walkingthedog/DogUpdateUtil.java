package online.cagocapps.walkingthedog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Calvin on 3/5/2017.
 */

public class DogUpdateUtil {
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;

    private static final String UPDATE_DOG_TAG = "updateDogs";

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context){
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncDogs = dispatcher.newJobBuilder()
                .setService(UpdateDogFirebaseJobService.class)
                .setTag(UPDATE_DOG_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS, SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(syncDogs);
    }


    synchronized public static void initialize(@NonNull final Context context){
        if (sInitialized) return;
        sInitialized = true;
        scheduleFirebaseJobDispatcherSync(context);
    }
}
