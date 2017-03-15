package net.puzzleco.gattapp.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.LaunchActivity;
import net.puzzleco.gattapp.util.Util;

public class AppService extends Service {

    private static final int NOTIFICATION_ID = 1;

    public static final String TAG = "App***Service";
    public static final String EXIT = "VITAL_TRACER_CLOSE_SERVICE";
    public static final String RESTART = "VITAL_TRACER_RESTART_SERVICE";

    private MediaPlayer fakePlayer;

    private int start_counter;
    private int test_counter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() returned: " + (++start_counter));
        runShittyService();
        if (Util.getMedicalStatus(this)) {
            runForeground();
        } else {
            stopForeground();
        }
        return START_NOT_STICKY;
    }

    private void runShittyService() {
        MediaPlayer mediaPlayer = getPlayer();
        if (mediaPlayer.isPlaying()) {
            return;
        }
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://net.puzzleco.gattapp/" + R.raw.blank));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.prepareAsync();
//            test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void test() {
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "run() returned: " + (++test_counter));
//                handler.postDelayed(this, 5000);
//            }
//        }, 1000);
//    }

    private MediaPlayer getPlayer() {
        if (fakePlayer != null) {
            return fakePlayer;
        }
        fakePlayer = new MediaPlayer();
        fakePlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        return fakePlayer;
    }

    private void runForeground() {
        startForeground(NOTIFICATION_ID, createNotification());
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(createView())
                .setOngoing(true)
                .setAutoCancel(false)
                .build();
    }

    private RemoteViews createView() {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_layout);
        views.setOnClickPendingIntent(R.id.notification_exit, PendingIntent.getBroadcast(this, 101, new Intent(EXIT), 0));

        Intent intent = new Intent(this, LaunchActivity.class);
        intent.putExtra(AppService.TAG, Intent.METADATA_DOCK_HOME);
        views.setOnClickPendingIntent(R.id.notification_layout, PendingIntent.getActivity(this, 102, intent, 0));
        return views;
    }

    private void stopForeground() {
        stopForeground(true);
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory() returned: ");
        super.onLowMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() returned: ");
    }

    @Override
    public boolean stopService(Intent name) {
        Log.d(TAG, "stopService() returned: ");
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() returned: ");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() returned: ");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() returned: ");
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved()");
        if (fakePlayer != null) {
            fakePlayer.release();
            fakePlayer = null;
        }
        stopSelf();
        sendBroadcast(new Intent(RESTART));
    }
}