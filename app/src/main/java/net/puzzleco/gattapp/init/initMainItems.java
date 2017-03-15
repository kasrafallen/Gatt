package net.puzzleco.gattapp.init;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.ChartActivity;
import net.puzzleco.gattapp.activity.MainActivity;
import net.puzzleco.gattapp.activity.SignalActivity;
import net.puzzleco.gattapp.util.CalendarUtil;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppText;

public class initMainItems implements View.OnClickListener {
    private static final int TEXT_ID = +88500;

    private MainActivity context;
    private float dimen[];
    private float width;
    private float height;

    private AppText tv1;
    private AppText tv5;
    private AppText tv6;
    private AppText tv7;
    private AppText tv9;
    private AppText tv10;

    public initMainItems(MainActivity context, float[] dimen, float width, float height) {
        this.context = context;
        this.dimen = dimen;
        this.width = width;
        this.height = height;
    }

    public View getText(int position) {
        AppText textView = new AppText(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setElevation(5);
            textView.setBackground(ViewUtil.createButtonRipple(context));
        } else {
            ViewUtil.setBackground(textView, context);
        }
        textView.setId(+(TEXT_ID + position));
        textView.setLayoutParams(getParams(position));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/start.ttf"));
        textView.setSingleLine(getSingleLine(position));
        textView.setTextSize(0, getTextSize(position));
        textView.setTextColor(getColor(position));
        if (position == 3 || position == 4 || position == 7 || position == 8 || position == 11 || position == 12 || position == 13) {
            textView.setText(getStringSingleLine(position, ""));
        } else if (position == 2) {
            textView.setText(getDate());
        } else {
            textView.setText(getStringWithLine(position, ""));
        }
        textView.setOnClickListener(this);
        //        textView.setBackgroundColor(Color.BLUE);
        return textView;
    }

    private ViewGroup.LayoutParams getParams(int position) {
        float size = 11f * this.width / 40;
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams((int) size, (int) size);
        switch (position) {
            case 1:
                p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                p.leftMargin = (int) (width / 25);
                p.topMargin = (int) (height / 40);
                break;
            case 2:
                p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                p.leftMargin = (int) (width / 3 + width / 33);
                p.topMargin = (int) (height / 40);
                break;
            case 3:
                p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                p.rightMargin = (int) (width / 26);
                p.topMargin = (int) (height / 40);
                break;
            case 4:
                p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                p.topMargin = (int) (height / 5 + height / 60);
                p.leftMargin = (int) (width / 4 - width / 18);
                break;
            case 5:
                p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                p.topMargin = (int) (height / 5 + height / 60);
                p.rightMargin = (int) (width / 4 - width / 18);
                break;
            case 6:
                p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                p.leftMargin = (int) (width / 27);
                p.topMargin = (int) (height / 2 - height / 11);
                break;
            case 7:
                p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                p.leftMargin = (int) (width / 3 + width / 34);
                p.topMargin = (int) (height / 2 - height / 11);
                break;
            case 8:
                p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                p.rightMargin = (int) (width / 26);
                p.topMargin = (int) (height / 2 - height / 11);
                break;
            case 9:
                p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                p.topMargin = (int) (height / 2 + height / 10);
                p.leftMargin = (int) (width / 4 - width / 18);
                break;
            case 10:
                p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                p.topMargin = (int) (height / 2 + height / 10);
                p.rightMargin = (int) (width / 4 - width / 18);
                break;
            case 11:
                p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                p.leftMargin = (int) (width / 3 + width / 35);
                p.bottomMargin = (int) (height / 45);
                break;
            case 12:
                p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                p.leftMargin = (int) (width / 35);
                p.bottomMargin = (int) (height / 45);
                break;
            case 13:
                p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                p.rightMargin = (int) (width / 25);
                p.bottomMargin = (int) (height / 45);
                break;
        }
        return p;
    }

