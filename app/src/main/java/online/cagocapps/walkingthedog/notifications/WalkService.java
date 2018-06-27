package online.cagocapps.walkingthedog.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;

import online.cagocapps.walkingthedog.R;
import online.cagocapps.walkingthedog.TrackWalk;

public class WalkService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i1) {
        if (intent.getBooleanExtra("start",false)){
            Intent walkIntent = new Intent(this, TrackWalk.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, walkIntent, 0);
            Notification notification = new Notification.Builder(this)
                    .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .setSmallIcon(R.drawable.ic_pets_black_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.app_thumbnail))
                    .setContentTitle(this.getString(R.string.notification_title))
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(6969,notification);
        }
        else {
            stopForeground(true);
            stopSelf();
        }
        return super.onStartCommand(intent, i, i1);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
