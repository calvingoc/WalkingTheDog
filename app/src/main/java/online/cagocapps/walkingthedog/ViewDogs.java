package online.cagocapps.walkingthedog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import online.cagocapps.walkingthedog.data.DbBitmapUtility;
import online.cagocapps.walkingthedog.data.DbHelper;
import online.cagocapps.walkingthedog.data.PetContract;

public class ViewDogs extends AppCompatActivity implements DogAdapter.DogAdapterOnClickHandler {
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;
    private RecyclerView rvDogList;
    private DogAdapter dogAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dogs);
        //set up DB
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();

        //set up Recycler View
        rvDogList = (RecyclerView) findViewById(R.id.recyclerview_dogs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvDogList.setLayoutManager(layoutManager);
        rvDogList.setHasFixedSize(true);
        dogAdapter = new DogAdapter(this);
        rvDogList.setAdapter(dogAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            Bitmap bitmap = DbBitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(PetContract.WalkTheDog.PROFILE_PIC)));
            images[i] = bitmap;
            i++;
        }
        dogAdapter.setDogsList(dogIDs, dogNames, null, null, null, null, null, null, images);
        cursor.close();
    }


    @Override
    public void onClick(Long petID) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.pet_id), petID);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRead.close();
    }
}
