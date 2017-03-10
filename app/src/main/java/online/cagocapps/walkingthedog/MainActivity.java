package online.cagocapps.walkingthedog;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

import online.cagocapps.walkingthedog.data.DbBitmapUtility;
import online.cagocapps.walkingthedog.data.DbHelper;
import online.cagocapps.walkingthedog.data.PetContract;
/*
* Main Class of Walking the Dog
* Author: Calvin Gehred-O'Connell
* This class sets up the main page by pulling and displaying the stats for the default dog.
* It also kicks off the up update alarm that refreshes dog stats once a day.
* */
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
//    set up local instance of SQL tables
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;

//    set up for finding the default dog to display
    private SharedPreferences shrdPrefs;
    private long defaultId;
    private long noDog = -1;

//    set up for showing ads
    private AdView mAdView;

// set up for alarm to refresh dog stats
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    /*
    * onCreate
    * Grabs a local instance of the database
    * sets up the adds
    * set up alarm for refresh
    * activate the floating action button
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        set up local database
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();
        setContentView(R.layout.activity_main);

//        set up ads
        MobileAds.initialize(getApplicationContext(), getString(R.string.adMob_app_id));
        mAdView = (AdView) findViewById(R.id.main_activity_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("76717E7B15FCA5425DF294B119AA246A")
                .build();
        mAdView.loadAd(adRequest);

//        build screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//      set up shared preference change listener to display the current default dog
        shrdPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        shrdPrefs.registerOnSharedPreferenceChangeListener(this);

//        set up FAB to start a walk when clicked
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TrackWalk.class);
                startActivity(intent);
            }
        });

//      Set up the daily alarm to reset the daily stats and save them to the lifetime numbers
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,1);
        alarmMgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, DogUpdateReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,alarmIntent);
        ComponentName receiver = new ComponentName(MainActivity.this, DogUpdateReceiver.class);
        PackageManager pm = MainActivity.this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);



    }
/*
* onStart
* requests permission for GPS use.
 * If no default dog set up automatically brings user to the add dog activity
 * if there is a default dog sets up the screen by displaying the current dog's stats
* */
    @Override
    protected void onStart() {
        super.onStart();
        //check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        defaultId = shrdPrefs.getLong(getString(R.string.default_dog), noDog);
        defaultId = getIntent().getLongExtra(getResources().getString(R.string.pet_id), defaultId);

        //make user add dog if no dogs in DB
        if (defaultId == noDog){
            Intent intent = new Intent(this, AddPet.class);
            intent.putExtra(getString(R.string.pet_id), noDog); //add dummy -1 ID to intent so the activity knows we want to add a dog
            startActivity(intent);
        } else{
            //set up screen to show current dog stats
            setUpScreen();
        }
    }
