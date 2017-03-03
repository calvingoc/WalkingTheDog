package com.example.cgehredo.walkingthedog;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Chronometer;

import com.example.cgehredo.walkingthedog.data.DbHelper;
import com.example.cgehredo.walkingthedog.data.PetContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//Class sets up the Tracking walk screen and lays the basework for timing the walk, tracking the walk with GPS and getting distance.
public class TrackWalk extends AppCompatActivity implements DogAdapter.DogAdapterOnClickHandler,
        OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;
    private RecyclerView rvDogList;
    private DogAdapter dogAdapter;
    private GoogleApiClient apiC;
    private Location mLastLocation;
    private static final String TAG = "TrackWalk";
    private Chronometer timer;
    private Long[] dogIDs;
    private String[] dogNames;
    private String[] dogIDsOnWalk;
    p




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_walk);
        Log.d(TAG, "set up view");

        //Setting up DB
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();
        Log.d(TAG, "set up db");

        //Setting up Chronometer
        timer = (Chronometer) findViewById(R.id.cur_time_val);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        //Setting up RecyclerView
        rvDogList = (RecyclerView) findViewById(R.id.dogWalkRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvDogList.setLayoutManager(layoutManager);
        rvDogList.setHasFixedSize(true);
        dogAdapter = new DogAdapter(this);
        rvDogList.setAdapter(dogAdapter);
        Log.d(TAG, "set up rec");

        //Setting up the GPS fragment with api client
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        if (apiC == null){
            apiC = new GoogleApiClient.Builder(this).addConnectionCallbacks(TrackWalk.this).
                    addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
        Log.d(TAG, "set up gps");

    }

    @Override
    public void onClick(Long petID) {
        return;
    }


    @Override
    protected void onStart() {
        apiC.connect();
        super.onStart();
        //Grabing the default dogs to include on a walk
        String[] columns = new String[]{PetContract.WalkTheDog.DOG_NAME,
                PetContract.WalkTheDog._ID,
                PetContract.WalkTheDog.TIME_GOAL,
                PetContract.WalkTheDog.WALKS_GOAL,
                PetContract.WalkTheDog.TIME_GOAL,
                PetContract.WalkTheDog.CUR_DIST,
                PetContract.WalkTheDog.CUR_TIME,
                PetContract.WalkTheDog.CUR_WALKS};
        String where = PetContract.WalkTheDog._ID + "=?";
        SharedPreferences shrdPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String dogsOnWalk = shrdPrefs.getString(getString(R.string.default_walks_dog), null);
        if (!dogsOnWalk.equals(null)) {
            dogIDsOnWalk = dogsOnWalk.split(" ");
            for (int x = 1; x > dogIDsOnWalk.length; x++)
            {
                where = where + " AND " + PetContract.WalkTheDog._ID + "=?";
            }
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
            Long[] curTime = new Long[cursor.getCount()];
            Long[] curDist = new Long[cursor.getCount()];
            Long[] goalWalks = new Long[cursor.getCount()];
            Long[] goalTime = new Long[cursor.getCount()];
            Long[] goalDist = new Long[cursor.getCount()];
            while (cursor.moveToNext()) {
                dogIDs[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
                dogNames[i] = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.DOG_NAME));
                curWalks[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_WALKS));
                curTime[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_TIME));
                curDist[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_DIST));
                goalWalks[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL));
                goalTime[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL));
                goalDist[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL));
                i++;
            }
            dogAdapter.setDogsList(dogIDs, dogNames, curWalks, curTime, curDist,
                    goalWalks, goalTime, goalDist);
            cursor.close();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),
                mLastLocation.getLongitude())));
    }
    //gets last know location
    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(apiC);
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

    public void editDogsOnList(){
        Intent intent = new Intent(this, EditDogsOnWalk.class);
        String ids = null;
        for (Long l : dogIDs){
            ids = ids + " " + Long.toString(l);
        }
        intent.putExtra(getString(R.string.dogs_on_walk), ids);
        startActivity(intent);
    }



    @Override
    protected void onStop() {
        apiC.disconnect();
        super.onStop();
    }

}
