package net.puzzleco.gattapp.init;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.SignalActivity;
import net.puzzleco.gattapp.adaptor.SignalAdaptor;
import net.puzzleco.gattapp.util.Util;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppButton;
import net.puzzleco.gattapp.view.AppText;
import net.puzzleco.gattapp.view.AppToolbar;
import net.puzzleco.gattapp.view.spark.SparkView;

import java.util.ArrayList;

public class InitSignal {
    private final static int SIGNAL_ID = +546982;

    private float[] dimen;
    private SignalActivity context;
    private String mode;
    private int signal_size;

    private int counter;

    private int id_counter;
    private ArrayList<SignalAdaptor> tagList = new ArrayList<>();

    private AppText tv;

    public InitSignal(SignalActivity signalActivity, String action) {
        this.context = signalActivity;
        this.dimen = Util.getDimen(context);
        this.mode = action;
        this.signal_size = ViewUtil.toPx(100, context);
    }

    public View getView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        layout.setBackgroundResource(R.color.base);
        layout.addView(toolbar());
        layout.addView(view());
        return layout;
    }

    private View view() {
        ScrollView view = new ScrollView(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        view.setHorizontalScrollBarEnabled(false);
        view.setVerticalScrollBarEnabled(false);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        switch (mode) {
            case SignalActivity.ECG:
                layout.addView(signal());
                layout.addView(line());
                break;
            case SignalActivity.PPG:
                layout.addView(signal());
                layout.addView(line());
                layout.addView(signal());
                layout.addView(line());
                layout.addView(signal());
                break;
            default:
                layout.addView(signal());
                layout.addView(line());
                break;
        }
        view.addView(layout);
        return view;
    }

    private View line() {
        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtil.toPx(1, context)));
        view.setBackgroundColor(Color.LTGRAY);
        return view;
    }

    private View signal() {
        SparkView view = new SparkView(context);
        view.setId(+((id_counter++) + SIGNAL_ID));
        Log.d("PREBUG", "signal() returned: " + view.getId());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, signal_size);
        p.setMargins(0, ViewUtil.toPx(10, context), 0, ViewUtil.toPx(10, context));
        view.setLayoutParams(p);
        view.setAnimateChanges(true);
        switch (mode) {
            case SignalActivity.ECG:
                view.setBaseLineColor(context.getResources().getColor(R.color.lite_6));
                view.setLineColor(context.getResources().getColor(R.color.dark_6));
                view.setScrubLineColor(Color.RED);
                break;
            case SignalActivity.PPG:
                view.setBaseLineColor(context.getResources().getColor(R.color.lite_10));
                view.setLineColor(context.getResources().getColor(R.color.dark_10));
                view.setScrubLineColor(Color.BLUE);
                break;
            default:
                view.setBaseLineColor(context.getResources().getColor(R.color.lite_2));
                view.setLineColor(context.getResources().getColor(R.color.dark_5));
                view.setScrubLineColor(Color.BLUE);
                break;
        }
        SignalAdaptor adaptor = new SignalAdaptor(dimen, mode, context);
        ArrayList<Float> arrayList = new ArrayList<>();
        adaptor.setData(arrayList);
        view.setAdapter(adaptor);
        view.setTag(adaptor);
        return view;
    }

    private View toolbar() {
        AppToolbar toolbar = new AppToolbar(context);
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtil.getToolbarSize(context)));
        switch (mode) {
            case SignalActivity.ECG:
                toolbar.setBackgroundResource(R.color.lite_6);
                break;
            default:
                toolbar.setBackgroundResource(R.color.lite_10);
                break;
        }

        tv = new AppText(context);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(-2, -2);
        p.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        p.leftMargin = ViewUtil.toPx(8, context);
        tv.setTextSize(1, 18);
        tv.setText(mode);
        tv.setTextColor(Color.WHITE);

        AppButton button = new AppButton(context);
        button.setBackgroundResource(R.drawable.ic_navigation);
        Toolbar.LayoutParams pp = new Toolbar.LayoutParams(ViewUtil.getToolbarSize(context), ViewUtil.getToolbarSize(context));
        pp.gravity = Gravity.LEFT;
        button.setLayoutParams(pp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });

        toolbar.addView(button.getView());
        toolbar.addView(tv, p);
        return toolbar;
    }

    public void update(Float[] intArrayExtra) {
        counter++;
        tv.setText("counter : " + counter + "    byte[] size : " + intArrayExtra.length);
        Log.d("InitSignal", "update() returned: " + new Gson().toJson(intArrayExtra));
        if (tagList.size() != 0) {
            for (SignalAdaptor tag : tagList) {
                setData(intArrayExtra, tag);
            }
        } else {
            for (int i = 0; i < 10; i++) {
                Log.d("PREBUG", "update() returned: " + +(SIGNAL_ID + i));
                SparkView sparkView = (SparkView) context.findViewById(+(SIGNAL_ID + i));
                if (sparkView != null && sparkView.getTag() != null) {
                    SignalAdaptor tag = (SignalAdaptor) sparkView.getTag();
                    tagList.add(tag);
                }
            }
            update(intArrayExtra);
        }
    }

    private void setData(Float[] data, SignalAdaptor tag) {
        tag.setData(data);
        tag.notifyDataSetChanged();
    }
}
