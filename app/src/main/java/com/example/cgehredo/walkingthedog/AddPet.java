package com.example.cgehredo.walkingthedog;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cgehredo.walkingthedog.data.DbHelper;
import com.example.cgehredo.walkingthedog.data.PetContract;

public class AddPet extends AppCompatActivity {
    private DbHelper DbHelp;
    private SQLiteDatabase dbWrite;
    private final String LOG_TAG = "Add Pet Activity";
    //pet variables
    private EditText petName;
    private EditText petWalks;
    private EditText petTime;
    private EditText petDist;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        // Find Views
        petName = (EditText) findViewById(R.id.edit_name);
        petWalks = (EditText) findViewById(R.id.et_walks_goals);
        petTime = (EditText) findViewById(R.id.et_time_goals);
        petDist = (EditText) findViewById(R.id.et_dist_goals);
        //Set up DB Helper
        DbHelp = new DbHelper(this);
        dbWrite = DbHelp.getWritableDatabase();
    }
    //Add a dog when the Add button is pressed.
    public void addDog(View view){
        if (petName.getText().length() == 0 ||
                petWalks.getText().length() == 0 ||
                petTime.getText().length() == 0 ||
                petDist.getText().length() == 0){
            Toast.makeText(this,getResources().getString(R.string.missing_data),Toast.LENGTH_SHORT);
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
            Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
        }
        addNewDog(petName.getText().toString(), pWalk, pTime, pDist);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
}
