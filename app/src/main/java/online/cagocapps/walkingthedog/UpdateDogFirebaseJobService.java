package online.cagocapps.walkingthedog;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Calvin on 3/5/2017.
 */

public class UpdateDogFirebaseJobService extends JobService {
    private AsyncTask<Void, Void, Void> mUpdateDogs;


    @Override
    public boolean onStartJob(final JobParameters job) {
        mUpdateDogs = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Context context = getApplicationContext();
                UpdateDogTask.updateDogs(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        };
        mUpdateDogs.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mUpdateDogs != null){
            mUpdateDogs.cancel(true);
        }
        return true;
    }
}
