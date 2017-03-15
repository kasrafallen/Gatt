package net.puzzleco.gattapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.util.Util;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppText;
import net.puzzleco.gattapp.view.spark.SparkView;

public class DetailActivity extends BaseCompatActivity {
    private AppText textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setBar(this, R.color.dark);
        setContentView(createText());
    }

    private View createText() {
        float dimen[] = Util.getDimen(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        textView = new AppText(this);
        textView.setMovementMethod(new ScrollingMovementMethod());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams((int) (dimen[0] - dimen[0] / 5), -2);
        p.topMargin = (int) (dimen[1] / 30);
        p.bottomMargin = (int) (dimen[1] / 30);
        p.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setLayoutParams(p);
        textView.setGravity(Gravity.LEFT);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        textView.setHint("");
        textView.setTextColor(Color.DKGRAY);
        textView.setText("Fetching data ...");

        layout.addView(textView);
        layout.addView(spark());
        return layout;
    }

    private View spark() {
        SparkView view = new SparkView(this);
        view.setAnimateChanges(true);
//        view.setAdapter(new SignalAdaptor());
        return view;
    }

    private void registerReceiver(boolean flag) {
        if (flag) {
            IntentFilter filter = new IntentFilter();
            registerReceiver(mGattUpdateReceiver, filter);
        } else {
            unregisterReceiver(mGattUpdateReceiver);
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            textView.append("\n" + intent.getStringExtra("data"));
        }
    };

    @Override
    protected void onDestroy() {
        registerReceiver(false);
        super.onDestroy();
    }
}
