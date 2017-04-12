package online.cagocapps.walkingthedog.notifications;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Calvin on 4/12/2017.
 */

public class WalkReminderFirebaseJobService extends JobService {
    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Context context = WalkReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ON_WALK_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        mBackgroundTask.execute();
        return true;
    }
}
