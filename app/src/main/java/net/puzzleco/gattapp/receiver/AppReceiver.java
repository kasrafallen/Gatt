package net.puzzleco.gattapp.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import com.google.gson.Gson;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.model.MedicineModel;
import net.puzzleco.gattapp.service.AppService;
import net.puzzleco.gattapp.util.Util;

public class AppReceiver extends BroadcastReceiver {

    private final static String TAG = "App***Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive() returned: " + intent.getAction());
        if (intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(AppService.EXIT)) {
            Util.setMedicalStatus(context, false);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            Intent service = new Intent(context, AppService.class);
            context.startService(service);
        } else if (intent.getAction().equalsIgnoreCase(AppService.RESTART)) {
            context.startService(new Intent(context.getApplicationContext(), AppService.class));
        } else {
            showNotification(intent.getAction(), context);
        }
    }

    private void showNotification(String action, Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) {
            return;
        }
        MedicineModel model = new Gson().fromJson(action, MedicineModel.class);
        if (model == null) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(nameSpan(model.getName()) + " / " + model.getNote())
                .setContentText("It's a reminder")
                .setAutoCancel(true)
                .setTicker(nameSpan(model.getName()) + " / " + model.getNote())
                .setSmallIcon(R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        manager.notify(model.getIndex(), builder.build());
    }

    private SpannableString nameSpan(String name) {
        SpannableString string = new SpannableString(name);
        string.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, name.length(), 0);
        return string;
    }
}
