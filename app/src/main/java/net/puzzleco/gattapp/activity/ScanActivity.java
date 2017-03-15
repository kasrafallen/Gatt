package net.puzzleco.gattapp.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.adaptor.DeviceAdaptor;
import net.puzzleco.gattapp.init.InitScan;
import net.puzzleco.gattapp.service.GattService;
import net.puzzleco.gattapp.util.ViewUtil;

import java.util.ArrayList;

public class ScanActivity extends BaseCompatActivity implements BluetoothAdapter.LeScanCallback {
    private static final int SCAN_PERIOD = 7 * 1000;

    private static final int REQUEST_ENABLE_BT = 254;
    private static final int REQUEST_DATA = 256;

    private BluetoothAdapter bluetoothAdapter;
    private Handler handler;

    private DeviceAdaptor adaptor;
    private ArrayList<BluetoothDevice> list;
    private InitScan init;

    private boolean connecting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setBar(this, R.color.dark);
        init = new InitScan(this);
        setContentView(init.getView());
        setAdaptor();
    }

    public void setAdaptor() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
            enableAdaptor();
        }
    }

    private void enableAdaptor() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            scanLeDevice();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            enableAdaptor();
        } else if (requestCode == REQUEST_DATA) {
            connecting = false;
        }
    }

    private void scanLeDevice() {
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter.stopLeScan(ScanActivity.this);
                init.offSearch();
            }
        }, SCAN_PERIOD);
        bluetoothAdapter.startLeScan(this);
        init.onSearch();
    }

    @Override
    public void onLeScan(final BluetoothDevice device, int i, byte[] bytes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (adaptor == null) {
                    adaptor = new DeviceAdaptor(ScanActivity.this, list, init.dimen);
                }
                if (init.listView.getAdapter() == null) {
                    init.listView.setAdapter(adaptor);
                }
                updateList(device);
            }
        });
    }

    private void updateList(BluetoothDevice device) {
        for (BluetoothDevice bluetoothDevice : list) {
            Log.d("BUG", "updateList() returned: " + device.getAddress());
            Log.d("BUG", "updateList() returned: " + bluetoothDevice.getAddress());
            if (bluetoothDevice.getAddress().equalsIgnoreCase(device.getAddress())) {
                return;
            }
        }
        list.add(device);

        ArrayList<BluetoothDevice> copy = new ArrayList<>();
        copy.addAll(list);

        list.clear();
        list.addAll(copy);

        adaptor.notifyDataSetChanged();
    }

    public void executeDevice(BluetoothDevice device) {
        if (this.connecting) {
            Toast.makeText(ScanActivity.this, "Please wait...", Toast.LENGTH_LONG).show();
            return;
        }
        this.connecting = true;

        Intent intent = new Intent(this, SignalActivity.class);
        intent.setAction(SignalActivity.TEST);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, REQUEST_DATA);

        GattService.device = device;
        startService(new Intent(ScanActivity.this, GattService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
