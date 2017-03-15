package net.puzzleco.gattapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.puzzleco.gattapp.util.ReminderUtil;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                ReminderUtil.setAlarms(context);
            }
        }
    }
}
