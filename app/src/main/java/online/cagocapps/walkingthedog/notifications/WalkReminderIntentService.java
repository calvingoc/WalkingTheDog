package online.cagocapps.walkingthedog.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Calvin on 4/12/2017.
 */

public class WalkReminderIntentService extends IntentService {
    public  WalkReminderIntentService(){super("WalkReminderIntentService");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ReminderTasks.executeTask(this, intent.getAction());
    }
}
