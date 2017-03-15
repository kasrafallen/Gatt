package net.puzzleco.gattapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewTreeObserver;

import net.puzzleco.gattapp.init.InitLaunch;
import net.puzzleco.gattapp.service.AppService;
import net.puzzleco.gattapp.util.Util;

public class LaunchActivity extends BaseActivity {
    private static final int BT_REQUEST = 578;
    private static final int REQUEST_CODE = 225;

    private InitLaunch init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init = new InitLaunch(this);
        setContentView(init.getView());
        boolean flag = checkDevice();
        if (flag) {
            checkApp();
        } else {
            onBackPressed();
        }
    }

    private boolean checkDevice() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            init.runError();
            return false;
        }
        return true;
    }

    private void checkApp() {
        if (Util.isDimen(this)) {
            checkPermission();
        } else {
            setObserver();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                run();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                                , Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.BLUETOOTH_ADMIN
                                , Manifest.permission.BLUETOOTH}, BT_REQUEST);
            }
        } else {
            run();
        }
    }

    private void run() {
        String extra = getIntent().getStringExtra(AppService.TAG);
//        Intent intent = new Intent(this, ScanActivity.class);
        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        if (extra != null && extra.equals(Intent.METADATA_DOCK_HOME)) {
            intent.setClass(this, MedicalActivity.class);
            intent.setAction(Intent.METADATA_DOCK_HOME);
        } else {
            intent.setClass(this, MainActivity.class);
        }
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == BT_REQUEST) {
            checkPermission();
        }
    }

    private void setObserver() {
        init.layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                init.layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Util.setDimen(LaunchActivity.this, new float[]{init.layout.getWidth(), init.layout.getHeight()});
                checkApp();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(AppService.TAG, "onActivityResult() returned: ");
            onBackPressed();
        }
    }
}
