package net.puzzleco.gattapp.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

import net.puzzleco.gattapp.instance.ReminderInstance;
import net.puzzleco.gattapp.model.MedicineModel;
import net.puzzleco.gattapp.receiver.AppReceiver;

import java.util.ArrayList;

public class ReminderUtil {
    private final static String TAG = "App***RemUtil";

    public static void put(MedicineModel medicineModel, Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager == null) {
            return;
        }
        long time = calculate(medicineModel);
        Log.d(TAG, "put() calculate: " + time);

        Intent downloader = new Intent(context, AppReceiver.class);
        downloader.setAction(new Gson().toJson(medicineModel));
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context, medicineModel.getIndex()
                , downloader, PendingIntent.FLAG_CANCEL_CURRENT);

        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + time, time, recurringDownload);
        Log.d(TAG, "put() getIndex: " + medicineModel.getIndex());
    }

    private static long calculate(MedicineModel medicineModel) {
        long millis = medicineModel.getHour() * 60 * 60 * 1000;
        millis = millis + medicineModel.getMinute() * 60 * 1000;
        return millis;
    }

    public static void cancel(MedicineModel medicineModel, Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager == null) {
            return;
        }
        Intent downloader = new Intent(context, AppReceiver.class);
        downloader.setAction(new Gson().toJson(medicineModel));
        PendingIntent recurringDownload = PendingIntent.getBroadcast(context, medicineModel.getIndex()
                , downloader, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(recurringDownload);
        Log.d(TAG, "cancel() returned: " + medicineModel.getIndex());
    }


    public static void setAlarms(Context context) {
        ArrayList<MedicineModel> medicineModels = ReminderInstance.getAll(context);
        for (MedicineModel medicineModel : medicineModels) {
            ReminderUtil.put(medicineModel, context);
        }
    }
}
