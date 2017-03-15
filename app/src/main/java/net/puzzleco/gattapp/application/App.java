package net.puzzleco.gattapp.application;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import net.puzzleco.gattapp.service.AppService;
import net.puzzleco.gattapp.util.ReminderUtil;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(AppService.TAG, "Application onCreate() returned: ");

        ReminderUtil.setAlarms(this);

        Intent intent = new Intent(this, AppService.class);
        startService(intent);
    }
}
