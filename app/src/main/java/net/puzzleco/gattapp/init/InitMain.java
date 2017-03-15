package net.puzzleco.gattapp.init;

import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.puzzleco.gattapp.R;
import net.puzzleco.gattapp.activity.MainActivity;
import net.puzzleco.gattapp.util.Util;
import net.puzzleco.gattapp.util.ViewUtil;
import net.puzzleco.gattapp.view.AppButton;
import net.puzzleco.gattapp.view.AppToolbar;

public class InitMain {

    private final int header_size;
    public float[] dimen;
    private MainActivity context;
    private final int screen_size;
    private int margin;

    public DrawerLayout layout;
    private initMainItems items;
    public InitDrawer initDrawer;

    public InitMain(MainActivity activity) {
        this.context = activity;
        this.dimen = Util.getDimen(context);
        this.header_size = ViewUtil.getToolbarSize(context);
        this.screen_size = Math.round(dimen[1] - header_size);
        this.margin = ViewUtil.toPx(10, context);
    }

    public View getView() {
        layout = new DrawerLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        layout.setBackgroundResource(R.color.base);
        layout.addView(getScreen());

        initDrawer = new InitDrawer(context, dimen, layout);
        layout.addView(initDrawer.getDrawer());
        return layout;
    }

    private View getScreen() {
        FrameLayout frameLayout = new FrameLayout(context);
        DrawerLayout.LayoutParams p = new DrawerLayout.LayoutParams(-1, -1);
        frameLayout.setLayoutParams(p);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        layout.addView(toolbar());
        layout.addView(screen());

        frameLayout.addView(layout);
        return frameLayout;
    }

    private View toolbar() {
        AppToolbar toolbar = new AppToolbar(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, header_size);
        toolbar.setLayoutParams(p);
        toolbar.setBackgroundResource(R.color.white);
        toolbar.addView(navigation());
        toolbar.addView(logo());
        return toolbar;
    }

    private View navigation() {
        AppButton button = new AppButton(context);
        button.setBackgroundResource(R.drawable.ic_nav_icon);
        Toolbar.LayoutParams p = new Toolbar.LayoutParams(ViewUtil.getToolbarSize(context), ViewUtil.getToolbarSize(context));
        p.gravity = Gravity.LEFT;
        button.setLayoutParams(p);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.openDrawer(Gravity.LEFT);
            }
        });
        return button.getView();
    }

    private View logo() {
        float height = (1f * header_size) - (1f * header_size / 2.45f);
        float width = 2669f / 791f * height;
        Button view = new Button(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(10);
        }
        Toolbar.LayoutParams p = new Toolbar.LayoutParams((int) width, (int) height);
        p.gravity = Gravity.LEFT;
        view.setLayoutParams(p);
        view.setBackgroundResource(R.mipmap.app_logo);
        view.setClickable(false);
        view.setFocusableInTouchMode(false);
        view.setFocusable(false);
        return view;
    }

    private View screen() {
        float height = screen_size - (2f * margin);
        float width = 1087f * height / 1550f;

        RelativeLayout all = new RelativeLayout(context);
        all.setLayoutParams(new LinearLayout.LayoutParams(-1, screen_size));

        HorizontalScrollView view = new HorizontalScrollView(context);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-2, (int) height);
        p.addRule(RelativeLayout.CENTER_IN_PARENT);
        p.topMargin = margin;
        p.bottomMargin = margin;
        view.setLayoutParams(p);
        view.setVerticalScrollBarEnabled(false);
        view.setHorizontalScrollBarEnabled(false);

        RelativeLayout box = new RelativeLayout(context);

        RelativeLayout layout = new RelativeLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams((int) width, -1));
        layout.addView(image());
        setTexts(layout, width, height);

        box.addView(layout);
        view.addView(box);
        all.addView(view);
        return all;
    }

    private View image() {
        View view = new ImageView(context);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1, -1);
        view.setLayoutParams(p);
        view.setBackgroundResource(R.mipmap.main_test);
        return view;
    }

    private void setTexts(RelativeLayout layout, float width, float height) {
        items = new initMainItems(context, dimen, width, height);

        layout.addView(items.getText(1));
        layout.addView(items.getText(2));
        layout.addView(items.getText(3));
        layout.addView(items.getText(4));
        layout.addView(items.getText(5));
        layout.addView(items.getText(6));
        layout.addView(items.getText(7));
        layout.addView(items.getText(8));
        layout.addView(items.getText(9));
        layout.addView(items.getText(10));
        layout.addView(items.getText(11));
        layout.addView(items.getText(12));
        layout.addView(items.getText(13));
    }

    public void updateView(byte[] data) {
        items.update(data);
    }
}
