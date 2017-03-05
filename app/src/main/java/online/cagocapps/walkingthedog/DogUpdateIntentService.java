package online.cagocapps.walkingthedog;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Calvin on 3/5/2017.
 */

public class DogUpdateIntentService extends IntentService {

    public  DogUpdateIntentService(){super("DogUpdateIntentService");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        UpdateDogTask.updateDogs(this);
    }
}
