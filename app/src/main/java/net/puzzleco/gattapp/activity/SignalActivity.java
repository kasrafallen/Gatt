package net.puzzleco.gattapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.attributes.BluetoothAttributes;
import net.puzzleco.gattapp.init.InitSignal;
import net.puzzleco.gattapp.util.ViewUtil;

import java.util.ArrayList;
import java.util.Random;

public class SignalActivity extends BaseCompatActivity {

    public static final String PPG = "PPG";
    public static final String ECG = "ECG";
    public static final String TEST = "TEST";
    private InitSignal initSignal;

    public boolean isClosed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setBar(this, getColor(getIntent().getAction()));
        initSignal = new InitSignal(this, getIntent().getAction());
        setContentView(initSignal.getView());
        setSignals();
        setBroad();
    }

    private void setBroad() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAttributes.INTENT_CONNECTED_);
        filter.addAction(BluetoothAttributes.INTENT_FAILED_);
        filter.addAction(BluetoothAttributes.INTENT_DATA_);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    private int getColor(String action) {
        switch (action) {
            case PPG:
                return R.color.dark_10;
            default:
                return R.color.dark_6;
        }
    }

    private void setSignals() {
        try {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initSignal.update(getTest());
                    handler.postDelayed(this, 1000);
                }
            }, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Float[] getTest() {
        ArrayList<Float> list = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            if (i == 10) {
                list.add((float) new Random().nextInt(50) + 200);
            } else {
                list.add((float) new Random().nextInt(70));
            }
        }
        Float[] stockArr = new Float[list.size()];
        stockArr = list.toArray(stockArr);
        return stockArr;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == null) {
                return;
            }
            switch (intent.getAction()) {
                case BluetoothAttributes.INTENT_CONNECTED_:
                    Toast.makeText(SignalActivity.this, "Device is connected through characteristics", Toast.LENGTH_LONG).show();
                    break;
                case BluetoothAttributes.INTENT_FAILED_:
                    Toast.makeText(SignalActivity.this, "Device is faild to send characteristics", Toast.LENGTH_LONG).show();
                    break;
                case BluetoothAttributes.INTENT_DATA_:
                    updateScreen(intent.getByteArrayExtra(BluetoothAttributes.DATA_));
                    break;
            }
        }
    };

    private void updateScreen(byte[] data) {
        Float[] array = new Float[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = Float.valueOf((int) data[i]);
        }
        initSignal.update(array);
    }

    @Override
    protected void onDestroy() {
        isClosed = true;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void finish() {
        isClosed = true;
        super.finish();
    }
}
