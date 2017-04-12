package online.cagocapps.walkingthedog.notifications;

import android.content.Context;

import online.cagocapps.walkingthedog.TrackWalk;

/**
 * Created by Calvin on 4/12/2017.
 */

public class ReminderTasks {
    public static final String ACTION_DISMISS = "dismiss-walk-reminder";
    public static final String END_WALK_TASK = "end-walk-task";
    static final String ON_WALK_REMINDER = "on-walk-reminder";

    public static void executeTask(Context context, String action){
        if (ACTION_DISMISS.equals(action) || END_WALK_TASK.equals(action)){
            NotificationUtils.clearNotification(context);
        } else if (action.equals(ON_WALK_REMINDER)){
            NotificationUtils.remindUserOnWalk(context);
        }

    }

}
