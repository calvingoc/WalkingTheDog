package online.cagocapps.walkingthedog;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import online.cagocapps.walkingthedog.data.Achievements;
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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

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
                int maxStreak = 0;
                if (curDate.getDate()!=(lastDaySync.getDate())) { //ensure refresh is only done once a day

                    // set local variables to do calculations
                    Long petID = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
                    float curTime = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_TIME));
                    float curDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_DIST));
                    float curWalks = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_WALKS));
                    float goalTime = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.TIME_GOAL));
                    float goalDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.DIST_GOAL));
                    float goalWalk = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.WALKS_GOAL));
                    int streak = cursor.getInt(cursor.getColumnIndex(PetContract.WalkTheDog.STREAK));
                    int bestStreak = cursor.getInt(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_STREAK));
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

                    if (goalDist > curDist || goalTime > curTime || goalWalk > curWalks){
                        streak = 0;
                    };

                    maxStreak = Math.max(streak, maxStreak);




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

                    String onlineID = cursor.getString(cursor.getColumnIndex(PetContract.WalkTheDog.ONLINE_ID));
                    if (onlineID.length() == 6){
                        ref.child(onlineID).child("curWalks").setValue(0);
                        ref.child(onlineID).child("curTime").setValue(0);
                        ref.child(onlineID).child("streak").setValue(streak);
                        ref.child(onlineID).child("curDist").setValue(0);
                        ref.child(onlineID).child("totDays").setValue(totDays);
                        ref.child(onlineID).child("totTime").setValue(totTimes);
                        ref.child(onlineID).child("totWalks").setValue(totWalks);
                        ref.child(onlineID).child("totDist").setValue(totDist);
                        ref.child(onlineID).child("bestStreak").setValue(bestStreak);
                        ref.child(onlineID).child("bestDistDay").setValue(dBestDist);
                        ref.child(onlineID).child("bestTimeDay").setValue(dBestTime);
                        ref.child(onlineID).child("bestWalks").setValue(bestWalks);
                        ref.child(onlineID).child("lastSync").setValue(curDateMillis);

                    }
                    String whereVal = PetContract.WalkTheDog._ID + "=?";
                    String[] whereArgs = new String[]{String.valueOf(petID)};
                    dbWrite.update(PetContract.WalkTheDog.TABLE_NAME, cv, whereVal, whereArgs);
                    Achievements.resetAchievements(8,dbWrite);
                    Achievements.resetAchievements(9,dbWrite);
                    Achievements.resetAchievements(10,dbWrite);
                    Achievements.resetAchievements(11, dbWrite);
                    if (maxStreak == 0){
                        Achievements.resetAchievements(7,dbWrite);
                    }

                    else {
                        Achievements.updateAchievements(7, maxStreak, dbWrite);
                    }
                }
            }
        cursor.close();
        dbWrite.close();
    }
}
