package online.cagocapps.walkingthedog;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import online.cagocapps.walkingthedog.data.DbHelper;
import online.cagocapps.walkingthedog.data.PetContract;

public class ImportDog extends AppCompatActivity {
    private EditText dogIDET;
    private TextView importedDogNameTV;
    private Button saveImportedDogButton;

    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_dog);

        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getWritableDatabase();

        if (database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }

        dogIDET = (EditText) findViewById(R.id.dog_code_edittext);
        importedDogNameTV = (TextView) findViewById(R.id.imported_dog_name);
        saveImportedDogButton = (Button) findViewById(R.id.save_imported_dog_button);
        dogIDET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveImportedDogButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void launchAddDog(View view){
        Intent intent = new Intent(this, AddPet.class);
        intent.putExtra(getString(R.string.pet_id), -1);
        startActivity(intent);
    }

    public void importDog(View view){
        final String id = dogIDET.getText().toString();
        if (id.length() != 6 || !id.matches("^[a-zA-Z0-9_]+$")){
            importedDogNameTV.setText(getString(R.string.bad_ID));
            return;
        }
        Cursor cursor = dbRead.query(
                PetContract.WalkTheDog.TABLE_NAME,
                null,
                PetContract.WalkTheDog.ONLINE_ID + " == ?",
                new String[]{id},
                null,
                null,
                null
        );
        if (cursor.moveToNext()){
            importedDogNameTV.setText(getString(R.string.already_have_dog));
            cursor.close();
            return;
        }
        cursor.close();
        database.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    importedDogNameTV.setText(getString(R.string.found_dog_import) + " " + dataSnapshot.child(PetContract.WalkTheDog.DOG_NAME).getValue() + "?");
                    saveImportedDogButton.setVisibility(View.VISIBLE);
                    saveImportedDogButton.setText(getString(R.string.import_shared_dog));
                }
                else {
                    importedDogNameTV.setText(getString(R.string.bad_ID));
                    saveImportedDogButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void saveImportedDog(View view){
        final Context cont = this;
        final String id = dogIDET.getText().toString();
        database.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ContentValues cv = new ContentValues();
                cv.put(PetContract.WalkTheDog.ONLINE_ID, id);
                for (DataSnapshot values : dataSnapshot.getChildren()){
                    if (values.getKey().equals(PetContract.WalkTheDog.DOG_NAME)){
                        cv.put(PetContract.WalkTheDog.DOG_NAME, values.getValue(String.class));
                    } else {
                        Log.d("databaseTS",values.getKey());
                        cv.put(values.getKey(), values.getValue(Double.class));
                    }
                }
                long sqlID = dbRead.insertOrThrow(PetContract.WalkTheDog.TABLE_NAME, null, cv);

                Intent intent = new Intent(cont, AddPet.class);
                intent.putExtra(getString(R.string.pet_id), sqlID);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        database.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ContentValues cv = new ContentValues();
                for (DataSnapshot values : dataSnapshot.getChildren()){
                    if (values.getKey().equals(PetContract.WalkTheDog.DOG_NAME)){
                        cv.put(PetContract.WalkTheDog.DOG_NAME, values.getValue(String.class));
                    } else {
                        Log.d("databaseTS",values.getKey());
                        cv.put(values.getKey(), values.getValue(Double.class));
                    }
                }
                dbRead.update(PetContract.WalkTheDog.TABLE_NAME, cv, PetContract.WalkTheDog.ONLINE_ID + " == ?", new String[]{dataSnapshot.getKey()});
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
