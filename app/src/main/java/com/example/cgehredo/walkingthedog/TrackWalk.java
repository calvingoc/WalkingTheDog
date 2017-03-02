package com.example.cgehredo.walkingthedog;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.cgehredo.walkingthedog.data.DbHelper;
import com.example.cgehredo.walkingthedog.data.PetContract;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.R.attr.fragment;

public class TrackWalk extends AppCompatActivity implements DogAdapter.DogAdapterOnClickHandler, OnMapReadyCallback {
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;
    private RecyclerView rvDogList;
    private DogAdapter dogAdapter;
    private MapFragment mapFrag;
    private GoogleApiClient apiC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_walk);
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();
        rvDogList = (RecyclerView) findViewById(R.id.recyclerview_dogs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvDogList.setLayoutManager(layoutManager);
        rvDogList.setHasFixedSize(true);
        dogAdapter = new DogAdapter(this);
        rvDogList.setAdapter(dogAdapter);
        android.app.FragmentManager fragMan = getFragmentManager();
        mapFrag = (MapFragment) fragMan.findFragmentById(R.id.map_fragment);
        mapFrag.getMapAsync(this);
        if (apiC == null){
            apiC = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).
                    addOnConnectionFailedListener(this).addApi(LocationService.API).build();
        }

    }

    @Override
    public void onClick(Long petID) {
        return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] columns = new String[]{PetContract.WalkTheDog.DOG_NAME,
                PetContract.WalkTheDog._ID};
        String where = PetContract.WalkTheDog._ID + "=?";
        SharedPreferences shrdPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String dogsOnWalk = shrdPrefs.getString(getString(R.string.default_walks_dog), null);
        if (!dogsOnWalk.equals(null)) {
            String[] dogsOnWalkIds = dogsOnWalk.split(" ");
            Cursor cursor = dbRead.query(
                    PetContract.WalkTheDog.TABLE_NAME,
                    columns,
                    where,
                    dogsOnWalkIds,
                    null,
                    null,
                    null
            );
            int i = 0;
            Long[] dogIDs = new Long[cursor.getCount()];
            String[] dogNames = new String[cursor.getCount()];
            while (cursor.moveToNext()) {
                dogIDs[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
                dogNames[i] = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.DOG_NAME));
                i++;
            }
            dogAdapter.setDogsList(dogIDs, dogNames);
            cursor.close();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position())

    }
}