/*
* onCreateOptionsMenu
* just inflates our menu.
* */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
/*
* onOptionsItemSelected
* Checks which option was selected and either adds a new dog or shows user a list of current dogs.
* */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //adds a new dog
        if (id == R.id.add_dog_item) {
            Intent intent = new Intent(this, AddPet.class);
            intent.putExtra(getString(R.string.pet_id), noDog); // puts dummy id in so a new dog is made
            startActivity(intent);
        }
        else if (id == R.id.view_dogs_item) // view dog list
        {
            Intent intent = new Intent(this, ViewDogs.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
/*
* onSharedPreferenceChanged
* makes sure to update screen if a new default dog is selected
* */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(getString(R.string.default_dog))){ //the default dog has been changed
            defaultId = shrdPrefs.getLong(getString(R.string.default_dog), noDog); //sets defaultId variable to the new default dog
            if (defaultId == noDog){ //all dogs have been deleted and must prompt user to make a new dog
                Intent intent = new Intent(this, AddPet.class);
                intent.putExtra(getString(R.string.pet_id), noDog);
                startActivity(intent);
            } else{
                setUpScreen(); //sets up the screen for new dog.
            }
        }
    }

    /*
    * setUpScreen
    * sets up the screen to show profile for the selected dog.
    * */
    public void setUpScreen(){
        //set up database query
        String where = PetContract.WalkTheDog._ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(defaultId)};

        Cursor cursor = dbRead.query(
                PetContract.WalkTheDog.TABLE_NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {// set local variables for dog stats if dog found
            String petName = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.DOG_NAME));
            Long timeGoal = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL));
            Long walkGoal = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL));
            float distGoal = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL));
            float curTime = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_TIME));
            float curDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_DIST));
            Long curWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_WALKS));
            Long streak = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.STREAK));
            Long bestStreak = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_STREAK));
            float totWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_WALKS));
            float totTimes = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_TIME));
            float totDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DIST));
            float totDays = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DAYS));
            float wBestTime = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_TIME));
            float dBestTime = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_TIME_DAY));
            float wBestDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_DIST));
            float dBestDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_DIST_DAY));
            float bestWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_WALKS));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(PetContract.WalkTheDog.PROFILE_PIC));
            ImageView profilepic = (ImageView) findViewById(R.id.dogPic);
            //profile picture stored as a byte[] in table to must be switched back to bitmap
            if (image != null) profilepic.setImageBitmap(DbBitmapUtility.getImage(image));

            //start setting view with correct data
            TextView tv = (TextView) findViewById(R.id.dog_name);
            tv.setText(petName + getString(R.string.main_pet_name)); // set up dog name header
            tv = (TextView) findViewById(R.id.cur_walks);
            tv.setText(Long.toString(curWalks) + " / " + Long.toString(walkGoal)); //show current walks
            if (curWalks >= walkGoal) { //pick which start to display if goal is met
                ImageView iv = (ImageView) findViewById(R.id.walks_star);
                iv.setImageResource(R.drawable.btn_star_big_on);
            } else {
                ImageView iv = (ImageView) findViewById(R.id.walks_star);
                iv.setImageResource(R.drawable.btn_star_big_off);
            }
            tv = (TextView) findViewById(R.id.cur_time); //show current time
            tv.setText(String.format("%.2f", curTime) + " / " + Long.toString(timeGoal));
            if (curTime >= timeGoal) {//pick which start to display if goal is met
                ImageView iv = (ImageView) findViewById(R.id.time_star);
                iv.setImageResource(R.drawable.btn_star_big_on);
            } else {
                ImageView iv = (ImageView) findViewById(R.id.time_star);
                iv.setImageResource(R.drawable.btn_star_big_off);
            }
            tv = (TextView) findViewById(R.id.cur_dist);//current distance
            tv.setText(String.format("%.2f", curDist) + " / " + Float.toString(distGoal));
            if (curDist >= distGoal) {//pick which start to display if goal is met
                ImageView iv = (ImageView) findViewById(R.id.dist_star);
                iv.setImageResource(R.drawable.btn_star_big_on);
            } else {
                ImageView iv = (ImageView) findViewById(R.id.dist_star);
                iv.setImageResource(R.drawable.btn_star_big_off);
            }
            tv = (TextView) findViewById(R.id.cur_streak); //current streak
            tv.setText(Long.toString(streak));
            tv = (TextView) findViewById(R.id.best_streak_val);// best streak
            tv.setText(Long.toString(bestStreak));
            tv = (TextView) findViewById(R.id.all_time_walks_val);//all time walks
            tv.setText(String.format("%.0f", totWalks));
            tv = (TextView) findViewById(R.id.all_time_time_val);//all time time
            tv.setText(String.format("%.2f", totTimes));
            tv = (TextView) findViewById(R.id.all_time_dist_val);//all time distance
            tv.setText(String.format("%.2f", totDist));
            tv = (TextView) findViewById(R.id.best_walks_val);//best walks in a day
            tv.setText(String.format("%.0f", bestWalks));
            tv = (TextView) findViewById(R.id.best_time_val);// best time in a day
            tv.setText(String.format("%.2f", dBestTime));
            tv = (TextView) findViewById(R.id.best_time_wk_val);//best time in a walk
            tv.setText(String.format("%.2f", wBestTime));
            tv = (TextView) findViewById(R.id.best_dist_val);//best distance in a day
            tv.setText(String.format("%.2f", dBestDist));
            tv = (TextView) findViewById(R.id.best_dist_wk_val);//best distance in a walk
            tv.setText(String.format("%.2f", wBestDist));
            tv = (TextView) findViewById(R.id.ave_time_walk_val); //average time per walk
            if (totWalks != 0) tv.setText(String.format("%.2f", totTimes / totWalks));
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_dist_walk_val); //average dist per walk
            if (totWalks != 0.0) tv.setText(String.format("%.2f", (totDist / totWalks)));
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_walk_day_value); // average walk per day
            if (totDays != 0.0) tv.setText(String.format("%.2f", totWalks / totDays));
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_time_day_value); // average time per day
            if (totDays != 0.0) tv.setText(String.format("%.2f", totTimes / totDays));
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_dist_day_value);//average distance per day
            if (totDays != 0.0) tv.setText(String.format("%.2f", (totDist / totDays)));
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_mph_value); //average mph
            float math;
            float time = (float) totTimes;
            if (totTimes != 0) {
                math = (totDist / (time / 60));
                if (math < .01) math = 0;
            }
            else math = 0;
            tv.setText(String.format("%.2f", math));
        }else { // no dog found reset shared preferences with dummy ID and prompt for new dog
            shrdPrefs.edit().putString(getString(R.string.default_walks_dog),null)
                    .putLong(getString(R.string.default_dog), -1).commit();
            onStart();
        }
        cursor.close();
    }
/*
* editDog
* Activated by clicking the update dog button, starts the edit dog activity and passes appropriate ID
* */
    public void editDog(View view){
        Intent intent = new Intent(this, AddPet.class);
        intent.putExtra(getString(R.string.pet_id), defaultId);
        startActivity(intent);
    }

}
