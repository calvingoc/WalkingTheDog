package online.cagocapps.walkingthedog.data;

import android.provider.BaseColumns;

/**
 * Contract for the walking the dog app table.
 * Holds Name, walking time goal, current walk time, goal number of walks, current number of walks
 * number of days goals have been met.
 * Created by cgehredo on 2/20/2017.
 */

public class PetContract {
    public static final class WalkTheDog implements BaseColumns{
        public static final String TABLE_NAME = "walkthedog";
        public static final String DOG_NAME = "name";
        public static final String TIME_GOAL = "timeGoals";
        public static final String WALKS_GOAL = "numWalksGoals";
        public static final String DIST_GOAL = "distanceGoal";
        public static final String CUR_TIME = "walkTime";
        public static final String CUR_WALKS = "numWalks";
        public static final String CUR_DIST = "curentDistance";
        public static final String STREAK = "streak";
        public static final String TOTAL_WALKS = "totalWalks";
        public static final String TOTAL_TIME = "totalTime";
        public static final String TOTAL_DIST = "totalDist";
        public static final String TOTAL_DAYS ="totalDays";
        public static final String BEST_TIME = "bestTimeWalk";
        public static final String BEST_DIST = "BestDistWalk";
        public static final String BEST_TIME_DAY = "bestTimeDay";
        public static final String BEST_DIST_DAY = "bestDistDay";
        public static final String BEST_WALKS = "bestWalks";
        public static final String BEST_STREAK = "bestStreak";
        public static final String LAST_DAY_SYNCED = "lastDaySynced";
        public static final String PROFILE_PIC = "profilePic";
    }

    public static final class Achievements implements BaseColumns{
        public static final String TABLE_NAME = "wtdachievements";
        public static final String ACHIEVEMENT = "achievement";
        public static final String COMPLETED = "completed";
        public static final String DATE = "date";
        public static final String SEEN = "seen";
        public static final String THRESHOLD = "threshold";
        public static final String PROGRESS = "progress";
        public static final String TYPE = "type";
        public static final String UPDATE_TRACKER = "tracker";
    }



}
