package com.example.cgehredo.walkingthedog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cgehredo.walkingthedog.data.DbHelper;
import com.example.cgehredo.walkingthedog.data.PetContract;

public class EditDogsOnWalk extends AppCompatActivity implements DogOnWalkAdapter.DogOnWalkAdapterOnClickHandler {
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;
    private RecyclerView rvDogList;
    private DogOnWalkAdapter dogAdapter;
    private String dogsOnWalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dogs_on_walk);

        //set up DB
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();

        dogsOnWalk =  getIntent().getStringExtra(getString(R.string.dogs_on_walk));
        Log.d("dogsonwalk", dogsOnWalk);

        //set up recycler view
        rvDogList = (RecyclerView) findViewById(R.id.recyclerview_dogs_on_walk);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvDogList.setLayoutManager(layoutManager);
        rvDogList.setHasFixedSize(true);
        dogAdapter = new DogOnWalkAdapter(this);
        rvDogList.setAdapter(dogAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        String[] columns = new String[]{PetContract.WalkTheDog.DOG_NAME,
                PetContract.WalkTheDog._ID};
        Cursor cursor = dbRead.query(
                PetContract.WalkTheDog.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        int i = 0;
        Long[] dogIDs = new Long[cursor.getCount()];
        String[] dogNames = new String[cursor.getCount()];
        while (cursor.moveToNext())
        {
            dogIDs[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
            dogNames[i] = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.DOG_NAME));
            i++;
        }
        String [] dogIdsOnWalk = dogsOnWalk.split(" ");
        dogAdapter.setDogsOnWalk(dogIDs, dogNames, dogIdsOnWalk);
        cursor.close();
    }

    @Override
    public void onClick(Long petID) {
        Log.d("dogsonwalk","PetID " + Long.toString(petID) + " dogs list " + dogsOnWalk);
        if (dogsOnWalk.contains(" " + Long.toString(petID))) {
            dogsOnWalk = dogsOnWalk.replace(" " + Long.toString(petID), "");
        } else dogsOnWalk = dogsOnWalk + " " + Long.toString(petID);
        Log.d("dogsonwalk","PetID " + Long.toString(petID) + " dogs list " + dogsOnWalk);
    }

    public void updateDogs(View view){
        Log.d("dogsonwalk", "dogsonwalk " + dogsOnWalk);
        if (dogsOnWalk.length() <=1){
            Toast.makeText(view.getContext(), getString(R.string.tw_need_one_pet),Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, TrackWalk.class);
        PreferenceManager.getDefaultSharedPreferences(this).edit().
                putString(getString(R.string.dogs_on_walk), dogsOnWalk).commit();
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRead.close();
    }
}
