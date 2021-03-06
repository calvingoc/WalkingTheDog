package online.cagocapps.walkingthedog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import online.cagocapps.walkingthedog.data.DbBitmapUtility;
import online.cagocapps.walkingthedog.data.DbHelper;
import online.cagocapps.walkingthedog.data.PetContract;
/*
* EditDogsOnWalk
* class to update what dogs are on your walk
* */
public class EditDogsOnWalk extends AppCompatActivity implements DogOnWalkAdapter.DogOnWalkAdapterOnClickHandler {
    //db vars
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;
    //recycler view vars
    private RecyclerView rvDogList;
    private DogOnWalkAdapter dogAdapter;
    // dogs curently on walk
    private String dogsOnWalk;

    /*
    * onCreate
    * sets up the recycler view, opens the database and stores what dogs are currently on the walk
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dogs_on_walk);

        //set up DB
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();

        //finds dogs currently on walk
        dogsOnWalk =  getIntent().getStringExtra(getString(R.string.dogs_on_walk));

        //set up recycler view
        rvDogList = (RecyclerView) findViewById(R.id.recyclerview_dogs_on_walk);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvDogList.setLayoutManager(layoutManager);
        rvDogList.setHasFixedSize(true);
        dogAdapter = new DogOnWalkAdapter(this);
        rvDogList.setAdapter(dogAdapter);
    }



    /*
    * onStart
    * queries the database for all dogs and passes info to the recycler view adapter
    * */
    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        String[] columns = new String[]{PetContract.WalkTheDog.DOG_NAME,
                PetContract.WalkTheDog._ID,
                PetContract.WalkTheDog.PROFILE_PIC};
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
        Bitmap[] images = new Bitmap[cursor.getCount()];
        while (cursor.moveToNext())
        {
            dogIDs[i] = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
            dogNames[i] = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.DOG_NAME));
            if (cursor.getBlob(cursor.getColumnIndex(PetContract.WalkTheDog.PROFILE_PIC)) != null)
                    images [i] = DbBitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(PetContract.WalkTheDog.PROFILE_PIC)));
            i++;
        }
        String [] dogIdsOnWalk = dogsOnWalk.split(" ");
        dogAdapter.setDogsOnWalk(dogIDs, dogNames, dogIdsOnWalk, images);
        cursor.close();
    }

    /*
    * onClick
    * updates the dog on walk string when a dog is clicked.
    * */
    @Override
    public void onClick(Long petID) {
        if (dogsOnWalk.contains(" " + Long.toString(petID))) {
            dogsOnWalk = dogsOnWalk.replace(" " + Long.toString(petID), "");
        } else dogsOnWalk = dogsOnWalk + " " + Long.toString(petID);
    }
    /*
    * updateDogs - called by update dogs button
    * if at least one dog is on the walk returns user to track walk activity with update list of dogs
    * */
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

    /*
    * onDestroy
    * closes database
    * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRead.close();
    }
}
