package com.example.cgehredo.walkingthedog;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cgehredo.walkingthedog.data.DbHelper;
import com.example.cgehredo.walkingthedog.data.PetContract;

import static java.security.AccessController.getContext;

public class AddPet extends AppCompatActivity {
    private DbHelper DbHelp;
    private SQLiteDatabase dbWrite;
    private final String LOG_TAG = "Add Pet Activity";
    private String petNameString;
    //pet variables
    private EditText petName;
    private EditText petWalks;
    private EditText petTime;
    private EditText petDist;
    //views to update
    private Button addButton;
    private Button deleteButton;
    private TextView header;
    private long petID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        petID = getIntent().getLongExtra(getString(R.string.pet_id), -1);
        // Find Views
        petName = (EditText) findViewById(R.id.edit_name);
        petWalks = (EditText) findViewById(R.id.et_walks_goals);
        petTime = (EditText) findViewById(R.id.et_time_goals);
        petDist = (EditText) findViewById(R.id.et_dist_goals);
        addButton = (Button) findViewById(R.id.add_button);
        deleteButton = (Button) findViewById(R.id.delete_button);
        header = (TextView) findViewById(R.id.add_pet_title);

        //Set up DB Helper
        DbHelp = new DbHelper(this);
        dbWrite = DbHelp.getWritableDatabase();

        setUpPage();
    }
    //Add a dog when the Add button is pressed.
    public void addDog(View view){
        if (petName.getText().length() == 0 ||
                petWalks.getText().length() == 0 ||
                petTime.getText().length() == 0 ||
                petDist.getText().length() == 0){
            Toast.makeText(AddPet.this,getResources().getString(R.string.missing_data),Toast.LENGTH_SHORT);
            return;
        }
        int pWalk = 3;
        int pTime = 60;
        int pDist = 3;
        try {
            pWalk = Integer.parseInt(petWalks.getText().toString());
            pTime = Integer.parseInt(petTime.getText().toString());
            pDist = Integer.parseInt(petDist.getText().toString());
        } catch (NumberFormatException ex) {
            Log.e(LOG_TAG, "Failed to parse text to number: " + ex.getMessage());
        }
        if (petID == -1) petID = addNewDog(petName.getText().toString(), pWalk, pTime, pDist);
        else updateDog(petID, petName.getText().toString(), pWalk, pTime, pDist);
        updateSharedPrefs(false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.pet_id), petID);
        startActivity(intent);
        finish();
    }

    //Add a dog takes String pet name, int walk time, int goal time, int goal distance
    public long addNewDog(String name, int walk, int time, int dist){
        ContentValues cv = new ContentValues();
        cv.put(PetContract.WalkTheDog.DOG_NAME, name);
        cv.put(PetContract.WalkTheDog.WALKS_GOAL, walk);
        cv.put(PetContract.WalkTheDog.TIME_GOAL, time);
        cv.put(PetContract.WalkTheDog.DIST_GOAL, dist);
        return dbWrite.insert(PetContract.WalkTheDog.TABLE_NAME, null, cv);
    }
    //update a passed in dog
    public void updateDog(long petID,String name, int walk, int time, int dist){
        ContentValues cv = new ContentValues();
        cv.put(PetContract.WalkTheDog.DOG_NAME, name);
        cv.put(PetContract.WalkTheDog.WALKS_GOAL, walk);
        cv.put(PetContract.WalkTheDog.TIME_GOAL, time);
        cv.put(PetContract.WalkTheDog.DIST_GOAL, dist);
        String where = PetContract.WalkTheDog._ID+"=?";
        String[] whereArgs = new String[] {String.valueOf(petID)};
        dbWrite.update(PetContract.WalkTheDog.TABLE_NAME, cv, where, whereArgs);
    }
    public void deleteDog(View view){
        if(dbWrite.delete(PetContract.WalkTheDog.TABLE_NAME, PetContract.WalkTheDog._ID
                + "=" + petID, null) > 0){
            updateSharedPrefs(true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else Toast.makeText(this, getResources().getString(R.string.delete_dog_fail) + " "
                + petNameString + " " + getResources().getString(R.string.farm), Toast.LENGTH_SHORT).show();
    }
    //formats the page based off of it we are updating or adding a dog.
    public void setUpPage(){
        if (petID == -1){
            header.setText(getString(R.string.add_pet_title));
            petWalks.setText("");
            petDist.setText("");
            petName.setText("");
            petTime.setText("");
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.INVISIBLE);
            addButton.setText(getString(R.string.add_button_text));
        }
        else {
            String where = PetContract.WalkTheDog._ID+"=?";
            String[] whereArgs = new String[] {String.valueOf(petID)};
            String[] columns = new String[]{PetContract.WalkTheDog.DOG_NAME,
                    PetContract.WalkTheDog.TIME_GOAL,
                    PetContract.WalkTheDog.WALKS_GOAL,
                    PetContract.WalkTheDog.DIST_GOAL};

            Cursor cursor = dbWrite.query(
                    PetContract.WalkTheDog.TABLE_NAME,
                    columns,
                    where,
                    whereArgs,
                    null,
                    null,
                    null);
            cursor.moveToFirst();
            header.setText(getString(R.string.update_pet_title));
            petNameString = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.DOG_NAME));
            petWalks.setText(cursor.getInt(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL)));
            petDist.setText(cursor.getInt(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL)));
            petName.setText(petNameString);
            petTime.setText(cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL)));
            deleteButton.setEnabled(true);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setText(getString(R.string.delete_dog) + " " + petNameString + " "
            + getString(R.string.farm));
            addButton.setText(getString(R.string.add_button_text));
            cursor.close();
        }
    }
    //update shared preferences
    private void updateSharedPrefs(boolean deleted){
        Cursor cursor = dbWrite.query(
                PetContract.WalkTheDog.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        SharedPreferences shrdPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = shrdPrefs.edit();
        if (cursor.getCount() == 0){
            editor.putLong(getString(R.string.default_dog), -1);
            editor.putString(getString(R.string.default_walks_dog), null);
            editor.commit();
        }
        if (cursor.getCount() == 1){
            cursor.moveToFirst();
            long tempPetID = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
            editor.putLong(getString(R.string.default_dog), tempPetID);
            editor.putString(getString(R.string.default_walks_dog), Long.toString(tempPetID) + " ");
            editor.commit();
        }
        if (deleted && cursor.getCount()!= 0){
            if (shrdPrefs.getLong(getString(R.string.default_dog), -1) == petID){
                cursor.moveToFirst();
                editor.putLong(getString(R.string.default_dog), petID);
                editor.commit();
            }
            String dogWalks = shrdPrefs.getString(getString(R.string.default_walks_dog),null);
            if(!dogWalks.equals(null) && dogWalks.contains(Long.toString(petID))){
                dogWalks = dogWalks.replace(Long.toString(petID) + " ", null);
                editor.putString(getString(R.string.default_walks_dog), dogWalks);
                editor.commit();
            }
        }


    }
}
