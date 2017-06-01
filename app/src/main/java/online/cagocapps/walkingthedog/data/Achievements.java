package online.cagocapps.walkingthedog.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by Calvin on 6/1/2017.
 */

public class Achievements {
    public static void achievementSetUp(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        //Make your first dog
        cv.put(PetContract.Achievements.ACHIEVEMENT, "make a dog");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 0);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Make five dogs
        cv.put(PetContract.Achievements.ACHIEVEMENT, "make five dogs");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 0);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Make ten dogs
        cv.put(PetContract.Achievements.ACHIEVEMENT, "make ten dogs");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 0);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Make one hundred dogs
        cv.put(PetContract.Achievements.ACHIEVEMENT, "make one hundred dogs");
        cv.put(PetContract.Achievements.THRESHOLD, 100);
        cv.put(PetContract.Achievements.TYPE, 0);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog
        cv.put(PetContract.Achievements.ACHIEVEMENT, "take a walk");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 1);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog ten times
        cv.put(PetContract.Achievements.ACHIEVEMENT, "take ten walks");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 1);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog one hundred times
        cv.put(PetContract.Achievements.ACHIEVEMENT, "take one hundred walks");
        cv.put(PetContract.Achievements.THRESHOLD, 100);
        cv.put(PetContract.Achievements.TYPE, 1);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog one thousand times
        cv.put(PetContract.Achievements.ACHIEVEMENT, "take one thousand walks");
        cv.put(PetContract.Achievements.THRESHOLD, 1000);
        cv.put(PetContract.Achievements.TYPE, 1);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour
        cv.put(PetContract.Achievements.ACHIEVEMENT, "an hour of walk time");
        cv.put(PetContract.Achievements.THRESHOLD, 60);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for ten hours
        cv.put(PetContract.Achievements.ACHIEVEMENT, "ten hours of walk time");
        cv.put(PetContract.Achievements.THRESHOLD, 600);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for 24 hours
        cv.put(PetContract.Achievements.ACHIEVEMENT, "24 hours of walk time");
        cv.put(PetContract.Achievements.THRESHOLD, 1440);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 100 hour
        cv.put(PetContract.Achievements.ACHIEVEMENT, "a 100 hours of walk time");
        cv.put(PetContract.Achievements.THRESHOLD, 6000);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a week
        cv.put(PetContract.Achievements.ACHIEVEMENT, "a week of walk time");
        cv.put(PetContract.Achievements.THRESHOLD, 10080);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 1000 hour
        cv.put(PetContract.Achievements.ACHIEVEMENT, "a 1000 hours of walk time");
        cv.put(PetContract.Achievements.THRESHOLD, 60000);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a mile
        cv.put(PetContract.Achievements.ACHIEVEMENT, "a mile of walking");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for 10 miles
        cv.put(PetContract.Achievements.ACHIEVEMENT, "10 miles of walking");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a marathon
        cv.put(PetContract.Achievements.ACHIEVEMENT, "a marathon of walking");
        cv.put(PetContract.Achievements.THRESHOLD, 26.2);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 100 miles
        cv.put(PetContract.Achievements.ACHIEVEMENT, "100 miles of walking");
        cv.put(PetContract.Achievements.THRESHOLD, 100);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 1000 miles
        cv.put(PetContract.Achievements.ACHIEVEMENT, "1000 miles of walking");
        cv.put(PetContract.Achievements.THRESHOLD, 1000);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for half an hour in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "half an hour in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 30);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "hour in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 60);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour and a half in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "hour and a half in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 90);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for two hours in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "2 hours in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 120);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for five hours in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "5 hours in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 300);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a mile in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "mile in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for two miles in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "2 miles in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 2);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for five miles in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "5 miles in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for ten miles in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "10 miles in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //take your dog on a marathon
        cv.put(PetContract.Achievements.ACHIEVEMENT, "26.2 miles in one walk");
        cv.put(PetContract.Achievements.THRESHOLD, 26.2);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //jogger
        cv.put(PetContract.Achievements.ACHIEVEMENT, "3 miles an hour in a walk");
        cv.put(PetContract.Achievements.THRESHOLD, 3);
        cv.put(PetContract.Achievements.TYPE, 6);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //runner
        cv.put(PetContract.Achievements.ACHIEVEMENT, "5 miles an hour in a walk");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 6);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //hit your goals
        cv.put(PetContract.Achievements.ACHIEVEMENT, "hit your goals");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 11);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //five day streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "5 day streak");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //7 streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "7 day streak");
        cv.put(PetContract.Achievements.THRESHOLD, 7);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //10 day streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "10 day streak");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //30 day streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "30 day streak");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //365 streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "365 day streak");
        cv.put(PetContract.Achievements.THRESHOLD, 365);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "hour in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 60);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour and a half in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "hour and a half in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 90);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for two hours in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "2 hours in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 120);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for five hours in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "5 hours in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 300);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for 8 hours in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "8 hours in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 480);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a mile in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "mile in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for two miles in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "2 miles in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 2);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for five miles in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "5 miles in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for ten miles in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "10 miles in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //take your dog on a marathon in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "26.2 miles in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 26.2);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //take your dog on five walks in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "five walks in a day");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 10);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
    }

    public static void updateAchievements(int type, int value, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        Cursor cursor = db.query(
                PetContract.Achievements.TABLE_NAME,
                null,
                PetContract.Achievements.TYPE + "=?",
                new String[]{String.valueOf(type)},
                null,
                null,
                null
        );
        while (cursor.moveToNext()){
            int prog = cursor.getInt(cursor.getColumnIndex(PetContract.Achievements.PROGRESS)) + value;
            cv.put(PetContract.Achievements.PROGRESS, prog);
            if (prog >= cursor.getInt(cursor.getColumnIndex(PetContract.Achievements.THRESHOLD)) &&
                    cursor.getInt(cursor.getColumnIndex(PetContract.Achievements.UPDATE_TRACKER)) == 0){
                cv.put(PetContract.Achievements.COMPLETED, cursor.getInt(cursor.getColumnIndex(PetContract.Achievements.COMPLETED)) + 1);
                cv.put(PetContract.Achievements.UPDATE_TRACKER, 1);
                cv.put(PetContract.Achievements.DATE, System.currentTimeMillis());
                cv.put(PetContract.Achievements.SEEN, 0);
            }
            db.update(PetContract.Achievements.TABLE_NAME,
                    cv,
                    PetContract.Achievements._ID + "=?",
                    new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(PetContract.Achievements._ID)))});
            cv.clear();
        }
        cursor.close();
    }

    public static void resetAchievements(int type, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        Cursor cursor = db.query(
                PetContract.Achievements.TABLE_NAME,
                null,
                PetContract.Achievements.TYPE + "=?",
                new String[]{String.valueOf(type)},
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            cv.put(PetContract.Achievements.UPDATE_TRACKER, 0);
            cv.put(PetContract.Achievements.PROGRESS, 0);
            db.update(PetContract.Achievements.TABLE_NAME,
                    cv,
                    PetContract.Achievements._ID + "=?",
                    new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(PetContract.Achievements._ID)))});
            cv.clear();
        }
        cursor.close();
    }

    public static void markAsSeen(int id, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        Cursor cursor = db.query(
                PetContract.Achievements.TABLE_NAME,
                null,
                PetContract.Achievements._ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            cv.put(PetContract.Achievements.SEEN, 1);
            db.update(PetContract.Achievements.TABLE_NAME,
                    cv,
                    PetContract.Achievements._ID + "=?",
                    new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(PetContract.Achievements._ID)))});
            cv.clear();
        }
        cursor.close();
    }
}