    private String getStringSingleLine(int position, String data) {
        switch (position) {
            case 3:
                return "BP";
            case 4:
                return "PPG";
            case 7:
                return data + "º C";
            case 8:
                return "ECG";
            case 11:
                return "Sleep";
            case 12:
                return "+Glucose";
            default:
                return "+Weight";
        }
    }

    private SpannableString getStringWithLine(int position, String data) {
        String text = "";
        switch (position) {
            case 1:
                text = data + "\n" + "CAL";
                break;
            case 5:
                text = data + "\n" + "SPO₂";
                break;
            case 6:
                text = data + "\n" + "RR";
                break;
            case 9:
                text = data + "\n" + "STEPS";
                break;
            case 10:
                text = data + "\n" + "HR";
                break;
        }
        SpannableString ss = new SpannableString(text);
        int mini = (int) (width / 27);
        ss.setSpan(new AbsoluteSizeSpan(mini, false), ss.toString().indexOf("\n"), ss.length(), 0);
        return ss;
    }

    private SpannableString getDate() {
        String text = CalendarUtil.getDay() + "\n" + CalendarUtil.getDate() + CalendarUtil.getMonth();
        SpannableString ss = new SpannableString(text);
        int medium = (int) (width / 22);
        ss.setSpan(new AbsoluteSizeSpan(medium, false), 0, ss.toString().indexOf("\n"), 0);
        return ss;
    }

    private float getTextSize(int position) {
        if (position == 13 || position == 12) {
            return width / 24;
        } else if (position == 11) {
            return width / 21;
        } else if (position == 2) {
            return width / 18;
        } else {
            return width / 11;
        }
    }

    private boolean getSingleLine(int position) {
        return position == 3 || position == 4 || position == 7 || position == 8 || position == 11 || position == 12 || position == 13;
    }

    private int getColor(int position) {
        if (position == 13 || position == 12) {
            return context.getResources().getColor(R.color.theme);
        } else {
            return Color.WHITE;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        switch (view.getId() - TEXT_ID) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                intent.setClass(context, ChartActivity.class);
                intent.setAction(ChartActivity.BLOOD_pressure);
                context.startActivity(intent);
                break;
            case 4:
                intent.setClass(context, SignalActivity.class);
                intent.setAction(SignalActivity.PPG);
                context.startActivity(intent);
                break;
            case 5:
                intent.setClass(context, ChartActivity.class);
                intent.setAction(ChartActivity.SPO2);
                context.startActivity(intent);
                break;
            case 6:
                intent.setClass(context, ChartActivity.class);
                intent.setAction(ChartActivity.RR);
                context.startActivity(intent);
                break;
            case 7:
                intent.setClass(context, ChartActivity.class);
                intent.setAction(ChartActivity.BODY_temperature);
                context.startActivity(intent);
                break;
            case 8:
                intent.setClass(context, SignalActivity.class);
                intent.setAction(SignalActivity.ECG);
                context.startActivity(intent);
                break;
            case 9:
                break;
            case 10:
                intent.setClass(context, ChartActivity.class);
                intent.setAction(ChartActivity.HR);
                context.startActivity(intent);
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
        }
    }

    public void update(byte[] data) {
        if (tv1 == null) {
            tv1 = (AppText) context.findViewById(+(TEXT_ID + 1));
            tv5 = (AppText) context.findViewById(+(TEXT_ID + 5));
            tv6 = (AppText) context.findViewById(+(TEXT_ID + 6));
            tv7 = (AppText) context.findViewById(+(TEXT_ID + 7));
            tv9 = (AppText) context.findViewById(+(TEXT_ID + 9));
            tv10 = (AppText) context.findViewById(+(TEXT_ID + 10));
        }

        tv1.setText(getStringWithLine(1, "760"));
        tv5.setText(getStringWithLine(5, "98%"));
        tv6.setText(getStringWithLine(6, "15"));
        tv9.setText(getStringWithLine(9, "2K"));
        tv10.setText(getStringWithLine(10, "60"));

        tv7.setText(getStringSingleLine(7, "37"));
    }
}
