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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

import online.cagocapps.walkingthedog.data.DbBitmapUtility;
import online.cagocapps.walkingthedog.data.DbHelper;
import online.cagocapps.walkingthedog.data.PetContract;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;
    private SharedPreferences shrdPrefs;
    private long defaultId;
    private long noDog = -1;
    private AdView mAdView;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();
        setContentView(R.layout.activity_main);
        MobileAds.initialize(getApplicationContext(), getString(R.string.adMob_app_id));
        mAdView = (AdView) findViewById(R.id.main_activity_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("76717E7B15FCA5425DF294B119AA246A")
                .build();
        mAdView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shrdPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        shrdPrefs.registerOnSharedPreferenceChangeListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, TrackWalk.class);
                    startActivity(intent);

            }
        });


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


        Cursor cursor = dbRead.query(
                PetContract.Achievements.TABLE_NAME,
                null,
                PetContract.Achievements.SEEN + "=?",
                new String[]{"0"},
                null,
                null,
                null
        );
        if(cursor.getCount() != 0){
            String toastText = "You have ";
            if (cursor.getCount() == 1){
                toastText = toastText + "1 new achievement! Good Job!";
            }
            else toastText = toastText + String.valueOf(cursor.getCount()) + " new achievements! Good Job!";
            Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        defaultId = shrdPrefs.getLong(getString(R.string.default_dog), noDog);
        defaultId = getIntent().getLongExtra(getResources().getString(R.string.pet_id), defaultId);

        //make user add dog if no dogs in DB
        if (defaultId == noDog){
            Intent intent = new Intent(this, AddPet.class);
            intent.putExtra(getString(R.string.pet_id), noDog);
            startActivity(intent);
        } else{
            //set up screen to show current dog stats
            setUpScreen();
        }

        //check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_dog_item) {
            Intent intent = new Intent(this, AddPet.class);
            intent.putExtra(getString(R.string.pet_id), noDog);
            startActivity(intent);
        }
        else if (id == R.id.view_dogs_item)
        {
            Intent intent = new Intent(this, ViewDogs.class);
            startActivity(intent);
        }
        else if (id == R.id.view_achievements){
            Intent intent = new Intent(this, AchievementsPage.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(getString(R.string.default_dog))){
            defaultId = shrdPrefs.getLong(getString(R.string.default_dog), noDog);
            if (defaultId == noDog){
                Intent intent = new Intent(this, AddPet.class);
                intent.putExtra(getString(R.string.pet_id), noDog);
                startActivity(intent);
            } else{
                setUpScreen();
            }
        }
    }
    public void setUpScreen(){
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
        if (cursor.moveToFirst()) {
            String petName = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.DOG_NAME));
            Long timeGoal = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL));
            Long walkGoal = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL));
            double distGoal = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL));
            double curTime = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_TIME));
            double curDist = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_DIST));
            Long curWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_WALKS));
            Long streak = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.STREAK));
            Long bestStreak = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_STREAK));
            double totWalks = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_WALKS));
            double totTimes = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_TIME));
            double totDist = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DIST));
            double totDays = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DAYS));
            double wBestTime = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_TIME));
            double dBestTime = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_TIME_DAY));
            double wBestDist = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_DIST));
            double dBestDist = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_DIST_DAY));
            double bestWalks = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_WALKS));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(PetContract.WalkTheDog.PROFILE_PIC));
            ImageView profilepic = (ImageView) findViewById(R.id.dogPic);
            if (image != null) profilepic.setImageBitmap(DbBitmapUtility.getImage(image));
            TextView tv = (TextView) findViewById(R.id.dog_name);
            tv.setText(petName + getString(R.string.main_pet_name));
            tv = (TextView) findViewById(R.id.cur_walks);
            tv.setText(Long.toString(curWalks) + " / " + Long.toString(walkGoal));
            if (curWalks >= walkGoal) {
                ImageView iv = (ImageView) findViewById(R.id.walks_star);
                iv.setImageResource(R.drawable.btn_star_big_on);
            } else {
                ImageView iv = (ImageView) findViewById(R.id.walks_star);
                iv.setImageResource(R.drawable.btn_star_big_off);
            }
            tv = (TextView) findViewById(R.id.cur_time);
            int hours = (int) curTime/60;
            int minutes = (int) curTime % 60;
            double seconds = ((((curTime) * 100) % 100 )* .6);
            int intSeconds = (int) seconds;
            String Hours = (Integer.toString(hours)+":");
            String Minutes = (Integer.toString(minutes) + ":");
            String Seconds = (Integer.toString(intSeconds));
            if (Minutes.length()==2) Minutes = "0" + Minutes;
            if (Seconds.length()==1) Seconds = "0" + Seconds;
            tv.setText(Hours+ Minutes + Seconds + " / " + Long.toString(timeGoal));
            if (curTime >= timeGoal) {
                ImageView iv = (ImageView) findViewById(R.id.time_star);
                iv.setImageResource(R.drawable.btn_star_big_on);
            } else {
                ImageView iv = (ImageView) findViewById(R.id.time_star);
                iv.setImageResource(R.drawable.btn_star_big_off);
            }
            tv = (TextView) findViewById(R.id.cur_dist);
            tv.setText(String.format("%.2f", curDist) + " / " + Double.toString(distGoal));
            if (curDist >= distGoal) {
                ImageView iv = (ImageView) findViewById(R.id.dist_star);
                iv.setImageResource(R.drawable.btn_star_big_on);
            } else {
                ImageView iv = (ImageView) findViewById(R.id.dist_star);
                iv.setImageResource(R.drawable.btn_star_big_off);
            }
            tv = (TextView) findViewById(R.id.cur_streak);
            tv.setText(Long.toString(streak));
            tv = (TextView) findViewById(R.id.best_streak_val);
            tv.setText(Long.toString(bestStreak));
            tv = (TextView) findViewById(R.id.all_time_walks_val);
            tv.setText(String.format("%.0f", totWalks));
            tv = (TextView) findViewById(R.id.all_time_time_val);
            hours = (int) totTimes/60;
            minutes = (int) totTimes % 60;
            seconds = ((((totTimes) * 100) % 100 )* .6);
            intSeconds = (int) seconds;
            Hours = (Integer.toString(hours)+":");
            Minutes = (Integer.toString(minutes) + ":");
            Seconds = (Integer.toString(intSeconds));
            if (Minutes.length()==2) Minutes = "0" + Minutes;
            if (Seconds.length()==1) Seconds = "0" + Seconds;
            tv.setText(Hours+Minutes+Seconds);
            tv = (TextView) findViewById(R.id.all_time_dist_val);
            tv.setText(String.format("%.2f", totDist));
            tv = (TextView) findViewById(R.id.best_walks_val);
            tv.setText(String.format("%.0f", bestWalks));
            tv = (TextView) findViewById(R.id.best_time_val);
            hours = (int) dBestTime/60;
            minutes = (int) dBestTime % 60;
            seconds = ((((dBestTime) * 100) % 100 )* .6);
            intSeconds = (int) seconds;
            Hours = (Integer.toString(hours)+":");
            Minutes = (Integer.toString(minutes) + ":");
            Seconds = (Integer.toString(intSeconds));
            if (Minutes.length()==2) Minutes = "0" + Minutes;
            if (Seconds.length()==1) Seconds = "0" + Seconds;
            tv.setText(Hours+Minutes+Seconds);
            tv = (TextView) findViewById(R.id.best_time_wk_val);
            hours = (int) wBestTime/60;
            minutes = (int) wBestTime % 60;
            seconds = ((((wBestTime) * 100) % 100 )* .6);
            intSeconds = (int) seconds;
            Hours = (Integer.toString(hours)+":");
            Minutes = (Integer.toString(minutes) + ":");
            Seconds = (Integer.toString(intSeconds));
            if (Minutes.length()==2) Minutes = "0" + Minutes;
            if (Seconds.length()==1) Seconds = "0" + Seconds;
            tv.setText(Hours+Minutes+Seconds);
            tv = (TextView) findViewById(R.id.best_dist_val);
            tv.setText(String.format("%.2f", dBestDist));
            tv = (TextView) findViewById(R.id.best_dist_wk_val);
            tv.setText(String.format("%.2f", wBestDist));
            tv = (TextView) findViewById(R.id.ave_time_walk_val);
            if (totWalks != 0){
                hours = (int) (totTimes / totWalks)/60;
                minutes = (int) (totTimes / totWalks) % 60;
                seconds = ((((totTimes / totWalks) * 100) % 100 )* .6);
                intSeconds = (int) seconds;
                Hours = (Integer.toString(hours)+":");
                Minutes = (Integer.toString(minutes) + ":");
                Seconds = (Integer.toString(intSeconds));
                if (Minutes.length()==2) Minutes = "0" + Minutes;
                if (Seconds.length()==1) Seconds = "0" + Seconds;
                tv.setText(Hours+Minutes+Seconds);
            }
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_dist_walk_val);
            if (totWalks != 0.0) tv.setText(String.format("%.2f", (totDist / totWalks)));
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_walk_day_value);
            if (totDays != 0.0) tv.setText(String.format("%.2f", totWalks / totDays));
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_time_day_value);
            if (totDays != 0.0){
                hours = (int) (totTimes / totDays)/60;
                minutes = (int) (totTimes / totDays) % 60;
                seconds = (((((totTimes / totDays)) * 100) % 100 )* .6);
                intSeconds = (int) seconds;
                Hours = (Integer.toString(hours)+":");
                Minutes = (Integer.toString(minutes) + ":");
                Seconds = (Integer.toString(intSeconds));
                if (Minutes.length()==2) Minutes = "0" + Minutes;
                if (Seconds.length()==1) Seconds = "0" + Seconds;
                tv.setText(Hours+Minutes+Seconds);
            }
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_dist_day_value);
            if (totDays != 0.0) tv.setText(String.format("%.2f", (totDist / totDays)));
            else tv.setText("0");
            tv = (TextView) findViewById(R.id.ave_mph_value);
            double math;
            double time = totTimes;
            if (totTimes != 0) {
                math = (totDist / (time / 60));
                if (math < .01) math = 0;
            }
            else math = 0;
            Log.d("mainactivity", Double.toString(math));
            tv.setText(String.format("%.2f", math));
        }else {
            shrdPrefs.edit().putString(getString(R.string.default_walks_dog),null)
                    .putLong(getString(R.string.default_dog), -1).commit();
            onStart();
        }
        cursor.close();
    }

    public void editDog(View view){
        Intent intent = new Intent(this, AddPet.class);
        intent.putExtra(getString(R.string.pet_id), defaultId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
