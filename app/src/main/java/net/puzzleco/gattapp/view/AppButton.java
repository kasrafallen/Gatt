package net.puzzleco.gattapp.view;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.puzzleco.gattapp.util.ViewUtil;

public class AppButton {

    private RelativeLayout layout;
    private RelativeLayout mid;
    private Context context;
    private View view;

    public AppButton(Context context) {
        this.context = context;
        layout = new RelativeLayout(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layout.addView(mid());
            if (mid != null) {
                mid.setClickable(true);
                ViewUtil.setBackgroundRipple(mid, context);
            }
        } else {
            layout.setClickable(true);
            layout.addView(view());
            ViewUtil.setBackground(layout, context);
        }
    }

    private View mid() {
        mid = new RelativeLayout(context);
        mid.addView(view());
        return mid;
    }

    public void setId(int id) {
        layout.setId(id);
        if (mid != null) {
            mid.setId(id);
        }
    }

    private View view() {
        view = new View(context);
        view.setClickable(false);
        view.setScaleX(1.12f);
        view.setScaleY(1.12f);
        return view;
    }

    public void setBackgroundResource(int resId) {
        if (view != null)
            view.setBackgroundResource(resId);
    }

    public void setLayoutParams(ViewGroup.LayoutParams params) {
        layout.setLayoutParams(params);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int width = (int) (8f * params.width / 10f);
            int height = (int) (8f * params.height / 10f);

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(width, height);
            p.addRule(RelativeLayout.CENTER_IN_PARENT);
            if (mid != null)
                mid.setLayoutParams(p);

            RelativeLayout.LayoutParams pp = new RelativeLayout.LayoutParams(width, height);
            pp.addRule(RelativeLayout.CENTER_IN_PARENT);
            if (view != null)
                view.setLayoutParams(pp);
        } else {
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(params.width, params.height);
            p.addRule(RelativeLayout.CENTER_IN_PARENT);
            if (view != null)
                view.setLayoutParams(p);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mid != null)
                mid.setOnClickListener(onClickListener);
        } else {
            if (layout != null)
                layout.setOnClickListener(onClickListener);
        }
    }

    public View getView() {
        return layout;
    }
}
