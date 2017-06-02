package online.cagocapps.walkingthedog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import online.cagocapps.walkingthedog.data.Achievements;
import online.cagocapps.walkingthedog.data.DbHelper;
import online.cagocapps.walkingthedog.data.PetContract;

public class AchievementsPage extends AppCompatActivity {
    private DbHelper DbHelp;
    private SQLiteDatabase dbWrite;
    private RecyclerView achRycView;
    private AchievementAdapter achievementAdapter;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        title = (TextView) findViewById(R.id.achieve_tv_title);
        achRycView = (RecyclerView) findViewById(R.id.achieve_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        achRycView.setLayoutManager(layoutManager);
        achRycView.setHasFixedSize(true);
        achievementAdapter = new AchievementAdapter();
        achRycView.setAdapter(achievementAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        int progress = 0;

        //Setting up DB
        DbHelp = new DbHelper(this);
        dbWrite = DbHelp.getWritableDatabase();
        Achievements.markAsSeen(dbWrite);

        Cursor cursor = dbWrite.query(
                PetContract.Achievements.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() == 0){
            Achievements.achievementSetUp(dbWrite);
            cursor.close();
            cursor = dbWrite.query(
                    PetContract.Achievements.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        String[] achievementsArray = new String[cursor.getCount()];
        double[] thresholdArray = new double[cursor.getCount()];
        double[] progressArray = new double[cursor.getCount()];
        double[] completedArray = new double[cursor.getCount()];
        double[] seenArray = new double[cursor.getCount()];
        double[] typeArray = new double[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()){
            achievementsArray[i] = cursor.getString(cursor.getColumnIndex(PetContract.Achievements.ACHIEVEMENT));
            thresholdArray[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.THRESHOLD));
            progressArray[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.PROGRESS));
            completedArray[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.COMPLETED));
            if (completedArray[i] != 0) progress++;
            seenArray[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.SEEN));
            typeArray[i] = cursor.getDouble(cursor.getColumnIndex(PetContract.Achievements.TYPE));
            i++;
        }
        cursor.close();
        title.setText("Achievements " + progress +"/" + completedArray.length);
        achievementAdapter.setAchList(achievementsArray, thresholdArray, progressArray, completedArray, seenArray, typeArray);
        achievementAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbWrite.close();
        finish();
    }
}
