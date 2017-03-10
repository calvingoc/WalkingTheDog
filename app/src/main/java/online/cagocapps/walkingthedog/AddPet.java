package online.cagocapps.walkingthedog;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import online.cagocapps.walkingthedog.data.DbBitmapUtility;
import online.cagocapps.walkingthedog.data.DbHelper;
import online.cagocapps.walkingthedog.data.PetContract;

import static java.security.AccessController.getContext;
/*
* AddPet activity
* if passed the dummy ID -1 it prompts the user to add a new pet, if passed a real ID it allows user to edit dog.
* */
public class AddPet extends AppCompatActivity {
    //set up db variables
    private DbHelper DbHelp;
    private SQLiteDatabase dbWrite;
    private final String LOG_TAG = "Add Pet Activity";
    private String petNameString;
    //pet variables
    private EditText petName;
    private EditText petWalks;
    private EditText petTime;
    private EditText petDist;
    private CheckBox defDog;
    private CheckBox autoWalk;
    //views to update
    private Button addButton;
    private Button deleteButton;
    private TextView header;
    private long petID;
    private ImageView profilePicture;
    //set up camera capture
    static final int REQUEST_IMAGE_CAPTURE = 12312;
    private byte[] profilePicByte;

    /*
    * onCreate
    * links views to variables
    * sets up database connection
    * */
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
        defDog = (CheckBox) findViewById(R.id.default_dog);
        autoWalk = (CheckBox) findViewById(R.id.autoOnWalk);
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        //Set up DB Helper
        DbHelp = new DbHelper(this);
        dbWrite = DbHelp.getWritableDatabase();
        //calls helper routine to set up page
        setUpPage();
    }


    /*
    * addDog - called by the add/update button
    * updates the database with dog info entered by user
    * */
    public void addDog(View view){
        if (petName.getText().length() == 0 ||
                petWalks.getText().length() == 0 ||
                petTime.getText().length() == 0 ||
                petDist.getText().length() == 0){ // makes sure  all fields have info entered
            Toast.makeText(AddPet.this,getResources().getString(R.string.missing_data),Toast.LENGTH_SHORT).show();
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
            Log.e(LOG_TAG, "Failed to parse text to number: " + ex.getMessage()); // ensure entered data is of the correct type
        }
        if (petID == -1) petID = addNewDog(petName.getText().toString(), pWalk, pTime, pDist); // if its a new dog add a row to the database
        else updateDog(petID, petName.getText().toString(), pWalk, pTime, pDist); // updating an exisiting dog
        updateSharedPrefs(false); // helper routine that updates sharedpreferences with a boolean saying we are not deleting a dog
        Intent intent = new Intent(this, MainActivity.class); //return to main activity
        startActivity(intent);
        finish();
    }

    /*
    * addNewDog
    * adds a brand new dog to database with user provided info
    * returns new dog's ID.
    * */
    public long addNewDog(String name, int walk, int time, int dist){
        ContentValues cv = new ContentValues();
        cv.put(PetContract.WalkTheDog.DOG_NAME, name);
        cv.put(PetContract.WalkTheDog.WALKS_GOAL, walk);
        cv.put(PetContract.WalkTheDog.TIME_GOAL, time);
        cv.put(PetContract.WalkTheDog.DIST_GOAL, dist);
        if (profilePicByte != null ) cv.put(PetContract.WalkTheDog.PROFILE_PIC, profilePicByte);
        cv.put(PetContract.WalkTheDog.LAST_DAY_SYNCED, System.currentTimeMillis());
        return dbWrite.insert(PetContract.WalkTheDog.TABLE_NAME, null, cv);
    }


   /*
   * updateDog
   * updates the dog with id petID
   * */
    public void updateDog(long petID,String name, int walk, int time, int dist){
        ContentValues cv = new ContentValues();
        cv.put(PetContract.WalkTheDog.DOG_NAME, name);
        cv.put(PetContract.WalkTheDog.WALKS_GOAL, walk);
        cv.put(PetContract.WalkTheDog.TIME_GOAL, time);
        cv.put(PetContract.WalkTheDog.DIST_GOAL, dist);
        if (profilePicByte != null ) cv.put(PetContract.WalkTheDog.PROFILE_PIC, profilePicByte);
        String where = PetContract.WalkTheDog._ID+"=?";
        String[] whereArgs = new String[] {String.valueOf(petID)};
        dbWrite.update(PetContract.WalkTheDog.TABLE_NAME, cv, where, whereArgs);
    }

    /*
    * deleteDog - called by Delete button
    * removes selected dog from database
    * */
    public void deleteDog(View view){
        if(dbWrite.delete(PetContract.WalkTheDog.TABLE_NAME, PetContract.WalkTheDog._ID
                + "=" + petID, null) > 0){
            updateSharedPrefs(true); // updates shared preferences to make sure the deleted dog isn't our default dog
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else Toast.makeText(this, getResources().getString(R.string.delete_dog_fail) + " "
                + petNameString + " " + getResources().getString(R.string.farm), Toast.LENGTH_SHORT).show();
    }
    /*
    * setUpPage
    * sets view on page based off of if we are adding or updating a dog.
    * */
    public void setUpPage(){
        if (petID == -1){ //dummy Id so set up page to add a new dog
            header.setText(getString(R.string.add_pet_title));
            petWalks.setText("");
            petDist.setText("");
            petName.setText("");
            petTime.setText("");
            deleteButton.setEnabled(false);
            deleteButton.setVisibility(View.INVISIBLE);
            addButton.setText(getString(R.string.add_button_text));
        }
        else { //set up page to edit an existing dog
            String where = PetContract.WalkTheDog._ID+"=?";
            String[] whereArgs = new String[] {String.valueOf(petID)};
            String[] columns = new String[]{PetContract.WalkTheDog.DOG_NAME,
                    PetContract.WalkTheDog.TIME_GOAL,
                    PetContract.WalkTheDog.WALKS_GOAL,
                    PetContract.WalkTheDog.DIST_GOAL,
                    PetContract.WalkTheDog.PROFILE_PIC};

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
            petWalks.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL))));
            petDist.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL))));
            petName.setText(petNameString);
            petTime.setText(Integer.toString(cursor.getInt(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL))));
            profilePicture
                    .setImageBitmap(DbBitmapUtility.getImage(
                            cursor.getBlob(cursor.getColumnIndex(PetContract.WalkTheDog.PROFILE_PIC))
                    ));
            deleteButton.setEnabled(true);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setText(getString(R.string.delete_dog) + " " + petNameString + " "
            + getString(R.string.farm));
            addButton.setText(getString(R.string.update_button_text));
            SharedPreferences shrdPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            long prefDogID = shrdPrefs.getLong(getString(R.string.default_dog), -1);
            if (prefDogID==petID) defDog.setChecked(true);
            else defDog.setChecked(false);
            String autoWalkDogs = shrdPrefs.getString(getString(R.string.default_walks_dog),null);
            String[] autoDogs = autoWalkDogs.split(" ");
            for (String s : autoDogs){
                if (s.equals(Long.toString(petID))) autoWalk.setChecked(true);
            }
            cursor.close();
        }
    }
   /*
   * updateSharedPrefs
   * helper routine to make sure adding/removing dogs updates and
    * doesn't corrupt shared preferences
   * */
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
        //set up shared preferences editor
        SharedPreferences shrdPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = shrdPrefs.edit();
        if (cursor.getCount() == 0){ //update to relect no dogs in db
            long i = -1;
            editor.putLong(getString(R.string.default_dog), i);
            editor.putString(getString(R.string.default_walks_dog), null);
            editor.commit();
        }
        else if (cursor.getCount() == 1){ //shared prefs if only 1 dog
            cursor.moveToFirst();
            long tempPetID = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
            editor.putLong(getString(R.string.default_dog), tempPetID);
            editor.putString(getString(R.string.default_walks_dog)," " + Long.toString(tempPetID));
            editor.commit();
        } else {
            if (defDog.isChecked() && !deleted) { //update default dog if checkbox marked
                editor.putLong(getString(R.string.default_dog), petID).commit();
            }
            if (autoWalk.isChecked() && !deleted) { //adds dog to default walk list
                String tempDogWalks = shrdPrefs.getString(getString(R.string.default_walks_dog),null);
                String[] autoDogs = tempDogWalks.split(" ");
                Boolean found = false;
                for (String s : autoDogs) {
                    if (s.equals(Long.toString(petID))) found = true;
                }
                tempDogWalks = tempDogWalks + " " +Long.toString(petID);
                if  (!found)
                editor.putString(getString(R.string.default_walks_dog), tempDogWalks).commit();
            } else { //removes dog from walk list if needed
                String dogWalks = shrdPrefs.getString(getString(R.string.default_walks_dog),"");
                if (dogWalks.contains(" " + Long.toString(petID))) {
                    dogWalks = dogWalks.replace(" " + Long.toString(petID), "");
                    editor.putString(getString(R.string.default_walks_dog), dogWalks);
                    editor.commit();
                }
            }
        }
        if (deleted && cursor.getCount()!= 0){ //if a dog is deleted
            if (shrdPrefs.getLong(getString(R.string.default_dog), -1) == petID){ //if default dog is deleted repalce with first dog in DB
                cursor.moveToFirst();
                editor.putLong(getString(R.string.default_dog), petID);
                editor.commit();
            }
            String dogWalks = shrdPrefs.getString(getString(R.string.default_walks_dog),null);
            if(!dogWalks.equals(null) && dogWalks.contains(" " + Long.toString(petID))){//remove deleted dog from walk list
                dogWalks = dogWalks.replace(" " + Long.toString(petID), "");
                editor.putString(getString(R.string.default_walks_dog), dogWalks);
                editor.commit();
            }
        }
        cursor.close();
    }

    /*
    * takeProfilePicture - called by take profile picture button
    * links out to phone's camera to take a picture
    * */
    public void takeProfilePicture(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
/*
* onActivityResult
* saves profile picture
* */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePicture.setImageBitmap(imageBitmap);
            profilePicByte = DbBitmapUtility.getBytes(imageBitmap);
        }
    }
/*
* onDestory
* close database.
* */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbWrite.close();
    }
}
