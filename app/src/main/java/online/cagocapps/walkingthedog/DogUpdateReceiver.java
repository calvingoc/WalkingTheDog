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
 */

public class DogUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            DbHelper DbHelp = new DbHelper(context);
            SQLiteDatabase dbWrite = DbHelp.getWritableDatabase();
            String[] columns = new String[]{
                    PetContract.WalkTheDog._ID,
                    PetContract.WalkTheDog.CUR_TIME,
                    PetContract.WalkTheDog.CUR_DIST,
                    PetContract.WalkTheDog.CUR_WALKS,
                    PetContract.WalkTheDog.STREAK,
                    PetContract.WalkTheDog.TOTAL_WALKS,
                    PetContract.WalkTheDog.TOTAL_TIME,
                    PetContract.WalkTheDog.TOTAL_DAYS,
                    PetContract.WalkTheDog.TOTAL_DIST,
                    PetContract.WalkTheDog.BEST_DIST_DAY,
                    PetContract.WalkTheDog.BEST_TIME_DAY,
                    PetContract.WalkTheDog.BEST_WALKS,
                    PetContract.WalkTheDog.LAST_DAY_SYNCED
            };
            Cursor cursor = dbWrite.query(
                    PetContract.WalkTheDog.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            while (cursor.moveToNext()) {
                Long lastDaySynced = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.LAST_DAY_SYNCED));
                Date lastDaySync = new Date(lastDaySynced);
                Date curDate = new Date(System.currentTimeMillis());
                if (!curDate.equals(lastDaySync)) {

                    Long petID = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog._ID));
                    Long curTime = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_TIME));
                    float curDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_DIST));
                    Long curWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.CUR_WALKS));
                    Long streak = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.STREAK));
                    Long bestStreak = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_STREAK));
                    Long totWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_WALKS));
                    Long totTimes = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_TIME));
                    float totDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DIST));
                    Long totDays = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.TOTAL_DAYS));
                    Long dBestTime = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_TIME_DAY));
                    float dBestDist = cursor.getFloat(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_DIST_DAY));
                    Long bestWalks = cursor.getLong(cursor.getColumnIndex(PetContract.WalkTheDog.BEST_WALKS));
                    Long curDateMillis = System.currentTimeMillis();

                    totDays = totDays++;
                    totWalks = totWalks + curWalks;
                    totTimes = totTimes + curTime;
                    totDist = totDist + curDist;
                    if (streak > bestStreak) bestStreak = streak;
                    if (dBestDist < curDist) dBestDist = curDist;
                    if (dBestTime < curTime) dBestTime = curTime;
                    if (bestWalks < curWalks) bestWalks = curWalks;

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
