package com.example.cgehredo.walkingthedog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.cgehredo.walkingthedog.data.DbHelper;
import com.example.cgehredo.walkingthedog.data.PetContract;

public class ViewDogs extends AppCompatActivity implements DogAdapter.DogAdapterOnClickHandler {
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;
    private RecyclerView rvDogList;
    private DogAdapter dogAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dogs);
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();
        rvDogList = (RecyclerView) findViewById(R.id.recyclerview_dogs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvDogList.setLayoutManager(layoutManager);
        rvDogList.setHasFixedSize(true);
        dogAdapter = new DogAdapter(this);
        rvDogList.setAdapter(dogAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] columns = new String[]{PetContract.WalkTheDog.DOG_NAME};
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
        dogAdapter.setDogsList(dogIDs, dogNames);
        cursor.close();
    }

    @Override
    public void onClick(Long petID) {
        Intent intent = new Intent(this, AddPet.class);
        intent.putExtra(getString(R.string.pet_id), petID);
        startActivity(intent);
    }
}
