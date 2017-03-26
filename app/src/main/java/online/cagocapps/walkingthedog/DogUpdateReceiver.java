package online.cagocapps.walkingthedog;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import online.cagocapps.walkingthedog.data.DbHelper;
import online.cagocapps.walkingthedog.data.PetContract;

/**
 * Created by cgehredo on 3/6/2017.
 * Alarm routine that refreshes the database for a new day.
 * Sets current day stats to zero
 * includes previous day's stats in life itme stats.
 *
 */

public class DogUpdateReceiver extends BroadcastReceiver {

   /*
   * onReceive
   * when alarm is triggered refresh the database.
   * */
    @Override
    public void onReceive(Context context, Intent intent) {
            //open database
            DbHelper DbHelp = new DbHelper(context);
            SQLiteDatabase dbWrite = DbHelp.getWritableDatabase();

            //set up query
            Cursor cursor = dbWrite.query(
                    PetContract.WalkTheDog.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) { // move through database to update every dog
                Long lastDaySynced = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.LAST_DAY_SYNCED));
                Date lastDaySync = new Date(lastDaySynced);
                Date curDate = new Date(System.currentTimeMillis());
                if (curDate.getDate()!=(lastDaySync.getDate())) { //ensure refresh is only done once a day

                    // set local variables to do calculations
                    Long petID = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
                    float curTime = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_TIME));
                    float curDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_DIST));
                    float curWalks = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_WALKS));
                    float goalTime = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL));
                    float goalDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL));
                    float goalWalk = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL));
                    float streak = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.STREAK));
                    float bestStreak = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_STREAK));
                    float totWalks = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_WALKS));
                    float totTimes = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_TIME));
                    float totDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DIST));
                    float totDays = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DAYS));
                    float dBestTime = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_TIME_DAY));
                    float dBestDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_DIST_DAY));
                    float bestWalks = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_WALKS));
                    Long curDateMillis = System.currentTimeMillis();

                    //increase lifetime stats
                    totDays = totDays + 1;
                    totWalks = totWalks + curWalks;
                    totTimes = totTimes + curTime;
                    totDist = totDist + curDist;

                    //evaluate if records need to be updated
                    if (streak > bestStreak) bestStreak = streak;
                    if (dBestDist < curDist) dBestDist = curDist;
                    if (dBestTime < curTime) dBestTime = curTime;
                    if (bestWalks < curWalks) bestWalks = curWalks;

                    if (goalDist > curDist || goalTime > curTime || goalWalk > curWalks) streak = 0;




                    // update dog with new values
                    ContentValues cv = new ContentValues();
                    cv.put(PetContract.WalkTheDog.TOTAL_DAYS, totDays);
                    cv.put(PetContract.WalkTheDog.TOTAL_TIME, totTimes);
                    cv.put(PetContract.WalkTheDog.TOTAL_WALKS, totWalks);
                    cv.put(PetContract.WalkTheDog.TOTAL_DIST, totDist);
                    cv.put(PetContract.WalkTheDog.BEST_STREAK, bestStreak);
                    cv.put(PetContract.WalkTheDog.BEST_DIST_DAY, dBestDist);
                    cv.put(PetContract.WalkTheDog.BEST_TIME_DAY, dBestTime);
                    cv.put(PetContract.WalkTheDog.BEST_WALKS, bestWalks);
                    cv.put(PetContract.WalkTheDog.CUR_DIST, 0);
                    cv.put(PetContract.WalkTheDog.CUR_TIME, 0);
                    cv.put(PetContract.WalkTheDog.CUR_WALKS, 0);
                    cv.put(PetContract.WalkTheDog.STREAK, streak);
                    cv.put(PetContract.WalkTheDog.LAST_DAY_SYNCED, curDateMillis);
                    String whereVal = PetContract.WalkTheDog._ID + "=?";
                    String[] whereArgs = new String[]{String.valueOf(petID)};
                    dbWrite.update(PetContract.WalkTheDog.TABLE_NAME, cv, whereVal, whereArgs);
                }
            }
            cursor.close();
            dbWrite.close();
    }
}
