package com.example.cgehredo.walkingthedog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cgehredo.walkingthedog.data.DbHelper;
import com.example.cgehredo.walkingthedog.data.PetContract;

import static java.lang.Long.getLong;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private DbHelper DbHelp;
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;
    private SharedPreferences shrdPrefs;
    private long defaultId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbHelp = new DbHelper(this);
        dbRead = DbHelp.getReadableDatabase();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shrdPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        shrdPrefs.registerOnSharedPreferenceChangeListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        defaultId = shrdPrefs.getLong(getString(R.string.default_dog), -1);
        if (defaultId == -1){
            Intent intent = new Intent(this, AddPet.class);
            intent.putExtra(getString(R.string.pet_id), -1);
            startActivity(intent);
        } else{
            setUpScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
    public void setUpScreen(){
        String where = PetContract.WalkTheDog._ID + "=?";
        String[] whereArgs = new String[] {String.valueOf(defaultId)};
        String[] columns = new String[]{PetContract.WalkTheDog.DOG_NAME,
                PetContract.WalkTheDog.TIME_GOAL,
                PetContract.WalkTheDog.WALKS_GOAL,
                PetContract.WalkTheDog.DIST_GOAL};

        Cursor cursor = dbRead.query(
                PetContract.WalkTheDog.TABLE_NAME,
                columns,
                where,
                whereArgs,
                null,
                null,
                null);
        cursor.moveToFirst();
        String petName = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.DOG_NAME));
        Long timeGoal = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL));
        Long walkGoal = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL));
        Long distGoal = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL));
        Long curTime = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_TIME));
        Long curDist = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_DIST));
        Long curWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_WALKS));
        Long streak = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.STREAK));
        Long bestStreak = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_STREAK));
        Long totWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_WALKS));
        Long totTimes = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_TIME));
        Long totDist = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DIST));
        Long totDays = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DAYS));
        Long wBestTime = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_TIME));
        Long dBestTime = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_TIME_DAY));
        Long wBestDist = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_DIST));
        Long dBestDist = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_DIST_DAY));
        Long bestWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_WALKS));
        TextView tv = (TextView) findViewById(R.id.dog_name);
        tv.setText(petName + getString(R.string.main_pet_name));
        tv = (TextView) findViewById(R.id.cur_walks);
        tv.setText(Long.toString(curWalks) + " / " + Long.toString(walkGoal));
        if (curWalks >= walkGoal){
            ImageView iv = (ImageView) findViewById(R.id.walks_star);
            iv.setImageResource(R.drawable.btn_star_big_on);
        } else {
            ImageView iv = (ImageView) findViewById(R.id.walks_star);
            iv.setImageResource(R.drawable.btn_star_big_off);
        }
        tv = (TextView) findViewById(R.id.cur_time);
        tv.setText(Long.toString(curTime) + " / " + Long.toString(timeGoal));
        if (curTime >= timeGoal){
            ImageView iv = (ImageView) findViewById(R.id.time_star);
            iv.setImageResource(R.drawable.btn_star_big_on);
        } else {
            ImageView iv = (ImageView) findViewById(R.id.time_star);
            iv.setImageResource(R.drawable.btn_star_big_off);
        }
        tv = (TextView) findViewById(R.id.cur_dist);
        tv.setText(Long.toString(curDist) + " / " + Long.toString(distGoal));
        if (curDist >= distGoal){
            ImageView iv = (ImageView) findViewById(R.id.dist_star);
            iv.setImageResource(R.drawable.btn_star_big_on);
        } else {
            ImageView iv = (ImageView) findViewById(R.id.dist_star);
            iv.setImageResource(R.drawable.btn_star_big_off);
        }
    }
}
