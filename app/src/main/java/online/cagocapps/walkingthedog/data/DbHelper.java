package online.cagocapps.walkingthedog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import online.cagocapps.walkingthedog.MainActivity;

/**
 * Creates the database if needed otherwise opens a connection to the database
 * Created by cgehredo on 2/20/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    // sets database name and version
    public static final String DATABASE_NAME = "walkTheDog.db";
    public static final int DATABASE_VERSION = 6;

    /*
    * generic constructor
    * */
    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /*
    * onCreate
    * sets up Walking the Dog table
    * */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WALKTHEDOG_TABLE =
                "CREATE TABLE " + PetContract.WalkTheDog.TABLE_NAME + " (" +
                        PetContract.WalkTheDog._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PetContract.WalkTheDog.DOG_NAME + " STRING NOT NULL, " +
                        PetContract.WalkTheDog.TIME_GOAL + " INTEGER NOT NULL, " +
                        PetContract.WalkTheDog.WALKS_GOAL + " INTEGER NOT NULL, " +
                        PetContract.WalkTheDog.DIST_GOAL + " INTEGER NOT NULL, " +
                        PetContract.WalkTheDog.CUR_TIME + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.CUR_WALKS + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.CUR_DIST + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.STREAK + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.BEST_STREAK + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.TOTAL_WALKS + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.TOTAL_TIME + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.TOTAL_DIST + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.TOTAL_DAYS + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.BEST_TIME + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.BEST_TIME_DAY + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.BEST_DIST_DAY + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.BEST_WALKS + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.LAST_DAY_SYNCED + " INTEGER DEFAULT 0, " +
                        PetContract.WalkTheDog.PROFILE_PIC + " BLOB, " +
                        PetContract.WalkTheDog.BEST_DIST + " INTEGER DEFAULT 0" +
                        ");";
        final String SQL_CREATE_ACHIEVEMENT_TABLE =
                "CREATE TABLE " + PetContract.Achievements.TABLE_NAME + " (" +
                        PetContract.Achievements._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        PetContract.Achievements.ACHIEVEMENT + " STRING NOT NULL, " +
                        PetContract.Achievements.COMPLETED + " INTEGER DEFAULT 0, " +
                        PetContract.Achievements.DATE + " INTEGER, " +
                        PetContract.Achievements.THRESHOLD + " INTEGER, " +
                        PetContract.Achievements.PROGRESS + " INTEGER, " +
                        PetContract.Achievements.TYPE + " INTEGER, " +
                        PetContract.Achievements.UPDATE_TRACKER + " INTEGER DEFAULT 0, " +
                        PetContract.Achievements.SEEN + " INTEGER" +
                        ");";
        sqLiteDatabase.execSQL(SQL_CREATE_WALKTHEDOG_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ACHIEVEMENT_TABLE);
        Achievements.achievementSetUp(sqLiteDatabase);

    }

    /*
    * onUpgrade
    * simply delete the old table and start fresh when updating db.
    * */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 5){
            final String SQL_CREATE_ACHIEVEMENT_TABLE =
                    "CREATE TABLE " + PetContract.Achievements.TABLE_NAME + " (" +
                            PetContract.Achievements._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            PetContract.Achievements.ACHIEVEMENT + " STRING NOT NULL, " +
                            PetContract.Achievements.COMPLETED + " INTEGER DEFAULT 0, " +
                            PetContract.Achievements.DATE + " INTEGER, " +
                            PetContract.Achievements.THRESHOLD + " INTEGER, " +
                            PetContract.Achievements.PROGRESS + " INTEGER, " +
                            PetContract.Achievements.TYPE + " INTEGER, " +
                            PetContract.Achievements.UPDATE_TRACKER + " INTEGER DEFAULT 0, " +
                            PetContract.Achievements.SEEN + " INTEGER" +
                            ");";
            sqLiteDatabase.execSQL(SQL_CREATE_ACHIEVEMENT_TABLE);
            Achievements.achievementSetUp(sqLiteDatabase);
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put(PetContract.Achievements.THRESHOLD, 30);
            cv.put(PetContract.Achievements.COMPLETED, 0);
            sqLiteDatabase.update(PetContract.Achievements.TABLE_NAME,
                    cv,
                    PetContract.Achievements.ACHIEVEMENT + "=?",
                    new String[]{"Streaking;30 day streak."});
        }
    }
}
