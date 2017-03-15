package net.puzzleco.gattapp.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import net.puzzleco.gattapp.attributes.BluetoothAttributes;
import net.puzzleco.gattapp.attributes.GattAttributes;

import java.util.ArrayList;
import java.util.UUID;

public class GattService extends Service {
    private final static String TAG = "App***Gatt";

    public static BluetoothDevice device;
    private BluetoothGatt bluetoothGatt;

    private ArrayList<Byte> data = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        if (GattService.device != null) {
            Log.d(TAG, "connectDevice: ");
            bluetoothGatt = device.connectGatt(this, false, gattCallback);
        } else {
            fail();
        }
        return START_NOT_STICKY;
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery: " + bluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "Disconnected from GATT server.");
                fail();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onServicesDiscovered SUCCESS: ");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doRead();
                    }
                }).start();
            } else {
                Log.d(TAG, "onServicesDiscovered FAIL: ");
                fail();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicRead received: " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcast(characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "onCharacteristicChanged received: ");
            broadcast(characteristic);
        }
    };

    private void doRead() {
        logAvailable();
        Log.d(TAG, "doRead starts: *****************");

        BluetoothGattService service = bluetoothGatt.getService(GattAttributes.SERVICE);
        Log.d(TAG, "service: " + (service != null));
        if (service == null) {
            fail();
            return;
        }
        getData(GattAttributes.CHARACTERISTICS_1, service);
//        getData(GattAttributes.CHARACTERISTICS_2, service);
        success();
    }

    private void getData(UUID id, BluetoothGattService service) {
        BluetoothGattCharacteristic characteristics = service.getCharacteristic(id);
        Log.d(TAG, "characteristics: " + (characteristics != null));
        if (characteristics == null) {
            fail();
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristics, true);
        BluetoothGattDescriptor descriptor = characteristics.getDescriptor(GattAttributes.CONFIG);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
    }

    private void success() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BluetoothAttributes.INTENT_CONNECTED_));
    }

    private void fail() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BluetoothAttributes.INTENT_FAILED_));
        stopSelf();
    }

    private void broadcast(BluetoothGattCharacteristic characteristics) {
        if (characteristics == null || characteristics.getValue() == null) {
            return;
        }
        if (data.size() >= 200) {
            Intent intent = new Intent(BluetoothAttributes.INTENT_DATA_);
            intent.putExtra(BluetoothAttributes.DATA_, arrayFromList(data));
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            data.clear();
        }
        for (byte b : characteristics.getValue()) {
            data.add(b);
        }
    }

    private byte[] arrayFromList(ArrayList<Byte> data) {
        Byte[] array = data.toArray(new Byte[data.size()]);
        byte[] byteArray = new byte[array.length];

        for (int i = 0; i < array.length; i++) {
            byteArray[i] = array[i];
        }
        return byteArray;
    }

    private void logAvailable() {
        for (BluetoothGattService service : bluetoothGatt.getServices()) {
            Log.d(TAG, "Service() returned: " + service.getUuid().toString());
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                Log.d(TAG, "{{ Characteristic() returned: " + characteristic.getUuid().toString() + " }}");
                for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                    Log.d(TAG, "{{{{{ Descriptor() returned: " + descriptor.getUuid().toString() + " }}}}}");
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        close();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved: ");
        close();
    }

    public void close() {
        Log.d(TAG, "close: ");
        device = null;
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt = null;
    }
}
