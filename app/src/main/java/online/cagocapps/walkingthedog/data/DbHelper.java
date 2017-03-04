package online.cagocapps.walkingthedog.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import online.cagocapps.walkingthedog.MainActivity;

/**
 * Creates
 * Created by cgehredo on 2/20/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "walkTheDog.db";
    public static final int DATABASE_VERSION = 2;

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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
                        PetContract.WalkTheDog.BEST_DIST + " INTEGER DEFAULT 0" +
                        ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WALKTHEDOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PetContract.WalkTheDog.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
