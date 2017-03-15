package net.puzzleco.gattapp.adaptor;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.ChartActivity;
import net.puzzleco.gattapp.data.ChartData;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppText;

public class ChartAdaptor extends PagerAdapter {

    private final static String[] TABS = new String[]{"Day", "Week", "Month", "Year"};
    private ChartActivity context;
    private float dimen[];
    private String mode;

    public ChartAdaptor(ChartActivity context, float[] dimen, String mode) {
        this.context = context;
        this.dimen = dimen;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(position);
        container.addView(view, 0);
        return view;
    }

    private View getView(int position) {
        NestedScrollView view = new NestedScrollView(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new NestedScrollView.LayoutParams(-1, -2));
        layout.addView(chartCard(position));
        layout.addView(detailCard(position));

        view.addView(layout);
        return view;
    }

    private View detailCard(int position) {
        int margin = ViewUtil.toPx(12, context);
        CardView cardView = new CardView(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, ViewUtil.toPx(120, context));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            p.topMargin = margin;
            p.bottomMargin = margin;
        }
        p.gravity = Gravity.CENTER_HORIZONTAL;
        p.leftMargin = margin;
        p.rightMargin = margin;
        cardView.setLayoutParams(p);
        cardView.setCardElevation(2);
        cardView.setRadius(5);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        CardView.LayoutParams pp = new CardView.LayoutParams(-1, -1);
        pp.gravity = Gravity.CENTER;
        layout.setLayoutParams(pp);
        layout.addView(lastSeen());
        layout.addView(number());

        cardView.addView(layout);
        return cardView;
    }

    private View number() {
        int big = 40;
        AppText tv = new AppText(context);
        tv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1f));
        tv.setSingleLine();
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(1, 13);
        SpannableString string;
        switch (mode) {
            case ChartActivity.BLOOD_pressure:
                string = new SpannableString("149SBP   73DBP");
                string.setSpan(new AbsoluteSizeSpan(big, true), 0, string.toString().indexOf("SBP"), 0);
                string.setSpan(new AbsoluteSizeSpan(big, true), string.toString().indexOf("   "), string.toString().indexOf("DBP"), 0);
                string.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.lite_5)), 0, string.toString().indexOf("   "), 0);
                string.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.lite_3)), string.toString().indexOf("   "), string.length(), 0);
                break;
            case ChartActivity.BODY_temperature:
                string = new SpannableString("37.16" + "  ºc");
                string.setSpan(new AbsoluteSizeSpan(big, true), 0, string.toString().indexOf(" "), 0);
                string.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.lite_5)), 0, string.toString().indexOf(" "), 0);
                break;
            case ChartActivity.RR:
                string = new SpannableString("19" + " Per Minute");
                string.setSpan(new AbsoluteSizeSpan(big, true), 0, string.toString().indexOf(" "), 0);
                string.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.lite_11)), 0, string.toString().indexOf(" "), 0);
                break;
            case ChartActivity.HR:
                string = new SpannableString("98" + " BPM");
                string.setSpan(new AbsoluteSizeSpan(big, true), 0, string.toString().indexOf(" "), 0);
                string.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.lite_9)), 0, string.toString().indexOf(" "), 0);
                break;
            default:
                string = new SpannableString("98%" + " SPO₂");
                string.setSpan(new AbsoluteSizeSpan(big, true), 0, string.toString().indexOf(" "), 0);
                string.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.lite_7)), 0, string.toString().indexOf(" "), 0);
                break;
        }
        tv.setText(string);
        return tv;
    }

    private View lastSeen() {
        RelativeLayout layout = new RelativeLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        int padding = ViewUtil.toPx(8, context);
        layout.setPadding(padding, padding, padding, padding);

        layout.addView(text(false));
        layout.addView(text(true));
        return layout;
    }

    private View text(boolean flag) {
        AppText textView = new AppText(context);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-2, -2);
        p.addRule(RelativeLayout.CENTER_VERTICAL);
        if (flag) {
            p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            textView.setText("23:00  17/11/2016");
        } else {
            p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            textView.setText("Last Check");
        }
        textView.setLayoutParams(p);
        textView.setSingleLine();
        textView.setTextSize(1, 14);
        textView.setTextColor(Color.DKGRAY);
        Log.d("TAG", "text() returned: " + textView);
        return textView;
    }

    private View chartCard(int position) {
        int margin = ViewUtil.toPx(12, context);
        CardView cardView = new CardView(context);
        cardView.setRadius(5);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, ViewUtil.toPx(290, context));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setElevation(2);
            p.topMargin = margin;
        }
        p.gravity = Gravity.CENTER_HORIZONTAL;
        p.leftMargin = margin;
        p.rightMargin = margin;
        cardView.setLayoutParams(p);

        cardView.addView(new ChartData(context, position, mode, dimen).getChart());
        return cardView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }
}
