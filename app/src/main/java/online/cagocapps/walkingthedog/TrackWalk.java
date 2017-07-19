package online.cagocapps.walkingthedog;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import online.cagocapps.walkingthedog.data.Achievements;
import online.cagocapps.walkingthedog.data.DbBitmapUtility;
import online.cagocapps.walkingthedog.data.DbHelper;
import online.cagocapps.walkingthedog.data.PetContract;
import online.cagocapps.walkingthedog.notifications.NotificationUtils;
import online.cagocapps.walkingthedog.notifications.ReminderUtilities;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Class sets up the Tracking walk screen and lays the basework for timing the walk, tracking the walk with GPS and getting distance.
public class TrackWalk extends AppCompatActivity implements DogAdapter.DogAdapterOnClickHandler,
        OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener{
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;
    private RecyclerView rvDogList;
    private DogAdapter dogAdapter;
    private GoogleApiClient apiC;
    private Location mLastLocation;
    private Chronometer timer;
    private Long[] dogIDs;
    private String[] dogNames;
    private String[] dogIDsOnWalk;
    private String dogsOnWalkString;
    private String tempDogString;
    private SharedPreferences shrdPrefs;
    private float distance = 0;
    private TextView distanceDisplay;
    private GoogleMap map;
    private LocationRequest locationRequest;
    private Marker marker;
    private AdView mAdView;
    private int locationDelay = 0;
    private int streak;
    private int locationCount = 0;
    public ProgressBar mProgressBar;
    public Button finalizeWalk;
    public RecyclerView popUpRecyc;
    public Context mContext;
    private PopupWindow mPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_walk);

        mContext = this;

        //Setting up Chronometer
        timer = (Chronometer) findViewById(R.id.cur_time_val);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        //find what dogs should be defaulted in.
        shrdPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        dogsOnWalkString = shrdPrefs.getString(getString(R.string.default_walks_dog), null);

        //notifications
        ReminderUtilities.schedulteWalkReminder(this);

        //set up ads
        mAdView = (AdView) findViewById(R.id.track_walk_adview);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("76717E7B15FCA5425DF294B119AA246A")
                .build();
        mAdView.loadAd(adRequest);

        distanceDisplay = (TextView) findViewById(R.id.distance_display);
        //Setting up RecyclerView
        rvDogList = (RecyclerView) findViewById(R.id.dogWalkRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvDogList.setLayoutManager(layoutManager);
        rvDogList.setHasFixedSize(true);
        dogAdapter = new DogAdapter(this);
        rvDogList.setAdapter(dogAdapter);


        //Setting up the GPS fragment with api client
        if (apiC == null){
            apiC = new GoogleApiClient.Builder(this).addConnectionCallbacks(TrackWalk.this).
                    addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).
                setInterval(10000).setFastestInterval(1000).setMaxWaitTime(15000);

    }

    @Override
    public void onClick(Long petID) {
        return;
    }


    @Override
    protected void onResume() {
        apiC.connect();
        super.onResume();

        dogAdapter.notifyDataSetChanged();

        //Setting up DB
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();

        //Grabing the default dogs to include on a walk
        String[] columns = new String[]{PetContract.WalkTheDog.DOG_NAME,
                PetContract.WalkTheDog._ID,
                PetContract.WalkTheDog.TIME_GOAL,
                PetContract.WalkTheDog.WALKS_GOAL,
                PetContract.WalkTheDog.TIME_GOAL,
                PetContract.WalkTheDog.CUR_DIST,
                PetContract.WalkTheDog.CUR_TIME,
                PetContract.WalkTheDog.CUR_WALKS,
                PetContract.WalkTheDog.DIST_GOAL,
                PetContract.WalkTheDog.PROFILE_PIC};
        tempDogString = shrdPrefs.getString(getString(R.string.dogs_on_walk), null);
        if (tempDogString != null) dogsOnWalkString = tempDogString;
        shrdPrefs.edit().putString(getString(R.string.dogs_on_walk), null).commit();
        if (dogsOnWalkString != null) {
            dogIDsOnWalk = dogsOnWalkString.split(" ");
            String where = PetContract.WalkTheDog._ID + " IN ("
                    + makePlaceholders( dogIDsOnWalk.length) + ")";
            Cursor cursor = dbRead.query(
                    PetContract.WalkTheDog.TABLE_NAME,
                    columns,
                    where,
                    dogIDsOnWalk,
                    null,
                    null,
                    null
            );
            int i = 0;
            dogIDs = new Long[cursor.getCount()];
            dogNames = new String[cursor.getCount()];
            Long[] curWalks = new Long[cursor.getCount()];
            double[] curTime = new double[cursor.getCount()];
            Float[] curDist = new Float[cursor.getCount()];
            Long[] goalWalks = new Long[cursor.getCount()];
            double[] goalTime = new double[cursor.getCount()];
            Float[] goalDist = new Float[cursor.getCount()];
            Bitmap[] images = new Bitmap[cursor.getCount()];
            while (cursor.moveToNext()) {
                dogIDs[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
                dogNames[i] = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.DOG_NAME));
                curWalks[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_WALKS));
                curTime[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_TIME));
                curDist[i] = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_DIST));
                goalWalks[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL));
                goalTime[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL));
                goalDist[i] = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL));
                images[i] = DbBitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(PetContract.WalkTheDog.PROFILE_PIC)));
                i++;
            }
            dogAdapter.setDogsList(dogIDs, dogNames, curWalks, curTime, curDist,
                    goalWalks, goalTime, goalDist, images);
            cursor.close();
            dbRead.close();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (mLastLocation != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),
                    mLastLocation.getLongitude())));
        }
    }
    //gets last know location
    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(apiC);
            LocationServices.FusedLocationApi.requestLocationUpdates(apiC, locationRequest, this);
        }
    }
    //not currently used
    @Override
    public void onConnectionSuspended(int i) {
        return;
    }
    //not currently used
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        return;
    }
    //disconnect the api

    public void editDogsOnList(View view){
        Intent intent = new Intent(view.getContext(), EditDogsOnWalk.class);
        intent.putExtra(getString(R.string.dogs_on_walk), dogsOnWalkString);

        startActivity(intent);
    }
    //makes the correct amount of ?s for the sql query
    private String makePlaceholders(int len){
        StringBuilder sb = new StringBuilder(len*2 -1);
        sb.append("?");
        for (int i = 1; i < len; i++){
            sb.append(",?");
        }
        return sb.toString();
    }

    public void launchPopup(View view){
        timer.stop();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.walk_summary, null);
        mPopupWindow = new PopupWindow(customView, RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        mPopupWindow.setElevation(5.0f);
        TextView timeSummary = (TextView) customView.findViewById(R.id.time_title);
        TextView distanceSummary = (TextView) customView.findViewById(R.id.distance_title);
        TextView mphSummary = (TextView) customView.findViewById(R.id.mph_title);
        popUpRecyc = (RecyclerView) customView.findViewById(R.id.popup_achievments_rv);
        finalizeWalk = (Button) customView.findViewById(R.id.popup_save_button);
        mProgressBar = (ProgressBar) customView.findViewById(R.id.progressBar2);
        finalizeWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endWalk(v);
            }
        });
        mPopupWindow.showAtLocation(findViewById(R.id.constraintLayout), Gravity.CENTER,0,0);
        long ElapsedTime = SystemClock.elapsedRealtime() - timer.getBase();
        double elaspedTimeFloat =  ElapsedTime / 60000.0;
        timeSummary.setText("You walked for " + String.format("%.2f", elaspedTimeFloat) + " minutes!");
        distanceSummary.setText("You went " + String.format("%.2f", distance) + " miles!");
        if (elaspedTimeFloat != 0){
            mphSummary.setText("That is " + String.format("%.2f", Math.round(distance) / (elaspedTimeFloat / 60)) + " mph!");
        }
        String time[] = {String.format("%.2f",elaspedTimeFloat)};
        new UpdateDogs().execute(time);


    }

    private class  UpdateDogs extends AsyncTask<String,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setEnabled(true);
            finalizeWalk.setEnabled(false);

        }

        @Override
        protected Void doInBackground(String ... params) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            dbRead = DbHelp.getWritableDatabase();
            Boolean alreadyHitStreak;
            String[] columns = new String[]{PetContract.WalkTheDog.STREAK,
                    PetContract.WalkTheDog._ID,
                    PetContract.WalkTheDog.TIME_GOAL,
                    PetContract.WalkTheDog.WALKS_GOAL,
                    PetContract.WalkTheDog.TIME_GOAL,
                    PetContract.WalkTheDog.CUR_DIST,
                    PetContract.WalkTheDog.CUR_TIME,
                    PetContract.WalkTheDog.CUR_WALKS,
                    PetContract.WalkTheDog.DIST_GOAL,
                    PetContract.WalkTheDog.BEST_WALKS,
                    PetContract.WalkTheDog.BEST_DIST,
                    PetContract.WalkTheDog.BEST_TIME,
                    PetContract.WalkTheDog.ONLINE_ID};
            if (!dogsOnWalkString.equals(null)) {
                dogIDsOnWalk = dogsOnWalkString.split(" ");
                String where = PetContract.WalkTheDog._ID + " IN ("
                        + makePlaceholders(dogIDsOnWalk.length) + ")";
                Cursor cursor = dbRead.query(
                        PetContract.WalkTheDog.TABLE_NAME,
                        columns,
                        where,
                        dogIDsOnWalk,
                        null,
                        null,
                        null
                );
                while (cursor.moveToNext()) {
                    Long petID = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
                    streak = cursor.getInt(cursor.getColumnIndex(PetContract.WalkTheDog.STREAK));
                    Long curWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_WALKS));
                    double curTime = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_TIME));
                    float curDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_DIST));
                    Long goalWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL));
                    double goalTime = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL));
                    float goalDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL));
                    float wBestDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_DIST));
                    double wBestTime = cursor.getDouble(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_TIME));
                    if (curWalks < goalWalks || curTime < goalTime || curDist < goalDist){
                        alreadyHitStreak = false;
                    } else alreadyHitStreak = true;
                    curWalks = curWalks +1;
                    curTime = curTime + (Float.parseFloat(params[0]));
                    curDist =  (curDist + distance);
                    if (curWalks >= goalWalks && curTime >= goalTime && curDist >= goalDist && !alreadyHitStreak){
                        streak = streak + 1;
                        Achievements.updateAchievements(7, streak,dbRead);
                        Achievements.updateAchievements(11, 1,dbRead);

                    }
                    if (wBestDist < distance) wBestDist = distance;
                    if (wBestTime < (Float.parseFloat(params[0]))) wBestTime = (Float.parseFloat(params[0]));
                    String onlineID = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.ONLINE_ID));
                    if (onlineID.length() == 6){
                        ref.child(onlineID).child(PetContract.WalkTheDog.CUR_WALKS).setValue(curWalks);
                        ref.child(onlineID).child(PetContract.WalkTheDog.CUR_TIME).setValue(curTime);
                        ref.child(onlineID).child(PetContract.WalkTheDog.STREAK).setValue(streak);
                        ref.child(onlineID).child(PetContract.WalkTheDog.CUR_DIST).setValue(curDist);
                        ref.child(onlineID).child(PetContract.WalkTheDog.BEST_DIST).setValue(wBestDist);
                        ref.child(onlineID).child(PetContract.WalkTheDog.BEST_TIME).setValue(wBestTime);
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(PetContract.WalkTheDog.CUR_WALKS, curWalks);
                    cv.put(PetContract.WalkTheDog.CUR_TIME, curTime);
                    cv.put(PetContract.WalkTheDog.STREAK, streak);
                    cv.put(PetContract.WalkTheDog.CUR_DIST, curDist);
                    cv.put(PetContract.WalkTheDog.BEST_DIST, wBestDist);
                    cv.put(PetContract.WalkTheDog.BEST_TIME, wBestTime);
                    String whereVal = PetContract.WalkTheDog._ID+"=?";
                    String[] whereArgs = new String[] {String.valueOf(petID)};
                    dbRead.update(PetContract.WalkTheDog.TABLE_NAME, cv, whereVal, whereArgs);
                }
                cursor.close();
            }
            double mph = 0;
            if ((Float.parseFloat(params[0])) != 0){
                mph = Math.round(distance) / ((Float.parseFloat(params[0])) / 60);
            }
            Achievements.updateAchievements(1, 1, dbRead);
            Achievements.updateAchievements(2, (Float.parseFloat(params[0])), dbRead);
            Achievements.updateAchievements(3,distance, dbRead);
            Achievements.updateAchievements(4, (Float.parseFloat(params[0])), dbRead);
            Achievements.updateAchievements(5, distance, dbRead);
            Achievements.updateAchievements(6, mph, dbRead);
            Achievements.updateAchievements(8, (Float.parseFloat(params[0])), dbRead);
            Achievements.updateAchievements(9, distance, dbRead);
            Achievements.updateAchievements(10, 1, dbRead);
            Achievements.resetAchievements(4,dbRead);
            Achievements.resetAchievements(5,dbRead);
            Achievements.resetAchievements(6,dbRead);
            dbRead.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setEnabled(false);
            mProgressBar.setVisibility(View.INVISIBLE);
            finalizeWalk.setEnabled(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            popUpRecyc.setLayoutManager(layoutManager);
            popUpRecyc.setHasFixedSize(true);
            AchievementAdapter achievementAdapter = new AchievementAdapter();
            popUpRecyc.setAdapter(achievementAdapter);
            dbRead = DbHelp.getReadableDatabase();

            Cursor cursor = dbRead.query(
                    PetContract.Achievements.TABLE_NAME,
                    null,
                    PetContract.Achievements.SEEN + "=?",
                    new String[]{"0"},
                    null,
                    null,
                    null
            );

            String[] achievementsArray = new String[cursor.getCount()];
            double[] thresholdArray = new double[cursor.getCount()];
            double[] progressArray = new double[cursor.getCount()];
            double[] completedArray = new double[cursor.getCount()];
            double[] seenArray = new double[cursor.getCount()];
            double[] typeArray = new double[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()){
                achievementsArray[i] = cursor.getString(cursor.getColumnIndex(PetContract.Achievements.ACHIEVEMENT));
                thresholdArray[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.THRESHOLD));
                progressArray[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.PROGRESS));
                completedArray[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.COMPLETED));
                seenArray[i] = 1;
                typeArray[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.TYPE));
                i++;
            }
            cursor.close();
            achievementAdapter.setAchList(achievementsArray, thresholdArray, progressArray, completedArray, seenArray, typeArray);
            achievementAdapter.notifyDataSetChanged();
            dbRead.close();
            if (achievementsArray.length == 0){
                popUpRecyc.setBackgroundColor(getColor(R.color.powderBlue));
            }

        }
    }


    public void endWalk(View view) {
        view.setEnabled(false);
        NotificationUtils.clearNotification(this);
        ReminderUtilities.endWalkReminders(this);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(apiC, this);
        apiC.disconnect();
        DbHelp.close();
        mPopupWindow.dismiss();
        NotificationUtils.clearNotification(this);
        ReminderUtilities.endWalkReminders(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (locationDelay != 0) {
            if (marker != null) marker.remove();
            marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude())));
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                    location.getLongitude())));
            map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            if (location != null && mLastLocation != null ) {
                if (location.distanceTo(mLastLocation) / 1609.0 < .25) {
                    distance = distance + (location.distanceTo(mLastLocation) / 1609);
                    mLastLocation = location;
                }
                else {
                    locationCount++;
                    if (locationCount > 5){
                        mLastLocation = location;
                        locationCount = 0;
                    }
                }
            }
            String distanceString = String.format("%.2f", distance);
            distanceDisplay.setText(distanceString);
        }
        locationDelay++;
    }
}
