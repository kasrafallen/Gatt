package net.puzzleco.gattapp.init;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.ChartActivity;
import net.puzzleco.gattapp.adaptor.ChartAdaptor;
import net.puzzleco.gattapp.util.Util;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppButton;
import net.puzzleco.gattapp.view.AppText;
import net.puzzleco.gattapp.view.AppToolbar;

public class InitChart implements ViewPager.OnPageChangeListener {
    public float[] dimen;
    private ChartActivity context;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private LinearLayout layout;

    private String mode;

    public InitChart(ChartActivity chartActivity, String mode) {
        this.context = chartActivity;
        this.dimen = Util.getDimen(context);
        this.mode = mode;
    }

    public View getView() {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        layout.setBackgroundResource(R.color.base);
        layout.addView(toolbar());
        layout.addView(tabs());
        layout.addView(pager());
        return layout;
    }

    private View pager() {
        pager = new ViewPager(context);
        pager.setLayoutParams(new LinearLayout.LayoutParams(-1, -2, 1f));
        return pager;
    }

    private View toolbar() {
        AppToolbar toolbar = new AppToolbar(context);
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(-1, ViewUtil.getToolbarSize(context)));
        switch (mode) {
            case ChartActivity.BLOOD_pressure:
                toolbar.setBackgroundResource(R.color.lite_3);
                break;
            case ChartActivity.BODY_temperature:
                toolbar.setBackgroundResource(R.color.lite_5);
                break;
            case ChartActivity.HR:
                toolbar.setBackgroundResource(R.color.lite_9);
                break;
            case ChartActivity.RR:
                toolbar.setBackgroundResource(R.color.lite_11);
                break;
            default:
                toolbar.setBackgroundResource(R.color.lite_7);
                break;
        }

        AppText tv = new AppText(context);
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

    private View tabs() {
        tabs = new PagerSlidingTabStrip(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabs.setElevation(8);
        }
        switch (mode) {
            case ChartActivity.BLOOD_pressure:
                tabs.setBackgroundResource(R.color.lite_3);
                break;
            case ChartActivity.BODY_temperature:
                tabs.setBackgroundResource(R.color.lite_5);
                break;
            case ChartActivity.RR:
                tabs.setBackgroundResource(R.color.lite_11);
                break;
            case ChartActivity.HR:
                tabs.setBackgroundResource(R.color.lite_9);
                break;
            default:
                tabs.setBackgroundResource(R.color.lite_7);
                break;
        }
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, ViewUtil.toPx(43, context));
        tabs.setLayoutParams(p);
        tabs.setUnderlineColorResource(R.color.transparent);
        tabs.setIndicatorHeight(ViewUtil.toPx(4, context));
        tabs.setIndicatorColorResource(R.color.white);
        tabs.setDividerColorResource(R.color.transparent);
        tabs.setAllCaps(false);
        return tabs;
    }

    public void setData() {
        pager.setAdapter(new ChartAdaptor(context, dimen, mode));
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(this);

        layout = (LinearLayout) tabs.getChildAt(0);
        for (int i = 0; i < layout.getChildCount(); i++) {
            TextView tv = (TextView) layout.getChildAt(i);
            tv.getLayoutParams().width = (int) (dimen[0] / 4);
            tv.setPadding(0, 0, 0, 0);
            if (i == 0) {
                tv.setTextColor(Color.WHITE);
            } else {
                tv.setTextColor(Color.DKGRAY);
            }
            tv.setTextSize(1, 13);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            TextView tv = (TextView) layout.getChildAt(i);
            if (i == position) {
                tv.setTextColor(Color.WHITE);
            } else {
                tv.setTextColor(Color.DKGRAY);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
