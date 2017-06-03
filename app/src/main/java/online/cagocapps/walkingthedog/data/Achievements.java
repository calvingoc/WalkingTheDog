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
        long id;
        //Make your first dog
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Potted a Puppy;Make your first dog.");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 0);
        id = db.insertOrThrow(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Make five dogs
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Windowsill Lab Garden;Make five dogs.");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 0);
        id= db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Make ten dogs
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Backyard Pug Plot;Make ten dogs.");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 0);
        id = db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Make one hundred dogs
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Corgi Farmer;Make 100 dogs.");
        cv.put(PetContract.Achievements.THRESHOLD, 100);
        cv.put(PetContract.Achievements.TYPE, 0);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Walking the Dog;Take your first walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 1);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog ten times
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Stretching Your Legs;Take ten walks.");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 1);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog one hundred times
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Professional Dog Walker;Take one hundred walks.");
        cv.put(PetContract.Achievements.THRESHOLD, 100);
        cv.put(PetContract.Achievements.TYPE, 1);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog one thousand times
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Dog Walking Mastery;Take one thousand walks.");
        cv.put(PetContract.Achievements.THRESHOLD, 1000);
        cv.put(PetContract.Achievements.TYPE, 1);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour
        cv.put(PetContract.Achievements.ACHIEVEMENT, "You Could Have Watched an Episode of Criminal Minds;One hour of walk time.");
        cv.put(PetContract.Achievements.THRESHOLD, 60);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for ten hours
        cv.put(PetContract.Achievements.ACHIEVEMENT, "You Could Have Watched 2/3s of the Lord Of The Rings Extended Edition;Ten hours of walk time.");
        cv.put(PetContract.Achievements.THRESHOLD, 600);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for 24 hours
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Ain't Got Time to Sleep;24 hours of walk time.");
        cv.put(PetContract.Achievements.THRESHOLD, 1440);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 100 hour
        cv.put(PetContract.Achievements.ACHIEVEMENT, "You're All Out of Bubble Gum;100 hours of walk time.");
        cv.put(PetContract.Achievements.THRESHOLD, 6000);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a week
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Still Have Time Before the First Line in 2001: A Space Odyssey;A week of walk time.");
        cv.put(PetContract.Achievements.THRESHOLD, 10080);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 1000 hour
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Game Over Man, Game Over;1000 hours of walk time.");
        cv.put(PetContract.Achievements.THRESHOLD, 60000);
        cv.put(PetContract.Achievements.TYPE, 2);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a mile
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Can I Have My Shoes Back?;Walk a mile.");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for 10 miles
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Hot to Trot;Walk ten miles.");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a marathon
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Made It to Athens (Please Don't Die Now);Walk a marathon.");
        cv.put(PetContract.Achievements.THRESHOLD, 26.2);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 100 miles
        cv.put(PetContract.Achievements.ACHIEVEMENT, "How Do I Get Home?;Walk 100 miles.");
        cv.put(PetContract.Achievements.THRESHOLD, 100);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 1000 miles
        cv.put(PetContract.Achievements.ACHIEVEMENT, "The Iditarod? Show Off;Walk 1000 miles.");
        cv.put(PetContract.Achievements.THRESHOLD, 1000);
        cv.put(PetContract.Achievements.TYPE, 3);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for half an hour in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Warming Up;Half an hour in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 30);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Was It a Good Podcast?;Hour in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 60);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour and a half in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Hope You Brought Water;Hour and a half in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 90);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for two hours in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Up Next, Nap Time;2 hours in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 120);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for five hours in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "You Got Lost;5 hours in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 300);
        cv.put(PetContract.Achievements.TYPE, 4);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a mile in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "A Smile a Mile;Mile in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for two miles in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Gotta Hit That Distance Goal;2 miles in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 2);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 5k in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "It Was For Charity;5k in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 3);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for five miles in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "I Hope You Don't Have a Dachshund;5 miles in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a 10k
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Dog Athlete;10k in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 6);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for ten miles in one walk
        cv.put(PetContract.Achievements.ACHIEVEMENT, "WHY WON'T YOU POO?;10 miles in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //take your dog on a marathon
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Do You Bring Up Your Rescue Dog Or Your Marathon First?;26.2 miles in one walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 26.2);
        cv.put(PetContract.Achievements.TYPE, 5);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //jogger
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Power Walker;3 miles an hour in a walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 3);
        cv.put(PetContract.Achievements.TYPE, 6);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //runner
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Don't Forget To Pee;5 miles an hour in a walk.");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 6);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //hit your goals
        cv.put(PetContract.Achievements.ACHIEVEMENT, "I Like Stars;Hit your goals.");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 11);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //five day streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Star Gazing;5 day streak.");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //7 streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "The Perfect Week;7 day streak.");
        cv.put(PetContract.Achievements.THRESHOLD, 7);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //10 day streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "In the Habit;10 day streak.");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //30 day streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Streaking;30 day streak.");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //365 streak
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Luckiest Dog in the World;365 day streak.");
        cv.put(PetContract.Achievements.THRESHOLD, 365);
        cv.put(PetContract.Achievements.TYPE, 7);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Getting Your Daily Dose of Vitamin D;Walk an hour in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 60);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for an hour and a half in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Gameification, Heard of It?;Walk 1.5 hours in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 90);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for two hours in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "The Kids Put On The Teletubbies Movies Again;2 hours in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 120);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for five hours in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "How's Retirement?;5 hours in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 300);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for 8 hours in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Bring Your Dog To Work Day;Walk 8 hours in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 480);
        cv.put(PetContract.Achievements.TYPE, 8);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for a mile in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Round the Block;Mile in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 1);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for two miles in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "There and Back Again;2 miles in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 2);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for five miles in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Summer Has Finally Come;5 miles in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //Walk your dog for ten miles in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Day at the Dog Park;10 miles in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 10);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //take your dog on a marathon in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "Took You All Day?;26.2 miles in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 26.2);
        cv.put(PetContract.Achievements.TYPE, 9);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
        //take your dog on five walks in a day
        cv.put(PetContract.Achievements.ACHIEVEMENT, "I'm Sorry Your Dog Has IBS;Five walks in a day.");
        cv.put(PetContract.Achievements.THRESHOLD, 5);
        cv.put(PetContract.Achievements.TYPE, 10);
        db.insert(PetContract.Achievements.TABLE_NAME, null, cv);
        cv.clear();
    }

    public static void updateAchievements(int type, double value, SQLiteDatabase db){
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
            double prog = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.PROGRESS)) + value;
            cv.put(PetContract.Achievements.PROGRESS, prog);
            if (prog >= cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.THRESHOLD)) &&
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

    public static void markAsSeen(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        Cursor cursor = db.query(
                PetContract.Achievements.TABLE_NAME,
                null,
                null,
                null,
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
