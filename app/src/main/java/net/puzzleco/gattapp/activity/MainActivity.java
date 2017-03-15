package net.puzzleco.gattapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;

import com.google.gson.Gson;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.attributes.BluetoothAttributes;
import net.puzzleco.gattapp.init.InitMain;
import net.puzzleco.gattapp.service.AppService;
import net.puzzleco.gattapp.util.ViewUtil;

public class MainActivity extends BaseCompatActivity {
    private InitMain init;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setBar(this, R.color.dark);
        init = new InitMain(this);
        setContentView(init.getView());
        setBroad();
        init.updateView(null);
        setResult(RESULT_OK);
    }

    private void setBroad() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver_medical_status, new IntentFilter(AppService.EXIT));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver_problem, new IntentFilter(BluetoothAttributes.INTENT_FAILED_));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver_data, new IntentFilter(BluetoothAttributes.INTENT_DATA_));
    }

    @Override
    public void onBackPressed() {
        if (init.layout.isDrawerOpen(Gravity.LEFT)) {
            init.layout.closeDrawer(Gravity.LEFT);
            return;
        }
        super.onBackPressed();
    }

    private BroadcastReceiver receiver_data = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] data = intent.getByteArrayExtra(BluetoothAttributes.DATA_);
            if (data == null) {
                return;
            }
            init.updateView(data);
            Log.d(AppService.TAG, "onReceive() receiver_data: " + new Gson().toJson(data));
        }
    };

    private BroadcastReceiver receiver_problem = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(AppService.TAG, "onReceive() receiver_problem: " + "error");
        }
    };

    private BroadcastReceiver receiver_medical_status = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (init != null && init.initDrawer != null)
                init.initDrawer.changeSwitch();
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver_data);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver_problem);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver_medical_status);
        super.onDestroy();
    }
}
