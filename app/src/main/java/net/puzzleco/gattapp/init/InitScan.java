package net.puzzleco.gattapp.init;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.ScanActivity;
import net.puzzleco.gattapp.util.Util;
import net.puzzleco.gattapp.view.AppButton;
import net.puzzleco.gattapp.view.AppToolbar;
import net.puzzleco.gattapp.util.ViewUtil;

public class InitScan {
    private ScanActivity context;
    private int app_bar_size;

    public float[] dimen;

    public AppToolbar toolbar;
    public RecyclerView listView;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RelativeLayout layout;
    private View bar;

    public InitScan(ScanActivity scanActivity) {
        this.context = scanActivity;
        this.app_bar_size = ViewUtil.toPx(140, context);
        this.dimen = Util.getDimen(context);
    }

    public View getView() {
        CoordinatorLayout layout = new CoordinatorLayout(context);
        layout.setBackgroundResource(R.color.base);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        layout.addView(appBar());
        layout.addView(list());
        return layout;
    }

    private View appBar() {
        final AppBarLayout layout = new AppBarLayout(context);
        layout.setId(+8250);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundResource(R.color.theme);
        CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(-1, app_bar_size);
        layout.setLayoutParams(p);

        collapsingToolbarLayout = new CollapsingToolbarLayout(context);
        AppBarLayout.LayoutParams pp = new AppBarLayout.LayoutParams(-1, -1);
        pp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        collapsingToolbarLayout.setLayoutParams(pp);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setContentScrimResource(R.color.theme);

        toolbar = new AppToolbar(context);
        CollapsingToolbarLayout.LayoutParams ppp = new CollapsingToolbarLayout.LayoutParams(-1, ViewUtil.getToolbarSize(context));
        ppp.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);
        toolbar.setLayoutParams(ppp);
        toolbar.addView(back(ViewUtil.getToolbarSize(context)));
        toolbar.addView(progress(ViewUtil.getToolbarSize(context)));

        collapsingToolbarLayout.addView(toolbar);
        collapsingToolbarLayout.addView(imageView());

        layout.addView(collapsingToolbarLayout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.setExpanded(false, true);
            }
        }, 200);
        return layout;
    }

    private View imageView() {
        ImageView view = new ImageView(context);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        CollapsingToolbarLayout.LayoutParams p = new CollapsingToolbarLayout.LayoutParams(-1, -1);
        p.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);
        view.setLayoutParams(p);
//        view.setBackgroundResource(R.drawable.ic_health_care);
        return view;
    }

    private View back(int i) {
        AppButton button = new AppButton(context);
        button.setBackgroundResource(R.drawable.ic_navigation);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(i, i);
        p.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        button.setLayoutParams(p);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.finish();
            }
        });
        return button.getView();
    }

    private View progress(int i) {
        layout = new RelativeLayout(context);
        layout.setBackgroundResource(R.drawable.ic_bluetooth);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(i, i);
        p.gravity = Gravity.RIGHT;
        p.rightMargin = toolbar.getContentInsetLeft();
        layout.setLayoutParams(p);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout.getTag() != null) {
                    context.setAdaptor();
                }
            }
        });

        int bar_size = (int) (1f * i - (1f * i / 3));
        bar = new View(context);
        RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(bar_size, bar_size);
        pp.addRule(RelativeLayout.CENTER_IN_PARENT);
        bar.setLayoutParams(pp);
        bar.setBackgroundResource(R.mipmap.progress);

        layout.addView(bar);
        return layout;
    }

    private View list() {
        listView = new RecyclerView(context);
        CoordinatorLayout.LayoutParams p = new CoordinatorLayout.LayoutParams(-1, -1);
        p.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        listView.setLayoutParams(p);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setHasFixedSize(true);
        return listView;
    }

    public void offSearch() {
        layout.setTag("");
        collapsingToolbarLayout.setTitle(Html.fromHtml("<small>Press for rescan</small>"));
        bar.clearAnimation();
    }

    public void onSearch() {
        layout.setTag(null);
        collapsingToolbarLayout.setTitle(Html.fromHtml("<small>Scanning...</small>"));
        bar.startAnimation(barAnim());
    }

    private Animation barAnim() {
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }
}
